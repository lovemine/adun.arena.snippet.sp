package adun.arena.sp.console;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * Standard in으로 멀티 라인 라인 입력 받기
 *
 */
public class ReadMultiLine {

	public static void main(String[] args) {

		// Standard in 으로 멀티라인을 입력받아서 List<Sting>으로 리턴 받는다.
		List<String> list = ReadMultiLine.read(new LineProcesser<String>() {

			@Override
			public String process(String line) {
				return "@echo : " + line;
			}
		});

		//입력받은 List<Sting> 출력
		for (String line : list) {
			System.out.println(line);
		}
	}

	/**
	 * Standard in으로 'q' 입력 전까지 계속 입력 받기 사용자에게서 라인단위로 계속 값 입력 받아서 List<Sting>으로
	 * 리턴한다.
	 * 
	 * 각 Line 입력시 {@link LineProcesser#process(String)}을 callback한다.
	 */
	public static <T> List<T> read(LineProcesser<T> processor) {
		List<T> list = new ArrayList<>();
		try (Scanner scanner = new Scanner(System.in)) {
			while (true) {
				System.out.print("VALUE : ");
				String line = scanner.nextLine();
				if ("q".equals(line))
					break;
				T peer = processor.process(line); // 값 처리 (line)
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
