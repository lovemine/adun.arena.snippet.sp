package adun.arena.snippet.sp.console;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReadMultiLine {

	public static void main(String[] args) {
		List<String> list = ReadMultiLine.read(new LineProcesser<String>() {

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
		try (Scanner scanner = new Scanner(System.in)) {
			while (true) {
				System.out.print("VALUE : ");
				String line = scanner.nextLine();
				if ("q".equals(line)) break;
				T peer = processor.process(line); //값 처리 (line)
				if (peer != null) {
					list.add(peer);
				}
			}
		}
		return list;
	}

	public static interface LineProcesser<T> {

		public T process(String line);
	}
}
