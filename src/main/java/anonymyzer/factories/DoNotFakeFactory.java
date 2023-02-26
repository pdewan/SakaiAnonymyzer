package anonymyzer.factories;

public class DoNotFakeFactory {
	static DoNotFakeManager doNotFakeManager = new BasicDoNotFakeManager();

	public static DoNotFakeManager getDoNotFakeManager() {
		return doNotFakeManager;
	}

	public static void setDoNotFakeManager(DoNotFakeManager doNotFakeManager) {
		DoNotFakeFactory.doNotFakeManager = doNotFakeManager;
	}
	
	public static boolean doNotReplaceWord(String aWord) {
		return doNotFakeManager.doNotReplaceWord(aWord);
	}
	public static boolean hideWord(String aWord) {
		return doNotFakeManager.hideWord(aWord);

	}

	

}
