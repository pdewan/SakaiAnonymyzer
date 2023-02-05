package anonymyzer;

import java.io.IOException;

public class PiazzaFakerDriverPD {
//	public static final String  PIAZZA_FILE_ALLPOSTS_NAME =
//			"C:\\Users\\dewan\\Downloads\\PiazzaOutput\\koslinq2ke12nl_ByAuthorPosts_Sun-Sep-11-08_51_48-EDT-2022.json"	;
//	public static final String  PIAZZA_FOLDER =
//			"C:\\Users\\dewan\\Downloads\\PiazzaOutput\\Comp401f18";
	public static final String  PIAZZA_FOLDER = 
			"D:\\PiazzaOutput\\Comp524f22";
//	public static final String  PIAZZA_FOLDER =
//			"C:\\Users\\dewan\\Downloads\\PiazzaOutput\\Comp533s22"	;
//	public static final String  PIAZZA_FOLDER =
//			"C:\\Users\\dewan\\Downloads\\PiazzaOutput\\Comp301ss22"	;
//	public static final String ALL_POSTS = "AllPosts";
//	
//	public static final String BY_AUTHOR_POSTS = "ByAuthorPosts";	
//	public static final String AUTHORS = "Authors";
	public static void main (String[] args) {
		
		String[] aFakerAllPostsArgs = {PIAZZA_FOLDER};
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
