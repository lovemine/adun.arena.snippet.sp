package adun.arena.sp.data.http.server;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;

import adun.arena.sp.data.Singleton;
import adun.arena.sp.data.Singleton.SampleDto;

/**
 *
 * Jetty로 Http Sever를 구동시킨다.
 * 
 * 127.0.0.1:8080 으로 /helloworld로 들어오는 request를 JettyMyServlet로 처리하도록 한다.
 *
 */
public class JettyServer {

	public static void main(String[] args) throws Exception {
		JettyServer server = new JettyServer();
		server.prepareSingleton();
		server.start();
	}

	/**
	 * JettyMyServlet 으로 명시적으로 데이터를 넘기기 곤란하다. (new JettyMyServlet()을 쓰는게 아니라 JettyMyServlet.class를 사용하므로)
	 * 그래서 Singleton을 사용하여 JettyMyServlet으로 넘길 데이터를 미리 준비한다.
	 */
	public void prepareSingleton() {
		List<SampleDto> list = new ArrayList<>();
		SampleDto sample = new SampleDto();
		sample.setKey("key1");
		sample.setValue("value1");
		list.add(sample);
		Singleton.getInstance().setList(list);
	}

	/**
	 * Server를 기동한다. 
	 * 
	 */
	public void start() throws Exception {
		
		Server server = new Server();
		
		ServerConnector http = new ServerConnector(server);
		http.setHost("127.0.0.1");
		http.setPort(8080);
		server.addConnector(http);
		
		
		ServletHandler servletHandler = new ServletHandler();
		servletHandler.addServletWithMapping(JettyMyServlet.class, "/helloworld");
		server.setHandler(servletHandler);
		
		
		server.start();
		server.join();
	}
}