package anonymyzer;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/*
 * 
 * Research: Make the anonimizer platform agnostic....make it work with folders w/o headers....and w them....also anonimize grades.csv!!! 
 * Pehaps make anonimizing process more readable too
 * ...student 1, student 2 etc...needs to work on windows....mac....linux....

After this i can get aikat and whoever else to use this and hand me sakai folders w/ grades.csv
 so I can then do a high-low-median in similiarity 
 scores for a semester's course and finally get some graphs going!!
 */
public class Anon {
	boolean courseMode = false;
	boolean deleteTXTAndHTML = false;
	String currIden;
	int depth;
	HashMap<String, String> CommentsIdenMap = new HashMap<String, String>();
	File log_file;
	FileWriter logger;
	int counter; // used for differentiating students
	static String[] prefixes = { "lName", "fName", "ID" };
	static String[] ignoreFileSuffixes = {};
	static final char DELETE = 'd';
	static final char ANON = 'a';
	static final char UNANON = 'u';
//	static final char COURSE = 'c';
	static final char TXT = 't';
	static char method = ' ';

	public Anon() throws IOException {
		log_file = new File("anon_log");
		log_file.delete();
		log_file.createNewFile();
		logger = new FileWriter(log_file);
	}
	
	public static void main(String[] args) throws IOException {
		if (args.length < 2) {
			System.err.println("Enter main args: d|a(t) path(s)");
			return;
		}

		Anon anon = new Anon();
		String mode = args[0];
		try {
			for (int i = 0; i < mode.length(); i++) {
				switch (mode.charAt(i)) {
				case DELETE:
				case ANON:
					anon.setMethod(mode.charAt(i));
					break;
//				case COURSE:
//					anon.setCourseMode(true);
//					break;
				case TXT:
					anon.setTxtMode(true);
					break;
				default:
					System.err.println("Unsupported argument!");
					return;
				}
			}
			for (int i = 1; i < args.length; i++) {
				args[i] = parseArg(args[i]);
			}
			anon.execute(Arrays.copyOfRange(args, 1, args.length));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Done");
	}

//	public void setCourseMode(boolean course) {
//		courseMode = course;
//	}

	public void setTxtMode(boolean txt) {
		deleteTXTAndHTML = txt;
	}

	public void setMethod(char me) {
		if (method == ' ') {
			method = me;
		} else {
			System.err.println("Cannot change method after set, method used is " + method);
		}
	}

	protected void init() throws IOException {
		depth = 1;
		counter = 0;
		
		if (!courseMode) {
			logger.write("In single assignment mode. Delete txt and html files = " + deleteTXTAndHTML + "\n");
		} else {
			logger.write("In course folder mode. Delete txt and html files = " + deleteTXTAndHTML + "\n");
		}
	}

	public void execute(String[] args) throws IOException, InterruptedException {
		if (method == ANON) {
			init();
			anonymize(args);
			logger.close();
		}
		if (method == DELETE) {
			for (String path : args) {
				File file = new File(path);
				if (file.exists()) {
					delete(file);
				} else {
					System.err.println(path + " not found");
				}
			}
		}
	}

	public void delete(File folder) throws IOException {
		if (folder == null || !folder.exists()) {
			return;
		}
		logger.write("Deleting " + folder.getPath());
		if (folder.isDirectory()) {
			for (File file : folder.listFiles()) {
				if (file.isDirectory()) {
					delete(file);
				} else {
					file.delete();
				}
			}
		}
		folder.delete();
	}

	public void anonymize(String[] args) throws IOException, InterruptedException {
		for (String path : args) {
			anonymize(path);
		}
	}	

	public void anonymize(String folderName) throws IOException, InterruptedException {
		// determine which version to run based on OS
		String os = System.getProperty("os.name").toLowerCase();
		File folder = new File(folderName);
		if (!folder.exists()) {
			System.out.println("Assignment folder not found.");
			System.exit(0);
		}
		folder = findFolderWithCSV(folder);
		if (folder == null) {
			System.out.println("Assignment folder not found.");
			System.exit(0);
		}
		if (os.contains("window")) {
			anonoymizeWindows(folderName);
		} else {
			System.out.println("Can't Figure out your os!");
		}
		//			logger.close();// must close upon completion for linux to show this stuff.
	}
	
	protected boolean isCourseFolder(String folderName) throws IOException {
		File folder = new File(folderName);
		if (folder.listFiles((file)->{return file.getName().contains("grades.csv");}).length == 0) {
			return false;
		} else {
			return true;
		}
	}

	public void anonoymizeWindows(String folderName) throws IOException, InterruptedException {
		if (!folderName.endsWith(".zip")) {
			File file = new File(folderName+"Backup.zip");
			if (!file.exists()) {
				System.out.println("Zipping " + folderName + " to " + folderName + "Backup.zip");
				logger.write("Zipping " + folderName + " to " + folderName + "Backup.zip");
				zip(folderName, folderName+"Backup");
			}
		} else {
			String dest = folderName.substring(0, folderName.length()-4);
			unzip(folderName, dest);
			folderName = dest;
		}
		unzipAllZipFiles(new File(folderName));
//		System.out.println("Anonymizing");
//		logger.write("Anonymizing");
		if (isCourseFolder(folderName)) {
//			if (!folderName.endsWith(".zip")) {
//				folderName = findFolderWithCSV(new File(folderName)).getParentFile().getPath();
//				unzipAllZipFiles(new File(folderName));
//			} else {
//				String dest = folderName.substring(0, folderName.length()-4);
//				unzip(folderName, dest);
//				folderName = dest;
//			}
//			folderName = findFolderWithCSV(new File(folderName)).getPath();
//			unzipAllZipFiles(new File(folderName));
			System.out.println("Anonymizing Course Folder: " + folderName);
			logger.write("Anonymizing Course Folder" + folderName);
			clearHeaders_Windows(folderName);
			Anon_ize_Windows(1, folderName);
			Anon_ize_grades_Windows(folderName);
		} else {
//			if (!folderName.endsWith(".zip")) {
//				folderName = findFolderWithCSV(new File(folderName)).getParentFile().getPath();
//				unzipAllZipFiles(new File(folderName));
//			} else {
//				String dest = folderName.substring(0, folderName.length()-4);
//				unzip(folderName, dest);
//				folderName = dest;
//			}
			System.out.println("Anonymizing Assignment Folder" + folderName);
			logger.write("Anonymizing Assignment Folder" + folderName);
			Anon_ize_Course_Windows(folderName);
		}
		if (folderName.endsWith(".zip")) {
			folderName = folderName.substring(0, folderName.length()-4);
		}
		zip(folderName, folderName+Character.toUpperCase(method));
		delete(new File(folderName));
	}

	//	public void anonymize(String[] args) throws IOException, InterruptedException {
	//		// instantiate vars
	////		CommentsIdenMap = new HashMap<String, String>();
	//		depth = 1;
	//		counter = 0;
	//		String folderName = ""; // will either act as the singleton assignment name....or the course folder name
	//		log_file = new File("anon_log");
	//		log_file.delete();
	//		log_file.createNewFile();
	//		logger = new FileWriter(log_file);
	//		System.out.println("made stuff!");
	//		if (args.length == 1) {
	//			folderName = args[0];
	//			courseMode = false;
	//			deleteTXTAndHTML = false;
	//			logger.write("In single assignment mode. Delete txt and html files = false\n");
	//		} else if (args.length == 2) {
	//			folderName = args[0];
	//			courseMode = false;
	//			if (args[1].equals("true")) {
	//				deleteTXTAndHTML = true;
	//			}
	//			logger.write("In single assignment mode. Delete txt and html files = " + deleteTXTAndHTML + "\n");
	//		} else if (args.length == 3) {
	//			folderName = args[0];
	//			courseMode = true;
	//			if (args[1].equals("true")) {
	//				deleteTXTAndHTML = true;
	//			}
	//			logger.write("In course folder mode. Delete txt and html files = " + deleteTXTAndHTML + "\n");
	//		} else {
	//			System.out.println("Enter main args: folderName (deleteTXTAndHTMLFiles) (CourseMode)");
	//			System.exit(0);
	//		}
	//		// determine which version to run based on OS
	//		String os = System.getProperty("os.name").toLowerCase();
	//		File folder = new File(folderName);
	//		if (!folder.exists()) {
	//			System.out.println("Assignment folder not found.");
	//			System.exit(0);
	//		}
	//		folder = findFolderWithCSV(folder);
	//		if (folder == null) {
	//			System.out.println("Assignment folder not found.");
	//			System.exit(0);
	//		}
	//		if (folderName.contains(".zip")) {
	//			File zipFile = new File(folderName);
	//			folderName = folderName.substring(0, folderName.length()-4);
	//			unzip(zipFile.getPath(), folderName);
	//		}
	//		if (os.contains("window")) {
	//			if (!courseMode) {
	//				folderName = findFolderWithCSV(new File(folderName)).getPath();
	//				unzipAllZipFiles(new File(folderName));
	//				clearHeaders_Windows(folderName);
	//				Anon_ize_Windows(1, folderName);
	//				Anon_ize_grades_Windows(folderName);
	//			} else {
	//				folderName = findFolderWithCSV(new File(folderName)).getParentFile().getPath();
	//				folderName = replaceTrainingSpacesInFolderNames(folderName);
	//				unzipAllZipFiles(new File(folderName));
	//				Anon_ize_Course_Windows(folderName);
	//			}
	//			zip(folderName, folderName+"Anon.zip");
	//		} else if (os.contains("mac")) { // currently not supported
	//			Process testMac = new ProcessBuilder(new String[] { "/bin/bash", "-c", "mkdir", "goo", "&", "touch",
	//					"goo/hi.txt", "&", "ls", "|", "grep", "goo" }).start();
	//			BufferedReader r = new BufferedReader(new InputStreamReader(testMac.getInputStream()));
	//			while (true) {
	//				String line = r.readLine();
	//				if (line == null)
	//					break;
	//				System.out.println(line);
	//			}
	//		} else if (os.contains("linux")) { // MUST CLOSE LOG so it can show for LINUX!!!
	//			// for this have to basically hand off the scripts too for running in unix
	//			// Process testlinux=Runtime.getRuntime().exec(new String[]{"doStuff.sh"});
	//			if (!courseMode) {
	//				clearHeaders_Linux(folderName);
	//				Anon_ize_Linux(1, folderName);
	//				Anon_ize_grades_Linux(folderName);
	//			} else {
	//				Anon_ize_Course_Linux(folderName);
	//			}
	//		} else {
	//			System.out.println("Can't Figure out your os!");
	//		}
	//		logger.close();// must close upon completion for linux to show this stuff.
	//	}

	static String replaceTrainingSpacesInFolderNames(String aFileName) {
		if (aFileName.endsWith(" ")) {
			aFileName = aFileName.substring(0, aFileName.length() - 1);
		}
		return aFileName.replace(" \\", "\\").replace(" /", "/");
	}

	public  void unzipAllZipFiles(File folder) {
		for (File zipFile : folder.listFiles()) {
			if (zipFile.isDirectory()) {
				unzipAllZipFiles(zipFile);
			} else if (zipFile.getName().endsWith(".zip")) {
				try {
					unzip(zipFile.getPath(), zipFile.getParent());
					zipFile.delete();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public  void unzip(String zipFilePath, String destDirectory) throws IOException {
		System.out.println("Unzipping " + zipFilePath + " to " + destDirectory);
		logger.write("Unzipping " + zipFilePath + " to " + destDirectory);
		File destDir = new File(destDirectory);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
		ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
		ZipEntry entry = zipIn.getNextEntry();
		// iterates over entries in the zip file
		while (entry != null) {
			String filePath = destDirectory + File.separator + entry.getName().replace("/", "\\");
			if (!entry.isDirectory()) {
				// if the entry is a file, extracts it
				extractFile(zipIn, filePath);
			} else {
				File dir = new File(filePath);
				dir.mkdirs();
			}
			zipIn.closeEntry();
			entry = zipIn.getNextEntry();
		}
		zipIn.close();
	}

	private  void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
		new File(filePath).getParentFile().mkdirs();
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
		byte[] bytesIn = new byte[4096];
		int read = 0;
		while ((read = zipIn.read(bytesIn)) != -1) {
			bos.write(bytesIn, 0, read);
		}
		bos.close();
	}

	public  void Anon_ize_Course_Linux(String folderName) throws IOException, InterruptedException {
		Process p = null;
		logger.flush();
		logger.write("PROCCESSING COURSE FOLDER\n");
		logger.flush();
		// look at course directory names
		try {
			p = Runtime.getRuntime().exec(new String[] { "getDirs.sh", folderName });
			Thread.sleep(2000);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		// reader for it
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while (true) {
			line = r.readLine();
			if (line == null) {
				break;
			}
			String newPath = folderName + "/" + line;
			logger.flush();
			logger.write("processing " + line);
			logger.flush();
			clearHeaders_Linux(newPath);
			Anon_ize_Linux(1, newPath);
			Anon_ize_grades_Linux(newPath);
		}
	}

	public  void Anon_ize_Course_Windows(String folderName) throws IOException, InterruptedException {
		//		Process p = null;
		logger.flush();
		logger.write("PROCCESSING COURSE FOLDER\n");
		logger.flush();
		File courseFolder = new File(folderName);
		for (File AssignmentFolder : courseFolder.listFiles()) {
			if (AssignmentFolder.isDirectory()) {
				String newPath = AssignmentFolder.getPath();
				clearHeaders_Windows(newPath);
				Anon_ize_Windows(1, newPath);
				Anon_ize_grades_Windows(newPath);
			}
		}
		//		try{
		//			//look at course directory name
		//			String[]command = new String[8];
		//			command[0]="cmd.exe";
		//			command[1]="/c";
		//				command[2]="cd";
		//			command[3]=folderName;
		//			command[4]="&";
		//			command[5]="ls";
		//			command[6]="&";
		//			command[7]="exit";
		//			p=new ProcessBuilder(command).start();
		//			Thread.sleep(2000);
		//		}
		//		catch(Exception e){
		//			e.printStackTrace();
		//			System.exit(0);
		//		}
		// reader for it
		//		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		//		String line;
		//		while(true){
		//			line = r.readLine();
		//			if (line == null) {
		//				break;
		//			}
		//			String newPath = folderName + "/" + line;
		//			clearHeaders_Windows(newPath);
		//			Anon_ize_Windows(1,newPath);
		//			Anon_ize_grades_Windows(newPath);
		//		}

	}

	public  void Anon_ize_grades_Windows(String folderName) throws IOException, InterruptedException {
		logger.write("CLEARING GRADES.CSV\n");
		// get csv file
		File csv = new File(folderName + "/grades.csv");
		// reader for it
		BufferedReader r = new BufferedReader(new FileReader(csv));
		// file to be the anoncsv
		File temp = new File(folderName + "/ANONGrades.csv");
		temp.createNewFile();
		// writer for it
		BufferedWriter w = new BufferedWriter(new FileWriter(temp));
		// first three lines do not contain any student stuff
		w.write(r.readLine() + "\n");
		w.flush();
		w.write(r.readLine() + "\n");
		w.flush();
		w.write(r.readLine() + "\n");

		while (true) {
			w.flush();
			String line = r.readLine();
			if (line == null) {
				break;
			}
			line = line.replaceAll(" ", "");// string spaces
			line = line.replaceAll("\"", ""); // get rid of all "
			String[] names = line.split(",");
			// replace each occurence of name w its shuffled version, passing in prefix
			line = replaceLine(line, names);
			// write to the new file
			w.write(line + "\n");
		}
		w.close();
		// remove old csv and rename the new one
		Process rmcsv = new ProcessBuilder(
				new String[] { "cmd.exe", "/c", "cd", folderName, "&", "rm", "grades.csv", "&", "exit" }).start();
		Thread.sleep(100);
		// if(!temp.renameTo(csv)){System.out.println("Couldn't replace
		// file!?");System.exit(0);}
		r.close();
	}

	protected String replaceLine(String line, String[] names) {
		line = line.replaceAll(names[0], shuffle(names[0], "ID"));
		line = line.replaceAll(names[1], shuffle(names[1], "ID"));
		line = line.replaceAll(names[2], shuffle(names[2], "fName"));
		line = line.replaceAll(names[3], shuffle(names[3], "lName"));
		return line;
	}

	protected  void Anon_ize_grades_Linux(String folderName) throws IOException, InterruptedException {
		logger.write("CLEARING GRADES.CSV\n");
		// get csv file
		File csv = new File(folderName + "/grades.csv");
		// reader for it
		BufferedReader r = new BufferedReader(new FileReader(csv));
		// file to be the anoncsv
		File temp = new File(folderName + "/ANONGrades.csv");
		temp.createNewFile();
		// writer for it
		BufferedWriter w = new BufferedWriter(new FileWriter(temp));
		// first three lines do not contain any student stuff
		w.write(r.readLine() + "\n");
		w.flush();
		w.write(r.readLine() + "\n");
		w.flush();
		w.write(r.readLine() + "\n");

		while (true) {
			String line = r.readLine();
			if (line == null) {
				break;
			}
			line = line.replaceAll(" ", "");// string spaces
			line = line.replaceAll("\"", ""); // get rid of all "
			String[] names = line.split(",");
			// replace each occurence of name w its shuffled version
			line = line.replaceAll(names[0], shuffle(names[0], "ID"));
			line = line.replaceAll(names[1], shuffle(names[1], "ID"));
			line = line.replaceAll(names[2], shuffle(names[2], "fName"));
			line = line.replaceAll(names[3], shuffle(names[3], "lName"));
			// write to the new file
			w.write(line + "\n");
		}
		w.close();
		// remove old csv and rename the new one
		Process rmcsv = Runtime.getRuntime().exec(new String[] { "rmCSV.sh", folderName });
		Thread.sleep(100);
		// if(!temp.renameTo(csv)){System.out.println("Couldn't replace
		// file!?");System.exit(0);}
		r.close();

	}

	public void Anon_ize_Windows(int depth, String folderName) throws IOException, InterruptedException {// depth
		logger.flush();
		logger.write("CLEARING TOP-LEVEL DIRECTORY NAMES\n");
		logger.flush();
		File folder = new File(folderName);

		for (File file : folder.listFiles()) {
			String line = file.getName();
			if (line.contains(".csv"))
				continue;// skip csv file
			if (!file.isDirectory()) {
				continue;
			}
			// get lastname,firstname,onyen
			String lastName = line.substring(0, line.indexOf(","));
			String firstName = line.substring(line.indexOf(",") + 2, line.indexOf("("));
			String onyen = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
			// toReplace has anon versions
			String toReplace = getToReplace(lastName, firstName, onyen);
			if (toReplace == null) {
				System.err.println("Cannot find match for " + onyen + ", skipping");
				logger.write("Cannot find match for " + onyen + ", skipping");
				continue;
			}
			// System.out.println("\"" + folderName + "/" +"\"");
			findTXTAndHTMLFiles_Windows(file, firstName, lastName, onyen);
			// rename the directory
			file.renameTo(new File(file.getParent() + "/" + toReplace));
			logger.write("renamed directory " + line + " to " + toReplace + "\n");
			if (deleteTXTAndHTML) {
				logger.write("removed all txt files and html files from directory " + line + "\n");
			}
			// log.flush();
		}
	}
	//	public  void Anon_ize_Windows(int depth, String folderName) throws IOException, InterruptedException {// depth
	////		Process p = null;
	//		logger.flush();
	//		logger.write("CLEARING TOP-LEVEL DIRECTORY NAMES\n");
	//		logger.flush();
	//		File folder = new File(folderName);
	////		try{
	////			//look at each student directory
	////			String[]command = new String[8];
	////			command[0]="cmd.exe";
	////			command[1]="/c";
	////				command[2]="cd";
	////			command[3]=folderName;
	////			command[4]="&";
	////			command[5]="ls";
	////			command[6]="&";
	////			command[7]="exit";
	////			p=new ProcessBuilder(command).start();
	////			Thread.sleep(2000);
	////		}
	////		catch(Exception e){
	////			e.printStackTrace();
	////			System.exit(0);
	////		}
	////		//reader for it
	////		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
	////		String line;
	////		while(true){
	//
	//		for (File file : folder.listFiles()) {
	////			line = r.readLine();
	//			// csv handled elsewhere
	//			String line = file.getName();
	//			if (line.contains(".csv"))
	//				continue;// skip csv file
	////			Process rm = (new ProcessBuilder(new String[]{"cmd.exe","/c","cd","\""+folderName+"/"+line+"\"","&","rm","*.txt","*.html","&","exit"}).start());//kill txt and html...could have names
	////			Thread.sleep(300);
	//			if (!file.isDirectory()) {
	//				continue;
	//			}
	//			// get lastname,firstname,onyen
	//			String lastName = line.substring(0, line.indexOf(","));
	//			String firstName = line.substring(line.indexOf(",") + 2, line.indexOf("("));
	//			String onyen = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
	//			// toReplace has anon versions
	//			String toReplace = getToReplace();
	//			// System.out.println("\"" + folderName + "/" +"\"");
	//			findTXTAndHTMLFiles_Windows(file, firstName, lastName, onyen);
	//			// rename the directory
	//			file.renameTo(new File(file.getParent() + "/" + toReplace));
	////			Process rename=(new ProcessBuilder(new String[]{"cmd.exe","/c","cd",folderName,"&","rename","\""+line+"\"","\""+toReplace+"\"","&","exit"}).start());
	////			Thread.sleep(100);
	//			logger.write("renamed directory " + line + " to " + toReplace + "\n");
	//			if (deleteTXTAndHTML) {
	//				logger.write("removed all txt files and html files from directory " + line + "\n");
	//			}
	//			// log.flush();
	//		}
	//	}

	protected  File findFolderWithCSV(File folder) {
		if (folder.getName().endsWith(".zip")) {
			return folder;
		}
		for (File file : folder.listFiles()) {
			if (file.getName().contains("grades.csv")) {
				return folder;
			}
		}
		File csvFolder = null;
		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				csvFolder = findFolderWithCSV(file);
				if (csvFolder != null)
					return csvFolder;
			}
		}
		return null;
	}

	//	protected  void findTXTAndHTMLFiles_Windows(File folder, String firstName, String lastName, String ID)
	//			throws IOException {
	////		File folder = new File(folderName);
	//		if (folder.listFiles() == null) {
	//			return;
	//		}
	//		for (File file : folder.listFiles()) {
	//			if (file.isDirectory()) {
	//				String filePath = file.getPath();
	//				String fileName = file.getName();
	//				if (fileName.contains(firstName) || fileName.contains(lastName) || fileName.contains(ID)) {
	//					String newFileName = fileName.replace(lastName, shuffle(lastName, "lName"))
	//							.replace(firstName, shuffle(firstName, "fName")).replace(ID, shuffle(ID, "ID"));
	//					File newFile = new File(filePath.replace(fileName, newFileName));
	//					file.renameTo(newFile);
	//					logger.write("renamed Folder " + fileName + " to " + newFileName + "\n");
	//				}
	//				findTXTAndHTMLFiles_Windows(file, firstName, lastName, ID);
	//			} else {
	//				if (deleteTXTAndHTML) {
	//					if (file.getName().endsWith(".txt") || file.getName().endsWith(".html")) {
	//						file.delete();
	//					} else {
	//						String filePath = file.getPath();
	//						String fileName = file.getName();
	//						if (fileName.contains(firstName) || fileName.contains(lastName) || fileName.contains(ID)) {
	//							String newFileName = fileName.replace(lastName, shuffle(lastName, "lName"))
	//									.replace(firstName, shuffle(firstName, "fName")).replace(ID, shuffle(ID, "ID"));
	//							File newFile = new File(filePath.replace(fileName, newFileName));
	//							file.renameTo(newFile);
	//							logger.write("renamed file " + fileName + " to " + newFileName + "\n");
	//						}
	//					}
	//				} else {
	//					String filePath = file.getPath();
	//					String fileName = file.getName();
	//					if (fileName.contains(firstName) || fileName.contains(lastName) || fileName.contains(ID)) {
	//						String newFileName = fileName.replace(lastName, shuffle(lastName, "lName"))
	//								.replace(firstName, shuffle(firstName, "fName")).replace(ID, shuffle(ID, "ID"));
	//						File newFile = new File(filePath.replace(fileName, newFileName));
	//						file.renameTo(newFile);
	//						logger.write("renamed file " + fileName + " to " + newFileName + "\n");
	//					}
	//				}
	//			}
	//		}
	//	}

	protected  void findTXTAndHTMLFiles_Windows(File folder, String firstName, String lastName, String ID)
			throws IOException {
		if (folder.listFiles() == null) {
			return;
		}
		for (File file : folder.listFiles()) {
			if (deleteTXTAndHTML && (file.getName().endsWith(".txt") || file.getName().endsWith(".html"))) {
				file.delete();
				continue;
			}
			renameFile(file, firstName, lastName, ID);
			if (file.isDirectory()) {
				findTXTAndHTMLFiles_Windows(file, firstName, lastName, ID);
			}
		}
	}

	protected String getToReplace(String lastName, String firstName, String onyen) {
		return shuffle(lastName, "lName") + ", " + shuffle(firstName, "fName") + "("
				+ shuffle(onyen, "ID") + ")";
	}

	protected void renameFile(File file, String firstName, String lastName, String ID) throws IOException {
		String filePath = file.getPath();
		String fileName = file.getName();
		if (fileName.contains(firstName) || fileName.contains(lastName) || fileName.contains(ID)) {
			String newFileName = getNewFileName(fileName, lastName, firstName, ID);
			if (newFileName == null) {
				return;
			}
			File newFile = new File(filePath.replace(fileName, newFileName));
			file.renameTo(newFile);
			logger.write("renamed file " + fileName + " to " + newFileName + "\n");
		}
	}

	protected String getNewFileName(String fileName, String lastName, String firstName, String ID) {
		return fileName.replace(lastName, shuffle(lastName, "lName"))
				.replace(firstName, shuffle(firstName, "fName")).replace(ID, shuffle(ID, "ID"));
	}

	protected  void Anon_ize_Linux(int i, String folderName) throws IOException, InterruptedException {
		Process p = null;
		logger.write("CLEARING TOP-LEVEL DIRECTORY NAMES\n");
		try {
			p = Runtime.getRuntime().exec(new String[] { "getDirs.sh", folderName });
			Thread.sleep(2000);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		// reader for it
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while (true) {
			line = r.readLine();
			if (line == null) {
				break;
			}
			// csv handled elsewhere
			// System.out.println(line);continue;
			if (line.contains(".csv"))
				continue;// skip csv file
			Process rm = Runtime.getRuntime().exec(new String[] { "delJunk.sh", folderName, line });// kill txt and
			// html...could have
			// names
			Thread.sleep(300);
			// get lastname,firstname,onyen
			String lastName = line.substring(0, line.indexOf(","));
			String firstName = line.substring(line.indexOf(",") + 2, line.indexOf("("));
			String onyen = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
			// toReplace has anon versions
			String toReplace = shuffle(lastName, "lName") + ", " + shuffle(firstName, "fName") + "("
					+ shuffle(onyen, "ID") + ")";
			// System.out.println("\"" + folderName + "/" +"\"");
			// rename the directory
			// Process rename=(new ProcessBuilder(new
			// String[]{"cmd.exe","/c","cd",folderName,"&","rename","\""+line+"\"","\""+toReplace+"\"","&","exit"}).start());
			Process rename = p = Runtime.getRuntime()
					.exec(new String[] { "renameDir.sh", folderName, line, toReplace });
			Thread.sleep(100);
			logger.write("renamed directory " + line + " to " + toReplace + "\n");
			logger.write("removed all txt files and html files from directory " + line + "\n");
			logger.flush();
		}
	}

	public  void clearHeaders_Windows(String folderName) throws IOException {
		//		Process p = null;
		logger.write("CLEARING JAVA FILES OF NAMES\n");
		findJavaFiles_Windows(new File(folderName), folderName.substring(0, folderName.lastIndexOf("\\") + 1));
		//		try {
		// get path to each java file

		//			if(!courseMode)p=new ProcessBuilder(new String[]{"cmd.exe","/c","find",folderName,"|","grep",".java","&","exit"}).start();
		//			else{
		//				String course = folderName.substring(0, folderName.indexOf("/"));
		//				folderName = folderName.substring(folderName.indexOf("/")+1);
		//				p=new ProcessBuilder(new String[]{"cmd.exe","/c","cd",course,"&","find",folderName,"|","grep",".java","&","exit"}).start();
		//			}

		//			Thread.sleep(2000);
		//		} catch (IOException | InterruptedException e) {
		//			e.printStackTrace();
		//			System.exit(0);
		//		}
		// reader for it
		//		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		//		String line;
		//		while (true) {
		//			line = r.readLine();
		//			if (line == null) {
		//				break;
		//			}
		// System.out.println(line);
		// hold on to orig line for cd later

		//		r.close();
	}

	protected  void findJavaFiles_Windows(File folder, String topFolderName) throws IOException {
		//		File folder = new File(folderName);
		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				findJavaFiles_Windows(file, topFolderName);
			} else {
				if (file.getName().contains(".java")) {
					replaceHeaders_Windows(file, topFolderName);
				}
			}
		}
	}

	protected  void replaceHeaders_Windows(File file, String topFolderName) throws IOException {

		String line = file.getPath().replace(topFolderName, "");
		String orig_line = file.getPath();
		line = line.replaceAll("\\\\", "/"); // sanitize
		String[] split = line.split("/");
		// load up our known names
		ArrayList<String> names = new ArrayList<String>();
		// should be last name
		names.add(split[depth].substring(0, split[depth].indexOf(",")));
		// should be first name
		names.add(split[depth].substring(split[depth].indexOf(",") + 2, split[depth].indexOf("(")));
		// should be onyen
		names.add(split[depth].substring(split[depth].indexOf("(") + 1, split[depth].indexOf(")")));
		// make a new file to write to
		File f = new File(orig_line);
		if (!f.canWrite()) {
			System.out.println("can't write file " + line);
			return;
		}
		File temp = new File("TEMP_GOO");
		temp.createNewFile();
		// writer for new file...and reader for our orig java file
		BufferedWriter w = new BufferedWriter(new FileWriter(temp));
		BufferedReader r_1 = new BufferedReader(new FileReader(f));
		int line_num = 0;
		while (true) {
			String line_1 = r_1.readLine();
			if (line_1 == null)
				break;
			// replace all instances of names with anon version
			for (int i = 0; i < names.size(); i++) {
				String name = names.get(i);
				if (line_1.toLowerCase().contains(name.toLowerCase())) {
					logger.write("changed " + name + " on line " + line_num + " of " + f.getName() + "\n");
					line_1 = replaceHeaders(name, line_num, f, names, line_1, i);
				}

			}
			// write it to new file
			w.write(line_1 + "\n");
			line_num++;
		}
		w.close();
		r_1.close();
		// delete orig java file
		f.delete();
		// rename our new file to orig name
		if (!temp.renameTo(f)) {
			System.out.println("Couldn't replace file!?");
			System.exit(0);
		}
	}

	public String replaceHeaders(String name, int line_num, File f, List<String> names, String line_1, int i) throws IOException {
		return line_1.replaceAll(name, shuffle(name, prefixes[i])).replaceAll(name.toLowerCase(),
				shuffle(name, prefixes[i]));// shuffle all names
	}

	protected  void clearHeaders_Linux(String folderName) throws IOException {
		Process p = null;
		logger.write("CLEARING JAVA FILES OF NAMES\n");
		try {
			// get path to each java file
			if (!courseMode)
				p = Runtime.getRuntime().exec(new String[] { "getPaths.sh", folderName });
			else {
				String course = folderName.substring(0, folderName.indexOf("/"));
				folderName = folderName.substring(folderName.indexOf("/") + 1);
				p = Runtime.getRuntime().exec(new String[] { "getPathsCourse.sh", course, folderName });
			}
			Thread.sleep(2000);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			System.exit(0);
		}
		// reader for it
		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while (true) {
			line = r.readLine();
			if (line == null) {
				break;
			}
			// System.out.println(line);
			// hold on to orig line for cd later
			String orig_line = line;
			line = line.replaceAll("\\\\", "/"); // sanitize
			String[] split = line.split("/");
			// load up our known names
			ArrayList<String> names = new ArrayList<String>();
			// should be last name
			names.add(split[depth].substring(0, split[depth].indexOf(",")));
			// should be first name
			names.add(split[depth].substring(split[depth].indexOf(",") + 2, split[depth].indexOf("(")));
			// should be onyen
			names.add(split[depth].substring(split[depth].indexOf("(") + 1, split[depth].indexOf(")")));
			// make a new file to write to
			File f = new File(orig_line);
			if (!f.canWrite()) {
				System.out.println("can't write file " + line);
				continue;
			}
			File temp = new File("TEMP_GOO");
			temp.createNewFile();
			// writer for new file...and reader for our orig java file
			BufferedWriter w = new BufferedWriter(new FileWriter(temp));
			BufferedReader r_1 = new BufferedReader(new FileReader(f));
			int line_num = 0;
			String[] prefixes = { "lName", "fName", "ID" };
			while (true) {
				String line_1 = r_1.readLine();
				if (line_1 == null)
					break;
				// replace all instances of names with anon version
				for (int i = 0; i < names.size(); i++) {
					String name = names.get(i);
					if (line_1.contains(name)) {
						logger.write("changed " + name + " on line " + line_num + " of " + f.getName() + "\n");
						logger.flush();
						line_1 = line_1.replaceAll(name, shuffle(name, prefixes[i]));// shuffle all names
					}

				}
				// write it to new file
				w.write(line_1 + "\n");
				line_num++;
			}
			w.close();
			r_1.close();
			// delete orig java file
			f.delete();
			// rename our new file to orig name
			if (!temp.renameTo(f)) {
				System.out.println("Couldn't replace file!?");
				System.exit(0);
			}
		}
		r.close();
	}

	public String shuffle(String text, String prefix) {
		// we see if we have seen the name before, returning its mapped anon version if
		// it exists
		if (CommentsIdenMap.get(text) != null)
			return CommentsIdenMap.get(text);
		// otherwise we generate 5 char suffix for 'salting' the identifier...don't want
		// to create something used already
		//	    int leftLimit = 97; // letter 'a'
		//	    int rightLimit = 122; // letter 'z'
		//	    int targetStringLength = 5;
		//	    StringBuilder buffer = new StringBuilder(targetStringLength);
		//	    for (int i = 0; i < targetStringLength; i++) {
		//	        int randomLimitedInt = leftLimit + (int) 
		//	          (new Random().nextFloat() * (rightLimit - leftLimit + 1));
		//	        buffer.append((char) randomLimitedInt);
		//	    }
		//	    String generatedSuffix = buffer.toString();
		//	    String generatedString;
		//	    if(prefix.equals("ID")) generatedString = prefix + ++counter + "_" + generatedSuffix; //only append counter to onyen
		//	    else generatedString = prefix + "_" + generatedSuffix;
		//		

		//sha256	
		String generatedString = "";
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			digest.update(text.getBytes("UTF-8"));
			byte[] generatedBytes = digest.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < generatedBytes.length; i++)
				sb.append(Integer.toHexString(0xff & generatedBytes[i]));
			generatedString = sb.toString();
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// load the generated identifier into hash map
		CommentsIdenMap.put(text, generatedString);
		return generatedString;

	}

	public static void deleteUnnecessaryFiles(File folder) {
		if (!folder.exists()) {
			System.err.println ("No folder:" + folder);
			return;
		}
		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				deleteUnnecessaryFiles(file);
			} else {
				maybeDeleteFile(file);
			}
		}
	}

	protected static boolean maybeDeleteFile(File file) {
		for (String aSuffix:getIgnoreFileSuffixes()) {
			if (file.getName().toLowerCase().endsWith(aSuffix)) {
				file.delete();
				return true;
				//				break;
			}
		}
		return false;
	}

	public static String[] getIgnoreFileSuffixes() {
		return ignoreFileSuffixes;
	}

	public static void setIgnoreFileSuffixes(String[] ignoreFileSuffixes) {
		Anon.ignoreFileSuffixes = ignoreFileSuffixes;
	}

	public void zip(String sourceDirPath, String zipFilePath) throws IOException {
		int num = 0;
		String ext = ".zip";
		while (new File(zipFilePath+ext).exists()) {
			num++;
			ext = "(" + num + ").zip";
		}
		zipFilePath += ext;
		Path p = Files.createFile(Paths.get(zipFilePath));
		System.out.println("Zipping " + sourceDirPath + " to " + zipFilePath);
		logger.write("Zipping " + sourceDirPath + " to " + zipFilePath);
		try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
			Path pp = Paths.get(sourceDirPath);
			Files.walk(pp)
			.filter(path -> !Files.isDirectory(path))
			.forEach(path -> {
				ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
				try {
					zs.putNextEntry(zipEntry);
					Files.copy(path, zs);
					zs.closeEntry();
				} catch (IOException e) {
					System.err.println(e);
				}
			});
		}
	}

	public static String parseArg(String arg) {
		if (arg.startsWith("'") && arg.endsWith("'")) {
			return arg.substring(1, arg.length()-1);
		}
		return arg;
	}
}
