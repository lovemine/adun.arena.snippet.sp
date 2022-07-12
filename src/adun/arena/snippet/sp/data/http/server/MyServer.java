package adun.arena.snippet.sp.data.http.server;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;

import adun.arena.snippet.sp.data.ReadListDataFromFile.SampleTo;
import adun.arena.snippet.sp.data.Singleton;

//서버 구도
public class MyServer {
	
	
	public static void main(String[] args) throws Exception {
		List<SampleTo> list = new ArrayList<>();
		SampleTo sample = new SampleTo();
		sample.setKey("key1");
		sample.setValue("value1");
		list.add(sample);
		Singleton.getInstance().setList(list);
		new MyServer().start();
	}

	public void start() throws Exception {
		Server server = new Server();
		ServerConnector http = new ServerConnector(server);
		http.setHost("127.0.0.1");
		http.setPort(8080);
		server.addConnector(http);
		ServletHandler servletHandler = new ServletHandler();
		servletHandler.addServletWithMapping(MyServlet.class, "/helloworld");
		server.setHandler(servletHandler);
		server.start();
		server.join();
	}
}