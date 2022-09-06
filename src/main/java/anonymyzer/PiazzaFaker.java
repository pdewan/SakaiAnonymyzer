package anonymyzer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONObject;
import com.github.javafaker.service.FakeValuesGrouping;

public class PiazzaFaker extends FakeNameGenerator {

	private static final String PIAZZA_POSTS_PATH = "F:\\Hermes Data\\PiazzaOutput\\ByAuthorPosts_Mon-Jul-25-03_55_20-EDT-2022.json";
	String logFileName = "piazza_faker_log";
	Pattern onyenPattern = Pattern.compile(".*\\((.*)@.*\\)");
	Pattern studentNamePattern = Pattern.compile("(.*) (.*)\\((.*)@.*\\)");

	
	public PiazzaFaker() throws IOException {
		super();
	}
	
	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.err.println("Enter main args: path to assignment folder");
			System.exit(1);
		}

		try {
//			String assignmentFolderPath = parseArg(args[0]);
//			File assignmentFolder = new File(assignmentFolderPath);
//			if (!assignmentFolder.exists()) {
//				System.err.println(assignmentFolderPath + " folder does not exist: ");
//				return;
//			}
			
//			String piazzaPostsPath = parseArg(args[0]);
			String piazzaPostsPath = PIAZZA_POSTS_PATH;
			File piazzaPosts = new File(piazzaPostsPath);
			if (!piazzaPosts.exists()) {
				System.err.println(piazzaPostsPath + " does not exist: ");
				System.exit(1);
			}
			if (!piazzaPostsPath.toLowerCase().endsWith(".json")) {
				System.err.println(piazzaPostsPath + " is not a JSON file");
				System.exit(1);
			}

			DownloadNameMap.main(args);
			PiazzaFaker faker = new PiazzaFaker();
			File namemap = new File(NAME_MAP);
			if (!namemap.exists()) {
				System.err.println("name map.csv not found, please download required files first");
				System.exit(1);
			}
			faker.setNameMap(namemap);
			File nameFile = new File(NAME_FILE);
			if (!nameFile.exists()) {
				System.err.println("name.yml not found, please download name map.csv first");
				System.exit(1);

			}
			((FakeValuesGrouping)faker.getFaker().fakeValuesService().getFakeValueList().get(0))
				.setSpecifiedFileName("name", nameFile.getPath());
			
//			faker.execute(assignmentFolder);
			faker.execute(piazzaPosts);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			UpdateNameMap.main(args);
		}
	}

	@Override
	public void anonymize(Object args) {
		if (!(args instanceof File)) {
			return;
		}
//		File assignmentFolder = (File) arg;
//		File piazzaPosts = findPiazzaFile(assignmentFolder);
		File piazzaPosts = (File) args;
		String piazzaPostsString = readFile(piazzaPosts).toString();
		JSONObject piazzaPostsJson = new JSONObject(piazzaPostsString);
		
		for (String author : piazzaPostsJson.keySet()) {
			if (author.startsWith("Instructor")) {
				continue;
			}
			Matcher matcher = studentNamePattern.matcher(author);
			System.out.println(author);
			System.out.println(matcher.matches());
			String firstName = matcher.group(1);
			String lastName = matcher.group(2);
			String onyen = matcher.group(3);
			String fakeName = CommentsIdenMap.get(onyen);
			String fakeAuthor = "";
			if (fakeName == null) {
				String fakeFirstName = faker.name().firstName();
				String fakeLastName = faker.name().lastName();
				String fakeOnyen = fakeFirstName + " " + fakeLastName;
				newPairs.put(concat(onyen, firstName, lastName), concat(fakeOnyen, fakeFirstName, fakeLastName));
				fakeAuthor = fakeFirstName + " " + fakeLastName + "(" + fakeOnyen + "@live.unc.edu)";
			} else {
				fakeAuthor = fakeName.substring(0, fakeName.length()-1) + "@live.unc.edu)";
			}
			piazzaPostsString = piazzaPostsString.replace(author, fakeAuthor);
		}
		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(piazzaPosts))){
			bw.write(piazzaPostsString);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public StringBuilder readFile(File file) {
		StringBuilder content = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(file))){
			String nextLine = "";
			while((nextLine = br.readLine()) != null) {
				content.append(nextLine+System.lineSeparator());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}
	
//	public File findPiazzaFile(File assignmentFolder) {
//		File[] piazzaPostFiles = assignmentFolder.listFiles(new FilenameFilter() {
//			public boolean accept(File dir, String name) {
//				return name.startsWith("ByAuthorPosts");
//			}
//		});
//		if (piazzaPostFiles.length == 0) {
//			return null;
//		}
//		if (piazzaPostFiles.length == 1) {
//			return piazzaPostFiles[0];
//		}
//		
//		File latestPiazzaPostFile = piazzaPostFiles[0];
//		for (int i = 1; i < piazzaPostFiles.length; i++) {
//			if (latestPiazzaPostFile.lastModified() < piazzaPostFiles[i].lastModified()) {
//				latestPiazzaPostFile = piazzaPostFiles[i];
//			}
//		}
//		return latestPiazzaPostFile;
//	}
}
