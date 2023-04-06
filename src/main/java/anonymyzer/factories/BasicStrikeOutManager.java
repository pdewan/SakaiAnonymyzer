package anonymyzer.factories;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import anonymyzer.AnonUtil;
import anonymyzer.AssignmentMetrics;

public class BasicStrikeOutManager implements StrikeOutManager{
//	String[] originals = {
//			"\"ickliu/"
//	};
//	String[] replacements = {
//			"\"------/"
//	}
	String[][] strikeOutTable = {
			{"xinrod", "\"ickliu/", "\"------/"}
	};
//	Map<String, String> originalToStrikeOut;
	static int MAX_STRUCK_OUT_PART_LENGTH = 50;
	static char STRIKE_OUT_CHARACTER = '-';
	String maxStrikeOutString;
	
	public BasicStrikeOutManager() {
		
	}
	
	protected boolean matches (List<String> aNames, String aNameDenotation) {
		for (String aName:aNames) {
			if (aName.matches(aNameDenotation)) {
				return true;
			}
		}
		return false;
	}
	
//	protected List<Integer> indicesOfTable (List<String> aNames) {
//		List<Integer> retVal = new ArrayList();
//		String[][] aStrikeOutTable = strikeOutTable(); 
//		for (int index = 0; index < aStrikeOutTable.length; index++) {
//			String aNameDenotation = aStrikeOutTable[index][0];
//			if (matches(aNames, aNameDenotation)) {
//				return index;
//			}
//		}
//		return -1;
//	}
	
//	protected String[] rowOfMatch(List<String> aNames) {
//		int index = indexOfMatch(aNames);
//		if (index < 0) {
//			return null;
//		}
//		return strikeOutTable()[index];
//	}
	
	protected String maxStrikeOutString () {
		if (maxStrikeOutString == null) {
			StringBuffer aContainer = new StringBuffer(MAX_STRUCK_OUT_PART_LENGTH);
			for (int anIndex = 0; anIndex < MAX_STRUCK_OUT_PART_LENGTH; anIndex++) {
				aContainer.append(STRIKE_OUT_CHARACTER);
			}
			maxStrikeOutString = aContainer.toString();
		}
		return maxStrikeOutString;
	}
	
//	@Override
//	public Map<String, String> originalToStrikeOut() {
//		if (originalToStrikeOut == null) {
//			String aMaxStrikeOutString = maxStrikeOutString();
//			for (String[] anElement:originalToStrikeOutArray()) {
//				String anOriginal = anElement[0];
//				String aReplacement = 
//						anElement.length == 1?
//							aMaxStrikeOutString.substring(0, anOriginal.length()):
//							anElement[1];
//						
//				
//				originalToStrikeOut.put(anOriginal, aReplacement);
//			}
//		}
//		return originalToStrikeOut;
//	}
	@Override
	public String[][] getStrikeOutTable() {
		return strikeOutTable;
	}
	@Override
	public void setStrikeOutTable( String[][] newVal) {
		strikeOutTable = newVal;
	}
	protected boolean strikeOut(String aReplacement) {
		return aReplacement.length() == 1 && aReplacement.charAt(0) == STRIKE_OUT_CHARACTER;
	}
//	@Override
	public String srikeOutOriginals(
			int aLineNumber, 
			String aString, 
			FileWriter aLogger, 
			Set<String> aMessagesOutput, 
			List<String> aNames, 
			AssignmentMetrics anAssignmentMetrics) {

//	public String srikeOutOriginals(List<String> aNames, String aString) {
		
		String[][] aStrikeOutTable = getStrikeOutTable();
		String retVal = aString;
		
		for (String[] aRow:aStrikeOutTable) {
			
			if (!matches(aNames, aRow[0])) {
				continue;
			}
			String anOriginal = aRow[1];
			String aStoredReplacement = aRow[2];
			
			List<Integer> anIndices = AnonUtil.indicesOf(retVal, anOriginal, false);
			if (anIndices.size() == 0) {
				continue;
			}
			for (Integer index:anIndices) {
				int anEnd = index + anOriginal.length();
				String anOriginalWithContext = AnonUtil.fragmentWithContext(aString, anOriginal, index, anEnd);
				String aReplacement = 
						strikeOut(aStoredReplacement)?
						maxStrikeOutString().substring(0, anOriginal.length()):
						aStoredReplacement;
				String aReplacementWithContext = AnonUtil.fragmentWithContext(aString, aReplacement, index, anEnd);
				retVal = retVal.replaceFirst(anOriginal, aReplacement);	
				String aNormalizedMessage = "Replaced " + anOriginalWithContext + " with " + aReplacementWithContext;
				anAssignmentMetrics.numStructuredKeywordPositives++;
				int aNumChars = anOriginalWithContext.length() + aReplacementWithContext.length();
				anAssignmentMetrics.numCharactersInStructuredPositives+= aNumChars;

				if (!aMessagesOutput.contains(aNormalizedMessage)) {
					aMessagesOutput.add(aNormalizedMessage);
					anAssignmentMetrics.numUniqueStructuredPositives++;
					anAssignmentMetrics.numCharactersInUniqueStructuredPositives+= aNumChars;
				}
				try {
					aLogger.write(aLineNumber + "," + index + ":" + aNormalizedMessage + "\n");
					aLogger.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
			
		}		
		return retVal;
	}
//	@Override
//	public String srikeOutOriginals(String aString) {
//		String retVal = aString;
//		Map<String, String> anOriginalToStrikeOut = originalToStrikeOut();
//		for (String anOriginal:anOriginalToStrikeOut.keySet()) {
//			retVal = retVal.replace(anOriginal, anOriginalToStrikeOut.get(anOriginal));
//		}
//		if (retVal != aString) {
//			System.out.println("Found match");
//		}
//		return retVal;
//	}
	
	public static void main(String[] args) {
//		String[] aNameArray = {"roderick", "roderickliu", "liu"};
//		List<String> aNameList = Arrays.asList(aNameArray);
//		BasicStrikeOutManager aStrikeOutManager = new BasicStrikeOutManager();
//		String aReplacedValue = aStrikeOutManager.srikeOutOriginals(aNameList, "hello \"ickliu/ bye");
//		System.out.println(aReplacedValue);
	}
	
	
}
