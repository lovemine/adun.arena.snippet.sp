# 1. List

```java
public class ListPrac {

  public static void maoin(String[] args) throws IOException {
    ArrayList<Grade> al = new ArrayList<>();
    
    try{
      BufferedReader in = new BufferedReader(new FileReader("sample.txt"));
      String str;
      
      while((str = in.readLine()) != null) {
        String words = str.split("\t");
        Grade g = new Grade(words[0], Integer.parseInt(words[1]), Integer.parseInt(words[2]), Integer.parseInt(words[3]));
        al.add(g);
      }
      in.close();
      
    } catch (IOException e) {
      System.err.println(e);
      System.exit(1);
    }
    
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    while(true){
      String strInput = br.readLine();
      switch(strInput) {
      case "PRINT" : 
        Collection.sort(al, (g1,g2) -> g1.getName().compareTo(g2.getName()));
        break;
      case "KOREAN" : 
        Collection.sort(a1, (g1,g2) -> (g2.getKorean() - g1.getKorean()) == 0 ? g1.getName().compareTo(g2.getName()) : g2.getKorean() - g1.getKorean());
        break;
      case "ENGLOSH" :   
        Collection.sort(a1, new Comparator<Grade>() {
          @Override
          public int compare(Grade x, Grade y) {
            if(y.getEnglish() - x.getEnglish() == 0){
              return x.getName().compareTo(y.getName());
            } else {
              return y.getEnglish() - x.getEnglish();
            }
          }
        });
        break;
      case "MATH" : 
        Collection.sort(a1, new SortByMath());
        break;
      case "QUIT" : 
        return;
      default : 
        break;
      }
      
      for(Grade val : al){
        System.out.println(String.format("%s\t%d\t%d\t%d", val.getName(), val.getKorean(), val.getEnglish(), val.getMath()));
      }
    
    }
  }
  
  class Grade{
    private String name;
    private int korean;
    private int english;
    private int math;
    
    public Grade(String name, int korean, int english, int math) {
      this.name = name;
      this.korean = korean;
      this.english = english;
      this.math = math;
    }
    
    public String getName() {
      return this.name;
    }
    
    public int getKorean() {
      return this.koran;
    }
  
    public int getEnglish() {
      return this.english;
    }
    
    public int getMath() {
      return this.math;
    }  
  }
  
  class SortByMath implements Comparator<Grade>{
    public int compare(Grade x, Grade y) {
        if(y.getMath() - x.getMath() == 0){
          return x.getName().compareTo(y.getName());
        } else {
          return y.getMath() - x.getMath();
        }
    }
  }

}
```

# 2. Queue

```java
public class MsgQueue {
  private int size;
  private int seqNo;
  
  private LinkedHashMap<Integer, List<String>> hashMsg;
  
  public MsgQueue(int size){
    this.size = size;
    this.seqNo = 0;
    hashMsg = new LinkedHashMap<>();  
  }
  
  //Queue에 message저장
  public String enqueue(String message){
    if(hashMsg.size() == size){
      return "Queue Full";
    }
  
    List<String> listMsg = new ArrayList<>(); // message 구조치
    listMsg.add("A"); //status 
    listMsg.add(message); //status 
    hashMsg.put(seqNo++,listMsg);
    
     return "Enqueued";
  }

  //Queue에서 message 출력 후 삭제
  public String dequeue(){
    if(hashMsg.size() == 0){
      return "Queue Empty";
    }
  
    int key = (int) hashMsg.keySet().iterator().next();
    String res = hashMsg.get(key).get(1) + "(" + key +")";
    hashMsg.remove(key);
    return res;
  }

  //Queue에서 message 출력 
  public String get() {
    if(hashMsg.size() > 0) {
      for(Integer key : hashMsg.keySet()){
        if(hashMsg.get(key).get(0).equals("A")){
          List<String> val = hashMsg.get(key);
          val.set(0, "U");
          hashMsg.put(key,val);
          return val.get(1) + "(" + key + ")";
        }
      }
    }
    return "No Message";
  }
  
  //Queue로 messgae 복원
  public String recover(int id) {
    if(hashMsg.size() > 0) {
      if(hashMsg.containsKey(id)){
        hashMsg.get(id).set(0,"A");
        return "Message Recoverd";
      }
    }
  }
  
  //Queue에서 메싲 삭제
  public String delete(int id) {
    if(hashMsg.size() > 0 ) {
       if(hashMsg.containsKey(id)){
         hashMsg.remove(id);
         return "Deleted";
       }
    }
    
    return "Not Deleted";
  } 
}
```

