package adun.arena.spx.http.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * 
 * HttpURLConnection을 사용해서 get / post 로 접속한다. (client 역할임)
 *
 */
public class UrlConnectionSample {

	private final String USER_AGENT = "Mozilla/5.0";

	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception {

		UrlConnectionSample http = new UrlConnectionSample();

		//naver daum, google의 경우 너무 긴 라인이 있어서 console 창에 출력하면 IDE가 죽는다..

		System.out.println("GET으로 데이터 가져오기");
		String getBody = http.sendGet("https://httpbin.org/anything");

		System.out.println("POST로 데이터 가져오기");
		String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
		String postBody = http.sendPost("https://httpbin.org/anything", urlParameters);
	}

	/**
	 * 
	 * HttpURLConnection을 사용해서 GET 으로 접속하여 body 값을 리턴한다.
	 * 
	 */
	public String sendGet(String url) {

		try {

			HttpURLConnection con = (HttpURLConnection)(new URL(url).openConnection());

			con.setRequestMethod("GET"); // optional default is GET
			con.setRequestProperty("User-Agent", USER_AGENT); // add request header

			int responseCode = con.getResponseCode();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
				response.append("\n");
				System.out.println("read line : " + inputLine.length());
			}

			in.close();

			if (responseCode >= 400) {
				throw new RuntimeException("Get Call Failed : [" + responseCode + "] " + url);
			}

			System.out.println("HTTP 응답 코드 : \n " + responseCode);
			System.out.println("HTTP body size : \n " + response.length());
			System.out.println("HTTP body : \n " + response.toString());

			return response.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * 
	 * HttpURLConnection을 사용해서 POST 으로 접속하여 body 값을 리턴한다.
	 * 
	 * parameters는 key=value&key=value 형식의 String 값이어야 한다.
	 * 
	 */
	public String sendPost(String url, String parameters) throws Exception {
		try {
			HttpsURLConnection con = (HttpsURLConnection)(new URL(url).openConnection());

			con.setRequestMethod("POST"); // HTTP POST 메소드 설정
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setDoOutput(true); // POST 파라미터 전달을 위한 설정

			// POST data를 body로 보낸다.
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(parameters);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
				response.append("\n");
				System.out.println("read line : " + inputLine.length());
			}
			in.close();

			if (responseCode >= 400) {
				throw new RuntimeException("Get Call Failed : [" + responseCode + "] " + url);
			}

			// print result
			System.out.println("HTTP 응답 코드 : " + responseCode);
			System.out.println("HTTP body size : \n " + response.length());
			System.out.println("HTTP body : \n " + response.toString());

			return response.toString();

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
