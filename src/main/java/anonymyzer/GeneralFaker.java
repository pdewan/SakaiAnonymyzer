package anonymyzer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesGrouping;

import anonymyzer.factories.LoggerFactory;

public abstract class GeneralFaker {
	
	protected static final String NAME_MAP = "name map.csv";
	protected static final String NAME_FILE = "name.yml";
	protected static final String NAME_MAP_ID = "1Q0zDNvbMmXKN7p7TVYVbsFsqYSrPaeIR";
	protected static final String NAME_FILE_ID = "1VN317S6CkfnTknVBuwbFbwqEjpXoCeRg";
	protected static final String TOKEN = "tokens" + File.separator + "StoredCredential";
	File nameMapCSV;
	String nameMapPath;
	Faker faker;
	HashMap<String, String> CommentsIdenMap = new HashMap<String, String>();
	HashMap<String, String> newPairs = new HashMap<>();
	HashSet<String> fakeNameSet = new HashSet<>();
	File log_file;
	String logFileName = "faker_log";
	FileWriter logger, specificLogger;
	AssignmentMetrics assignmentMetrics;
	public final static String HIDDEN_NAME = "";
	Map<String, String> maybeQuotedNameToOnyen = new HashMap<>();
	Map<String, String> nameToOnyen = new HashMap<>();

	Map<String, String> authorToFakeAuthor = new HashMap<>();
	Map<String, String> emailToFakeAuthor = new HashMap<>() ;
	Map<String, String> uidToFakeAuthor = new HashMap<>();
	Map<String, String> fullNameToFakeFullName = new HashMap<>();
	Map<String, String> firstNameToFakeFirstName = new HashMap<>();
	Map<String, String> lastNameToFakeLastName = new HashMap<>();
	Map<String, String> someNameToFakeAuthor = new HashMap<>();
	List<String> originalNameList  = new ArrayList();
	List<String> replacementNameList =new ArrayList() ;
	

	Map<String, String> uidToAuthor;


//	FileWriter logger;
	Set<String> messagesOutput = new HashSet();
	
	public GeneralFaker() throws IOException {
		log_file = new File(logFileName);
		log_file.delete();
		log_file.createNewFile();
		logger = new FileWriter(log_file);
//		authorToFakeAuthor = new HashMap<>();
//		uidToAuthor = new HashMap<>();
//		uidToFakeAuthor = new HashMap<>();
//		emailToFakeAuthor = new HashMap<>();
//		fullNameToFakeFullName = new HashMap<>();
//		firstNameToFakeFirstName = new HashMap<>();
//		lastNameToFakeLastName = new HashMap<>();
	}
	
	protected void processElements(Map<String, String> aMap) {
		for (String aKey : aMap.keySet()) {
//			originalNameList.add(aKey);
			String aValue = aMap.get(aKey);
			String anExistingValue = someNameToFakeAuthor.get(aKey);
			if (aValue.equals(anExistingValue)) {
				continue; // already processed
				
			}
			originalNameList.add(aKey);
			replacementNameList.add(aValue);
			someNameToFakeAuthor.put(aKey, aValue);
		}
	}

	protected void processElementsOfAllMaps() {
//		if (someNameToFakeAuthor == null) {
//			originalNameList = new ArrayList();
//			replacementNameList = new ArrayList();
//			someNameToFakeAuthor = new HashMap<>();
			processElements(authorToFakeAuthor);
			processElements(emailToFakeAuthor);
			processElements(uidToFakeAuthor);
			processElements(fullNameToFakeFullName);
			processElements(firstNameToFakeFirstName);
			processElements(lastNameToFakeLastName);
//			try {
//				specificLogger.write(authorToFakeAuthor.toString() + "/n");
//				specificLogger.flush();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

			// first add
//		}
	}
	
