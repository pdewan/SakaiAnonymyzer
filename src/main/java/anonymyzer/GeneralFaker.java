package anonymyzer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesGrouping;

import anonymyzer.factories.AliasesManagerFactory;
import anonymyzer.factories.LoggerFactory;

public abstract class GeneralFaker {
	protected Map<String, String> fullNameIdenMap = new HashMap<>();

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
	public final static String HIDDEN_NAME = "[h]";
	Map<String, String> maybeQuotedNameToOnyen = new HashMap<>();
	Map<String, String> nameToOnyen = new HashMap<>();
	Map<String, String> storedNamesToOnyen = new HashMap<>();

	Collection<String> onyens;

//	Map<String, String> onyenToFakeName = new HashMap<>();

	Map<String, String> authorToFakeAuthor = new HashMap<>();
	Map<String, String> emailToFakeAuthor = new HashMap<>();
	Map<String, String> uidToFakeAuthor = new HashMap<>();
	Map<String, String> fullNameToFakeFullName = new HashMap<>();
	Map<String, String> someNameToFakeFullName = new HashMap<>();
//	Map<String, String> firstNameToFakeFullName = new HashMap<>();
//	Map<String, String> middleNameToFakeFullName = new HashMap<>();

	Map<String, String> singlePersonTemporaryMap = new HashMap<>();

	Map<String, String> firstNameToFakeFirstName = new HashMap<>();
	Map<String, String> lastNameToFakeLastName = new HashMap<>();
	Map<String, String> someNameToFakeAuthor = new HashMap<>();
	protected List<String> originalNameList = new ArrayList();
	protected List<String> replacementNameList = new ArrayList();

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

	protected boolean isLocalSpace() {
		return false;
	}

	protected void processElements(Map<String, String> aMap) {
		for (String aKey : aMap.keySet()) {
//			originalNameList.add(aKey);
			String aValue = aMap.get(aKey);
			String anExistingValue = someNameToFakeAuthor.get(aKey);
//			if (aValue.equals(anExistingValue) && !isLocalSpace()) {
//				continue; // already processed
//
//			}
			if (anExistingValue != null) {
				continue;
			}
			originalNameList.add(aKey);
			replacementNameList.add(aValue);
			someNameToFakeAuthor.put(aKey, aValue);
		}
	}

	protected void processElementsOfAllMaps() {

		processElements(authorToFakeAuthor);
		processElements(emailToFakeAuthor);
		processElements(uidToFakeAuthor);
		processElements(fullNameToFakeFullName);
		processElements(firstNameToFakeFirstName);
		processElements(lastNameToFakeLastName);

	}

	protected String normalizedName(String aName) {
		return aName.split("\\[")[0].trim();
	}

	protected boolean normalizedEquals(String aValue1, String aValue2) {
		String aNormalizedValue1 = normalizedName(aValue1);
		String aNormalizedValue2 = normalizedName(aValue2);
		return aNormalizedValue1.equals(aNormalizedValue2);
	}

	protected void nonDuplicateLocalPut(Map<String, String> aMap, String aKey, String aValue) {
		if (aMap.get(aKey) != null) {
			return;
		}
//		if (aKey.equals("jilland")) {
//			System.out.println("found problematic string");
//		}
		aMap.put(aKey, aValue);
	}

