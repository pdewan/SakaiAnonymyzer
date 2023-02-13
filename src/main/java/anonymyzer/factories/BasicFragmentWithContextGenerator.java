package anonymyzer.factories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import anonymyzer.AnonUtil;

public class BasicFragmentWithContextGenerator implements FragmentsWithContextGenerator{

	@Override
	public String fragmentWithContext(String aString, String aFragment, int anIndexOfFragment, int aFragmentEnd) {
		return AnonUtil.fragmentWithContext(aString, aFragment, anIndexOfFragment, aFragmentEnd);
	}

	@Override
	public List<String> fragmentsWithContext(String aString, Map<Integer, String> anIndexToFragment) {
		return AnonUtil.fragmentsWithContext(aString, anIndexToFragment);
	}
	
}
