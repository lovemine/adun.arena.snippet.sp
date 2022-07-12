package adun.arena.snippet.sp.data.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class GJsonSample {

	public static void main(String[] args) {

		String json = "{ \"key\":\"value\" }";

		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		//json > to
		Test test = gson.fromJson(json, Test.class);
		System.out.println("* fromJson : " + test.getKey());

		//to > json
		String jsonString = gson.toJson(test);
		System.out.println(jsonString);

		//Using JsonParser
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