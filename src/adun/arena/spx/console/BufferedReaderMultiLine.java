package adun.arena.spx.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Standard in으로 멀티 라인 라인 입력 받기. BufferedReader를 사용한다.
 * 
 */
public class BufferedReaderMultiLine {

	public static void main(String[] args) {

		// Standard in 으로 멀티라인을 입력받아서 List<Sting>으로 리턴 받는다.
		List<String> list = BufferedReaderMultiLine.read(new LineProcesser<String>() {

			@Override
			public String process(String line) {
				return "@echo : " + line;
			}
		});
		// 입력받은 List<Sting> 출력
		for (String line : list) {
			System.out.println(line);
		}
	}

	/**
	 * Standard in으로 'q' 입력 전까지 계속 입력 받기 사용자에게서 라인단위로 계속 값 입력 받아서 List<Sting>으로
	 * 리턴한다.
	 * 
	 * 각 Line 입력시 {@link LineProcesser#process(String)}을 callback한다.
	 */	public static <T> List<T> read(LineProcesser<T> processor) {
		List<T> list = new ArrayList<>();

		try (InputStreamReader isr = new InputStreamReader(System.in); BufferedReader br = new BufferedReader(isr);) {
			String line = "";
			while ((line = br.readLine()) != null) {
				if ("q".equals(line))
					break;
				T peer = processor.process(line); // 값 처리 (line)
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
