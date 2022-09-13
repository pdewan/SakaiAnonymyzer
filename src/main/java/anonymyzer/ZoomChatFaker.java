package anonymyzer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZoomChatFaker extends FakeNameGenerator {

	Pattern speakerPattern = Pattern.compile("\\d\\d:\\d\\d:\\d\\d From (.*) to (.*):");
	Map<String, String> nameToFakeName;
	
	public ZoomChatFaker() throws IOException {
		super();
		nameToFakeName = new HashMap<>();
	}
	
	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.err.println("Enter main args: path to Piazza posts files folder");
			System.exit(1);
		}

		try {
			String zoomChatFolderPath = parseArg(args[0]);
//			String zoomChatFolderPath = PIAZZA_POSTS_PATH;

			File zoomChatFolder = new File(zoomChatFolderPath);
			if (!zoomChatFolder.exists()) {
				System.err.println(zoomChatFolderPath + " folder does not exist.");
				System.exit(1);
			}
			if (!zoomChatFolder.isDirectory()) {
				System.err.println(zoomChatFolderPath + " is not a directory.");
				System.exit(1);
			}
			
			DownloadNameMap.main(args);
			ZoomChatFaker faker = new ZoomChatFaker();
			if (!faker.setNameMapAndNameFile()) {
				System.exit(1);
			}
			
			faker.execute(zoomChatFolder);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			UpdateNameMap.main(args);
		}
	}
	
	@Override
	public void anonymize(Object args) {
		if (!(args instanceof File)) {
			return;
		}
		File zoomChatFolder = (File) args;
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
				anonymizeChat(chat);
			}
		}
	}
	
	public void anonymizeChat(File chat) {
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
			chatString.replace(entry.getKey(), entry.getValue());
		}
		File anonFolder = new File(chat.getParent(), "Anon");
		if(!anonFolder.exists()) {
			anonFolder.mkdirs();
		}
		File anonChat = new File(anonFolder, chat.getName().replace(".txt", "Anon.txt"));
		writeFile(anonChat, chatString);
	}
	
	public void mapNameToFakeName(String name) {
		if (nameToFakeName.containsKey(name)) {
			return;
		}
	}
}
