package anonymyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import anonymyzer.factories.DoNotFakeFactory;
import anonymyzer.factories.KeywordFactory;
import anonymyzer.factories.LineReplacerFactory;

public class ZoomChatFaker extends GeneralFaker {

	Pattern savedChatSpeakerPattern = Pattern.compile("\\d\\d:\\d\\d:\\d\\d From  (.*)  to  (.*):");
	Pattern fullNamePattern = Pattern.compile("(\\S+)\\s+(\\(.*\\))?\\s?(.*)");
	Pattern firstOrLastNamePattern = Pattern.compile("\\s*(\\S+).*");
	Pattern vttPattern = Pattern.compile(
			"(?m)^(\\d{2}:\\d{2}:\\d{2}\\.\\d+) +--> +(\\d{2}:\\d{2}:\\d{2}\\.\\d+).*[\\r\\n]+\\s*(?s)((?:(?!\\r?\\n\\r?\\n).)*)");
	Pattern vttTimePattern = Pattern.compile(
			"(?m)^(\\d{2}:\\d{2}:\\d{2}\\.\\d+) +--> +(\\d{2}:\\d{2}:\\d{2}\\.\\d+).*");
//	Pattern speakerPattern = Pattern.compile("([^:]*):.*");
	Pattern speakerPattern = Pattern.compile("(^.+):(.*)");

	Pattern txtPattern = Pattern.compile("(?m)^(\\d{2}:\\d{2}:\\d{2})\\s*(?s)((?:(?!(\\d{2}:\\d{2}:\\d{2})).)*)");
	final int TXT_GROUP_IDX = 2;
	final int VTT_GROUP_IDX = 3;

//	Map<String, String> nameToFakeName = new HashMap<>();
	Map<String, String> nameToFakeName = authorToFakeAuthor;
//	Map<String, String> maybeQuotedNameToOnyen = new HashMap<>();
//	Map<String, String> fullNameIdenMap = new HashMap<>();
	Map<File, String> chatMap = new HashMap<>();
	String logFileName = "zoom_chat_faker_log";
//	List<String> originalNameList = new ArrayList();
//	List<String> replacementNameList = new ArrayList();
//	Map<String, String> someNameToFakeName = new HashMap();

//	static String ZOOM_CHAT_FOLDER_PATH = "F:\\Hermes Data\\F21";
//	static String GRADES_CSV_PATH = "F:\\Hermes Data\\F21\\grades.csv";

	public ZoomChatFaker() throws IOException {
		super();
//		nameToFakeName = new HashMap<>();
//		maybeQuotedNameToOnyen = new HashMap<>();
	}
	
//	public  void checkGradesCSVAndOtherArg(String[] args, GeneralFaker faker, String folderPath) {
//		String gradesCsvPath = args.length == 2 ? parseArg(args[1]) : faker.getGradesCsv(folderPath);
//		if (gradesCsvPath.isEmpty()) {
//			System.err.println("Path for grades.csv is missing and cannot be found in the folder provided");
//			System.exit(1);
//		}
//		if (!gradesCsv.exists()) {
//			System.err.println(gradesCsvPath + " file does not exist.");
//			System.exit(1);
//		}
//	}

//	public static void main(String[] args) throws IOException {
//		if (args.length != 1 && args.length != 2) {
//			System.err.println("Enter main args: path to Zoom chat files folder and grades.csv for the class");
//			System.exit(1);
//		}
//
//		try {
//			ZoomChatFaker faker = new ZoomChatFaker();
//			String zoomChatFolderPath = parseArg(args[0]);
//
//			String gradesCsvPath = args.length == 2 ? parseArg(args[1]) : faker.getGradesCsv(zoomChatFolderPath);
//			if (gradesCsvPath.isEmpty()) {
//				System.err.println("Path for grades.csv is missing and cannot be found in the folder provided");
//				System.exit(1);
//			}
////			String zoomChatFolderPath = ZOOM_CHAT_FOLDER_PATH;
////			String gradesCsvPath = GRADES_CSV_PATH;
//
//			File zoomChatFolder = new File(zoomChatFolderPath);
//			if (!zoomChatFolder.exists()) {
//				System.err.println(zoomChatFolderPath + " folder does not exist.");
//				System.exit(1);
//			}
//			if (!zoomChatFolder.isDirectory()) {
//				System.err.println(zoomChatFolderPath + " is not a directory.");
//				System.exit(1);
//			}
//
//			File gradesCsv = new File(gradesCsvPath);
//			if (!gradesCsv.exists()) {
//				System.err.println(gradesCsvPath + " file does not exist.");
//				System.exit(1);
//			}
//
//			DownloadNameMap.main(args);
//			if (!faker.setNameMapAndNameFile()) {
//				System.exit(1);
//			}
//			File[] files = { zoomChatFolder, gradesCsv };
//			faker.execute(files);
//			System.out.println("Done");
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			UpdateNameMap.main(args);
//		}
//	}
	
