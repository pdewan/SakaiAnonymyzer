package anonymyzer.factories;

import java.io.FileWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import anonymyzer.AssignmentMetrics;

public interface StrikeOutManager {
	 String srikeOutOriginals(
			 int aLineNumber, 
			 String aLine, 
			 FileWriter aLogger, 
			 Set<String> aMessagesOutput, 
			 List<String> aNames,
			 AssignmentMetrics anAssignmentMetrics
			 );

	String[][] getStrikeOutTable();

	void setStrikeOutTable(String[][] newVal);


//	Map<String, String> originalToStrikeOut();

}
