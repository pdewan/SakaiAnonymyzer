package anonymyzer.factories;

import java.util.Set;

public class KeywordFactory {
	static KeywordGenerator keywordGenerator = new BasicKeywordGenerator();

	public static KeywordGenerator getKeywordGenerator() {
		return keywordGenerator;
	}

	public static void setKeywordGenerator(KeywordGenerator newVal) {
		keywordGenerator = newVal;
	}
	
	public static String[] getKeywords() {
		return keywordGenerator.getKeywords();
	}
	public static Set<String> keywordsSet() {
		return keywordGenerator.keywordsSet();
	}

	public static String keywordsRegex(String aLine) {
		return keywordGenerator.keywordsRegex(aLine);

	}
	

}
