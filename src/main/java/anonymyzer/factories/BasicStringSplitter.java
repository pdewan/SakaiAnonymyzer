package anonymyzer.factories;

import java.util.List;

import anonymyzer.AnonUtil;

public class BasicStringSplitter implements StringSplitter{

	@Override
	public String[] splitByKeywords(String aLine, String aKeywordRegex) {
		return AnonUtil.doSplit(aLine, aKeywordRegex);
	}

}
