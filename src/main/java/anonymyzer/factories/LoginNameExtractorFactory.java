package anonymyzer.factories;

public class LoginNameExtractorFactory {
	static LoginNameExtractor loginNameExtractor = new BasicLoginNameExtractor();

	public static LoginNameExtractor getLoginNameExtractor() {
		return loginNameExtractor;
	}

	public static void setLoginNameExtractor(LoginNameExtractor newVal) {
		LoginNameExtractorFactory.loginNameExtractor = newVal;
	}
	
	public static String extractLoginName (String aLinePossiblyContainingFileName) {
		return loginNameExtractor.extractLoginName(aLinePossiblyContainingFileName);
	}


};
