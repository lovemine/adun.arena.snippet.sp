package adun.arena.sp.file;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;

/**
 * 다양한 Stream을 copy 혹은 redirect 시키는 Utility
 *
 */
public class StreamCopy {

	public static final int BUFFER_SIZE = 8192;

	public static void copy(URL url, OutputStream out) throws IOException {
		copy(url.openStream(), out);
	}

	public static void copy(InputStream in, OutputStream out) throws IOException {
		try {
			redirect(in, out);
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException ie) {
			}
			try {
				if (out != null)
					out.close();
			} catch (IOException ie) {
			}
		}
	}

	public static void redirect(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[BUFFER_SIZE];
		int nrOfBytes = -1;
		while ((nrOfBytes = in.read(buffer)) != -1) {
			out.write(buffer, 0, nrOfBytes);
		}
		out.flush();
	}

	public static void copy(byte[] in, OutputStream out) throws IOException {
		copy(new ByteArrayInputStream(in), out);
	}

	public static byte[] copyToByteArray(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		copy(in, out);
		return out.toByteArray();
	}

	public static void copy(Reader in, Writer out) throws IOException {
		try {
			redirect(in, out);
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException ie) {
			}
			try {
				if (out != null)
					out.close();
			} catch (IOException ie) {
			}
		}
	}

	public static void redirect(Reader in, Writer out) throws IOException {
		char[] buffer = new char[BUFFER_SIZE];
		int nrOfBytes = -1;
		while ((nrOfBytes = in.read(buffer)) != -1) {
			out.write(buffer, 0, nrOfBytes);
		}
		out.flush();
	}

	public static void copy(String in, Writer out) throws IOException {
		copy(new StringReader(in), out);
	}

	public static String toString(Reader in) throws IOException {
		StringWriter out = new StringWriter();
		copy(in, out);
		return out.toString();
	}

	public static String toString(InputStream in, String encoding) throws IOException {
		return toString(new InputStreamReader(in, encoding));
	}

	public static String toString(InputStream in) throws IOException {
		return toString(new InputStreamReader(in));
	}

}
