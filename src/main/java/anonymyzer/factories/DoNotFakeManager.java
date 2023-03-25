package anonymyzer.factories;

public interface DoNotFakeManager {
	boolean doNotReplaceWord(String aWord);
	boolean hideWord(String aWord);
	String[] getDoNotReplaceWordArray();
	void setDoNotReplaceWordArray(String[] newVal);
	String[] getHideWordArray();
	void setHideWordArray(String[] newVal);
	
}
