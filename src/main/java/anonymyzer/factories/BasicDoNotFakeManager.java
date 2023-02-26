package anonymyzer.factories;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BasicDoNotFakeManager implements DoNotFakeManager{
	String[] doNotReplaceWordArray = {
			"do",
			"an"
	};
	String[] hideWordArray = {
			
	};
	Set<String> doNotReplaceSet;
	Set<String> hideWordSet;
	public BasicDoNotFakeManager() {
		doNotReplaceSet = new HashSet(Arrays.asList(doNotReplaceWordArray));
		hideWordSet = new HashSet(Arrays.asList(hideWordSet));
	}
	@Override
	public boolean doNotReplaceWord(String aWord) {
		return doNotReplaceSet.contains(aWord.toLowerCase());
	}

	@Override
	public boolean hideWord(String aWord) {
		return hideWordSet.contains(aWord.toLowerCase());
	}

}
