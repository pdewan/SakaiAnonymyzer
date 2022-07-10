package anonymyzer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

public class DownloadNameMap {
    protected static final long WEEK = 7 * 24 * 60 * 60 * 1000; 

	public static void main(String[] args) throws IOException {
		File token = new File(FakeNameGenerator.TOKEN);
		Path file = Paths.get(FakeNameGenerator.TOKEN);
		BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
		if (token.exists() && System.currentTimeMillis() - attr.creationTime().toMillis() > WEEK) {
			token.delete();
			if (token.getParentFile().exists()) {
				File tokens = token.getParentFile();
				tokens.delete();
			}
		}
		DriveAPI.downloadFileWithId(FakeNameGenerator.NAME_MAP_ID);
			
		File nameFile = new File(FakeNameGenerator.NAME_FILE);
		if (!nameFile.exists()) {
			DriveAPI.downloadFileWithId(FakeNameGenerator.NAME_FILE_ID);
		}
	}
}
