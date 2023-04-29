package anonymyzer.factories;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;

import anonymyzer.PiazzaFaker;

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
			"will",
			"sun",
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
			"long-winded",
			"doIntersection",
			"anIn",
			"doFinish",
			"does",
			"double",
			"window",
			"Myer-Patel",
			"wait(long,int)",
			"anIntegers",
			"BeauAnderson",
//			"be An", // args should be An to Am
//			"an alternative",
//			"an expression",
//			"an integer",
//			"an interpretable",
//			"an infinite",
//			"an absence",
			"AnAbstract",
			"AnOOP",
			"AnOverview",
			"AnAutomatic",
			"anAbstract",
			"copyright Prasun Dewan"
//			"Double",
//			"projectName", 
//			"starttimestamp", 
//			"timestamp",
//			"random" 
			};
	@Override
	public String[] getKeywords() {
		return keywords;
	}
	@Override
	public void setKeywords(String[] newVal) {
		keywords = newVal;
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
	/**
	 * Can return the regex depending on the line
	 */
	public String keywordsRegex(String aLine) {
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
		
//		Matcher matcher = PiazzaFaker.fullNamePattern.matcher(aLine);
//
//		//"Bill Luo(zhennanl@live.unc.edu)
//		if (matcher.matches()) {
//			return null;			
//		}
		return keywordsRegex;
	}

//public static void main (String[] args) {
//	String[] aNullPattern = "hello world".split("");	
//}
}
