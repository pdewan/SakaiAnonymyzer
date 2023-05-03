package anonymyzer.factories;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import anonymyzer.AnonUtil;
import anonymyzer.AssignmentMetrics;
/*
 * Not the same as DoNotFakeManager as it hides arbitrary strings
 */
public class BasicHideManager implements HideManager{

	String[] hideTable = 
			{"(she/her)", "(he/him)"};
	
	public BasicHideManager() {		
	}
	
	@Override
	public String[] getHideList() {
		return hideTable;
	}
	@Override
	public void setHideList( String[] newVal) {
		hideTable = newVal;
	}

	@Override
	public String hideOriginals(
			int aLineNumber, 
			String aString, 
			FileWriter aLogger, 
			Set<String> aMessagesOutput, 
			List<String> aNames, 
			AssignmentMetrics anAssignmentMetrics) {		
		String[] aHideList = getHideList();
		String retVal = aString;
		String prevRetVal = aString;
		for (String aToBeHidden:aHideList) {
			retVal = retVal.replace(aToBeHidden, "");	
			if (!retVal.equals(prevRetVal)) {
				String aMessage = "Replaced" + aToBeHidden + "with nothing\n";
				anAssignmentMetrics.numWordsHidden++;
				if (!aMessagesOutput.contains(aMessage)) {
					try {
						anAssignmentMetrics.numUniqueWordsHidden++;
						aLogger.write(aMessage);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			prevRetVal = retVal;
		}	
		
		return retVal;
	}

	

	
	
}
