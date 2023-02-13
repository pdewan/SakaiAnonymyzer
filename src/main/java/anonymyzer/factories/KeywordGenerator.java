package anonymyzer.factories;

import java.util.Set;

public interface KeywordGenerator {
	String[] getKeywords();

	Set<String> keywordsSet();

	String keywordsRegex();

}
