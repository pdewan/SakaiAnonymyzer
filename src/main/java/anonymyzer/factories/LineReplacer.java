package anonymyzer.factories;

import java.io.FileWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import anonymyzer.AssignmentMetrics;

public interface LineReplacer {
	String replaceLine(
			int aLineNumber, 
			String aLine, 
			Set<String> aMessagesOutput,
			FileWriter aLogger,
			String aKeywordsRegex, 
			List<String> anOriginals,
			List<String> aReplacements, 
			Map<String, String> anOriginalToReplacement, AssignmentMetrics anAssignmentMetrics);
}
