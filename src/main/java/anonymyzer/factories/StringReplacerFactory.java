package anonymyzer.factories;

import java.io.FileWriter;
import java.util.Map;
import java.util.Set;

import anonymyzer.AssignmentMetrics;

public class StringReplacerFactory {
	static StringReplacer stringReplacer = new BasicStringReplacer();

	public static StringReplacer getStringReplacer() {
		return stringReplacer;
	}

	public static void setStringReplacer(StringReplacer newVal) {
		StringReplacerFactory.stringReplacer = newVal;
	}
	
	public static String replaceString(int aLineNumber, String aLine, FileWriter aLogger, Set<String> aMessagesOutput, String aString,
			Map<Integer, String> aWordIndexMap, Map<Integer, String> aFragmentIndexMap, 
			Map<String, String> anOriginalToReplacement,
			AssignmentMetrics anAssignmentMetrics
			) {
		return stringReplacer.replaceString(aLineNumber, aLine, aLogger, aMessagesOutput, aString, aWordIndexMap, aFragmentIndexMap, anOriginalToReplacement, anAssignmentMetrics);
	}
	

}
