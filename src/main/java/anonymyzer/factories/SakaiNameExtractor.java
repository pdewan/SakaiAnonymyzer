package anonymyzer.factories;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SakaiNameExtractor implements NameExtractor {
	int depth = 1;
	@Override
	public List<String> extractNames(File aFile, String aTopFolderName) {
		String aNormalizedPath = aFile.getPath().replace(aTopFolderName, "");
//		String orig_line = aFile.getPath();
		aNormalizedPath = aNormalizedPath.replaceAll("\\\\", "/"); // sanitize
		String[] split = aNormalizedPath.split("/");
		// load up our known names
		ArrayList<String> names = new ArrayList<String>();
		// should be last name
		names.add(split[depth].substring(0, split[depth].indexOf(",")));
		// should be first name
		names.add(split[depth].substring(split[depth].indexOf(",") + 2, split[depth].indexOf("(")));
		// should be onyen
		names.add(split[depth].substring(split[depth].indexOf("(") + 1, split[depth].indexOf(")")));
		return names;
	}

}
