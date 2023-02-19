package anonymyzer.factories;

import java.io.FileWriter;
import java.util.List;
import java.util.Set;

import anonymyzer.AssignmentMetrics;

public class StrikeOutManagerFactory {
	static StrikeOutManager strikeOutManager = new BasicStrikeOutManager();
	public static StrikeOutManager getStrikeOutManager() {
		return strikeOutManager;
	}
	public static void setStrikeOutManager(StrikeOutManager struckOutPartsManager) {
		StrikeOutManagerFactory.strikeOutManager = struckOutPartsManager;
	}
	public static String srikeOutOriginals(int aLineNumber, String aString, FileWriter aLogger, Set<String> aMessagesOutput, List<String> aNames, AssignmentMetrics anAssignmentMetrics) {
		return strikeOutManager.srikeOutOriginals(aLineNumber, aString, aLogger, aMessagesOutput, aNames,  anAssignmentMetrics);
	}
}
