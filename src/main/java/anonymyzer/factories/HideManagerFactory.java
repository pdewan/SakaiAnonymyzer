package anonymyzer.factories;

import java.io.FileWriter;
import java.util.List;
import java.util.Set;

import anonymyzer.AssignmentMetrics;

public class HideManagerFactory {
	static HideManager hideManager = new BasicHideManager();
	public static HideManager getHideManager() {
		return hideManager;
	}
	public static void setHideManager(HideManager newVal) {
		hideManager = newVal;
	}
	public static String hideOriginals(int aLineNumber, String aString, FileWriter aLogger, Set<String> aMessagesOutput, List<String> aNames, AssignmentMetrics anAssignmentMetrics) {
		return hideManager.hideOriginals(aLineNumber, aString, aLogger, aMessagesOutput, aNames,  anAssignmentMetrics);
	}
}
