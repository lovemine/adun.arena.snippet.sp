package adun.arena.spx.gist.lovemine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class EchoServer {

	public static void startServer() throws IOException {
		int portNumber = 30000;

		ServerSocket serverSocket = new ServerSocket(portNumber);
		Socket socket = serverSocket.accept();
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		String echo;
		while ((echo = in.readLine()) != null) {
			System.out.println("echo: " + echo);
			out.println(echo);
			out.flush();
		}

		in.close();
		out.close();
		socket.close();
		serverSocket.close();
	}

	public static void startClient() throws UnknownHostException, IOException {
		String hostName = "127.0.0.1";
		int portNumber = 30000;

		Socket socket = new Socket(hostName, portNumber);
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

		String userInput;
		while ((userInput = stdIn.readLine()) != null) {
			out.println(userInput);
			out.flush();
			System.out.println("echo: " + in.readLine());
		}

		in.close();
		out.close();
		socket.close();
	}
}