package adun.arena.spx.gist.lovemine;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CollectionSample {

	public void loopMap() {
		Map<String, String> map = new HashMap<>();
		/* Map Looping */
		Iterator<String> i = map.keySet().iterator();
		while (i.hasNext()) {
			String key = i.next();
			String velue = map.get(key);
		}

		for (Map.Entry<String, String> entry : map.entrySet()) {
			String key = entry.getKey();
			String velue = entry.getValue();
		}

		for (String key : map.keySet()) {
			String velue = map.get(key);
		}
	}

	public static class KeyValuePair {

		private String key;

		private String value;

		public KeyValuePair(String key) {
			this.key = key;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

	}
}
