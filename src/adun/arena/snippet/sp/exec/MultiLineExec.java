package adun.arena.snippet.sp.exec;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MultiLineExec {

	public static void main(String[] args) {
		File cmd = new File("./input/hello-multi.exe");
		List<String> inputs = new ArrayList<String>();
		inputs.add("1234");
		inputs.add("abcd");
		inputs.add("9999");
		List<String> lines = MultiLineExec.execCmd(//
				inputs, new String[] { // 
						cmd.getAbsolutePath(), //
						"echo-from-exec" });

		for (String line : lines) {
			System.out.println(line);
		}

	}

	public static List<String> execCmd(List<String> inputs, String... commands) {
		List<String> lines = new ArrayList<String>();

		try {

			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commands);
			builder.redirectErrorStream(true);

			Process process = builder.start();

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

			//add first line
			lines.add(reader.readLine());

			//send a line and read a line
			for (String input : inputs) {
				writer.write(input);
				writer.write("\n");
				writer.flush();
				lines.add(reader.readLine());
			}
			return lines;

		} catch (IOException e) {
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
