package adun.arena.sp.file;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @see https://www.baeldung.com/java-write-to-file (Java – Write to File)
 */
public class RandomFile {

	public static void writeToPosition(String filename, int data, long position) throws IOException {
		RandomAccessFile writer = new RandomAccessFile(filename, "rw");
		writer.seek(position);
		writer.writeInt(data);
		writer.close();
	}

	public static int readFromPosition(String filename, long position) throws IOException {
		int result = 0;
		RandomAccessFile reader = new RandomAccessFile(filename, "r");
		reader.seek(position);
		result = reader.readInt();
		reader.close();
		return result;
	}

}
