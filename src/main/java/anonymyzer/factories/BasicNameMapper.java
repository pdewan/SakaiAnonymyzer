package anonymyzer.factories;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.github.javafaker.Faker;
/*
 * future class
 */
public class BasicNameMapper implements NameMapper {

	@Override
	public NameMapperOutput getNameSubsitutions(List<String> aNames) {
		// TODO Auto-generated method stub
		return null;
	}
//	HashMap<String, String> commentsIdenMap = new HashMap<String, String>();
//	File nameMapCSV;
//	String nameMapPath;
//	Faker faker;
//	HashMap<String, String> newPairs = new HashMap<>();
//	HashSet<String> fakeNameSet = new HashSet<>();
//	static int[] idx = { 2, 1, 0 }; // new, pd
//
//	public Faker getFaker() {
//		if (faker == null) {
//			faker = new Faker();
//		}
//		return faker;
//	}

//	protected String[] getTokens(String firstName, String lastName, String onyen) {
//		String fake = commentsIdenMap.get(onyen);
//		if (fake != null) {
//			return fake.split(",");
//		}
//		if (method == UNANON) {
//			return null;
//		}
//		String[] tokens = new String[3];
//		do {
//			tokens[1] = getFaker().name().firstName();
//			tokens[2] = getFaker().name().lastName();
//			tokens[0] = tokens[1] + " " + tokens[2];
//		} while (fakeNameSet.contains(tokens[2]));
//		commentsIdenMap.put(onyen, concatFirst3(tokens));
//		fakeNameSet.add(tokens[2]);
//		newPairs.put(concat(onyen, firstName, lastName), concatFirst3(tokens));
//		return tokens;
//	}
//
//	@Override
//	public NameMapperOutput getNameSubsitutions(List<String> aNames) {
//		String[] tokens = getTokens(aNames.get(1), aNames.get(0), aNames.get(2)); // computing them each time
//		if (tokens == null) {
//			return null;
//		}
//		List<String> aDerivedNames = new ArrayList();
//
//		List<String> aDerivedReplacements = new ArrayList();
//		for (int index = 0; index < tokens.length; index++) {
//			String aToken = tokens[idx[index]];
//			aDerivedReplacements.add(aToken);
//			aDerivedReplacements.add(aToken.toLowerCase());
//		}
//		String aFullReplacementNameNoSpaces = aDerivedReplacements.get(2) + aDerivedReplacements.get(0);
//		aDerivedReplacements.add(aFullReplacementNameNoSpaces);
//		aDerivedReplacements.add(aFullReplacementNameNoSpaces.toLowerCase());
//		for (int index = 0; index < aNames.size(); index++) {
//			aDerivedNames.add(aNames.get(index));
//			aDerivedNames.add(aNames.get(index).toLowerCase());
//		}
//		String aFullNameNoSpaces = aNames.get(1) + aNames.get(0);
//		aDerivedNames.add(aFullNameNoSpaces);
//		aDerivedNames.add(aFullNameNoSpaces.toLowerCase());
//		super.deriveNamesAndReplacements(aDerivedNames);
//		setNameReplacements(aDerivedReplacements);
//		originalToReplacement = new HashMap();
//		for (int index = 0; index < aDerivedNames.size(); index++) {
//			originalToReplacement.put(aDerivedNames.get(index), aDerivedReplacements.get(index));
//		};
//	}

}
