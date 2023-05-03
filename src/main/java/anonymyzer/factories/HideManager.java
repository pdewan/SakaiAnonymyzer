package anonymyzer.factories;

import java.io.FileWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import anonymyzer.AssignmentMetrics;

public interface HideManager {
	 String hideOriginals(
			 int aLineNumber, 
			 String aLine, 
			 FileWriter aLogger, 
			 Set<String> aMessagesOutput, 
			 List<String> aNames,
			 AssignmentMetrics anAssignmentMetrics
			 );

	String[] getHideList();

	void setHideList(String[] newVal);


//	Map<String, String> originalToStrikeOut();

}
