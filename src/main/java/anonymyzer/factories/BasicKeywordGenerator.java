package anonymyzer.factories;

import java.util.HashSet;
import java.util.Set;

public class BasicKeywordGenerator implements KeywordGenerator {	
	static String[] keywords = { 
//			"DiffBasedFileOpenCommand", 
//			"docASTNodeCount", 
//			"docActiveCodeLength",
//			"docActiveCodeLength", 
//			"docExpressionCount", 
//			"docLength", 
//			"Doc",
			"doc",
			"random",
//			"Random",
			"constant",
			"distance",
			"distancing",
//			"Distance",
			"undo",
//			"how do ",
//			"do you",
//			"do look",
//			"do not",
//			"Undo",
			"doPrivileged",
			"doIntersection",
			"doFinish",
			"does",
			"double",
			"window",
			"Myer-Patel",
			"wait(long,int)",
			"be An", // args should be An to Am
//			"an alternative",
//			"an expression",
//			"an integer",
//			"an interpretable",
//			"an infinite",
//			"an absence",
			"AnAbstract",
			"AnOOP",
			"copyright Prasun Dewan"
//			"Double",
//			"projectName", 
//			"starttimestamp", 
//			"timestamp",
//			"random" 
			};
	public String[] getKeywords() {
		return keywords;
	}
	protected Set<String> keywordsSet;
	
	@Override
	public Set<String> keywordsSet() {
		if (keywordsSet == null) {
			keywordsSet = new HashSet();
			for (String aString:getKeywords()) {				
				keywordsSet.add(aString);
				keywordsSet.add(capitalizeWordStart(aString));
			}

		}
		return keywordsSet;
	}
	public static String capitalizeWordStart(String aWord) {
		if (aWord.length() > 1) {
			return Character.toUpperCase(aWord.charAt(0)) + aWord.substring(1);
		}
		return aWord;
	}
	protected String keywordsRegex;
	@Override
	public String keywordsRegex() {
		if (keywordsRegex == null) {
			StringBuffer aStringBuffer = new StringBuffer();
			int anIndex = 0;
			for (String aKeyword : keywordsSet()) {
				if (anIndex > 0) {
					aStringBuffer.append("|");
				}
				aStringBuffer.append(aKeyword);
				anIndex++;

			}
			keywordsRegex = aStringBuffer.toString();
		}
		return keywordsRegex;
	}
}
