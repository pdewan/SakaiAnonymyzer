package anonymyzer.factories;

import java.util.List;

public interface AliasesManager {
	public String[][] getAliasesArray() ;
	public void setAliasesArray(String[][]  newVal) ;
	public List<String> getAliases(String aName);


}
