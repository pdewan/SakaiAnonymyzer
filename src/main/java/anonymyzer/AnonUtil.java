package anonymyzer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnonUtil {
	public static String[] splitCamelCaseHyphenDash(String aCamelCaseName) {
		// return
		// aCamelCaseName.split("(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])|(?<=[0-9])(?=[A-Z][a-z])|(?<=[a-zA-Z])(?=[0-9])");
		// return
		// aCamelCaseName.split("_|-|\\.|(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])|(?<=[0-9])(?=[A-Z][a-z])|(?<=[a-zA-Z])(?=[0-9])");
		String[] aComponents = aCamelCaseName.split(
				"@|\\+|_|-|\\.|(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])|(?<=[0-9])(?=[A-Z][a-z])|(?<=[a-zA-Z])(?=[0-9])");

		for (int anIndex = 0; anIndex < aComponents.length; anIndex++) {
			String[] aSplit = aComponents[anIndex].split("\\d+");
			if (aSplit.length > 1) {
				aComponents[anIndex] = aSplit[1];
			}
		}
		return aComponents;
	}

	static String idRegex = "\\b_*[a-zA-Z][_a-zA-Z0-9]*\\b";

	public static List<String> getIdentifiers(String aLine) {
		List<String> list = new ArrayList<String>();
		Matcher matcher = Pattern.compile(idRegex).matcher(aLine);
		while (matcher.find())
			list.add(matcher.group(0));
		return list;
	}
//		String aSplitKeyword = "Command __id|DiffBasedFileOpenCommand|docASTNodeCount|docActiveCodeLength|docExpressionCount|docLength|projectName|starttimestamp|timestamp";

//	static String[] keywords = {
//			"DiffBasedFileOpenCommand",
//			"docASTNodeCount",
//			"docActiveCodeLength",
//			"docActiveCodeLength",
//			"docExpressionCount",
//			"docLength",
//			"projectName",
//			"starttimestamp",
//			"timestamp",
//			"random"
//	};
//
//	static Set<String> keywordsSet = new HashSet(Arrays.asList(keywords));
	

	public static List<String> getComponents(List<String> anIdentifiers) {
		List<String> retVal = new ArrayList();
		for (String anIdentifier : anIdentifiers) {
			String[] aComponents = splitCamelCaseHyphenDash(anIdentifier);
			retVal.addAll(Arrays.asList(aComponents));
		}
		return retVal;

	}

	public static boolean isNameInLineKeyword(Set<String> aKeywordsSet, String aLine, List<String> aLineTokens, List<String> aNames) {
//	 if (aLine.contains("docASTNodeCount".toLowerCase()) &&
//			 aNames.contains("Do")) {
//		 System.out.println ("found problematic keyword");
//	 }
		if (!hasNonkeyWordCaseSensitiveMatch(aKeywordsSet, aLine, aLineTokens, aKeywordsSet)) {
			return false;
		}
		;
		for (String aName : aNames) {
			for (String aKeyword : aKeywordsSet) {
//				if (aKeyword.toLowerCase().contains(aName.toLowerCase())) {
				if (aKeyword.contains(aName)) {

					return true;
				}
			}
		}
		return false;
	}

	public static String getIdentifierFollowing(String aLine, String aPrecedingToken) {
		try {
			List<String> anIdentifiers = getIdentifiers(aLine);
			int aPrecedingTokenIndex = anIdentifiers.indexOf(aPrecedingToken);

//	 if (aPrecedingTokenIndex < 0) {
//		 System.err.println("Token " + aPrecedingToken + " not in " + aLine);
//		 return null;
//	 }

			return anIdentifiers.get(aPrecedingTokenIndex + 1);
		} catch (Exception e) {
			System.err.println("No token following " + aPrecedingToken + " in " + aLine);
//		e.printStackTrace();
			return null;
		}

	}

	public static String[] getComponentsFollowing(String aLine, String aPrecedingToken) {
		String anIdentifier = getIdentifierFollowing(aLine, aPrecedingToken);
		return splitCamelCaseHyphenDash(anIdentifier);
	}

	public static boolean containsNameFollowing(String aLine, String aName, String aPrecedingToken) {
		String[] aTokens = getComponentsFollowing(aLine, aPrecedingToken);
		return Arrays.asList(aTokens).contains(aName);
	}

	/*
	 * At this point we know that aString does have a substring that matches one of
	 * the candidate words .
	 * 
	 * So let us find out if the a word in the sentence matches one of the candidate
	 * word and the matching word is not part of a bigger reserved or key token.
	 * 
	 * 
	 */
	public static boolean hasNonkeyWordCaseSensitiveMatch(Set<String> aKeywordsSet, String aString, List<String> aStringTokens, Set<String> aWords) {
		return findNonKeywordMatches(aKeywordsSet, aString, aStringTokens, aWords).size() != 0;
//	 StringTokenizer aTokenizedString = new StringTokenizer(aString, "./\\ 	,;![]()+-*\"'>");
//	 while (aTokenizedString.hasMoreTokens()) {
//		 String aToken = aTokenizedString.nextToken();
//		 for (String aKeyword:aKeywords) {
//			 if (aToken.toLowerCase().equals(aKeyword)) {// token ma have extra chars
//				 return true;
//			 }
//		 }
//	 }
//	 
//	 return false;
	}

	static String tokenDelimiters = "=./\\ 	,;![]()+-*\"'><:";

	public static List<String> getTokens(String aString) {
		List<String> retVal = new ArrayList();
		StringTokenizer aTokenizedString = new StringTokenizer(aString, tokenDelimiters);
		while (aTokenizedString.hasMoreTokens()) {
			retVal.add(aTokenizedString.nextToken());
		}
		return retVal;

	}

// public static String findFirstKeyword (String aString, Set<String> aKeywords) {
//	 StringTokenizer aTokenizedString = new StringTokenizer(aString, "=./\\ 	,;![]()+-*\"'><:%");
//	 while (aTokenizedString.hasMoreTokens()) {
//		 String aToken = aTokenizedString.nextToken();
////		 System.out.println("Token:" + aToken);
//
//		 String[] aComponents = splitCamelCaseHyphenDash(aToken);
//		 for (String aComponent:aComponents) {
//			 System.out.println("Component:" + aComponent); 
//		 for (String aKeyword:aKeywords) {
//			 if (aComponent.toLowerCase().equals(aKeyword.toLowerCase())) {// token ma have extra chars
//
////			 if (aToken.equals(aKeyword.toLowerCase())) {// token ma have extra chars
//				 return aKeyword;
//			 }
//		 }
//		 }
//	 }
//	 
//	 return null;
// }
	public static int numMatches (String aString, List<String> aCandidateWords) {
		String aNormalizedString = aString.toLowerCase();
		int retVal = 0;
		for (String aCandidateWord:aCandidateWords) {
			if (aNormalizedString.contains(aCandidateWord.toLowerCase())) {
				retVal++;
			}
		}
		return retVal;
	}
	
	public static int numDisjointMatches (String aString, List<String> aCandidateWords) {
		Map<String, List<Integer>>  aWordToIndices = indicesOf(aString, aCandidateWords);
		Map<Integer, String> anIndexKeyMap = toIndexKeys(aWordToIndices);
		List<Integer> anIndices = getSortedDisjointIndices(anIndexKeyMap);
		return anIndices.size();
//		String aNormalizedString = aString.toLowerCase();
//		int retVal = 0;
//		for (String aCandidateWord:aCandidateWords) {
//			if (aNormalizedString.contains(aCandidateWord.toLowerCase())) {
//				retVal++;
//			}
//		}
//		return retVal;
	}
	
	public static boolean isOppositeCase (char aChar1, char aChar2) {
		return (Character.isUpperCase(aChar1) && Character.isLowerCase(aChar2)) ||
				(Character.isLowerCase(aChar1) && Character.isUpperCase(aChar2));
	}
	public static boolean isIdentifierCharacter(char ch) {
		return Character.isDigit(ch) || Character.isLetter(ch) ||
				ch == '-' || ch == '-';
	}
	public static boolean hasLeftDelimiter (String aString, int aNameStartIndex) {
		if (aNameStartIndex == 0) return true;
		char aStart = aString.charAt(aNameStartIndex);
		char aPredecessor = aString.charAt(aNameStartIndex -1);
		return isDelimiter(aStart, aPredecessor);
	
	}
	public static boolean isDelimiter (char aChar, char anAdjacentChar) {
		return !isIdentifierCharacter(anAdjacentChar) || isOppositeCase(aChar, anAdjacentChar);
	}
	
	public static boolean hasRightDelimiter (String aString, int aNameEndIndex) {
		if (aNameEndIndex == aString.length()) return true;	
		char anEnd = aString.charAt(aNameEndIndex-1);
		char aSuccessor = aString.charAt(aNameEndIndex);
		return isDelimiter(anEnd, aSuccessor);

		
	
	}
	
	public static boolean occurenceIsAWord(String aString, String aName, int aNameStartIndex) {
		int aNameEndIndex = aNameStartIndex + aName.length();
		return hasLeftDelimiter(aString, aNameStartIndex) && hasRightDelimiter(aString, aNameEndIndex);
		
	}
	
	public static List<Integer> indicesOf(String aString, String aSubstring) {
		List<Integer> retVal = new ArrayList();
		for (int anIndex = 0; (anIndex = aString.indexOf(aSubstring, anIndex)) >= 0; anIndex++) { 			
			retVal.add(anIndex); 
		}
		return retVal;
	}
	

	public static Map<String, List<Integer>> indicesOf  (String aString, List<String> aCandidateWords) {
		Map<String, List<Integer>>  retVal = new HashMap();
		for (String aCandidateWord:aCandidateWords) {
			retVal.put (aCandidateWord, indicesOf(aString, aCandidateWord));
		}
		return retVal;
		
	}
	
	public static Map<Integer, String> toIndexKeys (Map<String, List<Integer>> aStringKeys) {
		Map<Integer, String> retVal = new HashMap();
		for (String aString: aStringKeys.keySet()) {
			List<Integer> anIndices = aStringKeys.get(aString);
			for (Integer anIndex: anIndices) {
				String anExisting = retVal.get(anIndex);
				if (anExisting == null || anExisting.length() < aString.length())
				   retVal.put(anIndex, aString);
			}
		}
		return retVal;
	}
	public static final int MAX_LEFT_SURROUNDING_LENGTH = 5;
	public static final int MAX_RIGHT_SURROUNDING_LENGTH = 10;

	public static String fragmentWithContext(String aString, String aFragment,  int anIndexOfFragment, int aFragmentEnd) {
//		int aFragmentEnd = anIndexOfFragment + aFragment.length();
		int aPrefixLength = Math.min(MAX_LEFT_SURROUNDING_LENGTH, anIndexOfFragment);
		int aSuffixLength = Math.min(MAX_RIGHT_SURROUNDING_LENGTH, aString.length() - aFragmentEnd);
		String aContextPrefix = aString.substring(anIndexOfFragment-aPrefixLength, anIndexOfFragment);
		if (aSuffixLength < 0) {
			System.out.println("suffixlength " + aSuffixLength);
		}
		String aContextSuffix = aString.substring(aFragmentEnd, aFragmentEnd+aSuffixLength);
		return "..." + aContextPrefix + "(" + aFragment + ")" + aContextSuffix + "...";
		
	}
	
	public static List<String> fragmentsWithContext(String aString, Map<Integer, String> anIndexToFragment) {
		List<String> retVal = new ArrayList();
		List<Integer> anIndices = new ArrayList(anIndexToFragment.keySet());
		Collections.sort(anIndices);
		for (Integer anIndex:anIndices) {
			String aFragment = anIndexToFragment.get(anIndex);
//			retVal.add(anIndex + " " + fragmentWithContext(aString, aFragment, anIndex));
			retVal.add(anIndex + " " + fragmentWithContext(aString, aFragment, anIndex, anIndex + aFragment.length()));

		}
		return retVal;
	}
	
	public static List<String> fragmentsWithContext(String aString, List<String> aFragments) {
		Map<Integer, String> anIndexToFragment = indexToFragment(aString,aFragments);
		return fragmentsWithContext(aString, anIndexToFragment);
	}
	
	public static Map<Integer, String> indexToFragment (String aString, List<String> aFragments) {
		Map<String, List<Integer>> anIndicesOfFragments= indicesOf(aString, aFragments);
		return toIndexKeys(anIndicesOfFragments);
	}
	
	public static List<Integer> getSortedDisjointIndices(Map<Integer, String> anIndexMap) {
		List<Integer> anIndices = new ArrayList(anIndexMap.keySet());
		Collections.sort(anIndices);
		List<Integer> retVal = new ArrayList();
		int aLastFragmentEnd = 0;
		for (Integer anIndex:anIndices) {
			if (anIndex < aLastFragmentEnd) {
				continue;
			}
			retVal.add(anIndex);
			aLastFragmentEnd = anIndex + anIndexMap.get(anIndex).length();
			
		}
		return retVal;
	}
	
	public static int replaceAll(StringBuffer aReplacementsWithContext, StringBuffer retVal, FileWriter aLogger, String aString, Map<Integer, String> anIndexMap, Map<String, String> anOriginalToReplacement  ) {
		retVal.setLength(0);
		aReplacementsWithContext.setLength(0);
		if (anIndexMap.size() == 0) {
			return 0;
		}
		int aNumReplacements = 0;
//		StringBuffer retVal = new StringBuffer();
//		List<Integer> anIndices = new ArrayList(anIndexMap.keySet());
//		Collections.sort(anIndices);
		int aLastEndIndex = 0;
		List<Integer> aSortedDisjointIndices = getSortedDisjointIndices(anIndexMap);
//		boolean aChanged = false;
		for (Integer anIndex:aSortedDisjointIndices) {
			if (aLastEndIndex < 0 || anIndex < 0 || aLastEndIndex > anIndex) {
				System.out.println("negative index");
			}
			String aPreMatchFragment = aString.substring(aLastEndIndex, anIndex);
			String anOriginalAtIndex = anIndexMap.get(anIndex);
			String aReplacementAtIndex = anOriginalToReplacement.get(anOriginalAtIndex);
			String anOriginalWithContext = fragmentWithContext(aString, anOriginalAtIndex, anIndex, anIndex+anOriginalAtIndex.length());
			String aReplacementWithContext = fragmentWithContext(aString, aReplacementAtIndex, anIndex, anIndex+ anOriginalAtIndex.length());
			aNumReplacements++;
			String aReplacementMessage ="Replaced " + anOriginalWithContext + " with" + aReplacementWithContext + "\n";
			aReplacementsWithContext.append(aReplacementMessage);
////			aChanged = true;
//			if (aLogger != null) {
//				try {
//					String aReplacementMessage ="Replaced " + anOriginalWithContext + " with" + aReplacementWithContext + "\n";
//					aLogger.write(anIndex + ": " + aReplacementMessage);
//					aLogger.flush();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
			retVal.append(aPreMatchFragment);
			retVal.append(aReplacementAtIndex);
			aLastEndIndex = anIndex + anOriginalAtIndex.length();			
		}
//		if (aLogger != null && !aChanged) {
//			try {
//				aLogger.write("Made no change\n");
//				aLogger.flush();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		String aRemainingString = aString.substring(aLastEndIndex, aString.length());
		retVal.append(aRemainingString);
		return aNumReplacements;
//		return retVal.toString();		
	}
	public static List<String> findTokenWords(String aString) {
		StringTokenizer aTokenizedString = new StringTokenizer(aString, "=./\\ 	,;![]()+-*\"'><:%");
		List<String> retVal = new ArrayList();

		while (aTokenizedString.hasMoreTokens()) {
			retVal.add(aTokenizedString.nextToken());
		}
		return retVal;
		
	}
	
	public static List<String> findNonKeywordMatches(Set<String> aKeywordsSet, String aString, List<String> aStringTokens, Set<String> aCandidateWords) {
//		if (aString.contains("jaand")) {
//			System.out.println("Found probem string" + aString);
//		}

//		StringTokenizer aTokenizedString = new StringTokenizer(aString, "=./\\ 	,;![]()+-*\"'><:%");
//		List<String> aTokens = new ArrayList();
//
//		while (aTokenizedString.hasMoreTokens()) {
//			aTokens.add(aTokenizedString.nextToken());
//		}
		List<String> retVal = new ArrayList();

		for (String aToken:aStringTokens) {
//			String aToken = aTokenizedString.nextToken();
			if (aKeywordsSet.contains(aToken)) {
				continue; // breaking it up may perform a match
			}
			String[] aComponents = splitCamelCaseHyphenDash(aToken);
			for (String aComponent : aComponents) {
//				System.out.println("Component:" + aComponent);
				for (String aCandidateWord : aCandidateWords) {
					if (aComponent.equals(aCandidateWord)) {

//					 if (aToken.equals(aKeyword.toLowerCase())) {// token ma have extra chars
						retVal.add(aCandidateWord);
					}
				}
			}
//				}
		}
		return retVal;

	}
	
	public static String replaceAllNonKeywords(String aKeywordsRegex, String aString, String anOriginal, String aReplacement) {
		String[] aSplits = aString.split(aKeywordsRegex);
		StringBuffer aReplacedValue = new StringBuffer();
		int aLastEnd = 0;
		int aLastStart = 0;
		String aRemainingString = aString;
		String anOriginalLowerCase = anOriginal.toLowerCase();
		for (String aSplit:aSplits) {
			String aSplitSubstitution = aSplit.replaceAll(anOriginal, aReplacement).replaceAll(anOriginalLowerCase, aReplacement);
			aRemainingString = aRemainingString.substring(aLastEnd);
			aLastStart = aRemainingString.indexOf(aSplit);
			aLastEnd = aLastStart + aSplit.length();
			String aPreSplit = aRemainingString.substring(0, aLastStart);
			aReplacedValue.append(aPreSplit+aSplitSubstitution);			
		}
		return aReplacedValue.toString();
	}
	public static boolean hasName(String aString, List<String> aNames) {
		String aStringLowerCase = aString.toLowerCase();
		for (String aName:aNames) {
			if (aStringLowerCase.contains(aName.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
//	static Set<String> replacementsMessages = new HashSet();
	public static String replaceAllNonKeywords(
			StringBuffer aReplacementsMessageList,
			FileWriter aLogger,
			int aNumMaxMatches,
			String aKeywordsRegex, 
			String aString, 
			List<String> anOriginals, 
			List<String> aReplacements) {
		aReplacementsMessageList.setLength(0);
		String[] aSplits = aString.split(aKeywordsRegex);
		StringBuffer aReplacedValue = new StringBuffer();
		int aLastEnd = 0;
		int aLastStart = 0;
		String aRemainingString = aString;
		Map<String, String> anOriginalToReplacement = new HashMap();
		for (int index = 0; index< anOriginals.size(); index++) {
			anOriginalToReplacement.put(anOriginals.get(index), aReplacements.get(index));
		}
		StringBuffer aSplitSubstitution = new StringBuffer();
		int aNumActualChanges = 0;
//		String anOriginalLowerCase = anOriginal.toLowerCase();
		for (String aSplit:aSplits) {
//			Map<String, List<Integer>> aSplitKeys  = indicesOf  (aSplit, anOriginals);
//			Map<Integer, String> aSplitIndexKeysMap = toIndexKeys(aSplitKeys);
			Map<Integer, String> aSplitIndexKeysMap = indexToFragment(aSplit, anOriginals);

//			if (anIndexKeysMap.size() == 0) {
//				aReplacedValue.append(aSplit);
//				continue;
//			}
			aNumActualChanges += replaceAll(aReplacementsMessageList, aSplitSubstitution, aLogger, aSplit, aSplitIndexKeysMap, anOriginalToReplacement);

//			aRemainingString = aRemainingString.substring(aLastEnd);
			aLastStart = aRemainingString.indexOf(aSplit);
			aLastEnd = aLastStart + aSplit.length();
			String aPreSplit = aRemainingString.substring(0, aLastStart);
			aReplacedValue.append(aPreSplit+aSplitSubstitution);
			aRemainingString = aRemainingString.substring(aLastEnd);
		}
		aReplacedValue.append(aRemainingString);		
		if (aNumMaxMatches != aNumActualChanges) {
			aReplacementsMessageList.append("Maximum matches " + aNumMaxMatches + " != num actual changes " + aNumActualChanges + "\n");
//				aLogger.write("Maximum matches " + aNumMaxMatches + " != num actual changes " + aNumActualChanges + "\n");
			
		}
		String aReplacementsMessage = aReplacementsMessageList.toString();
		aReplacementsMessageList.append(aReplacementsMessage);
//		if (!replacementsMessages.contains(aReplacementsMessage) && aLogger != null) {
//			replacementsMessages.add(aReplacementsMessage);
//			try {
//				aLogger.write(aReplacementsMessage);
//				aLogger.flush();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}		
		return aReplacedValue.toString();
	}
	

	public static void main(String[] args) {
//		String aString = "package jilliand_Assignment1";
//		boolean found = containsNameFollowing(aString, "jilliand", "package");
//		System.out.println("Found:" + found);
		String aString = "<Command __id=\"27\" _type=\"DiffBasedFileOpenCommand\" date=\"Thu Aug 25 20:00:20 ICT 2022\" docASTNodeCount=\"6\" docActiveCodeLength=\"55\" docExpressionCount=\"2\" docLength=\"55\" projectName=\"A1.mattdo\" starttimestamp=\"1661432312024\" timestamp=\"108593\">";
		String aSplitKeyword = "Command __id|DiffBasedFileOpenCommand|docASTNodeCount|docActiveCodeLength|docExpressionCount|docLength|projectName|starttimestamp|timestamp";
		
		
		String[] aSplits = aString.split(aSplitKeyword);
		StringBuffer aReplacedValue = new StringBuffer();
		int aLastEnd = 0;
		int aLastStart = 0;
		String aRemainingString = aString;
		for (String aSplit:aSplits) {
			String aSplitSubstitution = aSplit.replaceAll("Do", "Christopher").replaceAll("do", "Christopher");
			aRemainingString = aRemainingString.substring(aLastEnd);
			aLastStart = aRemainingString.indexOf(aSplit);
			aLastEnd = aLastStart + aSplit.length();
			String aPreSplit = aRemainingString.substring(0, aLastStart);
			aReplacedValue.append(aPreSplit+aSplitSubstitution);			
		}
		System.out.println(aString);
		System.out.println(aReplacedValue);
		
	}

}
