package anonymyzer.factories;

import java.io.FileWriter;
import java.util.Map;
import java.util.Set;

import anonymyzer.AnonUtil;
import anonymyzer.AssignmentMetrics;

public class BasicStringReplacer implements StringReplacer {

	@Override
	public String replaceString(int aLineNumber, String aLine, FileWriter aLogger, Set<String> aMessagesOutput,
			String aString, Map<Integer, String> aWordIndexMap, Map<Integer, String> aFragmentIndexMap,
			Map<String, String> anOriginalToReplacement, AssignmentMetrics anAssignmentMetrics) {
		return AnonUtil.replaceAll(aLineNumber, aLine, aLogger, aMessagesOutput, aString, aWordIndexMap, aFragmentIndexMap, anOriginalToReplacement, null);
	}
	
	

}
