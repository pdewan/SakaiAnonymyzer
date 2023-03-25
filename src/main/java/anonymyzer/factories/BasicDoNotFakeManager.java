package anonymyzer.factories;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import anonymyzer.AnonUtil;

public class BasicDoNotFakeManager implements DoNotFakeManager{
	String[] doNotReplaceWordArray = {
			"do",
			"an",
			"uh",
			"he"
	};
	String[] hideWordArray = {
			
	};
	Set<String> doNotReplaceSet;
	Set<String> hideWordSet;
	public BasicDoNotFakeManager() {
		doNotReplaceSet = new HashSet();		
		hideWordSet = new HashSet();
		AnonUtil.arraysToWordSet(getDoNotReplaceWordArray(), doNotReplaceSet);
		AnonUtil.arraysToWordSet(getHideWordArray(), hideWordSet);
	}
	@Override
	public String[] getDoNotReplaceWordArray() {
		return doNotReplaceWordArray;
	}
	@Override
	public void setDoNotReplaceWordArray(String[] newVal) {
		doNotReplaceWordArray = newVal;
	}
	@Override
	public String[] getHideWordArray() {
		return hideWordArray;
	}
	@Override
	public void setHideWordArray(String[] newVal) {
		hideWordArray = newVal;
	}
	
	
//	protected void arraysToWordSet(String[] anArray, Set<String> aSet) {
//		for (String aWor:anArray) {
//			aSet.add(anElement);
//			aSet.add(AnonUtil)
//		}
//		
//
//	}
	@Override
	public boolean doNotReplaceWord(String aWord) {
		return doNotReplaceSet.contains(aWord.toLowerCase());
	}

	@Override
	public boolean hideWord(String aWord) {
		return hideWordSet.contains(aWord.toLowerCase());
	}

}
