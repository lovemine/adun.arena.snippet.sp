package adun.arena.snippet.sp.data.http.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import adun.arena.snippet.sp.data.ReadListDataFromFile.SampleTo;
import adun.arena.snippet.sp.data.Singleton;

public class MyServlet extends HttpServlet {

	private static final long serialVersionUID = -7392488114757271392L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		Object obj = req.getParameter("param");
		System.out.println((String)obj);
		res.setStatus(200);
		res.getWriter().write("Hello!");
		res.getWriter().flush();

		List<SampleTo> list = Singleton.getInstance().getList();
		for (SampleTo sample : list) {
			System.out.println(sample.getKey() + ":" + sample.getValue());
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		Object obj = req.getParameter("param");
		System.out.println((String)obj);
		res.setStatus(200);
		res.getWriter().write("Hello!");
		res.getWriter().flush();
	}

	/**
	 * doGet/doPost 같이 쓰고 싶으면 doGet/doPost에서 호출하고 아래에 구현한다.
	 */
	protected void dispatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println(readBody(req));
	}

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
