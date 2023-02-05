package anonymyzer;

import java.io.IOException;

public class GradescopeFakerDriverPD {
	static final String[] ARGS = {
//			"D:\\sakaidownloads\\Comp401\\F18\\Comp_401_Fall_2018_grades.csv",
//			"G:\\My Drive\\AnonymyzerMaps\\Comp401\\F18\\Assignment 1\\grades.csv"
			"D:\\sakaidownloads\\Comp524\\F22\\Comp_524_Fall_2022_grades.csv",
			"D:\\sakaidownloads\\Comp524\\F22\\Assignment 1\\Assignment 1\\grades.csv",

//			"G:\\My Drive\\AnonymyzerMaps\\Comp401\\F18\\Assignment 1\\grades.csv"
	};
	
	public static void main(String[] args) throws IOException {
		GradescopeFaker.main(ARGS);
	}
}
