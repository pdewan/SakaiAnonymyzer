package anonymyzer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

public class UpdateNameMap {

	public static void main(String[] args) throws IOException {
		File token = new File(FakeNameGenerator.TOKEN);
		Path file = Paths.get(FakeNameGenerator.TOKEN);
		BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
		if (token.exists() && System.currentTimeMillis() - attr.creationTime().toMillis() > DownloadNameMap.WEEK) {
			token.delete();
			if (token.getParentFile().exists()) {
				File tokens = token.getParentFile();
				tokens.delete();
			}
		}
		File namemap = new File(FakeNameGenerator.NAME_MAP);
		if (!namemap.exists()) {
			System.err.println("name map.csv does not exists");
			System.exit(1);
		}
		DriveAPI.updateFile(FakeNameGenerator.NAME_MAP_ID, namemap.getPath());
	}
}
