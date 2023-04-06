package anonymyzer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesGrouping;

import anonymyzer.factories.AliasesManagerFactory;
import anonymyzer.factories.KeywordFactory;
import anonymyzer.factories.LineReplacerFactory;

public class AnonFaker extends Anon {

	File nameMapCSV;
	String nameMapPath;
	Faker faker;
	HashMap<String, String> newPairs = new HashMap<>();
	HashSet<String> fakeNameSet = new HashSet<>();
//	static int[] idx = { 1, 0, 2 }; // original, honghai
	static int[] idx = { 2, 1, 0 }; // new, pd
//	static int[] idx = { 2, 0, 1 };

	public AnonFaker() throws IOException {
		super();
		localPhase = true; // each map is different so essentially local phase
	}

	public Faker getFaker() {
		if (faker == null) {
			faker = new Faker();
		}
		return faker;
	}

	public static void main(String[] args) throws IOException {
		if (args.length < 2) {
			System.err.println("Enter main args: d|a|u(t) (name map.csv) path(s)");
			return;
		}

		AnonFaker faker = new AnonFaker();
		String mode = args[0];
		try {
			for (int i = 0; i < mode.length(); i++) {
				switch (mode.charAt(i)) {
				case DELETE:
				case ANON:
				case UNANON:
					faker.setMethod(mode.charAt(i));
					break;
//				case COURSE:
//					faker.setCourseMode(true);
//					break;
				case TXT:
					faker.setTxtMode(true);
					break;
				default:
					System.err.println("Unsupported argument!");
					return;
				}
			}
			for (int i = 1; i < args.length; i++) {
				args[i] = parseArg(args[i]);
			}
			int idx = 1;
			if (method == DELETE) {
				faker.execute(Arrays.copyOfRange(args, 1, args.length));
				return;
			}
//			String[] piazzaPostsPath = {""};
//			if (args[idx].startsWith("ByAuthorPosts") && args[idx].endsWith(".json")) {
//				piazzaPostsPath[0] = args[idx]; 
//				idx++;
//			} 
//			String[] zoomChatsPath = {"",""};
//			if (args[idx].toLowerCase().contains("zoom")){
//				zoomChatsPath[0] = args[idx]; 
//				idx++;
//			} 
//			if (args[idx].endsWith(".csv")) {
//				faker.setNameMap(args[idx]);
//				idx++;
////				args = Arrays.copyOfRange(args, 1, args.length);
//			} else {
//				faker.setNameMap(NAME_MAP);
//				faker.execute(Arrays.copyOfRange(args, 1, args.length));
//			}
			File namemap = new File(GeneralFaker.NAME_MAP);
//			try {
//				namemap = DriveAPI.downloadFileWithId(NAME_MAP_ID);
//			} catch (Exception e) {
			// TODO: handle exception
//				File token = new File(TOKEN);
//				if (token.exists()) {
//					token.delete();
//				}
//				if (token.getParentFile().exists()) {
//					File tokens = token.getParentFile();
//					System.out.println(tokens.getAbsolutePath());
//					boolean deleted = tokens.delete();
//					System.out.println(deleted);
////					token.getParentFile().delete();
//				}
//				namemap = DriveAPI.downloadFileWithId(NAME_MAP_ID);
			DownloadNameMap.main(args);
//			}
			faker.setNameMap(namemap);
			((FakeValuesGrouping) faker.getFaker().fakeValuesService().getFakeValueList().get(0))
					.setSpecifiedFileName("name", GeneralFaker.NAME_FILE);
			;
//			if (args[idx].endsWith(".yml")) {
//				if (method == ANON) {
//					((FakeValuesGrouping)faker.getFaker().fakeValuesService().getFakeValueList().get(0)).setSpecifiedFileName("name", args[1]);;
//				}
//				idx++;
//			}
			String[] paths = Arrays.copyOfRange(args, idx, args.length);
			faker.execute(paths);

//			for (String folderName : paths) {
//				zoomChatsPath[1] = folderName + File.separator + "grades.csv";;
//				if (!piazzaPostsPath[0].isEmpty()) {
//					PiazzaFaker.main(piazzaPostsPath);
//				}
//				if (!zoomChatsPath[0].isEmpty() && !zoomChatsPath[1].isEmpty()) {
//					ZoomChatFaker.main(zoomChatsPath);
//				}
//				zoomChatsPath[1] = "";
//				faker.zip(folderName, folderName+Character.toUpperCase(method));
//				faker.delete(new File(folderName));
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Done");
	}

	public void setNameMap(String path) throws IOException {
		nameMapPath = path;
		nameMapCSV = new File(path);
	}

	public void execute(String[] args) throws IOException, InterruptedException {
		if (method == ANON || method == UNANON) {
			init();
			anonymize(args);
			logger.close();
		}
//		if (method == UNANON) {
//			init();
//			unanonymize(args);
//			logger.close();
//		}
		if (method == DELETE) {
			for (String path : args) {
				File file = new File(path);
				if (file.exists()) {
					delete(file);
				} else {
					logger.write(path + " not found");
				}
			}
		}
	}
//	public static boolean isCodeFole(File f) {
//		String aName = f.getName();
//		return aName.endsWith(".c") || 
//				aName.endsWith(".java") ||
//				aName.endsWith(".py");
//	}
//	public static boolean isEclipseLog(File f) {
//		String aName = f.getName();
//		return aName.startsWith("Log") && aName.endsWith(".xml");
//	}
//	
//	
//	public static boolean isCommentStart(String aTrimmedLine) {
//		return aTrimmedLine.startsWith("/*");
//				
//	}
//	public static boolean isCommentEnd(String aTrimmedLine) {
//		return aTrimmedLine.contains("*/");
//				
//	}
//	public  boolean inProjectStat(String aLine) {
//		return aLine.startsWith("Edited") && 
//				(aLine.endsWith("Projects: "));
//	}

//	public boolean inProjectStat() {
//		return previousLine != null && 
//				previousLine.startsWith("Edited") &&
//				previousLine.endsWith("Projects: ");
//	}
//	
//	boolean inComment;
////	boolean inProjectStat;
//	protected boolean isSafeLineConservative(String aLineTrimmed) {
//		return (!aLineTrimmed.contains("file") &&
////				!aLineTrimmed.contains("onyen") &&
//				!aLineTrimmed.contains("package") && (
//				aLineTrimmed.contains("javadoc") ||
//				aLineTrimmed.contains("[cdata[") ||
//
//				aLineTrimmed.startsWith("<command") ||
//				aLineTrimmed.startsWith("<csvrow") ||
//				aLineTrimmed.contains("id=") ||
//				aLineTrimmed.contains("document") ||
//				aLineTrimmed.contains("stbuildercheck") ||
//				aLineTrimmed.contains("gradingTools") ||
//				aLineTrimmed.startsWith("</")   ||
//				aLineTrimmed.contains("random")	||
//				aLineTrimmed.contains("does")	||				
//				aLineTrimmed.contains("unc.checks") ||
//				aLineTrimmed.contains("at ")
////				line1_trimmed.startsWith("¶unc.checks")
//				
//				) );
////			return line_1;
//		
//	}
//	 
//	protected boolean currentLineHasPackage;
//	protected boolean currentLineHasClass;
//
//
//	protected Set<String> identifiersWithAuthorNames = new HashSet();
//	protected boolean isSafeLineLiberal(String aLineTrimmed) {
//		currentLineHasPackage = 
//				aLineTrimmed.contains("package");
//		currentLineHasClass = 
//				aLineTrimmed.contains("class");
//		return (
////				(
////				aLineTrimmed.contains("diffbasedfileopencommand")
////				)|| (
//				!aLineTrimmed.contains("file") && 
//				!aLineTrimmed.contains("onyen") && 
//				!currentLineHasPackage &&
//				!currentLineHasClass &&
////				!aLineTrimmed.contains("package") &&
//				!aLineTrimmed.contains("github") &&
//				!aLineTrimmed.contains("\"/") && 
//				!aLineTrimmed.contains("/users") && 
//				!aLineTrimmed.contains("/home") && 
////				!aLineTrimmed.contains("/") &&
//				!aLineTrimmed.contains("\\")) &&
//				!inProjectStat() 
////				&&
////				!AnonUtil.containsNonkeyWord(aLineTrimmed, identifiersWithAuthorNames)
////				)	
//				;
//
////				line1_trimmed.startsWith("¶unc.checks")
//				
//				
//		
////			return line_1;
//		
//	}
////	protected boolean isSafeEclipseLogLineLiberal(String aLineTrimmed) {
////		currentLineHasPackage = 
////				aLineTrimmed.contains("package");
////		currentLineHasClass = 
////				aLineTrimmed.contains("class");
////		return (
//////				(
//////				aLineTrimmed.contains("diffbasedfileopencommand")
//////				)|| (
////				!aLineTrimmed.contains("file") && 
////				!aLineTrimmed.contains("onyen") && 
////				!currentLineHasPackage &&
////				!currentLineHasClass &&
//////				!aLineTrimmed.contains("package") &&
////				!aLineTrimmed.contains("github") &&
////				!aLineTrimmed.contains("\"/") && 
////				!aLineTrimmed.contains("/users") && 
////				!aLineTrimmed.contains("/home") && 
//////				!aLineTrimmed.contains("/") &&
////				!aLineTrimmed.contains("\\")) &&
////				!inProjectStat() &&
////				!AnonUtil.contains(aLineTrimmed, identifiersWithAuthorNames)
//////				)	
////				;
////
//////				line1_trimmed.startsWith("¶unc.checks")
////				
////				
////		
//////			return line_1;
////		
////	}
//	protected boolean isSafeLine(String aLine) {
//		return false;
//	}
	public String replaceHeaders(String name, int line_num, File f, List<String> names, String aLine, int i)
			throws IOException {

//		
//		String[] tokens = getTokens(names.get(2), names.get(1), names.get(0));
		String[] tokens = getTokens(names.get(1), names.get(0), names.get(2)); // computing them each time

		if (tokens == null) {
			return aLine;
		}
//		if (aLine.contains("andrewbyerle")) {
//			System.out.println("found offending line");
//		}

		String aReplacedValue = aLine.replaceAll(name, tokens[idx[i]]).replaceAll(name.toLowerCase(), tokens[idx[i]]);// shuffle
																														// all
																														// names

//		String aReplacedValue = AnonUtil.replaceAllNonKeywords(
//				keywordsRegex(), aLine, name, tokens[idx[i]]);

		return aReplacedValue;
	}
	@Override
	protected boolean isLocalSpace() {
		return true;
	}
	
	String currentReplacementFullName = null;
	
	protected void deriveNamesAndReplacements(List<String> aNames) {
//		super.setNames(aNames);
//		originalToReplacement.clear();
		String[] tokens = getTokens(aNames.get(1), aNames.get(0), aNames.get(2)); // computing them each time
		if (tokens == null) {
			currentReplacementFullName = null;
			return ;
		}
		List<String> aDerivedNames = new ArrayList();

		List<String> aDerivedReplacements = new ArrayList();
		for (int index = 0; index < tokens.length; index++) {
			String aToken = tokens[idx[index]];
			aDerivedReplacements.add(aToken);
			aDerivedReplacements.add(aToken.toLowerCase());
		} // consstent with passed aNames
		String aFullNameSpace = aNames.get(1) + " " + aNames.get(0);
		String aFullReplacementSpace = aDerivedReplacements.get(2) + " " + aDerivedReplacements.get(0);
		originalToReplacement = new HashMap();

		String anOnyen = aNames.get(2);
		String anOyenReplacement = aFullReplacementSpace;
		if (currentReplacementFullName == null) {
		currentReplacementFullName= anOyenReplacement;
		}
		
		putFullNameAndAliases(originalToReplacement, anOnyen, anOyenReplacement, true);
		

		
		

		putFullNameAndAliases(originalToReplacement, aFullNameSpace, aFullReplacementSpace, false);
		processElements(originalToReplacement);
		
//		putFullNameAndLiases(originalToReplacement, aName, aReplacement);

		
//		String aFullNameNoSpaces = aNames.get(1).replace(" ", "") + aNames.get(0).replace(" ", "");
//
//		
//		String aFullNameSpaceLowerCase = aFullNameSpace.toLowerCase();
//
//
//		String aFullReplacementNoSpaces = aDerivedReplacements.get(2) + aDerivedReplacements.get(0); // no middle name here
////		String aFullReplacementSpace = aDerivedReplacements.get(2) + " " + aDerivedReplacements.get(0);
//		String aFullReplacementSpaceLowerCase = aFullReplacementSpace.toLowerCase();
//		String aFullReplacementNoSpacesLowerCase = aFullReplacementNoSpaces.toLowerCase();
//		
//
//		
//		
//		
//		
////		String aFullReplacementNameNoSpaces = aDerivedReplacements.get(2) + aDerivedReplacements.get(0);
//		aDerivedReplacements.add(aFullReplacementNoSpaces+"[NS]");
//		aDerivedReplacements.add(aFullReplacementNoSpacesLowerCase+"[L]");
//		for (int index = 0; index < aNames.size(); index++) {
//			aDerivedNames.add(aNames.get(index));
//			aDerivedNames.add(aNames.get(index).toLowerCase());
//		} // a derived names consistent with names plus a lower case
//		aDerivedNames.add(aFullNameNoSpaces);
//		aDerivedNames.add(aFullNameNoSpaces.toLowerCase());
//		super.deriveNamesAndReplacements(aDerivedNames);
//		setNameReplacements(aDerivedReplacements);
//		
//		aDerivedNames.add(aFullNameSpace);
//		aDerivedReplacements.add(aFullReplacementSpace);
//		aDerivedNames.add(aFullNameSpaceLowerCase);
//		aDerivedReplacements.add(aFullReplacementSpaceLowerCase);
//		
//		
//		
//		
//		List<String> anOnyenAliases = AliasesManagerFactory.getAliases(anOnyen);
//		for (String anOnyenAlias:anOnyenAliases) {
//			aDerivedNames.add(anOnyenAlias);
//			aDerivedReplacements.add(anOyenReplacement+"[A]");
//		}
//		
//		List<String> aFullNameAliases = AliasesManagerFactory.getAliases(aFullNameSpace);
//		for (String aFullNameAlias:aFullNameAliases) {			
//			
//			String aFullNameAliasLowerCase = aFullNameAlias.toLowerCase();
//			
//			String aFullNameAliasNoSpace = aFullNameAlias.replace(" ", "");
//			String aFullNameAliasNoSpaceLowerCase = aFullNameAliasNoSpace.toLowerCase();
//			
//			aDerivedNames.add(aFullNameAlias);
//			aDerivedReplacements.add(aFullReplacementSpace+"[A]");
//			
//			aDerivedNames.add(aFullNameAliasLowerCase);
//			aDerivedReplacements.add(aFullReplacementSpaceLowerCase+"[A]");
//			
//			aDerivedNames.add(aFullNameAliasNoSpace);
//			aDerivedReplacements.add(aFullReplacementNoSpaces+"[A]");
//			
//			aDerivedNames.add(aFullNameAliasNoSpaceLowerCase);
//			aDerivedReplacements.add(aFullReplacementNoSpacesLowerCase+"[A]");
//			
//			
//		}
//		
//		originalToReplacement = new HashMap();
//		for (int index = 0; index < aDerivedNames.size(); index++) {
//			originalToReplacement.put(aDerivedNames.get(index), aDerivedReplacements.get(index));
//		}
//		if (aUserName != null && !aDerivedNames.contains(aUserName)) {
//			aDerivedNames.add(aUserName);
//			aDerivedReplacements.add(aFullReplacementNameNoSpaces.toLowerCase());			
//		}
		
	}
	
	
	
	
	
	protected List<String> nameReplacements;
	
//	protected Map<String, String> originalToReplacement = new HashMap();
//	
//	public Map<String, String> getOriginalToReplacement() {
//		return originalToReplacement;
//	}
//
//	public void setOriginalToReplacement(Map<String, String> newVal) {
//		this.originalToReplacement = newVal;
//	}

	protected void setNameReplacements(List<String> aNameReplacements) {
//		nameReplacements = aNameReplacements;
		replacementNameList = aNameReplacements;
	}
	
	protected List<String> getNameReplacements() {
//		return nameReplacements;
		return replacementNameList;
	}
	
	protected void setUserName(String aUserName) {
		if (userName != null) {
			return;
		}
//		if (aUserName != null && !getNames().contains(aUserName)) {
//			getNames().add(aUserName);
//			String aReplacement = nameReplacements.get(nameReplacements.size() - 1).toLowerCase(); // first + last
//			nameReplacements.add(aReplacement);	
//			originalToReplacement.put(aUserName, aReplacement);
//		}
		/*
		 * originalNameList.add(aKey);
			replacementNameList.add(aValue);
		 */
		if (aUserName != null && !originalNameList.contains(aUserName)){
			originalNameList.add(aUserName);
//			getNames().add(aUserName);
//			String aReplacement = replacementNameList.get(replacementNameList.size() - 1).toLowerCase(); // first + last
			replacementNameList.add(currentReplacementFullName);	
			originalToReplacement.put(aUserName, currentReplacementFullName);
		}
		super.setUserName(aUserName);
	}
	
	
//	Set<String> messagesOutput = new HashSet();
//	StringBuffer replacementsMessageList = new StringBuffer();

	public String replaceHeaders(int line_num, File f, String aLine, 
			AssignmentMetrics aAassignmentMetrics) throws IOException {
//	}
//
//	public String replaceHeaders(int line_num, File f, List<String> names, String aLine, int i) throws IOException {

//		
//		String[] tokens = getTokens(names.get(2), names.get(1), names.get(0));
//		replacementsMessageList.setLength(0);
		
		// This is being done for each line, not a good idea.
//		String[] tokens = getTokens(names.get(1), names.get(0), names.get(2)); // computing them each time
//		if (tokens == null) {
//			return aLine;
//		}
//		List<String> aDerivedNames = new ArrayList();
//
//		List<String> aDerivedReplacements = new ArrayList();
//		for (int index = 0; index < tokens.length; index++) {
//			String aToken = tokens[idx[index]];
//			aDerivedReplacements.add(aToken);
//			aDerivedReplacements.add(aToken.toLowerCase());
//		}
//		String aFullReplacementNameNoSpaces = aDerivedReplacements.get(2) + aDerivedReplacements.get(0);
//		aDerivedReplacements.add(aFullReplacementNameNoSpaces);
//		aDerivedReplacements.add(aFullReplacementNameNoSpaces.toLowerCase());
//		for (int index = 0; index < names.size(); index++) {
//			aDerivedNames.add(names.get(index));
//			aDerivedNames.add(names.get(index).toLowerCase());
//		}
//		String aFullNameNoSpaces = names.get(1) + names.get(0);
//		aDerivedNames.add(aFullNameNoSpaces);
//		aDerivedNames.add(aFullNameNoSpaces.toLowerCase());
		List<String> aDerivedNames = getNames();
		List<String> aDerivedReplacements = getNameReplacements();
		if (aDerivedNames == null || aDerivedReplacements == null) {
			return aLine;
		}
//		if (aUserName != null && !aDerivedNames.contains(aUserName)) {
//			aDerivedNames.add(aUserName);
//			aDerivedReplacements.add(aFullReplacementNameNoSpaces.toLowerCase());			
//		}
		// to lower case in case words are upper case and we do not have candidate words that are upper case
//		List<String> aFragmentsWithContext = AnonUtil.fragmentsWithContext(aLine.toLowerCase(), aDerivedNames, false);
//		int aNumFragments = aFragmentsWithContext.size();
//		if (aNumFragments == 0) {
//			System.out.println ("Found problematic line");
//		}
//		String aFragmentsWithContextToString = "Fragments found:" + aFragmentsWithContext.toString() +"\n";
//		if (aFragmentsWithContextToString.contains("(jilland)_assignment1")) {
//			System.out.println ("Found problematic word");
//		}
		replacementsMessageList.setLength(0);
//		int anOriginalNumberOfMessages = messagesOutput.size();
//		String aReplacedValue = AnonUtil.replaceAllNonKeywords(replacementsMessageList, specificLogger, aNumFragments, keywordsRegex(), aLine,
//				aDerivedNames, aDerivedReplacements);
		
		String aReplacedValue = LineReplacerFactory.replaceLine(line_num, aLine,
				messagesOutput, 
				specificLogger, 
				KeywordFactory.keywordsRegex(), 
				aDerivedNames, 
				aDerivedReplacements, 
				originalToReplacement,
				aAassignmentMetrics);
		
//		String aReplacedValue = AnonUtil.replaceAllNonKeywords(line_num, aLine,
//				messagesOutput, specificLogger, keywordsRegex(), aDerivedNames, aDerivedReplacements, originalToReplacement);
		
//		if (messagesOutput.size() != anOriginalNumberOfMessages) {
//			specificLogger.write("Replacement:" + aReplacedValue + "\n");
//		}
//		String aReplacementsMessageString = replacementsMessageList.toString();
		
		
//		String aMessageOutput = aFragmentsWithContextToString + aReplacementsMessageString;
//		
//
//		if (!messagesOutput.contains(aMessageOutput)) {
////			List<String> aFragmentsWithContext = AnonUtil.fragmentsWithContext(aString, aDerivedNames);
//
//
//			specificLogger.write(line_num + ":" + aMessageOutput);
//			specificLogger.flush();
//			messagesOutput.add(aMessageOutput);
//		}

		return aReplacedValue;
	}

	public void anonymize(String[] args) throws IOException, InterruptedException {
		loadNameMap();
		for (String path : args) {
			anonymize(path);
		}
		updateNameMap();
	}

//	public void unanonymize(String[] args) throws IOException, InterruptedException {
//		loadNameMap();
//		for (String path : args) {
//			anonymize(path);
//		}
////		super.anonymize(args);
//	}

	public void setNameMap(File file) {
		nameMapPath = file.getPath();
		nameMapCSV = file;
	}

	public void loadNameMap() throws IOException {
		if (!nameMapCSV.exists()) {
			System.out.println(nameMapPath + " not found, creating name map: " + nameMapPath);
			logger.write(nameMapPath + " not found, creating name map: " + nameMapPath);
			nameMapCSV.createNewFile();
			System.out.println("Name map created: " + nameMapPath);
			logger.write("Name map created: " + nameMapPath);
			return;
		}
		logger.write("Loading name map: " + nameMapPath);
		try (BufferedReader br = new BufferedReader(new FileReader(nameMapCSV))) {
			String line = "";
			while ((line = br.readLine()) != null) {
				String[] vals = line.split(",");
				if (method == ANON) {
					loadAnonNameMap(vals);
				} else {
					loadUnanonNameMap(vals);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	protected void loadAnonNameMap(String[] vals) {
		commentsIdenMap.put(vals[0], concat(vals[3], vals[4], vals[5]));
		fakeNameSet.add(vals[3]);
	}

	protected void loadUnanonNameMap(String[] vals) {
		commentsIdenMap.put(vals[3], concatFirst3(vals));
	}

	public void updateNameMap() throws IOException {
		if (!nameMapCSV.exists()) {
			System.out.println("Default name map not found, creating default name map: " + GeneralFaker.NAME_MAP);
			logger.write("Default name map not found, creating default name map: " + GeneralFaker.NAME_MAP);
			nameMapCSV.createNewFile();
			System.out.println("Default name map created: " + GeneralFaker.NAME_MAP);
			logger.write("Default name map created: " + GeneralFaker.NAME_MAP);
		}
		System.out.println("Updating name map: " + nameMapCSV.getPath());
		logger.write("Updating name map: " + nameMapCSV.getPath());
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(nameMapCSV, true))) {
			for (Entry<String, String> entry : newPairs.entrySet()) {
				bw.write(entry.getKey() + "," + entry.getValue());
				bw.newLine();
			}
		}
		newPairs.clear();
//		DriveAPI.updateFile(GeneralFaker.NAME_MAP_ID, nameMapPath);
		UpdateNameMap.main(new String[0]);
	}

	protected String getToReplace(String lastName, String firstName, String onyen) {
		String[] tokens = getTokens(firstName, lastName, onyen);
		classNameMap.put(concat(onyen, firstName, lastName), concatFirst3(tokens));
		if (tokens == null) {
			return null;
		}
		return tokens[2] + ", " + tokens[1] + "(" + tokens[0] + ")";
	}
	
	protected String getFakeOfNameOfPossiblyAlias(String aName) {
		String retVal = commentsIdenMap.get(aName);
		if (retVal != null) {
			return retVal;
		}
		List<String> anAliases = AliasesManagerFactory.getAliases(aName);
		for (String anAlias:anAliases) {
			retVal = commentsIdenMap.get(anAlias);
			if (retVal == null) {
				retVal = commentsIdenMap.get(anAlias.toLowerCase());
			}
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
	
	protected String[] getTokens(String firstName, String lastName, String onyen) {
//		String fake = commentsIdenMap.get(onyen);
		String fake = getFakeOfNameOfPossiblyAlias(onyen);
		if (fake != null) {
			return fake.split(",");
		}
		if (method == UNANON) {
			return null;
		}
		String[] tokens = new String[3];
		do {
			tokens[1] = getFaker().name().firstName();
			tokens[2] = getFaker().name().lastName();
			tokens[0] = tokens[1] + " " + tokens[2];
		} while (fakeNameSet.contains(tokens[2]));
		commentsIdenMap.put(onyen, concatFirst3(tokens));
		fakeNameSet.add(tokens[2]);
		newPairs.put(concat(onyen, firstName, lastName), concatFirst3(tokens));
		return tokens;
	}

	protected String concatFirst3(String[] tokens) {
		return concat(tokens[0], tokens[1], tokens[2]);
	}

	protected String concat(String onyen, String firstName, String lastName) {
		return onyen + "," + firstName + "," + lastName;
	}

	protected String getNewFileName(String fileName, String lastName, String firstName, String ID) {
		String[] tokens = getTokens(firstName, lastName, ID);
		if (tokens == null) {
			return null;
		}
		return fileName.replace(lastName, tokens[2]).replace(firstName, tokens[1]).replace(ID, tokens[0]);
	}

	protected String replaceLine(String line, String[] names) {
		String[] tokens = getTokens(names[2], names[3], names[0]);
		if (tokens == null) {
			return line;
		}
		line = line.replaceAll(names[0], tokens[0]);
		line = line.replaceAll(names[1], tokens[0]);
		line = line.replaceAll(names[2], tokens[1]);
		line = line.replaceAll(names[3], tokens[2]);
		return line;
	}
//	protected void nonDuplicatePut(Map<String, String> aMap, String aKey, String aValue) {
//		if (aMap.get(aKey) != null) {
//			return;
//		}
////		if (aKey.equals("jilland")) {
////			System.out.println("found problematic string");
////		}
//		aMap.put(aKey, aValue);
//	}

}
