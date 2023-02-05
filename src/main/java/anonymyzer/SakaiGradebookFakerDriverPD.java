package anonymyzer;

import java.io.IOException;

public class SakaiGradebookFakerDriverPD {
//	static String[] gradebooks = {"D:\\sakaidownloads\\Comp401\\F18\\gradebook_export.csv"};
	static String[] gradebooks = {"D:\\sakaidownloads\\Comp524\\F22\\gradebook_export.csv"};

	public static void main(String[] args) throws IOException {
		SakaiGradebookFaker.main(gradebooks);
	}
}
