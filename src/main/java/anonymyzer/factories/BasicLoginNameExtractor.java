package anonymyzer.factories;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BasicLoginNameExtractor implements LoginNameExtractor {
	static final Pattern MAC_USER = Pattern.compile("/Users/(.*?)/");
	static final Pattern WIN_USER = Pattern.compile("C:\\\\Users\\\\(.*?)\\\\");
	@Override
	public String extractLoginName(String aLinePossiblyContainingFileName) {
		String retVal = null;
		// /Users/username for mac C:\Users\\username\
		Matcher winMatcher = WIN_USER.matcher(aLinePossiblyContainingFileName);
		Matcher macMatcher = MAC_USER.matcher(aLinePossiblyContainingFileName);
//		String retVal = null;
		if (winMatcher.find()) {
			 retVal = winMatcher.group(1);
		}  else if (macMatcher.find()) {
			 retVal = macMatcher.group(1);
		}
		return retVal;
	}

}
