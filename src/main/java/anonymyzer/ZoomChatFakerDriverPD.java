package anonymyzer;

import java.io.IOException;

public class ZoomChatFakerDriverPD {
//	static String ZOOM_CHAT_FOLDER_PATH = "C:\\Users\\dewan\\Downloads\\ZoomChats\\533\\s22";
//	static String GRADES_CSV_PATH = "C:\\Users\\dewan\\Downloads\\ZoomChats\\533\\s22\\grades.csv";
//	static String ZOOM_CHAT_FOLDER_PATH = "C:\\Users\\dewan\\Downloads\\ZoomChats\\401\\ss21";
//	static String GRADES_CSV_PATH = "C:\\Users\\dewan\\Downloads\\ZoomChats\\401\\ss21\\grades.csv";
//	static String ZOOM_CHAT_FOLDER_PATH = "C:\\Users\\dewan\\Downloads\\ZoomChats\\524\\F20";
//	static String GRADES_CSV_PATH = "C:\\Users\\dewan\\Downloads\\ZoomChats\\524\\F20\\grades.csv";
	static String ZOOM_CHAT_FOLDER_PATH = "D:\\ZoomChats\\524\\F22";
	static String GRADES_CSV_PATH = "D:\\ZoomChats\\524\\F22\\grades.csv";
	public static void main (String[] args) {
		String[] myArgs = {ZOOM_CHAT_FOLDER_PATH, GRADES_CSV_PATH };
		try {
			ZoomChatFaker.main(myArgs);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