# 3. ENC/DEC
```java
public class EncDec{
  public static void main(String[] args) throws NosuchAlgorithmException, IOException {
    InputStreamReader reader = new InputStreamReader(System.in);
    BufferedReader br = new BufferedReader(reader);
    String str;
    while(true){
      str = br.readLine();
      Base64CaseString(str);
      SHA256(str);    
    }
  }
  
  public static void Base64CaseString(String test) throws UnsupportedEncodingException {
    
    System.out.println(test);
    
    Encoder encodeer = Base64.getEncoder();
    String encodedString = encoder.encodeToString(test.getBytes("UTF-8"));
    System.out.println(encodedString);
    
    Decoder decoder = Base64.getDecoder();
    byte[] decodedBytes = decoder.decode(encodedString);
    Striing decodedString = new String(decodedBytes."UTF-8");
    System.out.println(decodedString);
  
  }
  
  public static void SHA256(String input) throws NosuchAlgorithmException {
  
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    byte[] result = digets.digest(input.getBytes());
    StringBuffer sb = new StringBuffer();
    for(int i=0; i< result.length; i++){
      sb.append(Integer.toString((result[i] & 0xFF) + 0x100, 16).substring(1));
    }
    
    System.out.println(sb.toString());
  
  }
}
```

# 4. FILE IO
```java
public class FileIO {
  static String rootPath = ".\\Input";
  public static void main(String[] args) {
    fileSearchAll(rootPath);
  }
  
  public static void fileSearchAll(String path) {
    File directory = new File(path);
    File[] files = directory.listFiles();
    for(File file : files){
      if(file.isDirectory()){
        fileSearchAll(fuke,getPath());
      } else {
        String partPath = path.substring(rootPath.length());
        System.out.println("." + partPath + "\\" + file.getName());
        if(file.length() > 3* 1024) {
          myCopyFile(partPath, file.getName());
        }
      }
    }
    
    public static void myCopyFile(String partPath, String filename){
      final int BUFFER_SIZE = 512;
      int readLen;
      
      try{
        File destination = new File(".\\Output" + partPath);
        if(!destination.exists()){
          destination.mkdirs();
        }
        
        InputStream inputStream = new FileInputStream(rootPath + partPath + "\\" + filename);
        OutputStream outputStream = new FileOutputStream(".\\Output" + partPath + "\\" + filename);
        
        byte[] buffer = new byte[BUFFER_SIZE];
        while((readLen = inputStream.read(buffer)) != -1) {
          outputStream.write(buffer, 0, readLen);
        }
        
        inputStream.close();
        outputStream.close();
        
      } catch (IOException e) {
        e.printStackTrace();
      }
    
    }
  
  }


}
```

# 5. FILE Socket
## 5.1. File Client
```java
public class FileClient {
  public static void main(String[] args) throws IOException {
    Socket socket = null;
    DataOutputStream os = null;
    try{
      socket = new Socket("127.0.0.1" , 27015);
      os = new DataOutputStream(socket.getOutputStream());
      
      byte[] buffer = new byte[4096];
      int lentgh;
      
      File directory = new File("./ClientFiles");
      File[] files = directory.listFiles();
      for(File file: files) {
        if(file.isFile()){
          os.writeUTF(file.getName());
          os.writeInt((int) file.length());
          
          FileInputStream is = null;
          try{
            is = new FileInputStream(file.getPath());
            while((length = is.read(buffer)) != -1) {
             os.write(buffer,0,length);
            }
          } finally {
            if(is != null) is.close();
          }
        }
      
      }
      
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if(os != null) os.close();
      if(socket != null) socket.close();    
    } 
  
  }
}
```
## 5.2. File Server
```java
public class FileServer {
 
 public static void main(String[] args) throws IOException, InterruptedException {
   File directory = new File("./ServerFiles");
   if(!directory.exists()){
     directory.mkdirs();
   }
   
   ThreadClass tc = new ThreadClass();
   Thread th = new Thread(tc);
   th.start();
   
   InputStream is = System.in;
   InputStreamReader reader = new InputStreamReader(is);
   BufferedReader br = new BufferedReader(reader);
   String str;
   while(true){
     str = br.readLine();
     if(str.equals("QUIT"){
       tc.listener.close();
       th.join();
       break;
     }
   }
 }
}

class ThreadClass implements Runnable {
  public ServerSocket listener;
  public void run() {
    final int BUFFER_SIZE = 4096;
    int recvLen;
    byte[] buffer = new byte[BUFFER_SIZE];
    
    listener = null;
    try{
      listener = new ServerSocket(27015);
      while(true){
        Socket socket = listener.accept();
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        try{
          String fineName = null;
          while((fileName = dis.readUTF()) != null) {
            int fileSize = is.readInt();
            FileOutputSize fos = new FileOutputSize("./ServerFiles/" + fileName);
            int length;
            while(fileSize > 0) {
              length = dis.read(buffer,0, Math.min(fileSize, buffer.length));
              fileSize -= length;
              fos.write(buffer, 0, length);
            }
            fos.close();
            System.out.println(fineName + "is received");
            
          }
        } catch (EOFException e) {
          System.out.println("Finish Receive...");
          socket.close();
        } 
      
      } catch (IOException e){
        e.printStackTrace();
      } finally {
        try{
          listener.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    
  }
}
```

