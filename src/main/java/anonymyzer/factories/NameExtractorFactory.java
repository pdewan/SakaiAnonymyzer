package anonymyzer.factories;

import java.io.File;
import java.util.List;

public class NameExtractorFactory {
	static NameExtractor nameExtractor = new SakaiNameExtractor();

	public static NameExtractor getNameExtractor() {
		return nameExtractor;
	}

	public static void setNameExtractor(NameExtractor nameExtractor) {
		NameExtractorFactory.nameExtractor = nameExtractor;
	}
	public static List<String> extractNames(File aFile, String aTopFolderName) {		
		return  nameExtractor.extractNames(aFile, aTopFolderName);
	}

	
}