	protected void nonDuplicatePut(Map<String, String> aMap, String aKey, String aValue) {
		if (aKey.contains("nstructor")) {
			return;
		}

//		if (aKey.equals("kianw")) {
//			System.out.println("found offending text");
//		}
		if (localPhase) {
			nonDuplicateLocalPut(aMap, aKey, aValue);
			return;
		}
//		if (aKey.equals("Lee") ) {
//			System.out.println(" found offending text");
//		}
		String anExistingValue = aMap.get(aKey);

//		String[] anExistingValueComponents = anExistingValue.split("\\[");
//		String anExistingValueNormalized = anExistingValueComponents[0];
		if (anExistingValue == null) {
			aMap.put(aKey, aValue);
//			if (aValue.contains("[f]")) {
//				System.out.println("Found first name \n");
//			}
			return;
		}
		if (anExistingValue.equals(HIDDEN_NAME)) {
			return;
		}

		if (!normalizedEquals(aValue, anExistingValue)) {
//		if (anExistingValue != null && 
//				!normalizedEquals(aValue, anExistingValue)) {
			String aMessage = "Duplicate Value for (" + aKey.toLowerCase() + ")" + normalizedName(anExistingValue)
					+ "-->" + normalizedName(aValue);
			assignmentMetrics.numNameClashes++;
			aMap.put(aKey, HIDDEN_NAME);

			try {
				if (!messagesOutput.contains(aMessage)) {
					specificLogger.write("Key:" + aKey + " Duplicate Values:" + anExistingValue + "," + aValue + "\n");
					specificLogger.flush();
					messagesOutput.add(aMessage);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
//		else {
//			
//			aMap.put(aKey, aValue);
//
////			aMap.put(aKey.toLowerCase(), aValue);
//		}
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
		((FakeValuesGrouping) this.getFaker().fakeValuesService().getFakeValueList().get(0))
				.setSpecifiedFileName("name", nameFile.getPath());
		return true;
	}

	public static GeneralFaker createFaker() throws IOException {
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

	abstract protected void processExecuteArg(Object arg);

	protected boolean filterByOnyens() {
		return true;
	}

	public void execute(Object arg) throws IOException, InterruptedException {
		processExecuteArg(arg);
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

	protected void putAliases(Map<String, String> aMap, String aKey, String aValue, boolean isId) {
		List<String> anAliases = AliasesManagerFactory.getAliases(aKey);
		for (String anAlias : anAliases) {
//			if (anAlias.equals("mattdo")) {
//				System.out.println("found offending text");
//			}
			putFullName(aMap, anAlias, aValue + "[a]", isId, true);
		}
	}

	protected void putNameAndLowerCase(Map<String, String> aMap, String aName, String aReplacement) {
//		String[] aNameComponents = aName.split(" ");
		nonDuplicatePut(aMap, aName, aReplacement);
		String aNameLowercase = aName.toLowerCase();
		if (aName.equals(aNameLowercase)) {
			return;
		}
//		if (aNameLowercase.equals("jilland")) {
//			System.out.println("found problematic string");
//		}
//		nonDuplicatePut(aMap, aNameLowercase, aReplacement.toLowerCase() + "[lc]");
		nonDuplicatePut(aMap, aNameLowercase, aReplacement + "[lc]");

	}

	protected boolean localPhase = false;

	protected void putFullNameAndAliases(Map<String, String> aMap, String aName, String aReplacement, boolean isId) {
//		localPhase = true;
		putFullName(aMap, aName, aReplacement, isId, false);
		putAliases(aMap, aName, aReplacement, isId);
//		localPhase = false;

//		singlePersonTemporaryMap.clear();
//		putFullName(singlePersonTemporaryMap, aName, aReplacement, isId, false);
//		putAliases(singlePersonTemporaryMap, aName, aReplacement, isId);
//		for (String aName:singlePersonTemporaryMap)
	}

	protected void putFullName(Map<String, String> aMap, String aName, String aReplacement, boolean isId,
			boolean isAlias) {
//		if (aName.equals("kianw")) {
//			System.out.println("found offending text");
//		}
//		boolean isAlias = aReplacement.endsWith("[a]");
		String aSuffix = isAlias ? "[a]" : "";

		String[] aNameComponents = aName.split(" ");
		String[] aReplacementComponents = aReplacement.split(" ");
		putNameAndLowerCase(aMap, aName, aReplacement);

		if (aNameComponents.length == 1 || 
				aReplacementComponents.length == 1 || 
				isId) {
//			putNameAndLowerCase(aMap, aName, aReplacement);
			return;
		}
//		if (addFullName)
		String aFirstName = aNameComponents[0];
		String aLastName = aNameComponents[aNameComponents.length - 1];

		String aReplacementFirst = aReplacementComponents[0];
		String aReplacementLast = aReplacementComponents[aReplacementComponents.length - 1];

		String aFullNameNoSpace = aFirstName + aLastName;
//		String aFullReplacementNoSpace = aReplacementFirst + aReplacementLast;

//		putNameAndLowerCase(aMap, aFullNameNoSpace, aFullReplacementNoSpace + "[ns]");
		putNameAndLowerCase(aMap, aFullNameNoSpace, aReplacement + "[ns]");

//		putNameAndLowerCase(aMap, aFirstName, aReplacementFirst + "[f]" + aSuffix);
//		putNameAndLowerCase(aMap, aLastName, aReplacementLast + "[l]");

		putNameAndLowerCase(aMap, aFirstName, aReplacement + "[f]" + aSuffix);
		putNameAndLowerCase(aMap, aLastName, aReplacement + "[l]");

		if (aNameComponents.length >= 3 && assignmentMetrics != null) {
			assignmentMetrics.numMiddleNames++;
			String aMiddleName = aNameComponents[1];
			putMiddleName(aMap, aFirstName, aMiddleName, aLastName, aReplacement, aReplacementFirst, aReplacementLast,
					isAlias);

		}
	}

	protected void putMiddleName(Map<String, String> aMap, String aFirstName, String aMiddleName, String aLastName,
			String aReplacement, String aReplacementFirst, String aReplacementLast, boolean isAlias) {
		String aSuffix = isAlias ? "[a]" : "";
//		putNameAndLowerCase(aMap, aMiddleName, aReplacementFirst + "[m]" + aSuffix);
//		putNameAndLowerCase(aMap, aMiddleName + " " + aLastName,
//				aReplacementFirst + " " + aReplacementLast + "[ml]" + aSuffix);
//		putNameAndLowerCase(aMap, aFirstName + " " + aMiddleName, aReplacementFirst + "[fm]" + aSuffix);
//		putNameAndLowerCase(aMap, aFirstName + " " + aLastName,
//				aReplacementFirst + " " + aReplacementLast + "[fl]" + aSuffix);
//		putNameAndLowerCase(aMap, aFirstName + aMiddleName + aLastName,
//				aReplacementFirst + aReplacementLast + "[fml]" + aSuffix);

		putNameAndLowerCase(aMap, aMiddleName, aReplacement + "[m]" + aSuffix);
		putNameAndLowerCase(aMap, aMiddleName + " " + aLastName, aReplacement + "[ml]" + aSuffix);
		putNameAndLowerCase(aMap, aFirstName + " " + aMiddleName, aReplacement + "[fm]" + aSuffix);
		putNameAndLowerCase(aMap, aFirstName + " " + aLastName, aReplacement + "[fl]" + aSuffix);
		putNameAndLowerCase(aMap, aFirstName + aMiddleName + aLastName, aReplacement + "[fml]" + aSuffix);

	}

	protected String nonDuplicateGet(Map<String, String> aMap, String aKey) {
		String retVal = aMap.get(aKey);
		if (retVal != null && !HIDDEN_NAME.equals(retVal)) {
			return retVal;
		}
		return retVal;
	}

	protected String nonDuplicateCaseIndependentGet(Map<String, String> aMap, String aKey) {
		String retVal = nonDuplicateGet(aMap, aKey);
		if (retVal == null) {
			retVal = nonDuplicateGet(aMap, aKey.toLowerCase());
		}
		return retVal;
	}
	
	protected String getFakeOfNameFromIDMap(String aName) {
		String aFullFakeName = CommentsIdenMap.get(aName); // somebody who did not submit an assignment
		if (aFullFakeName == null) {
			
			return null;
		}
		String[] aSplitFullFakeName = aFullFakeName.split(",");
		return aSplitFullFakeName[0];
	}
	
	protected String getFakeOfNameFromStoredNameMap(String aName) {
		String anID = storedNamesToOnyen.get(aName);
		if (anID == null) {
			return null;
		}
		return getFakeOfNameFromIDMap(anID);
		
		
	}

	protected String getFakeOfName(String aName) {
//		String retVal = CommentsIdenMap.get(aName);
//		if (retVal != null) {
//			return retVal;
//		}
//		retVal = CommentsIdenMap.get(aName.toLowerCase());
//		if (retVal != null) {
//			return retVal;
//		}
//		String retVal = fullNameToFakeFullName.get(aName);
		String retVal = nonDuplicateCaseIndependentGet(fullNameToFakeFullName, aName);
		String[] aNameComponents = aName.split(" ");

		if (retVal != null) {
			if (aNameComponents.length > 1) {
				assignmentMetrics.numFullNameResolutions++;
			}
			return retVal;
		}

//		if (retVal != null && !HIDDEN_NAME.equals(retVal)) {
//			return retVal;
//		}
//		return null;
		// do we really need this as we are adding all combinations in the
		// loadAnonNameMap
//		retVal = fullNameToFakeFullName.get(aName.toLowerCase());
//		if (retVal != null) {
//			return retVal;
//		}

//		String[] aNameComponents = aName.split(" ");
		if (aNameComponents.length == 1) { // onyen
			return getFakeOfNameFromIDMap(aName);
//			String aFullFakeName = CommentsIdenMap.get(aName); // somebody who did not submit an assignment
//			if (aFullFakeName == null) {
//				
//				return null;
//			}
//			String[] aSplitFullFakeName = aFullFakeName.split(",");
//			return aSplitFullFakeName[0];
		}
		retVal = getFakeOfNameFromStoredNameMap(aName);
		if (retVal != null) {
			return retVal;
		}
//		String anOnyen = nameToOnyen.get(aName);
//		if (anOnyen != null) {
//			return getFakeOfNameFromIDMap(anOnyen);
//		}
		
		for (String aNameComponent : aNameComponents) {
			retVal = nonDuplicateCaseIndependentGet(fullNameToFakeFullName, aNameComponent);

			if (retVal != null) {
				assignmentMetrics.numNameComponentResolutions++;

				return retVal;
			}

		}
		String aFullNameNoSpace = aNameComponents[0] + aNameComponents[aNameComponents.length - 1];
		retVal = nonDuplicateCaseIndependentGet(fullNameToFakeFullName, aFullNameNoSpace);
		if (retVal != null) {
			assignmentMetrics.numFullNameNoSpaceResolutions++;

			return retVal;
		}

		String aLastNameFirst = aNameComponents[aNameComponents.length - 1] + " " + aNameComponents[0];
		retVal = nonDuplicateCaseIndependentGet(fullNameToFakeFullName, aLastNameFirst);

		if (retVal != null) {
			assignmentMetrics.numNameReversalResolutions++;

			return retVal;
		}

		if (aNameComponents.length >= 3) {
			String aFirstLastSpace = aNameComponents[0] + " " + aNameComponents[aNameComponents.length - 1];
			retVal = nonDuplicateCaseIndependentGet(fullNameToFakeFullName, aFirstLastSpace);
			if (retVal != null) {
				assignmentMetrics.numNameDropResolutions++;
				return retVal;
			}

			String aMiddleLastNameNoSpace = aNameComponents[1] + aNameComponents[2];
			retVal = nonDuplicateCaseIndependentGet(fullNameToFakeFullName, aMiddleLastNameNoSpace);
			if (retVal != null) {
				assignmentMetrics.numNameDropResolutions++;

				return retVal;
			}
//			retVal = nonDuplicateGet(fullNameToFakeFullName, aMiddleLastNameNoSpace.toLowerCase());
//			if (retVal != null) {
//				return retVal;
//			}
			String aMiddleLastSpace = aNameComponents[1] + " " + aNameComponents[2];
			retVal = nonDuplicateCaseIndependentGet(fullNameToFakeFullName, aMiddleLastSpace);
			if (retVal != null) {
				assignmentMetrics.numNameDropResolutions++;
				return retVal;
			}
//			retVal = nonDuplicateGet((aMiddleLastSpace.toLowerCase());
//			if (retVal != null) {
//				return retVal;
//			}
			aLastNameFirst = aNameComponents[3] + " " + aNameComponents[1] + " " + aNameComponents[2];
			retVal = nonDuplicateCaseIndependentGet(fullNameToFakeFullName, aLastNameFirst);
			if (retVal != null) {
				assignmentMetrics.numNameReversalResolutions++;
				return retVal;
			}

		}
		return retVal;
	}

	protected String[] toFakeNames(String aFakeName) {
		String[] aNames = aFakeName.split(" ");
		String[] retVal = new String[] { aNames[0] + " " + aNames[aNames.length - 1], aNames[0],
				aNames[aNames.length - 1] };
		return retVal;
	}

	// ugh duplicating code in AnonFaker!
	protected String getFakeOfNameOrPossiblyAlias(String aName) {
//		String retVal = CommentsIdenMap.get(aName);
		String retVal = getFakeOfName(aName);

		if (retVal != null) {
			return retVal;
		}
		List<String> anAliases = AliasesManagerFactory.getAliases(aName);
		for (String anAlias : anAliases) {
//			retVal = CommentsIdenMap.get(anAlias);
			retVal = getFakeOfName(anAlias);
//			if (retVal == null) {
//				retVal = CommentsIdenMap.get(anAlias.toLowerCase());
//
//			}
			if (retVal != null) {
				String aMessage = "Using alias " + anAlias + " for " + aName;
				assignmentMetrics.numAliasesUsed++;
				if (!messagesOutput.contains(aMessage)) {
					try {
						specificLogger.write(aMessage + "\n");
						specificLogger.flush();
						assignmentMetrics.numUniqueAliasesUsed++;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return retVal;
			}
		}
		return null;
	}

	protected void loadAnonNameMap(String[] vals) {
		String aKey = vals[0];
		String aValue = concat(vals[3], vals[4], vals[5]);
//		CommentsIdenMap.put(vals[0], concat(vals[3], vals[4], vals[5]));
		CommentsIdenMap.put(aKey, aValue);

		fakeNameSet.add(vals[3]);
		String aFullRealName = vals[1] + " " + vals[2];
		String anOnyen = vals[0];
		
		storedNamesToOnyen.put(aFullRealName, anOnyen);

//		if (aKey.equals("jergle")) {
//			System.out.println("found offending onyen");
//		}

		String aKey2 = vals[1] + " " + vals[2];
//		String aValue2 = concat(vals[3], vals[4], vals[5]);
//		putFullNameAndAliases(fullNameToFakeFullName, aKey2, aValue2, false);
//		putFullNameAndAliases(fullNameToFakeFullName, aKey, aValue2, true);

//		if (!filterByOnyens() || (onyens != null && onyens.contains(aKey))) {
		if (addFullName(aKey)) {
			putFullNameAndAliases(fullNameToFakeFullName, aKey, vals[3], true);
			putFullNameAndAliases(fullNameToFakeFullName, aKey2, vals[3], false);
		}
	}
	
	protected boolean addFullName(String anOnyen) {
		return !filterByOnyens() || (onyens != null && onyens.contains(anOnyen));

	}
	

	public void putNamePair(String aName, String aFakeName) {
		specificLogLine(aName + "->" + aFakeName);
		newPairs.put(aName, aFakeName);
		String[] aNames = aName.split(",");
		if (aNames.length == 3) {
			String[] aMergedArray = new String[6];
			for (int i = 0; i < aNames.length; i++) {
				aMergedArray[i] = aNames[i];
			}
			aMergedArray[3] = aFakeName;
			String[] aFakeNames = aFakeName.split(" ");
			aMergedArray[4] = aFakeNames[0];
			aMergedArray[5] = aFakeNames[1];
			loadAnonNameMap(aMergedArray);
		}
	}

	public void updateNameMap() throws IOException {
		if (!nameMapCSV.exists()) {
			logger.write("Default name map not found, creating default name map: " + NAME_MAP);
			nameMapCSV.createNewFile();
			logger.write("Default name map created: " + NAME_MAP);
		}
		logger.write("Updating name map: " + nameMapCSV.getPath());
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(nameMapCSV, true))) {
			for (Entry<String, String> entry : newPairs.entrySet()) {
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
		return onyen + "," + firstName + "," + lastName;
	}

	public static String parseArg(String arg) {
		if (arg.startsWith("'") && arg.endsWith("'")) {
			return arg.substring(1, arg.length() - 1);
		}
		return arg;
	}

	public StringBuilder readFile(File file) {
		StringBuilder content = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String nextLine = "";
			while ((nextLine = br.readLine()) != null) {
				content.append(nextLine + System.lineSeparator());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

	public void writeFile(File file, String content) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
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

	public File getGradesCSV(String[] args, String folderPath) {
		String gradesCsvPath = args.length == 2 ? parseArg(args[1]) : getGradesCsv(folderPath);
		if (gradesCsvPath.isEmpty()) {
			System.err.println("Path for grades.csv is missing and cannot be found in the folder provided");
			System.exit(1);
		}
		File gradesCsv = new File(gradesCsvPath);

		if (!gradesCsv.exists()) {
			System.err.println(gradesCsvPath + " file does not exist.");
			return null;
		}
		return gradesCsv;
	}

	protected String getGradesCsv(String path) {
		File folder = new File(path);
		for (File file : folder.listFiles()) {
			if (file.getName().equals("grades.csv")) {
				return file.getPath();
			}
		}
		return "";
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
//			if (aFullName.contains("Bill")) {
//				System.out.println("Found Bill");
//			}
			maybeQuotedNameToOnyen.put(aFullName, onyen);
			nameToOnyen.put(anUnquotedFullName, anUnquotedOnyen);

//			mapNames(aFullName, firstName, lastName, fakeName);
		}
		onyens = nameToOnyen.values();
	}

	public String unquote(String s) {
		if (s.startsWith("\"") && s.endsWith("\"")) {
			return s.substring(1, s.length() - 1);
		} else {
			return s;
		}
	}
}
