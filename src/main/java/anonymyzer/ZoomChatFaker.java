package anonymyzer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZoomChatFaker extends GeneralFaker {

	Pattern savedChatSpeakerPattern = Pattern.compile("\\d\\d:\\d\\d:\\d\\d From  (.*)  to  (.*):");
	Pattern fullNamePattern = Pattern.compile("(\\S+)\\s+(\\(.*\\))?\\s?(.*)");
	Pattern firstOrLastNamePattern = Pattern.compile("\\s*(\\S+).*");
	Pattern vttPattern = Pattern.compile("(?m)^(\\d{2}:\\d{2}:\\d{2}\\.\\d+) +--> +(\\d{2}:\\d{2}:\\d{2}\\.\\d+).*[\\r\\n]+\\s*(?s)((?:(?!\\r?\\n\\r?\\n).)*)");
	Pattern speakerPattern = Pattern.compile("(.*): .*");
	Pattern txtPattern = Pattern.compile("(?m)^(\\d{2}:\\d{2}:\\d{2})\\s*(?s)((?:(?!(\\d{2}:\\d{2}:\\d{2})).)*)");
	final int TXT_GROUP_IDX = 2;
	final int VTT_GROUP_IDX = 3;
	
	Map<String, String> nameToFakeName = new HashMap<>();
	Map<String, String> nameToOnyen = new HashMap<>();
	Map<String, String> fullNameIdenMap = new HashMap<>();
	Map<File, String> chatMap = new HashMap<>();
	String logFileName = "zoom_chat_faker_log";
	
//	static String ZOOM_CHAT_FOLDER_PATH = "F:\\Hermes Data\\F21";
//	static String GRADES_CSV_PATH = "F:\\Hermes Data\\F21\\grades.csv";

	public ZoomChatFaker() throws IOException {
		super();
		nameToFakeName = new HashMap<>();
		nameToOnyen = new HashMap<>();
	}
	
	public static void main(String[] args) throws IOException {
		if (args.length != 1 && args.length != 2) {
			System.err.println("Enter main args: path to Zoom chat files folder and grades.csv for the class");
			System.exit(1);
		}

		try {
			ZoomChatFaker faker = new ZoomChatFaker();
			String zoomChatFolderPath = parseArg(args[0]);

			String gradesCsvPath = args.length == 2 ? parseArg(args[1]) : faker.getGradesCsv(zoomChatFolderPath);
			if (gradesCsvPath.isEmpty()) {
				System.err.println("Path for grades.csv is missing and cannot be found in the folder provided");
				System.exit(1);
			}
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
			
			File gradesCsv = new File(gradesCsvPath);
			if (!gradesCsv.exists()) {
				System.err.println(gradesCsvPath + " file does not exist.");
				System.exit(1);
			}
			
			DownloadNameMap.main(args);
			if (!faker.setNameMapAndNameFile()) {
				System.exit(1);
			}
			File[] files = {zoomChatFolder, gradesCsv};
			faker.execute(files);
			System.out.println("Done");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			UpdateNameMap.main(args);
		}
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
	
	protected void loadAnonNameMap(String[] vals) {
		// TODO Auto-generated method stub
		super.loadAnonNameMap(vals);
		fullNameIdenMap.put(vals[1] + " " + vals[2], concat(vals[3], vals[4], vals[5]));
	}
	
	@Override
	public void anonymize(Object args) {
		if (!(args instanceof File[])) {
			return;
		}
		File[] files = (File[]) args;
		File gradesCsv = files[1];
		loadNameToOnyenMap(gradesCsv);
		
		File zoomChatFolder = files[0];
		File[] chatFolders = zoomChatFolder.listFiles(
				(file)->{return file.isDirectory() && !file.getName().equals("Anon");});
		File anonFolder = new File(zoomChatFolder, "Anon");
		if (!anonFolder.exists()) {
			anonFolder.mkdirs();
		}
		
		for (File chatFolder : chatFolders) {
			File[] chats = chatFolder.listFiles(
					(parent, fileName)->{return (fileName.endsWith(".txt") && !fileName.contains("total") )|| fileName.endsWith(".vtt");});
			for (File chat : chats) {
				chatMap.put(chat, readFile(chat).toString());
			}
		}
		for (File chat: chatMap.keySet()) {
			if (chat.getName().equals("meeting_saved_chat.txt")) {
				anonymizeSavedChat(chat);
			} else {
				anonymizeChat(chat);
			}
		}
		File anonChat = null;

		for (File chat: chatMap.keySet()) {
			if (chat.getName().equals("meeting_saved_chat.txt")) {
				File parentFolder = new File(anonFolder, chat.getParentFile().getName());
				if (!parentFolder.exists()) {
					parentFolder.mkdirs();
				}
				anonChat = new File(parentFolder, chat.getName().replace(".txt", "Anon.txt"));
			} else {
				anonChat = new File(anonFolder, chat.getName()
						.replace(".txt", "Anon.txt")
						.replace(".vtt", "Anon.vtt"));
			}
			System.out.println("Writing to " + anonChat.getPath());
			writeFile(anonChat, chatMap.get(chat));
		}
	}
	
	public void loadNameToOnyenMap(File gradesCsv) {
		String gradesCsvString = readFile(gradesCsv).toString();
		String[] lines = gradesCsvString.split("\\R");
		for (int i = 3; i < lines.length; i++) {
			String[] fields = lines[i].split(",");
			String onyen = unquote(fields[1]);
			String lastName = unquote(fields[2]);
			String firstName = unquote(fields[3]);
			nameToOnyen.put(firstName + " " + lastName, onyen);
		}
	}
	
	public String unquote(String s) {
		return s.substring(1, s.length() - 1);
	}
	
	public void anonymizeSavedChat(File chat) {
		String chatString = chatMap.get(chat);
		String[] lines = chatString.split("\\R");
		String chatName = chat.getName();

		System.out.println("Anonymizing " + chatName);

		for (Entry<String, String> entry: nameToFakeName.entrySet()) {
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
		for (Entry<String, String> entry: nameToFakeName.entrySet()) {
			chatString = chatString.replace(entry.getKey(), entry.getValue());
		}
		chatMap.put(chat, chatString);
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

		Matcher matcher = pattern.matcher(chatString);
		Matcher speakerMatcher = null; 
		while (matcher.find()) {
			speakerMatcher = speakerPattern.matcher(matcher.group(groupIdx));
			if (speakerMatcher.matches()) {
				String speaker = speakerMatcher.group(1);
				mapNameToFakeName(speaker);
			}
		}

		for (Entry<String, String> entry: nameToFakeName.entrySet()) {
			chatString = chatString.replace(entry.getKey(), entry.getValue());
		}
		chatMap.put(chat, chatString);
	}
	
	public void mapNameToFakeName(String zoomName) {
		if (nameToFakeName.containsKey(zoomName)) {
			return;
		}
		Matcher matcher = fullNamePattern.matcher(zoomName);
		boolean found = false;
		if (matcher.matches()) {
			String firstName = matcher.group(1);
			String lastName = matcher.group(3);
			String fullName = firstName + " " + lastName;
			for (String name : nameToOnyen.keySet()) {
				if (name.contains(fullName)) {
					nameToFakeName.put(zoomName, getFakeName(nameToOnyen.get(name)));
					found = true;
					break;
				}
			}
			if (!found) {
				String fakeName = getFakeName(firstName, lastName, fullName);
				nameToFakeName.put(zoomName, fakeName);
			}
			return;
		}
		matcher = firstOrLastNamePattern.matcher(zoomName);
		Set<String> matches = new HashSet<>();
		if (matcher.matches()) {
			String firstOrLastName = matcher.group(1);
			for (String name : nameToOnyen.keySet()) {
				String[] names = name.split(" ");
				if (names[0].equals(firstOrLastName) || names[1].equals(firstOrLastName)) {
					matches.add(nameToOnyen.get(name));
				} 
			}
			
			if (matches.size() == 1) {
				nameToFakeName.put(zoomName, getFakeName(nameToOnyen.get(matches.iterator().next())));
			} else {
				nameToFakeName.put(zoomName, getFakeName(zoomName, zoomName, zoomName));
			}
			return;
		}
		System.out.println(zoomName + " cannot be parsed");
	}
	
	public String getFakeName(String firstName, String lastName, String onyen) {
		String fakeName = CommentsIdenMap.get(onyen);
		if (fakeName == null) {
			fakeName = fullNameIdenMap.get(firstName + " " + lastName);
		}
		if (fakeName == null) {
			String fakeFirstName = faker.name().firstName();
			String fakeLastName = faker.name().lastName();
			String fakeOnyen = fakeFirstName + " " + fakeLastName + "?";
			newPairs.put(concat(onyen, firstName, lastName), concat(fakeOnyen, fakeFirstName, fakeLastName));
			fakeName = fakeOnyen;
		} else {
			fakeName = fakeName.substring(0, fakeName.indexOf(','));
		}
		return fakeName;
	}
	
	public String getFakeName(String onyen) {
		String fakeName = CommentsIdenMap.get(onyen);
		return fakeName.substring(0, fakeName.indexOf(','));
	}
}
