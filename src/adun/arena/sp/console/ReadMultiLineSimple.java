package adun.arena.sp.console;

import java.util.Scanner;

/**
 *
 * Standard in으로 멀티 라인 라인 입력 받기
 *
 */
public class ReadMultiLineSimple {

	public static void main(String[] args) {

		try (Scanner scanner = new Scanner(System.in)) {
			while (true) {
				System.out.print("VALUE : ");
				String line = scanner.nextLine();
				if ("q".equals(line)) {
					break;
				}
				// 여기서 line 값을 처리하던지 한다.
				// System.out.println(line);
			}
		}
	}

}
