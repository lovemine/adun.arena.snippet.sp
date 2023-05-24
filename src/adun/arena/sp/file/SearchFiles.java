package adun.arena.sp.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 특정 폴더를 기준으로 모든 파일 / 하위 폴더를 검색한다.
 *
 *
 */
public class SearchFiles {

	public static void main(String[] args) {

		File dir = new File("./input");

		MyFileVisitor visitor = new SearchFiles().process(dir);
		Map<String, String> map = visitor.getMap();

		for (String key : map.keySet()) {
			System.out.println(key + ":" + map.get(key));
		}

	}

	public MyFileVisitor process(File dir) {
		MyFileVisitor visitor = new MyFileVisitor();
		try {
			Files.walkFileTree(dir.toPath(), visitor);
			return visitor;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	public class MyFileVisitor extends SimpleFileVisitor<Path> {

		public MyFileVisitor() {}

		Map<String, String> map = new LinkedHashMap<String, String>();

		public Map<String, String> getMap() {
			return map;
		}

		@Override
		public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
			File file = path.toFile();
			System.out.println("[F] : " + file);
			map.put(file.getName(), file.getAbsolutePath());
			return FileVisitResult.CONTINUE; //TERMINATE
		}

		/**
		 * directory를 들어가기 전에 호출된다. (최 상위 Root 디렉토리도 호출된다.)
		 */
		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			System.out.println("[PRE] : " + dir.toFile());
			return FileVisitResult.CONTINUE;  //TERMINATE, SKIP_SUBTREE
		}

		/**
		 * directory를 나가면서 호출된다.
		 */
		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
			System.out.println("[POST] : " + dir.toFile());
			return FileVisitResult.CONTINUE;   //TERMINATE, SKIP_SIBLINGS
		}

	}

}