# 6. Mutex 
```java
public class ThreadSample {
 public static void main(String[] args) throws InterruptException {
   ThreadClass tc1 = new ThreadClass("[Thread1] ");
   ThreadClass tc2 = new ThreadClass("[Thread2] ");
   tc1.start();
   tc2.start();
   
   ThreadClass.lock.lock();
   try{
     ThreadClass.printNums("[Main]");
   } finally {
     ThreadClass.lock.unlock();
   }
   
   tc1.join();
   tc2.join();
 }
}

class ThreadClass extends Thread {
  static ReentrantLock lock = new ReentrantLock();
  
  private String threadName;
  
  public ThreadClass(String threadName){
    this.threadName = threadName;
  }
  
  @Override
  public void run() {
    lock.lock();
    try{
      printNums(threadName);
    } finally{
      lock.unlock();
    }
  }
  
  static void printNums(String str) {
    int i;
    System.out.println(str);
    for(int i=0;i<30;i++){
      System.out.print((i+1) + " ");
    }
    System.out.println();
  }

}
```

# 7. ProcessThread
```java
public class ProcessExe {
  public static void main(String[] args) throws IOException, InterruptedException {
    System.out.println(String.format("Start - " + new Date().toString()));
    
    FileReader fileReader = new FileReader("NUM.txt");
    BufferedReader br = new BufferedReader(fileReader);
    String line;
    List<ProcessThread> processThreads = new ArrayList<>();
    
    while((line = br.readLine()) != null){
      String[] words = line.split(" ");
      int num1 = Integer.parseInt(words[0]);
      int num2 = Integer.parseInt(words[1]);
      
      ProcessThread processThread = new ProcessThread(num1,num2);
      processThread.start();
      processThreads.add(processThread);    
    }
    
    br.close();
    for(ProcessThread pt : processThreads({
      th.join();
    }
    
    System.out.println(String.format("End - " + new Date().toString()));
  }
}

class ProcessThread extends Thread {
  int num1;
  int num2;
  
  public ProcessThread(int num1, int num2) {
    this.num1 = num1;
    this.num2 = num2;
  }
  
  @Override
  public void run() {
    Strung output;
    try{
      output = getProcessOutput(Arrays.asList("add_2sec.exe", Integer.toString(num1), Integer.toString(num2)));
    } catch (IOEXception | InterruptedException e) [
      e.printStackTrace();
    }
  }
  
  public String getProcessOutput(List<String> commands) throws IOException, InterruptedException {
    ProcessBuilder builder = new ProcessBuilder(commands);
    Process process = builder.start();
    
    InputStream is = process.getInputStream();
    byte buffer = new byte[1024];
    int len = is.read(buffer);
    
    return (new String(buffer, 0, len);  
  }
}


```

