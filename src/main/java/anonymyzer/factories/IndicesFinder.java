package anonymyzer.factories;
import java.util.List;
import java.util.Map;

public interface IndicesFinder {
	 List<Integer> indicesOf(String aString, String aSubstring, boolean checkWord) ;
	 Map<String, List<Integer>> indicesOf(String aString, List<String> aCandidateFragments,
			boolean checkWord) ;
	Map<Integer, String> toIndexKeys(Map<String, List<Integer>> aStringKeys);

}
