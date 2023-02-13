package anonymyzer.factories;

import java.util.List;
import java.util.Map;

import anonymyzer.AnonUtil;

public class FragmentsWithContextGeneratorFactory {
	static FragmentsWithContextGenerator fragmentWithContextGenerator= new BasicFragmentWithContextGenerator();

	public static FragmentsWithContextGenerator getFragmentWithContextGenerator() {
		return fragmentWithContextGenerator;
	}

	public static void setFragmentWithContextGenerator(FragmentsWithContextGenerator newVal) {
		FragmentsWithContextGeneratorFactory.fragmentWithContextGenerator = newVal;
	}
	
	public static String fragmentWithContext(String aString, String aFragment, int anIndexOfFragment, int aFragmentEnd) {
		return fragmentWithContextGenerator.fragmentWithContext(aString, aFragment, anIndexOfFragment, aFragmentEnd);
	}

	public static List<String> fragmentsWithContext(String aString, Map<Integer, String> anIndexToFragment) {
		return fragmentWithContextGenerator.fragmentsWithContext(aString, anIndexToFragment);
	}

}
