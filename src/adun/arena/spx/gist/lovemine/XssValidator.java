package adun.arena.spx.gist.lovemine;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XssValidator extends AbstractValidator {

	private final boolean autoCorrect;

	private final boolean throwError;

	private final boolean inferEvasion;

	private final static int criticalLength = 8;

	private final List allowTags;

	private final List denyTags;

	private final Pattern regRc;

	private final Pattern regRcIntag;

	private final IProcessingRule allowProcessingRule;

	private final IProcessingRule denyProcessingRule;

	private final IProcessingRule strictProcessingRule;

	private static final Pattern REG_EVENT = Pattern.compile("(\\s+)([a-zA-Z]+)(.*?)(=)(.+?[\\(|\\#].+?)", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE | Pattern.DOTALL);

	private static final Pattern REG_TAG = Pattern.compile("<((/?)([a-zA-Z]+)([^<>]*)(\\s*/?))>?", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE | Pattern.DOTALL);

	public XssValidator(String id, Map option, MessageIF messagePlugin) {
		super(id, option, messagePlugin);
		autoCorrect = new Cast(option.get("auto-correct")).cboolean();
		throwError = new Cast(option.get("throw-error")).cboolean();
		inferEvasion = new Cast(option.get("infer-evasion")).cboolean();
		allowTags = convertToList((String)option.get("allow-tags"));
		denyTags = convertToList((String)option.get("deny-tags"));
		regRc = compileReplaceCharacterPattern((String)option.get("replace-character"));
		regRcIntag = compileReplaceCharacterPattern((String)option.get("intag-replace-character"));
		allowProcessingRule = new AllowProcessingRule();
		denyProcessingRule = new DenyProcessingRule();
		strictProcessingRule = new StrictProcessingRule();
	}

	public String validate(String value, String title, String exparam) throws ValidationException {
		if (None.isNone(value)) return value;
		IProcessingRule rule = null;
		value = value.replaceAll("\0", "");
		if (!None.isNone(allowTags)) {
			rule = allowProcessingRule;
		} else if (!None.isNone(denyTags)) {
			rule = denyProcessingRule;
		} else {
			rule = strictProcessingRule;
		}
		return processValidation(rule, value, title);
	}

	private interface IProcessingRule {

		public boolean need(String tag);

	}

	private class AllowProcessingRule implements IProcessingRule {

		public boolean need(String tag) {
			return !allowTags.contains(tag);
		}
	}

	private class DenyProcessingRule implements IProcessingRule {

		public boolean need(String tag) {
			return denyTags.contains(tag);
		}
	}

	private class StrictProcessingRule implements IProcessingRule {

		public boolean need(String tag) {
			return true;
		}
	}

	protected String processValidation(IProcessingRule rule, String value, String title) throws ValidationException {
		if (!throwError && !autoCorrect) return value;
		StringBuffer sb = new StringBuffer();
		Matcher matcher = REG_TAG.matcher(value);
		int last = 0;
		while (matcher.find()) {
			//matcher group count would be 5
			//0:all match, 1:all inner tag without <>, 2:/?\\s*, 3:tag, 4:inner tag, 5:\\s*/?
			String sub = value.substring(last, matcher.start());
			String tag = matcher.group(3).toLowerCase();
			if (!HtmlTags.tagList.contains(tag)) continue;
			if (rule.need(tag)) {//if found denied tag
				if (throwError) {
					throw new ValidationException(getMessage(title));
				} else if (autoCorrect) {
					sb.append(processTextString(regRc, sub, title));
					sb.append("&#60;");
					sb.append(matcher.group(2));
					sb.append(matcher.group(3));
					sb.append(processTextString(regRcIntag, matcher.group(4), title));
					sb.append(matcher.group(5));
					sb.append("&#62;");
				}
			} else {
				sb.append(processTextString(regRc, sub, title)).append(matcher.group(0));
			}
			last = matcher.end();
		}

		sb.append(processTextString(regRc, value.substring(last), title));
		return sb.toString();
	}

	protected String processTextString(Pattern regReplace, String value, String title) throws ValidationException {
		if (value.length() < criticalLength) {
			return value;
		}
		if (inferEvasion) {
			value = processHackEventString(value, title);
		}
		return processReplaceCharacter(regReplace, value, title);
	}

	protected String processHackEventString(String value, String title) throws ValidationException {
		StringBuffer sb = new StringBuffer();
		Matcher matcher = REG_EVENT.matcher(value);
		int last = 0;
		while (matcher.find()) {
			//0:all match, 1:\\s+, 2:onxxx, 3:.*?, 4:=, 5:.+?[\\(|\\#].+? 
			String sub = value.substring(last, matcher.start());
			String event = matcher.group(2).toLowerCase();
			if (!HtmlTags.eventList.contains(event)) continue;
			if (throwError) {
				throw new ValidationException(getMessage(title));
			} else if (autoCorrect) {
				sb.append(sub);
				sb.append(matcher.group(1));
				sb.append(matcher.group(2));
				sb.append(matcher.group(3));
				sb.append("&#61;");
				sb.append(matcher.group(5));
			} else {
				sb.append(sub).append(matcher.group(0));
			}
			last = matcher.end();
		}

		sb.append(value.substring(last));
		return sb.toString();
	}

	protected String processReplaceCharacter(Pattern pattern, String value, String title) throws ValidationException {
		if (pattern == null) return value;
		Matcher matcher = pattern.matcher(value);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			if (throwError) {
				throw new ValidationException(getMessage(title));
			} else if (autoCorrect) {
				int ch = matcher.group(0).charAt(0);
				matcher.appendReplacement(sb, "&#" + ch + ";");
			}
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	protected String getMessage(String title) {
		return messagePlugin.getMessage(getId(), title);
	}

	protected List convertToList(String tags) {
		if (tags == null || tags.length() == 0) return null;
		List list = new LinkedList();
		String[] tokens = tags.split("\\,");
		for (int idx = 0; idx < tokens.length; idx++) {
			list.add(tokens[idx].trim().toLowerCase());
		}
		return list;
	}

	protected Pattern compileReplaceCharacterPattern(String rcs) {
		if (None.isNone(rcs)) return null;
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		char[] rc = rcs.toCharArray();
		for (int idx = 0; idx < rc.length; idx++) {
			sb.append("\\").append(rc[idx]);
			//[] 안에 | 는 or 의미가 아니라 char 의미로 작동함  
			//if (idx != rc.length - 1) sb.append("|");
		}
		sb.append("]");
		return Pattern.compile(sb.toString(), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE | Pattern.DOTALL);
	}

	private static final Pattern REG_STYPE = Pattern.compile("\\s+style\\s*=.+\\(.+", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE | Pattern.DOTALL);

	private static final Pattern REG_SRC = Pattern.compile("\\s+src\\s*=.+\\(.+", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE | Pattern.DOTALL);

}