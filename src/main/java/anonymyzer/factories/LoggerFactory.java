package anonymyzer.factories;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import anonymyzer.AssignmentMetrics;

public class LoggerFactory {
	AssignmentMetrics assignmentMetrics; 
	public AssignmentMetrics getAssignmentMetrics() {
		return assignmentMetrics;
	}
	public FileWriter getSpecificLogger() {
		return specificLogger;
	}
	FileWriter specificLogger;
//	List<String> messagesOutput;
	
	public LoggerFactory(File aFolder) {
		this(aFolder, true);
//		try {
//			createSpecificLoggerAndMetrics(aFolder,true);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	public LoggerFactory(File aFolder, boolean useParent) {
		try {
			createSpecificLoggerAndMetrics(aFolder,useParent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	protected void createSpecificLoggerAndMetrics(File aFile, boolean useParent) throws IOException {
//		File folder = new File(folderName);
		File aBaseFolder = useParent?
				aFile.getParentFile():
					aFile;
		File specificLoggerFile = new File(aBaseFolder, aFile.getName() + " Log.csv");

//		File specificLoggerFile = new File(aFile.getParentFile(), aFile.getName() + " Log.csv");
		if (!specificLoggerFile.exists()) {
			specificLoggerFile.createNewFile();
		}
		specificLogger = new FileWriter(specificLoggerFile);
		assignmentMetrics = new AssignmentMetrics();
	}
}
