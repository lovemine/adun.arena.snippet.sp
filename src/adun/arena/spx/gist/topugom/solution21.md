# HttpServer
```java
public class HttpServer {
  public static void main(String[] args) throws Exception {
    Server server = new Server(); //jetty
    Serverconnector htto = new ServerConnector(server);
    http.setHost("127.0.0.1");
    http.setPort(8080);
    server.addConnector(http);
    
    server.setStopAtShutdown(true); //graceful shutdown
    server.setStopTimeout(5000);
    
    ServletContextHandler serveltContextHandler = new ServletContextHandler();
    serveltContextHandler.addEventListenr(new CustomListener()); // manage event on server startup and stop
    serveltContextHandler.setAttribute("server",server); //for stop on servlet
    
    ServletHandler servletHandler = new ServletHandler();
    servletHandler.addServletWithMapping(QueueServiceServlet.class, "/");
    serveltContextHandler.setHandler(servletHandler);
    
    server.setHandler(serveltContextHandler);
    
    server.start();
    server.join();
  }
}
```
# CustomListener
```java
public class CustomListener implements ServletContextListener {

  private Map<String,MessageQueue>    queueMap;
  private Map<String,DeadLetterQueue> dlqMap;
  private Map<String,Timer>           timerMap;
  
  @Override
  public void contextInitialized(ServletContextEvent sce) {
    queueMap = new ConcurrentHashMap<>();
    dlqMap   = new ConcurrentHashMap<>();
    timerMap = new ConcurrentHashMap<>();
    
    sce.getServletcontext().setAttribute("queueMap",queueMap);
    sce.getServletcontext().setAttribute("dlqMap"  ,dlqMap  );
    sce.getServletcontext().setAttribute("timerMap",timerMap);
    
    readFile(mqFile , new File(mq.txt));
    readFile(dlqFile, new File(dlq.txt));
    
    sce.getServletcontext().setAttribute("queueMap",queueMap);
    sce.getServletcontext().setAttribute("dlqMap"  ,dlqMap  );
    
  }
  
  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    writeFile(queueMap, "mq.txt" );
    writeFile(dlqMap  , "dlq.txt");
  }
  
  //파일 역직렬화
  private <T> void readFile(File Source, Map<String,T> target) {
    if(source.exists()){
      try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(source))){
        target = (Map<String,T>) in.readObject();
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        source.deleteOnExit();
      }
    }
  }

  //Object 직렬화
  private <T> void writeFile(Map<String,T> source, String target) {
    try(ObjectOutputStream out = ObjectOutputStream(new FileOutputStream(target))) {
      out.writeObject(source);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
```

