package adun.arena.snippet.sp.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class BufferedReaderMultiLine {

	public static void main(String[] args) {
		List<String> list = BufferedReaderMultiLine.read(new LineProcesser<String>() {

			@Override
			public String process(String line) {
				return "@echo : " + line;
			}
		});

		for (String line : list) {
			System.out.println(line);
		}
	}

	/**
	 * Standard in으로 'q' 입력 전까지 계속 입력 받기
	 * 사용자에게서 라인단위로 계속 값 입력 받기
	 */
	public static <T> List<T> read(LineProcesser<T> processor) {
		List<T> list = new ArrayList<>();

		try (InputStreamReader isr = new InputStreamReader(System.in); BufferedReader br = new BufferedReader(isr);) {
			String line = "";
			while ((line = br.readLine()) != null) {
				if ("q".equals(line)) break;
				T peer = processor.process(line); //값 처리 (line)
				if (peer != null) {
					list.add(peer);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return list;
	}

	public static interface LineProcesser<T> {

		public T process(String line);
	}
}
