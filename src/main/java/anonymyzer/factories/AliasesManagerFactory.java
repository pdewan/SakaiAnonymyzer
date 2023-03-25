package anonymyzer.factories;

import java.util.List;

public class AliasesManagerFactory {
	static AliasesManager aliasesManager = new BasicAliasesManager();
	public static AliasesManager getAliasesManager() {
		return aliasesManager;
	}
	
	public static void setAliasesManager (AliasesManager newVal) {
		aliasesManager = newVal;
	}
	
	public static List<String> getAliases(String aName) {
		return getAliasesManager().getAliases(aName);
	}

}
