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
		File token = new File(GeneralFaker.TOKEN);
		Path file = Paths.get(GeneralFaker.TOKEN);
		if (token.exists()) {
			BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
			if (System.currentTimeMillis() - attr.creationTime().toMillis() > WEEK) {
				token.delete();
				if (token.getParentFile().exists()) {
					File tokens = token.getParentFile();
					tokens.delete();
				}
			}
		}
//		BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
//		if (System.currentTimeMillis() - attr.creationTime().toMillis() > WEEK) {
//			token.delete();
//			if (token.getParentFile().exists()) {
//				File tokens = token.getParentFile();
//				tokens.delete();
//			}
//		}
		DriveAPI.downloadFileWithId(GeneralFaker.NAME_MAP_ID);
			
		File nameFile = new File(GeneralFaker.NAME_FILE);
		if (!nameFile.exists()) {
			DriveAPI.downloadFileWithId(GeneralFaker.NAME_FILE_ID);
		}
	}
}
