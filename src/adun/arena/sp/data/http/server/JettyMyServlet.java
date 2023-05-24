package adun.arena.sp.data.http.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import adun.arena.sp.data.Singleton;
import adun.arena.sp.data.Singleton.SampleDto;

/**
 * 
 * Jetty로 Server를 구동시키기 위한 Servlet 클래스르 준비한다.
 * 
 * GET / POST로 들어오는 request 에 대한 작업을 수행할 수 있다.
 * 
 * 쉽게 데이터를 교류하기 위해 Singleton 클래스를 사용하고 있다.
 *
 */
public class JettyMyServlet extends HttpServlet {

	private static final long serialVersionUID = -7392488114757271392L;

	/**
	 * Server로 GET 으로 접근했을때의 처리를 수행한다.
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		//request에서 정보를 얻는다.
		Object obj = req.getParameter("param");
		System.out.println((String)obj);

		//response를 준비한다.
		res.setStatus(200);
		res.getWriter().write("Hello!");
		res.getWriter().flush();

		//Singleton에서 데이터를 가져올 수 있다.
		List<SampleDto> list = Singleton.getInstance().getList();
		for (SampleDto sample : list) {
			System.out.println(sample.getKey() + ":" + sample.getValue());
		}

	}

	/**
	 * Server로 PSOT 로 접근했을때의 처리를 수행한다.
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		//request에서 정보를 얻는다.
		Object obj = req.getParameter("param");
		System.out.println((String)obj);

		//response를 준비한다.
		res.setStatus(200);
		res.getWriter().write("Hello!");
		res.getWriter().flush();

		//Singleton에서 데이터를 가져올 수 있다.
		List<SampleDto> list = Singleton.getInstance().getList();
		for (SampleDto sample : list) {
			System.out.println(sample.getKey() + ":" + sample.getValue());
		}
	}

	/**
	 * GET/POST가 상관 없는 경우 doGet(), doPost()에서 dispatch()를 호출하게 할 수도 있다.
	 */
	protected void dispatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println(readBody(req));
	}

	/**
	 * 
	 * request에서 Body를 읽어 String으로 만들어서 리턴한다.
	 * 필요에 따라 List<String>으로 만들어서 리턴할 수도 있겠다.
	 * 
	 */
	public String readBody(HttpServletRequest request) throws IOException {
		BufferedReader input = new BufferedReader(new InputStreamReader(request.getInputStream()));
		StringBuilder builder = new StringBuilder();
		String buffer;
		while ((buffer = input.readLine()) != null) {
			if (builder.length() > 0) {
				builder.append("\n");
			}
			builder.append(buffer);
		}
		return builder.toString();
	}

}