	public static void main(String[] args) throws IOException {
		if (args.length != 1 && args.length != 2) {
			System.err.println("Enter main args: path to Zoom chat files folder and grades.csv for the class");
			System.exit(1);
		}

		try {
			ZoomChatFaker faker = new ZoomChatFaker();
			String zoomChatFolderPath = parseArg(args[0]);

//			s
//			String zoomChatFolderPath = ZOOM_CHAT_FOLDER_PATH;
//			String gradesCsvPath = GRADES_CSV_PATH;

			File zoomChatFolder = new File(zoomChatFolderPath);
			if (!zoomChatFolder.exists()) {
				System.err.println(zoomChatFolderPath + " folder does not exist.");
				System.exit(1);
			}
			if (!zoomChatFolder.isDirectory()) {
				System.err.println(zoomChatFolderPath + " is not a directory.");
				System.exit(1);
			}

//			File gradesCsv = new File(gradesCsvPath);
//			if (!gradesCsv.exists()) {
//				System.err.println(gradesCsvPath + " file does not exist.");
//				System.exit(1);
//			}
			File gradesCsv = faker.getGradesCSV(args, zoomChatFolderPath);
			if (gradesCsv == null) {
				System.exit(1);
			}
			faker.loadNameToOnyenMap(gradesCsv);

			DownloadNameMap.main(args);
			if (!faker.setNameMapAndNameFile()) {
				System.exit(1);
			}
			File[] files = { zoomChatFolder, gradesCsv };
			faker.execute(files);
			System.out.println("Done");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			UpdateNameMap.main(args);
		}
	}

//	protected String getGradesCsv(String path) {
//		File folder = new File(path);
//		for (File file : folder.listFiles()) {
//			if (file.getName().equals("grades.csv")) {
//				return file.getPath();
//			}
//		}
//		return "";
//	}

//	protected void loadAnonNameMap(String[] vals) {
//		// TODO Auto-generated method stub
//		super.loadAnonNameMap(vals);
//		String aKey = vals[1] + " " + vals[2];
//		String aValue = concat(vals[3], vals[4], vals[5]);
//		putFullNameAndAliases(fullNameIdenMap, aKey, aValue, false);
////		fullNameIdenMap.put(aKey, aValue);
////
////		putAliases(fullNameIdenMap, aKey, aValue);
//
//	}
	
	protected void processExecuteArg(Object args)  {
		 if (!(args instanceof File[])) {
				return;
			}
		 File[] files = (File[]) args;
		 File zoomChatFolder = files[0];
			try {
				createSpecificLoggerAndMetrics(zoomChatFolder, false);
			} catch (IOException e) {
				e.printStackTrace();
			}
		
	 }

