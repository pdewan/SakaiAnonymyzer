package anonymyzer;

import java.io.IOException;

public class GradescopeFakerDriver {
	static final String[] ARGS = {"F:\\Hermes Data\\301ss21\\G_Midterm_scores.csv",
			"F:\\Hermes Data\\301ss21\\Assignment 2\\grades.csv"
	};
	
	public static void main(String[] args) throws IOException {
		GradescopeFaker.main(ARGS);
	}
}
