package anonymyzer.factories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import anonymyzer.AnonUtil;

public class BasicAliasesManager implements AliasesManager{
	String[][] aliasesArray = {
			{"harrywh", "harrywang11"},
//			{"Matt Do", "Hoang Son Do"},
//			{"Kian W", "Kian Watkins"},
//			{"ajwortas@cs.unc.edu", "ajwortas@ad.unc.edu"},
//			{"Qi An Lee", "Lee Qi An"},
	};
			
	List<List<String>> aliasesList =  new ArrayList();
	
	public String[][] getAliasesArray() {
		return  aliasesArray;
	}
	public void setAliasesArray(String[][]  newVal) {
		aliasesArray = newVal;
	}
	public BasicAliasesManager() {
		for (String[] anAliasArray:getAliasesArray()) {
			List<String> anAliasList = new ArrayList(Arrays.asList(anAliasArray));
			aliasesList.add(anAliasList);
		}
		
	}
	
	static final List<String> emptyList = new ArrayList();
	
	@Override
	public List<String> getAliases(String aName) {
		List<String> retVal;
		for (List<String> anAliasList:aliasesList) {
			if (anAliasList.contains(aName)) {
				retVal = new ArrayList(anAliasList);
				retVal.remove(aName);
				return retVal;
			}
		}
		return emptyList;
		
	}
	

	
//	protected void arraysToWordSet(String[] anArray, Set<String> aSet) {
//		for (String aWor:anArray) {
//			aSet.add(anElement);
//			aSet.add(AnonUtil)
//		}
//		
//
//	}
//	@Override
//	public boolean doNotReplaceWord(String aWord) {
//		return doNotReplaceSet.contains(aWord.toLowerCase());
//	}
//
//	@Override
//	public boolean hideWord(String aWord) {
//		return hideWordSet.contains(aWord.toLowerCase());
//	}

}
