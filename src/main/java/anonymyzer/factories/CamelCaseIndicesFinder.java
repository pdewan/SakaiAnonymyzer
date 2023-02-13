package anonymyzer.factories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import anonymyzer.AnonUtil;

public class CamelCaseIndicesFinder implements IndicesFinder{

	public  List<Integer> indicesOf(String aString, String aSubstring, boolean checkWord) {
		List<Integer> retVal = new ArrayList();
//		if (aString.contains("istance") && aSubstring.contains("tan")) {
//			System.out.println("Found problemantic word");
//		}
		for (int anIndex = 0; (anIndex = aString.indexOf(aSubstring, anIndex)) >= 0; anIndex++) {

			if (!checkWord || AnonUtil.occurenceIsAWord(aString, aSubstring, anIndex))
				retVal.add(anIndex);
		}
		return retVal;
	}

	public  Map<String, List<Integer>> indicesOf(String aString, List<String> aCandidateFragments,
			boolean checkWord) {
		Map<String, List<Integer>> retVal = new HashMap();
		for (String aCandidateFragment : aCandidateFragments) {
			retVal.put(aCandidateFragment, indicesOf(aString, aCandidateFragment, checkWord));
		}
		return retVal;

	}
	@Override
	public  Map<Integer, String> toIndexKeys(Map<String, List<Integer>> aStringKeys) {
		return AnonUtil.toIndexKeys(aStringKeys);
	}

}
