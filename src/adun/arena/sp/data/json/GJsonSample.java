package adun.arena.sp.data.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Gson을 사용하여 application/json 유형의 데이터를 처리하는 Sample을 보여준다.
 */
public class GJsonSample {

	public static void main(String[] args) {

		String json = "{ \"key\":\"value\" }";

		//gson 객체 생성
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		//JSON 에서 DTO 인스턴스로 매핑 (serialize)
		Test test = gson.fromJson(json, Test.class);
		System.out.println("* fromJson : " + test.getKey());

		//DTO 인스턴스를 JSON String으로 변환 (deserialize)
		String jsonString = gson.toJson(test);
		System.out.println(jsonString);

		//JsonParser를 사용하여 JSON을 JsonElement로 만듬. (바로 객체 매핑하는게 아니라 좀 더 다양한 parsing 로직 구현할 수 있음)
		JsonElement jsonElement = JsonParser.parseString(json);
		System.out.println("* JsonParser : " + jsonElement.toString());

	}

	public static class Test {

		private String key;

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

	}
}