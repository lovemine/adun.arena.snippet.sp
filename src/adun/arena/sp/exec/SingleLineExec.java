package adun.arena.sp.exec;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SingleLineExec {

	public static void main(String[] args) {
		File cmd = new File("./input/hello.exe");

		List<String> lines = SingleLineExec.execCmd(//
				new String[] { // 
						cmd.getAbsolutePath(), //
						"echo-from-exec" //
				});

		for (String line : lines) {
			System.out.println(line);
		}

	}

	public static List<String> execCmd(String... commands) {
		List<String> lines = new ArrayList<String>();

		try {

			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commands);
			builder.redirectErrorStream(true); // so we can ignore the error stream
			//processBuilder.command("cmd.exe", "/c", "dir C:\\Users\\mkyong"); // Run a command
			//processBuilder.command("C:\\Users\\mkyong\\hello.bat"); //Run a bat file

			Process process = builder.start();

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}

			int exitVal = process.waitFor();
			if (exitVal == 0) {
				return lines;
			} else {
				throw new RuntimeException("abnormal exec (" + exitVal + ")");
			}

		} catch (IOException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static void printResults(Process process) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line = "";
		while ((line = reader.readLine()) != null) {
			System.out.println(line);
		}
	}

}
