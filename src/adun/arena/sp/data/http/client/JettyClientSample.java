package adun.arena.sp.data.http.client;

import java.io.IOException;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.FormContentProvider;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.util.Fields;
import org.eclipse.jetty.util.ssl.SslContextFactory;

/**
 * 
 * Jetty HttpClient을 사용해서 get / post 로 접속한다. (client 역할임)
 *
 */
public class JettyClientSample {

	/**
	 * JettyClientSample을 사용하여 GET/POST 테스트를 진행한다.
	 * 
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception {

		JettyClientSample http = new JettyClientSample();

		System.out.println("GET으로 데이터 가져오기");
		String getBody = http.sendGet("https://httpbin.org/anything?param=param1");

		System.out.println("POST로 데이터 가져오기");
		String postBody = http.sendPost("https://httpbin.org/anything");

	}

	/**
	 * 
	 * Jetty HttpClient를 사용해서 GET 으로 접속하여 body 값을 리턴한다.
	 * 
	 */
	public String sendGet(String url) {
		try {

			//httpClient를 준비한다. SSL 이 필요할 수도 있다.
			HttpClient httpClient = url.startsWith("https") ? new HttpClient(new SslContextFactory.Client()) : new HttpClient();
			httpClient.setFollowRedirects(false);
			httpClient.start();

			//request를 준비한다.
			Request request = httpClient.newRequest(url).method(HttpMethod.GET);

			//header를 설정해야 하는 경우 아래처럼 설정한다.
			request.header("key", "value");

			//Http Request를 send 한다.
			ContentResponse response = request.send();

			int status = response.getStatus();

			//status가 400 이상이라면 에러로 취급힌다.
			if (status >= 400) {
				throw new RuntimeException("Get Call Failed : [" + status + "] " + url);
			}

			String resposneBody = response.getContentAsString();

			System.out.println("HTTP 응답 코드 : \n " + status);
			System.out.println("HTTP body size : \n " + resposneBody.length());
			System.out.println("HTTP body : \n " + resposneBody.toString());

			return resposneBody;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * 
	 * Jetty HttpClient를 사용해서 POST 으로 접속하여 body 값을 리턴한다.
	 * 
	 * parameters는 key=value&key=value 형식의 String 값이어야 한다.
	 * 
	 */
	public String sendPost(String url) {
		try {

			//httpClient를 준비한다. SSL 이 필요할 수도 있다.
			HttpClient httpClient = url.startsWith("https") ? new HttpClient(new SslContextFactory.Client()) : new HttpClient();
			httpClient.setFollowRedirects(false);
			httpClient.start();

			//request를 준비한다.
			Request request = httpClient.newRequest(url).method(HttpMethod.POST);

			//for Header
			request.header("key", "value");

			//URI query param으로도 데이터를 넘길 수 있다. (url?key=value형태로)
			request.param("param", "hellllloooooooooooo");

			//request.attribute 는 하나의 JVM 상에서 request 객체를 통해 값을 교환하기 위한 것이다. (jvm exchange)
			request.attribute("param", "attributeAddeeeddd");
			request.attribute("param1", "aatribbsdlifjxdfijsldifjslifj");

			//POST Body를 위해서는 fields 객체를 사용하여야 한다. (application/x-www-form-urlencoded body) 
			Fields fields = new Fields();
			fields.put("fruit1", "apple");
			fields.put("fruit2", "orange");
			fields.put("fruit3", "berry");
			request.content(new FormContentProvider(fields));

			// POST Body로 File Upload(multipart upload)를 하는 경우에는 아래와 같이 한다.(multipart/form-data)
			// MultiPartContentProvider multiPart = new MultiPartContentProvider();
			// multiPart.addFieldPart("fruit", new StringContentProvider("apple"), null);
			// multiPart.addFilePart("icon", "img.png", new PathContentProvider(Paths.get("/tmp/img.png")), null);
			// multiPart.close();
			// request.content(multiPart);

			// POST Body로 JSON데이터를 보낸다면 아래와 같이 한다.
			// request.content(new StringContentProvider(JSON_HERE), "application/json");

			// POST Body로 XML 데이터를 보낸다면 아래와 같이 한다.
			// request.content(new StringContentProvider(JSON_HERE), "application/xml");

			// POST Body로 byte[]를 그냥 보낸다면 형식을 보낸다면 아래와 같이 한다.
			// byte[] bytes = new byte[256];
			// request.content(new ByteBufferContentProvider("application/octet-stream", ByteBuffer.wrap(bytes)));

			//Http Request를 send 한다.
			ContentResponse response = request.send();

			int status = response.getStatus();

			//status가 400 이상이라면 에러로 취급힌다.
			if (status >= 400) {
				throw new RuntimeException("Get Call Failed : [" + status + "] " + url);
			}

			String resposneBody = response.getContentAsString();

			System.out.println("HTTP 응답 코드 : \n " + status);
			System.out.println("HTTP body size : \n " + resposneBody.length());
			System.out.println("HTTP body : \n " + resposneBody.toString());

			return resposneBody;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

}
