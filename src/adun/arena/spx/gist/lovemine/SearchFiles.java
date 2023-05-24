package adun.arena.spx.gist.lovemine;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SearchFiles {

	public Map<String, String> process(File dir, FileFilter filter) throws IOException {
		MyFileVisitor visitor = new MyFileVisitor(filter);
		Files.walkFileTree(dir.toPath(), visitor);
		Map<String, String> map = visitor.getMap();
		return map;
	}

	public class MyFileVisitor extends SimpleFileVisitor<Path> {

		final FileFilter filter;

		public MyFileVisitor(FileFilter filter) {
			this.filter = filter;
		}

		Map<String, String> map = new LinkedHashMap<String, String>();

		public Map<String, String> getMap() {
			return map;
		}

		@Override
		public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
			File file = path.toFile();
			if (filter.accept(file)) {
				processFile(file);
			}
			return FileVisitResult.CONTINUE;
		}

		private void processFile(File file) {
			try {
				List<String> lines = Files.readAllLines(file.toPath());

				for (String line : lines) {
					line = line.trim();
					map.put("", "");
				}

				Files.write(file.toPath(), lines, StandardOpenOption.WRITE);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}