	protected void nonDuplicatePut(Map<String, String> aMap, String aKey, String aValue) {
		if (aKey.contains("nstructor")) {
			return;
		}
		String anExistingValue = aMap.get(aKey);
		if (anExistingValue != null && !aValue.equals(anExistingValue)) {
			aMap.put(aKey, HIDDEN_NAME);

			try {
				specificLogger.write("Key:" + aKey + " Duplicate Values:" + anExistingValue + "," + aValue + "\n");
				specificLogger.flush();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			aMap.put(aKey, aValue);
			aMap.put(aKey.toLowerCase(), aValue);
		}
	}

	
	public Faker getFaker() {
		if (faker == null) {
			faker = new Faker();
		}
		return faker;
	}
	protected void createSpecificLoggerAndMetrics(File folder, boolean useParent) throws IOException {
//		File folder = new File(folderName);
//		File specificLoggerFile = new File(folder.getParentFile(), folder.getName() + " Log.csv");
//		if (!specificLoggerFile.exists()) {
//			specificLoggerFile.createNewFile();
//		}
//		specificLogger = new FileWriter(specificLoggerFile);
//		assignmentMetrics = new AssignmentMetrics();
		LoggerFactory aLoggerFactory = new LoggerFactory(folder, useParent);
		specificLogger = aLoggerFactory.getSpecificLogger();
		assignmentMetrics = aLoggerFactory.getAssignmentMetrics();
	}
	public static String[] parseArgs(String[] args) {
		for (int i = 0; i < args.length; i++) {
			args[i] = parseArg(args[i]);
		}
		return args;
	}
	
	public boolean setNameMapAndNameFile() {
		File namemap = new File(NAME_MAP);
		if (!namemap.exists()) {
			System.err.println("name map.csv not found, please download required files first");
			return false;
		}
		setNameMap(namemap);
		File nameFile = new File(NAME_FILE);
		if (!nameFile.exists()) {
			System.err.println("name.yml not found, please download name map.csv first");
			return false;
		}
		((FakeValuesGrouping)this.getFaker().fakeValuesService().getFakeValueList().get(0))
			.setSpecifiedFileName("name", nameFile.getPath());
		return true;
	}
		
	public static GeneralFaker createFaker() throws IOException{
		return new FakeNameGenerator();
	}
	
	public void setNameMap(String path) throws IOException {
		nameMapPath = path;
		nameMapCSV = new File(path);
	}

	protected void specificLogLine(String aString) {
		try {
			specificLogger.write(aString + "\n");
			specificLogger.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void maybeSpecificLogLine(String aString) {
		if (messagesOutput.contains(aString)) {
			return;
		}
		specificLogLine(aString);
	}
	
	
	
	public void execute(Object arg) throws IOException, InterruptedException {
		loadNameMap();
		anonymize(arg);
		updateNameMap();
	}

	public abstract void anonymize(Object arg);
	
	public void setNameMap(File file) {
		nameMapPath = file.getPath();
		nameMapCSV = file;
	}
	
	public void loadNameMap() throws IOException {
		if (!nameMapCSV.exists()) {
			logger.write(nameMapPath + " not found, creating name map: " + nameMapPath);
			nameMapCSV.createNewFile();
			logger.write("Name map created: " + nameMapPath);
			return;
		}
		logger.write("Loading name map: " + nameMapPath);
		try (BufferedReader br = new BufferedReader(new FileReader(nameMapCSV))) {
			String line = "";
			while ((line = br.readLine()) != null) {
				String[] vals = line.split(",");
				loadAnonNameMap(vals);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
	}
	
	protected void loadAnonNameMap(String[] vals) {
		CommentsIdenMap.put(vals[0], concat(vals[3], vals[4], vals[5]));
		fakeNameSet.add(vals[3]);
	}
	
	public void updateNameMap() throws IOException {
		if (!nameMapCSV.exists()) {
			logger.write("Default name map not found, creating default name map: " + NAME_MAP);
			nameMapCSV.createNewFile();
			logger.write("Default name map created: " + NAME_MAP);
		}
		logger.write("Updating name map: " + nameMapCSV.getPath());
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(nameMapCSV, true))) {
			for (Entry<String, String> entry: newPairs.entrySet()) {
				bw.write(entry.getKey() + "," + entry.getValue());
				bw.newLine();
			}
		} 
//		DriveAPI.updateFile(NAME_MAP_ID, nameMapPath);
	}
	
	protected String concatFirst3(String[] tokens) {
		return concat(tokens[0], tokens[1], tokens[2]);
	}
	
	protected String concat(String onyen, String firstName, String lastName) {
		return onyen+","+firstName+","+lastName;
	}
		
	public static String parseArg(String arg) {
		if (arg.startsWith("'") && arg.endsWith("'")) {
			return arg.substring(1, arg.length()-1);
		}
		return arg;
	}
	
	public StringBuilder readFile(File file) {
		StringBuilder content = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(file))){
			String nextLine = "";
			while((nextLine = br.readLine()) != null) {
				content.append(nextLine+System.lineSeparator());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}
	
	public void writeFile(File file, String content) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))){
			bw.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	public String maybeUnquote(String s) {
//		if (s.startsWith("\"") && s.endsWith("\"")) {
//			return s.substring(1, s.length() - 1);
//		} else {
//			return s;
//		}
//	}
	
	public String maybeUnquote(String s) {
		return unquote(s);
//		if (s.startsWith("\"") && s.endsWith("\"")) {
//			return s.substring(1, s.length() - 1);
//		} else {
//			return s;
//		}
	}
	
	public void loadNameToOnyenMap(File gradesCsv) {
		String gradesCsvString = readFile(gradesCsv).toString();
		String[] lines = gradesCsvString.split("\\R");
		for (int i = 3; i < lines.length; i++) {
			String[] fields = lines[i].split(",");
			String onyen = maybeUnquote(fields[1]);
			String lastName = maybeUnquote(fields[2]);
			String firstName = maybeUnquote(fields[3]);
			String aFullName = firstName + " " + lastName;
			
			String anUnquotedOnyen = unquote(onyen);
			String anUnquotedLastName = unquote(lastName);
			String anUnquotedFirstName = unquote(firstName);
			String anUnquotedFullName = anUnquotedFirstName + " " + anUnquotedLastName; 

//			maybeQuotedNameToOnyen.put(firstName + " " + lastName, onyen);
			maybeQuotedNameToOnyen.put(aFullName, onyen);
			nameToOnyen.put(anUnquotedFullName, anUnquotedOnyen);			

//			mapNames(aFullName, firstName, lastName, fakeName);
		}
	}
	public String unquote(String s) {
		if (s.startsWith("\"") && s.endsWith("\"")) {
			return s.substring(1, s.length() - 1);
		} else {
			return s;
		}
	}
}