	@Override
	public void anonymize(Object args) {
		if (!(args instanceof File[])) {
			return;
		}
		File[] files = (File[]) args;
//		File gradesCsv = files[1];
//		loadNameToOnyenMap(gradesCsv);

		File zoomChatFolder = files[0];
//		try {
//			createSpecificLoggerAndMetrics(zoomChatFolder, false);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		File[] chatFolders = zoomChatFolder.listFiles((file) -> {
			return file.isDirectory() && !file.getName().equals("Anon");
		});
		File anonFolder = new File(zoomChatFolder, "Anon");
		if (!anonFolder.exists()) {
			anonFolder.mkdirs();
		}

		for (File chatFolder : chatFolders) {
			File[] chats = chatFolder.listFiles((parent, fileName) -> {
				return (fileName.endsWith(".txt") && !fileName.contains("total")) || fileName.endsWith(".vtt");
			});
			for (File chat : chats) {
				chatMap.put(chat, readFile(chat).toString());
			}
		}
		for (File chat : chatMap.keySet()) {
			this.specificLogLine("Anonymyzing " + chat);

			if (chat.getName().equals("meeting_saved_chat.txt")) {
				anonymizeSavedChat(chat);
			} else {
				anonymizeChat(chat);
			}
		}
		File anonChat = null;

		for (File chat : chatMap.keySet()) {
			if (chat.getName().equals("meeting_saved_chat.txt")) {
				File parentFolder = new File(anonFolder, chat.getParentFile().getName());
				if (!parentFolder.exists()) {
					parentFolder.mkdirs();
				}
				anonChat = new File(parentFolder, chat.getName().replace(".txt", "Anon.txt"));
			} else {
				anonChat = new File(anonFolder, chat.getName().replace(".txt", "Anon.txt").replace(".vtt", "Anon.vtt"));
			}
			System.out.println("Writing to " + anonChat.getPath());
			writeFile(anonChat, chatMap.get(chat));
		}
	}

//	public void loadNameToOnyenMap(File gradesCsv) {
//		String gradesCsvString = readFile(gradesCsv).toString();
//		String[] lines = gradesCsvString.split("\\R");
//		for (int i = 3; i < lines.length; i++) {
//			String[] fields = lines[i].split(",");
//			String onyen = maybeUnquote(fields[1]);
//			String lastName = maybeUnquote(fields[2]);
//			String firstName = maybeUnquote(fields[3]);
//			String aFullName = firstName + " " + lastName;
//			
//			String anUnquotedOnyen = unquote(onyen);
//			String anUnquotedLastName = unquote(lastName);
//			String anUnquotedFirstName = unquote(firstName);
//
////			maybeQuotedNameToOnyen.put(firstName + " " + lastName, onyen);
//			maybeQuotedNameToOnyen.put(aFullName, onyen);
//
////			mapNames(aFullName, firstName, lastName, fakeName);
//		}
//	}

	public String maybeUnquote(String s) {
//		return s.substring(1, s.length() - 1);
		return s;
	}

