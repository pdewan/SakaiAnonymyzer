package anonymyzer;

import java.io.IOException;

public class SakaiGradebookFakerDriver {
	static String[] gradebooks = {"F:\\CompPaper\\301ss21\\gradebook_export-3cc8fc42-2eaf-4b51-b381-3dcde76527b1.csv"};
	
	public static void main(String[] args) throws IOException {
		SakaiGradebookFaker.main(gradebooks);
	}
}
