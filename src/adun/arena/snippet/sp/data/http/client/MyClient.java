package adun.arena.snippet.sp.data.http.client;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.FormContentProvider;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.util.Fields;

public class MyClient {

	public static void main(String[] args) throws Exception {
		UrlConnectionSample http = new UrlConnectionSample();
		System.out.println("GET으로 데이터 가져오기");
		http.sendGet("http://127.0.0.1:8080/helloworld?param=param1");

		System.out.println("POST로 데이터 가져오기");
		String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

		http.sendPost("https://www.google.co.kr/", urlParameters);
	}

	public void sendGet(String url) throws Exception {
		HttpClient httpClient = new HttpClient();
		httpClient.start();
		Request request = httpClient.newRequest(url).method(HttpMethod.GET);
		//		request.header("", "");
		ContentResponse contentRes = request.send();
		System.out.println(contentRes.getContentAsString());
	}

	public void sendPost(String url, String parameters) throws Exception {
		HttpClient httpClient = new HttpClient();
		httpClient.start();
		Request request = httpClient.newRequest(url).method(HttpMethod.POST);
		//URI query param
		request.param("param", "hellllloooooooooooo");

		//for Header
		request.header("key", "value");

		//request.attribute for in jvm exchange
		request.attribute("param", "attributeAddeeeddd");
		request.attribute("param1", "aatribbsdlifjxdfijsldifjslifj");

		//Set form fields for Post Body (application/x-www-form-urlencoded body)
		Fields fields = new Fields();
		fields.put("fruit", "apple");
		request.content(new FormContentProvider(fields));

		//Set form multipart upload Post Body (multipart/form-data)
		//MultiPartContentProvider multiPart = new MultiPartContentProvider();
		//multiPart.addFieldPart("fruit", new StringContentProvider("apple"), null);
		//multiPart.close();
		//request.content(multiPart);

		// Set request body for json
		// request.content(new StringContentProvider(JSON_HERE), "application/json");

		// Set text/xml for body
		//request.content(new ByteBufferContentProvider("text/xml", ByteBuffer.wrap(SOMTHING)));

		ContentResponse contentRes = request.send();
		System.out.println(contentRes.getContentAsString());

	}

}