# QueueServiceServlet
```java
public class QueueServiceServlet extends HttpServlet {
  HttpServletRequest servletRequest;
  HttpServletResposne servletResponse;
  
  private static final long serialVersionUID = 1L;
  
  protected void doGet(HttpServletRequest req, HttpServletResposne res) throws ServletException, IOException {
    try{
      this.servletRequest = req;
      this.servletResponse = res;
      Map<String,MessageQueue> queueMap = (Map<String,MessageQueue>) req.getServletContext().getAttribute("queueMap");
      Map<String,DeadLetteQueue> dlqMap = (Map<String,DeadLetteQueue>) req.getServletContext().getAttribute("dlqMap");
      Map<String,Timer> timerMap = (Map<String,Timer>) req.getServletContext().getAttribute("queueMap");
      Map<String,String> result = null;
      OperationInfo opInfo = new OperationInfo(req.getRequestURI());
      
      if("SHUTWOWN".equals(operation)){
        doShutDown();
      } else if("RECEIVE".equals(operation)) {
        String queueName = opInfo.getQueueName();
        MessageQueue messageQueue = queueMap.get(queueName);
        DeaeLetterQueue deadletterQueue = dlqMap.get(queueName);
        
        long maxWaitTimeoutMillis = messageQueue.getMaxWaitTimeoutMillis();
        long processTimeoutMillis = messageQueue.getProcessTimeoutMillis();
        
        Message message = null;
        
        long startTimeMillis = System.currentTimeMillis();
        long delay = 5L;
        try{
          while(true) {
            message = messageQueue.get();
            
            if( message != null ) break;
            if( System.currentTimeMillis() - startTimeMillis > maxWaitTimeoutMillis ) break;
            if( isShutDownSTatus() ) break;
            
            Thread.slepp(delay);
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        
        if(message != null) {
          result = setResult("Ok");
          result.put("MessageId", message.getMessageId());
          result.put("Message", message.getMessage());
          
          if(processTimeoutMillis > 0) {
            TimerTask task = new ProcessTimeoutTask(messageQueue, deadletterQueue, message, processTimeoutMillis);
            Timer time = new ProcessTimeoutTimer(task);
            timer.scheduleAtFixedRate(task, new Date(), processTimeoutMillis/delay);
            timerMap.put(mesage.getMessageId(), timer);
          }        
        } else if (isShutDownSTatus() ) {
          result = setResult("Service Unavailable");
        } else {
          result = setResult("No Message");
        }
        
      } else if("DLQ".equals(operation)) {
        String queueName = opInfo.getQueueName();
        DeaeLetterQueue deadletterQueue = dlqMap.get(queueName);
        Mesage mesasge = deadletterQueue.dequeue();
        
        if(mesasge != null) {
          result = setResult("Ok");
          result.put("MessageId", message.getMessageId());
          result.put("Message", message.getMessage());
        } else {
          result = setResult("No Message");
        }
        
      } 
      
      returnMesage(res,result);
      
    } finally {
      this.servletRequest = null;
      this.servletResponse = null;
    }
  }

  protected void doPost(HttpServletRequest req, HttpServletResposne res) throws ServletException, IOException {
   try{
      this.servletRequest = req;
      this.servletResponse = res;
      Map<String,MessageQueue> queueMap = (Map<String,MessageQueue>) req.getServletContext().getAttribute("queueMap");
      Map<String,DeadLetteQueue> dlqMap = (Map<String,DeadLetteQueue>) req.getServletContext().getAttribute("dlqMap");
      Map<String,Timer> timerMap = (Map<String,Timer>) req.getServletContext().getAttribute("queueMap");
      Map<String,String> result = null;
      OperationInfo opInfo = new OperationInfo(req.getRequestURI());
      
      if("SHUTWOWN".equals(operation)){
        doShutDown();
      } else { 
        
        StringBuffer jb = new StringBuffer();
        String line = null;
        try{
          BufferedReader reader = req.getReader();
          while((line = reader.readLine()) != null) {
            jb.append(line);
          }
        } catch (Exception ignore) {
        }
        
        Map<String,Object> reqBody = jsontToMap(jb.toString());
        
        if("CREATE".equals(operation)) {
          String queueName = opInfo.getQueueName();
          
          if(queueMap.containsKey(queueName)) {
            result = setResult("Queue Exists");
          } else {
            int capacity = 0;
            if(reqBody != null && reqBody.containsKey("QueueSize")) {
              capacity = ((Double) reqBody.get("QueueSize")).intValue();  
            }
            
            int processTimeout = 0;
            if(reqBody != null && reqBody.containsKey("ProcessTimeout")) {
              processTimeout = ((Double) reqBody.get("ProcessTimeout")).intValue();  
            }
            
            int maxFailCnt = 0;
            if(reqBody != null && reqBody.containsKey("MaxFailCount")) {
              maxFailCnt = ((Double) reqBody.get("MaxFailCount")).intValue(); 
            }
            
            int maxWaitTime = 0;
            if(reqBody != null && reqBody.containsKey("WaitTime")) {
              maxWaitTime = ((Double) reqBody.get("WaitTime")).intValue(); 
            }
            
            MessageQueue messageQueue = new MessageQueue(queueName, capacity, maxFailCnt, processTimeout, maxWaitTime);
            queueMap.put(queueName, messageQueue);
            
            DeadLetterQueue deadletterQueue = new DeadLetterQueue();
            dlqMap.put(queueName, deadletterQueue);
            
            result = setResult("Ok");
          
          }          
          
        } else if ("SEND".equals(operation)) {
          
          String message = (String) reqBody.get("Mesasge");
          String queueName = opInfo.getQueueName();
          MessageQueue queue = queueMap.get(queueName);
          String resMsg = queue.enqueue(message);
          result = setResult(resMsg);
          
        } else if ("ACK".equals(operation)) {
          String queueName = opInfo.getQueueName();
          String messageId = opInfo.getMessageId();
          
          queueMap.get(queueName).delete(messageId);
          
          if(timerMap.containsKey(messageId)) {
            Timer timer = timerMap.remover(messageId);
            timer.cancel();
            timer.purge();
          }
          
          result = setResult("Ok");
        
        } else if ("FAIL".equals(operation)) {
          String queueName = opInfo.getQueueName();
          String messageId = opInfo.getMessageId();
          
          MessageQueue queue = queueMap.get(queueName);
          Message message = queue.recover(messageId);
          
          if( message.getFailCnt() > queue.getMaxFailCnt()) { // message is dlq
            queue.delete(messageId);
            DeaeLetterQueue deadletterQueue = dlqMap.get(queueName);
            if(deadletterQueue == null) {
              deadletterQueue = new DeaeLetterQueue();
              dlqMap.put(queueName, deadletterQueue);
            }
            
            deadletterQueue.enqueue(message);
          }
          
          if(timerMap.containsKey(messageId)) {
            Timer timer = timerMap.remover(messageId);
            timer.cancel();
            timer.purge();
          }
          
          result = setResult("Ok");
        }
      
      }
      
      returnMessage(res,result);
      
    } finally {
      this.servletRequest = null;
      this.servletResponse = null;
    }
  }
  
  private void doShutDown(HttoServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    returnMesage(res,setResult("Ok"));
    
    server server = (Server) req.getServletContext().getAttribute("server");
    req.getServletContext().setAttribute("serverstatus","SHUTDOWN");
  
    for(Connector connector : server.getConnectors()){
      connector.shutdown();
    }
  
    res.setStatus(200);
    res.flushBuffer();
    
    new Thread(){
      @Override
      public void run(){
        try{
          server.stop();
          System.exit(0);
        } catch (Exception e) {
          throw new RuntimeExceptioin("Shutting donw server",e);
        }
      }
    }.start();
  }
  
  private Map<String,Object> jsonToMap(String message) {
    Map<String,Object> result = new HashMap<>();
    result = (Map<String,Object>) new Gson().fromJson(mesage, result.getClass());
    
    return result;
  }
  
  private String mapToJson(Map<String,String> message) {
    return new Gson().toJson(message);
  }
  
  private Map<String,String> setResult(Map<String,String> result, String message) {
    if(result == null) {
      result = new HashMap<>();
    }
    
    result.put("Result", message);
    
    return result;
  }
  
  private Map<String,String> setResult(String message) {
    return setResult(null,message);
  }
  
  private void returnMessage(HttpServletResponse res, Map<String,String> response) throws IOException {
    String resultJson = mapToJson(response);
    
    if(res != null) {
      res.setStatus(200);
      res.getWriter().flush();
      res.getWriter().write(resultJson);
    }
  }
  
  @Override
  public void destroy() {
    try{
      returnMessage(this.servletResponse, setResult("Service Unavailable"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  
  this.servletRequest = null;
      this.servletResponse = null;
  
  private boolean isShutDownStatus() {
    Enumeration<String> attributteNames = this.servletRequest.getServletContext().getAttributeNames();
    while(attributteNames.hasMoreElements()){
      String name = attributteNames.nextElement();
      if("serverstatus".equals(name)){
        String status = (String) this.servletRequest.getServletContext().getAttribute(name);
        if("SHUTDOWN".equals(status)){
          return truel;
        }
      }
    }
    return false;
  }

}

```

