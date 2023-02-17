package anonymyzer.factories;

import java.io.FileWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import anonymyzer.AnonUtil;
import anonymyzer.AssignmentMetrics;

public class LineReplacerFactory {
	static LineReplacer lineReplacer = new KeywordBasedLineReplacer();

	public static LineReplacer getLineReplacer() {
		return lineReplacer;
	}

	public static void setLineReplacer(LineReplacer newVal) {
		LineReplacerFactory.lineReplacer = newVal;
	}
	public static String replaceLine(int aLineNumber, String aLine, Set<String> aMessagesOutput, FileWriter aLogger,
			String aKeywordsRegex, List<String> anOriginals, List<String> aReplacements,
			Map<String, String> anOriginalToReplacement,
			AssignmentMetrics anAssignmentMetrics) {
		return lineReplacer.replaceLine(aLineNumber, aLine, aMessagesOutput, aLogger, aKeywordsRegex, anOriginals, aReplacements, anOriginalToReplacement, anAssignmentMetrics);
	}
}
