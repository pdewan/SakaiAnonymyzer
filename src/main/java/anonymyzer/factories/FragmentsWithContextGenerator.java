package anonymyzer.factories;

import java.util.List;
import java.util.Map;

public interface FragmentsWithContextGenerator {
	 String fragmentWithContext(String aString, String aFragment, int anIndexOfFragment, int aFragmentEnd) ;
	 List<String> fragmentsWithContext(String aString, Map<Integer, String> anIndexToFragment);
}