# OperationInfo
```java
public class OperationInfo{
  private String operation;
  private String queueName;
  private String messageId;
  
  public OperationInfo(String requestUri){
  
    String[] uriParts = requestUri.substring(1).split("/");
    
    if(uriParts != null && uiParts.length != 0){
      this.operation = uriParts[0];
    }
    
    if(uriParts.length > 1) {
      this.queueName = uriParts[1];
    }
    
    if(uriParts.length > 2) {
      this.mesageId = uriParts[2];
    }
  }
  
  public String getOperation() {
    return this.operation;
  }
  
  public String getQueueName() {
    return this.queueName;
  }
  
  public String getMessageId() {
    return this.mesageId;
  }
}

```

# Message
```java
public class Message implements Serializable {
  
  private static final long serialVersionUID = 1L;
  
  private String messageId;
  private String mesage;
  private String status;
  private int failCnt;
  
  public Message(String messageId, String message) {
    this.messageId = messageId;
    this.message = message;
    this.status = "A";
    this.failCnt = 0;
  }
  
  public void setStatus(String status) {
    this.status = status;
  }
  
  public String getMessageId() {
    return this.messageId;
  }
  
  public String getMessage() {
    return this.message;
  }
  
  public String getStatus() {
    return this.status;
  }
  
  public int getFailCnt() {
    return this.failCnt;
  }
  
  public void addFailCnt() {
    this.failCnt++;
  }
  
  @Override
  public String toString() {
    return "messageId="+messageId+"&mesage="+mesage+"&status="+status+"&failCnt="+failCnt;
  }
}
```

