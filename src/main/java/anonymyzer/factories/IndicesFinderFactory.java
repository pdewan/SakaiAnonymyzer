package anonymyzer.factories;

import java.util.List;
import java.util.Map;

public class IndicesFinderFactory {
	static IndicesFinder indicesFinder = new CamelCaseIndicesFinder();

	public static IndicesFinder getIndicesFinder() {
		return indicesFinder;
	}

	public static void setIndicesFinder(IndicesFinder nameFinder) {
		IndicesFinderFactory.indicesFinder = nameFinder;
	}

	public static List<Integer> indicesOf(String aString, String aSubstring, boolean checkWord) {
		return indicesFinder.indicesOf(aString, aSubstring, checkWord);
	};

	public static Map<String, List<Integer>> indicesOf(String aString, List<String> aCandidateFragments,
			boolean checkWord) {
		return indicesFinder.indicesOf(aString, aCandidateFragments, checkWord);
	}
	public static Map<Integer, String> toIndexKeys(Map<String, List<Integer>> aStringKeys) {
		return indicesFinder.toIndexKeys(aStringKeys);
	}


}
