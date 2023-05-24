# Date Socket Client
```java
public class DateClient {
  public static void main(String[] args) throws IOException {
    Socket s = new Socket("127.0.0.1", 9090);
    BufferedReader input = new BufferedReader(new ItemStreamReader(s.getInputStream()));
    String answer = input.readLine();
    System.out.println(answer);
  }
}
```

# Date Socket Server
```java
public class DateServer {
  public static void main(String[] args) throws IOException {
    ServerSocket listener = new ServerSocket(9090);
    try{
      while(true) {
        Socket socket = listener.accept();
        try{
          PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
          out.println(new Date().toString());
        } finally {
          socket.close();
        }
      }
    } finally {
      listener.close();
    }
  }
}
```