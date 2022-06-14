package adun.arena.snippet.sp.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * text 파일의 내용을 각 Line 별로 TO 클래스를 만들어서 리턴하는 샘플 코드.
 * 
 * ReadFile.readFile() 메소드를 사용할 수 있으며 사용하기 위해 TO 클래스와 각 라인을 파싱할 LineParser가
 * 필요하다.
 * 
 * keyvalue.txt의 내용이 key,value로 이루어져 있을때 Sample 사용 방식은 main 함수를 참고하라.
 *
 */
public class ReadFile {

	public static void main(String[] args) {
		File file = new File("./input/keyvalue.txt");
		List<SampleTo> list = ReadFile.readFile(file, new LineParser<SampleTo>() {
			@Override
			public SampleTo parse(String line) {
				String[] token = line.split("\\,");
				SampleTo to = new SampleTo();
				to.key = token[0];
				to.value = token[1];
				return to;

			}
		});
		for (SampleTo to : list) {
			System.out.println(to.key + ":" + to.value);
		}
	}

	/**
	 * 주어진 파일을 Line 별로 해석해서 T List로 만들어 리턴한다. 이때 각 Line을 T 로 parsing할 LineParser는
	 * 주어져야 한다.
	 * 
	 * @param <T>
	 * @param file
	 * @param parser
	 * @return
	 */
	public static <T> List<T> readFile(File file, LineParser<T> parser) {
		List<T> list = new ArrayList<>();
		try {
			List<String> lines = Files.readAllLines(file.toPath());
			for (String line : lines) {
				T to = parser.parse(line);
				if (to != null) {
					list.add(to);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return list;
	}

	public static interface LineParser<T> {
		public T parse(String line);
	}

	public static class SampleTo {
		public String key;
		public String value;
	}

}
