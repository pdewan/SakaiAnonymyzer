package anonymyzer;

import java.io.IOException;

public class PiazzaFakerDriverPD {
//	public static final String  PIAZZA_FILE_ALLPOSTS_NAME =
//			"C:\\Users\\dewan\\Downloads\\PiazzaOutput\\koslinq2ke12nl_ByAuthorPosts_Sun-Sep-11-08_51_48-EDT-2022.json"	;
//	public static final String  PIAZZA_FOLDER =
//			"C:\\Users\\dewan\\Downloads\\PiazzaOutput\\Comp401f18";
	
//	public static final String  PIAZZA_FOLDER = 
//	"D:\\PiazzaOutput\\Comp524f22";
//static String GRADES_CSV_PATH = "D:\\sakaidownloads\\Comp524\\F22\\grades.csv";

//	public static final String  PIAZZA_FOLDER = 
//	"D:\\PiazzaOutput\\Comp524f21";
//static String GRADES_CSV_PATH = "D:\\sakaidownloads\\Comp524\\F21\\grades.csv";


	
//	public static final String  PIAZZA_FOLDER = 
//			"D:\\PiazzaOutput\\Comp533s22";
//	static String GRADES_CSV_PATH = "D:\\sakaidownloads\\Comp533\\s22\\grades.csv";

//	public static final String  PIAZZA_FOLDER = 
//	"D:\\PiazzaOutput\\Comp533s21";
//static String GRADES_CSV_PATH = "D:\\PiazzaOutput\\Comp533s21\\grades.csv";
	
//	public static final String  PIAZZA_FOLDER = 
//			"D:\\PiazzaOutput\\Comp533s20";
//	static String GRADES_CSV_PATH = "D:\\sakaidownloads\\Comp533\\s20\\grades.csv";


//	public static final String  PIAZZA_FOLDER = 
//			"D:\\PiazzaOutput\\Comp524f19";
	
	
//	public static final String  PIAZZA_FOLDER = 
//			"D:\\PiazzaOutput\\Comp533s21";
//	static String GRADES_CSV_PATH = "D:\\PiazzaOutput\\Comp533s21\\grades.csv";

//	public static final String  PIAZZA_FOLDER = 
//			"D:\\PiazzaOutput\\Comp533s20";
	
//	public static final String  PIAZZA_FOLDER = 
//			"D:\\PiazzaOutput\\Comp533s19";
//	public static final String  PIAZZA_FOLDER = 
//			"D:\\PiazzaOutput\\Comp533s18";		

	
//	public static final String  PIAZZA_FOLDER = 
//			"D:\\PiazzaOutput\\Comp524f19";
	
	
//	public static final String  PIAZZA_FOLDER = 
//			"D:\\PiazzaOutput\\Comp401f18";
//	public static final String  PIAZZA_FOLDER = 
//			"D:\\PiazzaOutput\\Comp401f17";
//	public static final String  PIAZZA_FOLDER = 
//			"D:\\PiazzaOutput\\Comp401f16";
	
	public static final String  PIAZZA_FOLDER = 
	"D:\\PiazzaOutput\\Comp301ss22";
	static String GRADES_CSV_PATH = "D:\\sakaidownloads\\Comp401\\ss21\\grades.csv";			

			


	
//	static String GRADES_CSV_PATH = "D:\\PiazzaOutput\\Comp533s21\\grades.csv";
//	static String GRADES_CSV_PATH = "D:\\sakaidownloads\\Comp524\\F21\\grades.csv";			

//			"D:\\PiazzaOutput\\Comp533s21\\grades.csv";
		
//	public static final String  PIAZZA_FOLDER = 
//			"D:\\PiazzaOutput\\Comp533s21";
//	static String GRADES_CSV_PATH = "D:\\ZoomChats\\533\\s21\\grades.csv";

//	public static final String  PIAZZA_FOLDER =
//			"C:\\Users\\dewan\\Downloads\\PiazzaOutput\\Comp533s22"	;
	
//	public static final String  PIAZZA_FOLDER =
//			"C:\\Users\\dewan\\Downloads\\PiazzaOutput\\Comp301ss22"	;
//	static String GRADES_CSV_PATH = "D:\\sakaidownloads\\comp401\\ss22\\grades.csv";			

//	public static final String ALL_POSTS = "AllPosts";
//	
//	public static final String BY_AUTHOR_POSTS = "ByAuthorPosts";	
//	public static final String AUTHORS = "Authors";
	public static void main (String[] args) {
		
		String[] aFakerAllPostsArgs = {PIAZZA_FOLDER, GRADES_CSV_PATH};
		try {
//			String aByAuthorPostsFile = PIAZZA_FILE_ALLPOSTS_NAME.replace(ALL_POSTS, BY_AUTHOR_POSTS );
//			String anAuthorsTextFile = PIAZZA_FILE_ALLPOSTS_NAME.replace(ALL_POSTS, AUTHORS );
//			anAuthorsTextFile = anAuthorsTextFile.replace(".json", ".txt");
//			String[] anAuthorsTextFileArgs = {anAuthorsTextFile};
//			String[] aByAuthorPostsFileArgs = {aByAuthorPostsFile};
			PiazzaFaker.main(aFakerAllPostsArgs);
//			PiazzaFaker.main(aByAuthorPostsFileArgs);
//			PiazzaFaker.main(anAuthorsTextFileArgs);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