# 8. HTTP
## 8.1. HTTP Client 
```java
public class MyClient {
  public static voiod main(String[] args) throws Exception {
    HttpClient httpClient = new HttpClient(); //jetty client
    httpClient.start();
    ContentResponse contentResposne = httpClient.newRequest("http://127.0.0.1:8080/mypath")
                                                .method(HttpMethod.GET)
                                                .send();
    System.out.println(contentResponse.getContentAsString());                                            
  }
}

```

 ## 8.2. HTTP SERVER
 ### 8.2.1. MyServer
 ```java
 public class MyServer {
   public static voiod main(String[] args) throws Exception {
     new MyServer().start();
   }
   
   public void start() throws Exception {
     Server server = new Server(); //jetty server
     ServerConnector http = new ServerConnector(server);
     http.setHost("127.0.0.1");
     http.setPort(8080);
     server.addConnector(http);
     
     ServletHandler servletHandler = new ServletHandler();
     servletHandler.addServeltWithMapping(MyServlet.class,"mypath");
     server.setHandler(servletHandler);
     
     server.start();
     server.join();
   }
 } 
 ```
 
 ### 8.2.2. MyServlet
 ```java
 public class Myservlet extends HttpServlet {
   private static final long serialVersionUID = 1L;
   
   protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
     res.setStatus(200);
     res.getWriter().write("Hello!!");
   }
   
   protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
     File destFolder = new File("./Output");
     if(destFolder.exists()){
       destFolder.mkdirs();
     }
     
     LocalTime currentTime - LocalTime.now();
     String filename = String.format("./Outout/%20d%20d%20d.json", currentTime.getHour, currentTime.getMinute, currentTime.getSecond());
     PrintWriter pw = new PrintWriter(new FileWriter(new File(filename)));
     
     BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
     String buffer;
     while((buffer = br.readLine()) != null){
       pw.print(buffer);
     }
     br.close();
     pw.close();
          
     res.setStatus(200);
     res.getWriter().write(filename + " saved!");
   }
   
 }
 ```
 
 ### 8.3. Http File Client 
 ```java
 public class FileClient {
   public static void main(String[] args) {
     String strFileList = getFileList();
     HttpClient httpClient = new HttpClient(); //jetty http client
     httpClient.start();
     Request request = httpClient.newRequest("http://127.0.0.1:8080/fileList").method(HttpMethod.POST);
     request.header(HttpHeader.CONTENT_TYPE, "application.json");
     request.content(new StringContentProvider(strfileList, "utf-8"));
     ContentResponse contentRes = request.send();
     System.out.println(contentRes.getContentAsString());
     httpClient.stop();   
   }
   
   private static String getFileList() {
     Gson gson = new Gson();
     JsonObject jo = new JsonObject();
     File directory = new File("./Input");
     jo.addProperty("Folder","Input");
     JsonArray ja = new JsonArray();
     File[] files = directory.listFiles();
     for(File file : files) {
       jarr.add(file.getName());
     }
     joi.add("FILES","jarr");
     
     String res = jo.toString();
     return res;
   }
 }
 ```
 
 ### 8.4. Http Date Client
  ```java
 public class FileClient {
   public static void main(String[] args) {
     HttpClient httpClient = new HttpClient(); //jetty http client
     httpClient.start();
     ContentResponse contentRes = httpClient.newRequest("http://127.0.0.1:8080/requestDate").method(HttpMethod.GET)
                                 .send();
     System.out.println(contentRes.getContentAsString());
   }
 }  
 ```
 
 # 9. JSON 
 ```java
 public class MyJson {
  
   public static void main(String[] args) {
     JsonElement jsonElement = JsonParsert.parseString("{\"key\" : \"value\" }");
     System.out.println(jsonElement.toString());
   }
 } 
 ```
 
 # Console 입력 - scanner
 ```java
 public class Launcher {
   public static void main(String[] args) {
     Scanner scanner = new Scanner(System.in);
     String id;
     String pwd;
     
     while(true){
       if(scanner.hasNext()) {
         String words = sacnner.nextLine().split(" ");
         String id = words[0]    ;
         String pwd = words[1];
         
         System.out.println("ID : [" + id + "] , PWD : [" + pwd +"]");
       }
     }
   }
 } 
 ```


# 10. Sigleton
```java
public Class Singleton {
  private static Object lock = new Object();
  private static Singleton instance;
  
  private Singleton(){
  }
  
  public static Singleton getInstance(){
    if(instance == nuil) {
      synchronized(lock) {
        if(instance == null) {
          instance = new Singleton();
        }
      }
    }
    
    return instance;
  }

}
```