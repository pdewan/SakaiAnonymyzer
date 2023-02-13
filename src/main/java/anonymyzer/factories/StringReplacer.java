package anonymyzer.factories;

import java.io.FileWriter;
import java.util.Map;
import java.util.Set;

import anonymyzer.AssignmentMetrics;

public interface StringReplacer {
	String replaceString(int aLineNumber, 
			String aLine,
			FileWriter aLogger, 
			Set<String> aMessagesOutput, 
			String aString,
			Map<Integer, String> aWordIndexMap, 
			Map<Integer, String> aFragmentIndexMap, 
			Map<String, String> anOriginalToReplacement, AssignmentMetrics anAssignmentMetrics
			);
}
