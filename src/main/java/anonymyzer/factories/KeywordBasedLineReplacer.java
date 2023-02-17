package anonymyzer.factories;

import java.io.FileWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import anonymyzer.AnonUtil;
import anonymyzer.AssignmentMetrics;

public class KeywordBasedLineReplacer implements LineReplacer {

	@Override
	public String replaceLine(int aLineNumber, String aLine, Set<String> aMessagesOutput, FileWriter aLogger,
			String aKeywordsRegex, List<String> anOriginals, List<String> aReplacements,
			Map<String, String> anOriginalToReplacement, AssignmentMetrics anAssignmentMetrics) {
		return AnonUtil.replaceAllNonKeywords(aLineNumber, aLine, aMessagesOutput, aLogger, aKeywordsRegex, anOriginals, aReplacements, anOriginalToReplacement, anAssignmentMetrics);
	}

}
