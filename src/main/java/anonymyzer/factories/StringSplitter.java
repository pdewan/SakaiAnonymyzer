package anonymyzer.factories;

import java.util.List;

public interface StringSplitter {
	String[] splitByKeywords(String aLine, String aKeywordRegex);

}
