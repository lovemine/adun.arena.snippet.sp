package adun.arena.sp.data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * text 파일의 내용을 각 Line 별로 읽어서 "Map 유형의" TO로 만들 때 참고한다. (Map으로 리턴)
 * 
 * SampleDTo 는 원하는 TO 형태로 변경하고 파싱 부분은 parseLine(String line)를 수정한다.
 *
 */
public class ReadMapDataFromFile {

	public static void main(String[] args) {

		File file = new File("./input/keyvalue.txt");
		Map<String, SampleDTo> map = new ReadMapDataFromFile().proccess(file);

		for (String key : map.keySet()) {
			SampleDTo to = map.get(key);
			System.out.println(to.key + ":" + to.value);
		}

	}

	/**
	 * file을 모두 읽은 다음 parseLine으로 SampleDto로 만들어서 Map으로 리턴한다.
	 * Map의 키는 SampleDto의 getKey()를 사용한다.
	 */
	public Map<String, SampleDTo> proccess(File file) {

		Map<String, SampleDTo> map = new LinkedHashMap<>();

		try {
			List<String> lines = Files.readAllLines(file.toPath());
			for (String line : lines) {
				SampleDTo to = parseLine(line);
				if (to != null) {
					map.put(to.getKey(), to);
				}
			}
			return map;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * line을 Parsing 해서 SampleTo로 만든다.
	 */
	public SampleDTo parseLine(String line) {
		SampleDTo to = new SampleDTo();
		String[] token = line.split("\\,");
		to.setKey(token[0]);
		to.setValue(token[1]);
		return to;
	}

	public static class SampleDTo {

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