//	public String unquote(String s) {
//		return s.substring(1, s.length() - 1);
////		return s;
//	}

	public void anonymizeSavedChat(File chat) {
		String chatString = chatMap.get(chat);
		String[] lines = chatString.split("\\R");
		String chatName = chat.getName();

		System.out.println("Anonymizing " + chatName);

		for (Entry<String, String> entry : nameToFakeName.entrySet()) {
			chatString = chatString.replace(entry.getKey(), entry.getValue());
		}
		for (String line : lines) {
			Matcher matcher = savedChatSpeakerPattern.matcher(line);
			if (!matcher.matches()) {
				continue;
			}
			String speaker = matcher.group(1);
			mapNameToFakeName(speaker);
			String listner = matcher.group(2);
			if (!listner.equals("Everyone")) {
				mapNameToFakeName(listner);
			}
		}
		for (Entry<String, String> entry : nameToFakeName.entrySet()) {
			chatString = chatString.replace(entry.getKey(), entry.getValue());
		}
		chatMap.put(chat, chatString);
	}

	protected String getSpeakerFromPattern(String aChatLine) {
		Matcher aMatcher = speakerPattern.matcher(aChatLine);
		if (aMatcher.matches()) {
			return aMatcher.group(1);
		}
		return null;
	}
	
	protected String getSpeakerFromSplit(String aChatLine) {
		
		String[] aChatParts = aChatLine.split(":");
		if (aChatParts.length == 1) {
			return null;
		}
		
//		if (aChatParts[0].contains(",")) {
//			return null;
//		}
		String[] pronounSeparated = aChatParts[0].split("\\("); // remove he/him
		String aName = pronounSeparated[0];
		if (!aName.matches("[A-Za-z ]+")) {
			return null;
		}
		String[] aNames = aName.split(" ");
		if (aNames.length == 1) {
			String aWord = aNames[0];
			String aMessage;
			if (DoNotFakeFactory.doNotReplaceWord(aWord)) {
				maybeSpecificLogLine ("Not replacing: " + aWord);
				return null;				
			}
			maybeSpecificLogLine ("Single token name:" + aWord);
//			 maybeSpecificLogLine(aMessage);
//			if (!messagesOutput.contains(aMessage)) {
//				messagesOutput.add(aMessage);
//				specificLogLine(aMessage + "\n");
//			}
		}
		if (aNames.length > 3) {
			return null;
		}
		return aName.trim();
	}
	public void anonymizeChat(File chat) {
		String chatString = chatMap.get(chat);
		String chatName = chat.getName();
		Pattern pattern = vttPattern;
		int groupIdx = VTT_GROUP_IDX;

		if (chat.getName().endsWith(".txt") && !chatName.equals("total OH transcript.txt")) {
			pattern = txtPattern;
			groupIdx = TXT_GROUP_IDX;
			if (chatString.endsWith(System.lineSeparator())) {
				chatString += System.lineSeparator();
			}
		}
		System.out.println("Anonymizing " + chatName);
		String[] aChatLines = chatString.split("\r\n");


		boolean lastLineWasTime = false;
		for (int anIndex = 0; anIndex < aChatLines.length; anIndex++) {
			String aChatLine = aChatLines[anIndex];
//			Matcher aTimeMatcher = vttPattern.matcher(aChatLine);
			Matcher aTimeMatcher = vttTimePattern.matcher(aChatLine);

			if (aTimeMatcher.matches()) {
				lastLineWasTime = true;
				continue;
			} 


			if (lastLineWasTime) {
				lastLineWasTime = false;
				String aSpeaker = getSpeakerFromSplit(aChatLine);
				if (aSpeaker != null) {
					mapNameToFakeName(aSpeaker);

				}				

			}
//			}
		}

		processElementsOfAllMaps();

//		String[] aChatLines = chatString.split("\n");
		StringBuilder retVal = new StringBuilder();

		boolean hasName = false;

		for (int anIndex = 0; anIndex < aChatLines.length; anIndex++) {
			if (anIndex != 0) {
				retVal.append("\r\n");
			}
			assignmentMetrics.numLinesProcessed++;
			String aSegment = aChatLines[anIndex];
			int numCharsInSegment = aSegment.length();
			assignmentMetrics.numCharactersProcessed += numCharsInSegment;
			String aReplacedSegment = aSegment;
			if (AnonUtil.hasName(aSegment, originalNameList)) {
				hasName = true;
				assignmentMetrics.numLinesWithNames++;
				assignmentMetrics.numCharactersInLinesWithNames += numCharsInSegment;

				aReplacedSegment = LineReplacerFactory.replaceLine(anIndex, aSegment, messagesOutput, specificLogger,
						KeywordFactory.keywordsRegex(), originalNameList, replacementNameList, someNameToFakeAuthor,
						assignmentMetrics);
			}
			retVal.append(aReplacedSegment);

		}

//		for (Entry<String, String> entry: nameToFakeName.entrySet()) {
//		
//			chatString = chatString.replace(entry.getKey(), entry.getValue());
//		}
//		chatMap.put(chat, chatString);
		chatMap.put(chat, retVal.toString());
	}

	protected void mapNames(String aFullName, String aFirstName, String aLastName, String aFakeName) {
		String[] aFakeNames = aFakeName.split(" ");
		String aFakeFirstName = aFakeName;
		String aFakeLastName = aFakeName;
		if (aFakeNames.length > 1) {
			aFakeFirstName = aFakeNames[0];
			aFakeLastName = aFakeNames[1];
		}
		nonDuplicatePut(firstNameToFakeFirstName, aFirstName, aFakeFirstName);
		nonDuplicatePut(lastNameToFakeLastName, aLastName, aFakeLastName);
		nonDuplicatePut(lastNameToFakeLastName, aLastName, aFakeLastName);
		nonDuplicatePut(fullNameToFakeFullName, aFullName, aFakeName);
		String anOnyen = nameToOnyen.get(aFullName);
		if (anOnyen != null) {
			nonDuplicatePut(uidToFakeAuthor, anOnyen, aFakeName);
		}
	}

	public void mapNameToFakeName(String zoomName) {
		if (nameToFakeName.containsKey(zoomName)) {// we do not know if two individuals have the same zoom name
			return;
		}
		Matcher matcher = fullNamePattern.matcher(zoomName);
		boolean found = false;
		if (matcher.matches()) {
			String firstName = matcher.group(1);
			String lastName = matcher.group(3);
			String fullName = firstName + " " + lastName;
			for (String name : maybeQuotedNameToOnyen.keySet()) {
  				if (name.contains(fullName)) {
					String aMaybeQuotedOnyen = maybeQuotedNameToOnyen.get(name);
					String aFakeName = getFakeName(aMaybeQuotedOnyen);
//					nameToFakeName.put(zoomName, getFakeName(maybeQuotedNameToOnyen.get(name)));
					nameToFakeName.put(zoomName, aMaybeQuotedOnyen);
					mapNames(fullName, firstName, lastName, aFakeName);

					found = true;
					break;
				}
			}
			if (!found) {
				String fakeName = getFakeName(firstName, lastName, fullName);
//				String anExistingFakeName = nameToFakeName.get(zoomName);
//				if (fakeName)
				nameToFakeName.put(zoomName, fakeName);
				mapNames(fullName, firstName, lastName, fakeName);

			}
			return;
		}
		specificLogLine("Could not find first and last name for " + zoomName);
		matcher = firstOrLastNamePattern.matcher(zoomName);
		Set<String> matches = new HashSet<>();
		if (matcher.matches()) {
			String firstOrLastName = matcher.group(1);
			for (String name : maybeQuotedNameToOnyen.keySet()) {
				String[] names = name.split(" ");
				if (names[0].equals(firstOrLastName) || names[1].equals(firstOrLastName)) {

					String anOnyen = maybeQuotedNameToOnyen.get(name);
					specificLogLine("Found onyen  " + anOnyen + " for " + firstOrLastName);

//					matches.add(maybeQuotedNameToOnyen.get(name));
					matches.add(anOnyen);
				}
			}

			if (matches.size() == 1) {
				String aFakeName = getFakeName(maybeQuotedNameToOnyen.get(matches.iterator().next()));
//				nameToFakeName.put(zoomName, getFakeName(maybeQuotedNameToOnyen.get(matches.iterator().next())));
				nameToFakeName.put(zoomName, aFakeName);
//				mapNames(aFull, firstName, lastName, aFakeName);

			} else {
				String aFakeName = getFakeName(zoomName, zoomName, zoomName);
				specificLogLine("Created fake name  " + aFakeName + " for non decomposable " + zoomName);

				nameToFakeName.put(zoomName, aFakeName);
//				nameToFakeName.put(zoomName, getFakeName(zoomName, zoomName, zoomName));
			}
			return;
		}
		System.out.println(zoomName + " cannot be parsed");
	}

	public String getFakeName(String firstName, String lastName, String onyen) {
		if (firstName.equals(lastName)) {
			System.out.println("first name == last name");
		}
		
//		String fakeName = CommentsIdenMap.get(onyen);
		String fakeName = getFakeOfNameOrPossiblyAlias(onyen);

		if (fakeName == null) {
			String aFullName = firstName + " " + lastName;
//			fakeName = fullNameIdenMap.get(firstName + " " + lastName);
			fakeName = fullNameIdenMap.get(aFullName);
			if (fakeName == null) {
				fakeName = fullNameIdenMap.get(aFullName.toLowerCase());
			}

		}
		if (fakeName == null) {
			String fakeFirstName = faker.name().firstName();
			String fakeLastName = faker.name().lastName();
			String fakeOnyen = fakeFirstName + " " + fakeLastName + "?";
			newPairs.put(concat(onyen, firstName, lastName), concat(fakeOnyen, fakeFirstName, fakeLastName));
			fakeName = fakeOnyen;
		} 
//		else {
//			fakeName = fakeName.substring(0, fakeName.indexOf(','));
//		}
		return fakeName;
	}

	public String getFakeName(String onyen) {

		String fakeName = getFakeOfNameOrPossiblyAlias(onyen);
		return fakeName.substring(0, fakeName.indexOf(','));
	}

}
