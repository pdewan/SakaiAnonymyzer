package anonymyzer.factories;

import java.io.File;
import java.util.List;

public interface NameExtractor {
	List<String> extractNames (File aFile, String aTopFolderName);

}