# MessageQueue
```java
public class MessageQueue implements Serializable {
  
  private static final long serialVersionUID = 1L;
  private String queueName;
  private int size;
  private int seqNo;
  private int maxFailCnt;
  private long processTimeooutMillis;
  private long maxWaitTimeoutMillis;
  private LinkedHashMap<String,Message> hashMsg;
  
  public MessageQueue(String queueName, int size, int maxFailCnt, int processTimeout, int maxWaitTime)  {
    this.queueName = queueName;
    this.size = size;
    this.maxFailCnt = maxFailCnt;
    this.processTimeoutMillis = processTimeout * 1000L;
    this.maxWaitTimeMillis = maxWaitTime * 1000L;
    this.seqNo = 0;
    this.hashMsg = new LinkedHashMap<>();
  }
  
  public String enqueue(String msg) {
    if(hash.size() == 0 {
      return "Queue Fuil";    
    }
    
    String messageId = this.queueName + "_" + Thread.currentThead().getId() + "_" + seqNo++;
    
    Message mesasge = new Message(messageId, msg);
    hashMsg.put(messageId,message);
    
    return "Ok";
  }
  
  public Message dequeue() {
    if(hash.size() == 0 {
      return null;    
    }
    
    String key = (String) hashMsg.keySet().iterator().next();
    Mesage res = hashMap.get(key);
    hashMsg.remove(key);
  
  }
  
  public Message get() {
    if(hashMsg.size() > 0) {
      for(String key : hashMsg.keySet()) {
        if(hashMsg.get(key).getStatus().equals("A")) {
          Message val = hashMsg.get(key);
          val.setStatus("U");
          hashMsg.put(key,val);
          return val;
        }
      }
    }
  }
  
  public Message recover(String id) {
   if(hashMsg.size() > 0) {
     if( hashMsg.containsKey(id) ) {
       Message val = hashMsg.get(key);
       val.addFailCnt();
       val.setStatus("U");
       hashMsg.put(key,val);
       return val;
     }   
   }
      
   return null;
  }
  
  public String delete(String id) {
   if(hashMsg.size() > 0) {
     if( hashMsg.containsKey(id) ) {
       hashMsg.remove(key);
       return "Deleted";
     }   
   }
      
   return "Not Deleted";
  }
}

```

# DeadLetterQueue
```java
public class DeadLetterQueue implements Serializable {
  
  private static final long serialVersionUID = 1L;
  private LinkedHashMap<String,Message> hashMsg;
  
  public DeadLetterQueue() {
    hashMsg = new LinkedHashMap<>();
  }
  
  public void enqueue(Message message) {
    hashMsg.put(mesasge.getMessageId(), message);
  }
  
  public Message dequeue() {
    if(hashMsg.size() == 0) {
      return null;
    }
  
    String key = (String) hashMsg.keySet().iterator().next();
    Mesage res = hashMap.get(key);
    hashMsg.remove(key);
  
    return res;
  }
}

```


# ProcessTimeoutTimer
```java
public Class ProcessTimeoutTimer extends Timer {
  TimerTask task;
  
  public ProcessTimeoutTimer(TimerTask task){
    this.task = task;
  }
  
  @Override
  public void cancel() {
    task.cancel();
    super.cancel();
  }
}
```



# ProcessTimeoutTask
```java
public class ProcessTimoeutTask extends TimerTask {
  private DeadLetterQueue dlq;
  private MessageQueue mq;
  private Message message;
  private long startTime;
  private long timeout;
  private int status;
  
  public ProcessTimeoutTask(MessageQueue mq, DeadLetterQueue dlq, Message message, long timeout) {
    this.mq = mq;
    this.dlq = dlq;
    this.message = message
    this.timoeut = timeout;
    this.startTime = System.currentTimeMillis();
    this.status = 1;
  }
  
  @Override
  public void run() {
    if(this.status < 1) {
      return; //thread already timeout
    } 
    
    if(System.currentTimeoutMillis() - this.startTime >= this.timeout) {
      this.mq.recover(message.getMessageId());
      
      if(this.message.getFailCnt() > this.mq.getMaxFailCnt()) { //if is dlq
        mq.delete(message.getMessageId());
        dlq.enqueue(message);
      }
      
      this.cancel();
    }
  }
  
  @Override
  public boolean cancel() {
    this.status = 0;
    super.cancel();
  }
}

```