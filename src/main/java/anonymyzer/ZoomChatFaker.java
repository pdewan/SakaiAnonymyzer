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

	Pattern speakerPattern = Pattern.compile("\\d\\d:\\d\\d:\\d\\d From  (.*)  to  (.*):");
	Pattern fullNamePattern = Pattern.compile("\\s*(\\S+)\\s+(\\S+).*");
	Pattern firstOrLastNamePattern = Pattern.compile("\\s*(\\S+).*");
	Map<String, String> nameToFakeName = new HashMap<>();
	Map<String, String> nameToOnyen = new HashMap<>();
	Map<String, String> fullNameIdenMap = new HashMap<>();
	String logFileName = "zoom_chat_faker_log";
	
	static String ZOOM_CHAT_FOLDER_PATH = "F:\\Hermes Data\\ZoomChats";
	static String GRADES_CSV_PATH = "F:\\Hermes Data\\Assignment 0\\grades.csv";

	public ZoomChatFaker() throws IOException {
		super();
		nameToFakeName = new HashMap<>();
		nameToOnyen = new HashMap<>();
	}
	
	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			System.err.println("Enter main args: path to Zoom chat files folder and grades.csv for the class");
			System.exit(1);
		}

		try {
			String zoomChatFolderPath = parseArg(args[0]);
			String gradesCsvPath = parseArg(args[1]);
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
			ZoomChatFaker faker = new ZoomChatFaker();
			if (!faker.setNameMapAndNameFile()) {
				System.exit(1);
			}
			File[] files = {zoomChatFolder, gradesCsv};
			faker.execute(files);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			UpdateNameMap.main(args);
		}
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
				(file)->{return file.isDirectory();});
		File anonFolder = new File(zoomChatFolder, "Anon");
		if (!anonFolder.exists()) {
			anonFolder.mkdirs();
		}
		for (File chatFolder : chatFolders) {
			File[] chats = chatFolder.listFiles(
					(parent, fileName)->{return fileName.endsWith(".txt");});
			for (File chat : chats) {
				anonymizeChat(chat, anonFolder);
			}
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
	
	public void anonymizeChat(File chat, File anonFolder) {
		String chatString = readFile(chat).toString();
		String[] lines = chatString.split("\\R");
		for (String line : lines) {
			Matcher matcher = speakerPattern.matcher(line);
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
		File parentFolder = new File(anonFolder, chat.getParentFile().getName());
		parentFolder.mkdir();
		File anonChat = new File(parentFolder, chat.getName().replace(".txt", "Anon.txt"));
		
		writeFile(anonChat, chatString);
	}
	
	public void mapNameToFakeName(String zoomName) {
		if (nameToFakeName.containsKey(zoomName)) {
			return;
		}
		Matcher matcher = fullNamePattern.matcher(zoomName);
		boolean found = false;
		if (matcher.matches()) {
			String firstName = matcher.group(1);
			String lastName = matcher.group(2);
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
