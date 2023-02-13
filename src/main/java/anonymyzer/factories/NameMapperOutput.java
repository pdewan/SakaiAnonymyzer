package anonymyzer.factories;

import java.util.List;
import java.util.Map;

public class NameMapperOutput {
	 List<String> derivedNames;
	 List<String> derivedNamesReplacment;
	 Map<String, String> nameToReplacement;
	public List<String> getDerivedNames() {
		return derivedNames;
	}
	public List<String> getDerivedNamesReplacment() {
		return derivedNamesReplacment;
	}
	public Map<String, String> getNameToReplacement() {
		return nameToReplacement;
	}
	
}
