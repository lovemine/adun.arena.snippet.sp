package adun.arena.spx.gist.lovemine;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternMatcher {

	private static final Pattern REG_EVENT = Pattern.compile("(\\s+)([a-zA-Z]+)(.*?)(=)(.+?[\\(|\\#].+?)", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE | Pattern.DOTALL);

	private static final Pattern REG_TAG = Pattern.compile("<((/?)([a-zA-Z]+)([^<>]*)(\\s*/?))>?", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE | Pattern.DOTALL);

	public static void main(String[] args) {
		String line = "start.. <script src=\"..src\"> script </script> last...";
		System.out.println(line);
		System.out.println(process(line));
	}

	protected static String process(String value) {
		StringBuffer sb = new StringBuffer();
		Matcher matcher = REG_TAG.matcher(value);
		int last = 0;
		while (matcher.find()) {
			// matcher group count would be 5
			// 0:all match, 1:all inner tag without <>, 2:/?\\s*, 3:tag, 4:inner tag,
			// 5:\\s*/?
			String sub = value.substring(last, matcher.start());
			String tag = matcher.group(3).toLowerCase();
			if (tag.equals("script")) {// if found match tag
				sb.append(sub); //match 되지 않은 문자열
				sb.append("&#60;");
				sb.append(matcher.group(2)); //앞공백
				sb.append(matcher.group(3)); //tag
				sb.append(matcher.group(4)); //attributes
				sb.append(matcher.group(5)); //뒷공백
				sb.append("&#62;");
			} else {
				sb.append(sub).append(matcher.group(0));
			}
			last = matcher.end();
		}

		sb.append(value.substring(last)); //match 되지 않은 문자열 (마지막)
		return sb.toString();
	}
}
