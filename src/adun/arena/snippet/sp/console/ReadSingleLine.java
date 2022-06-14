package adun.arena.snippet.sp.console;

import java.util.Scanner;

public class ReadSingleLine {

	/**
	 * Standard in으로 1라인 입력 받기
	 */
	public static String read() {

		try (Scanner scanner = new Scanner(System.in)) {
			// 값 입력 전에 Console에 표시할 내용. println()이 아니고 print()임.
			System.out.print("VALUE : ");

			// 값 받기
			return scanner.nextLine();
		}
	}

}
