package adun.arena.sp.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * 
 * 간단한 파일 유틸을 제공한다.
 *
 */
public class FileUtil {

	/**
	 * 파일을 Copy한다. 이미 파일이 있따면 Replace 한다.
	 */
	public static void copyFile(File source, File target) {
		try {
			Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
