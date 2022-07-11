package adun.arena.snippet.sp.data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * text 파일의 내용을 각 Line 별로 읽어서 TO로 만들 때 참고한다. (Map으로 리턴)
 * 
 * SampleTo 는 원하는 TO 형태로 변경하고 파싱 부분은 parseLine(String line)를 수정한다.
 *
 */
public class ReadMapDataFromFile {

	public static void main(String[] args) {

		File file = new File("./input/keyvalue.txt");
		Map<String, SampleTo> map = new ReadMapDataFromFile().proccess(file);

		for (String key : map.keySet()) {
			SampleTo to = map.get(key);
			System.out.println(to.key + ":" + to.value);
		}

	}

	public Map<String, SampleTo> proccess(File file) {

		Map<String, SampleTo> map = new LinkedHashMap<>();

		try {
			List<String> lines = Files.readAllLines(file.toPath());
			for (String line : lines) {
				SampleTo to = parseLine(line);
				if (to != null) {
					map.put(to.getKey(), to);
				}
			}
			return map;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public SampleTo parseLine(String line) {
		SampleTo to = new SampleTo();
		String[] token = line.split("\\,");
		to.setKey(token[0]);
		to.setValue(token[1]);
		return to;
	}

	public static class SampleTo {

		public String key;

		public String value;

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
