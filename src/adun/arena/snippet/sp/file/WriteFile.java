package adun.arena.snippet.sp.file;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class WriteFile {

	public static void main(String[] args) {
		List<String> lines = new ArrayList<String>();
		lines.add("abcde");
		lines.add("12345");

		File target = new File("./output/output.txt");

		WriteFile.writeFileNew(target, lines);
		//		WriteFile.writeFileAppend(target, lines);

	}

	public static void writeFileAppend(File target, List<String> lines) {
		try {
			Files.write(target.toPath(), lines, Charset.defaultCharset(), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void writeFileNew(File target, List<String> lines) {
		try {
			Files.write(target.toPath(), lines, Charset.defaultCharset(), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param target
	 * @param lines
	 * @param options <br/>
	 *            StandardOpenOption.WRITE : 쓰기 모드로 오픈한다.<br/>
	 *            StandardOpenOption.APPEND : 추가 모드로 오픈한다.<br/>
	 *            StandardOpenOption.TRUNCATE_EXISTING : 파일이 이미 있으면 WRITE 모드로 오픈하고 원래 있던 내용을 지운다.<br/>
	 *            StandardOpenOption.CREATE : 파일이 없으면 새 파일을 만든다. CREATE_NEW가 같이 설정되어 있으면 이옵션은 무시된다.<br/>
	 *            StandardOpenOption.CREATE_NEW : 새 파일을 생성하되 이미 파일이 존재하면 실패한다.
	 */
	public static void writeFile(File target, List<String> lines, OpenOption... options) {
		try {
			Files.write(target.toPath(), lines, Charset.defaultCharset(), options);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
