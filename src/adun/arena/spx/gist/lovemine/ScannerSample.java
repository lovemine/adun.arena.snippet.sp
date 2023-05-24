package adun.arena.spx.gist.lovemine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class ScannerSample {

	/**
	 * 사용자에게서 1개의 값 입력 받기
	 */
	public static void readALineUsingScanner() {
		Scanner scanner = new Scanner(System.in);

		// 값 입력 전에 Console에 표시할 내용. println()이 아니고 print()임.
		System.out.print("VALUE : ");

		// 값 받기
		String line = scanner.nextLine();

		scanner.close();

		//		 [[값 처리 (line)]]
	}

	/**
	 * 사용자에게서 라인단위로 계속 값 입력 받기
	 */
	public static void readLineUsingScanner() {
		Scanner scanner = new Scanner(System.in);

		while (true) {
			// 값 입력 전에 Console에 표시할 내용. println()이 아니고 print()임.
			System.out.print("VALUE : ");

			// 값 받기
			String line = scanner.nextLine();

			// 종료 문자면 break
			if ("q".equals(line)) break;

			//		    [[값 처리 (line)]]
		}

		scanner.close();
	}

	/**
	 * BufferedReader를 이용한 문자 입력받기. 키보드에서 한 줄 단위로 입력을 받는다.
	 */
	public static void readLine() {
		try {
			InputStreamReader isr = new InputStreamReader(System.in);
			BufferedReader br = new BufferedReader(isr);
			String s = "";
			System.out.println("끝내고 싶다면 ctrl+c를 입력하시오");// ctrl+c
			// while(!(s=br.readLine()).equals("@esc")){//@esc
			while ((s = br.readLine()) != null) {// ctrl+c
				System.out.println(s);// 출력
			}
			br.close();
			isr.close();
		} catch (Exception ee) {
			System.out.println(ee.toString());
		}
	}
}
