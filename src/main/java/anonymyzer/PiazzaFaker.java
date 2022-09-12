package anonymyzer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;
import com.github.javafaker.service.FakeValuesGrouping;

public class PiazzaFaker extends FakeNameGenerator {

//	private static final String PIAZZA_POSTS_PATH = "C:\\Users\\yiwk3\\Desktop\\New Folder";
	String logFileName = "piazza_faker_log";
//	Pattern onyenPattern = Pattern.compile(".*\\((.*)@.*\\)");
	Pattern studentNamePattern = Pattern.compile("(.*) (.*)\\((.*)@.*\\)");
	HashMap<String, String> authorToFakeAuthor;

	
	public PiazzaFaker() throws IOException {
		super();
		authorToFakeAuthor = new HashMap<>();
	}
	
	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.err.println("Enter main args: path to Piazza posts files folder");
			System.exit(1);
		}

		try {
			String piazzaPostsFolderPath = parseArg(args[0]);
//			String piazzaPostsFolderPath = PIAZZA_POSTS_PATH;

			File piazzaPostsFolder = new File(piazzaPostsFolderPath);
			if (!piazzaPostsFolder.exists()) {
				System.err.println(piazzaPostsFolderPath + " folder does not exist.");
				System.exit(1);
			}
			if (!piazzaPostsFolder.isDirectory()) {
				System.err.println(piazzaPostsFolderPath + " is not a directory.");
				System.exit(1);
			}
			
//			String piazzaPostsPath = parseArg(args[0]);
//			String piazzaPostsPath = PIAZZA_POSTS_PATH;
//			File piazzaPosts = new File(piazzaPostsPath);
//			if (!piazzaPosts.exists()) {
//				System.err.println(piazzaPostsPath + " does not exist: ");
//				System.exit(1);
//			}
//			if (!piazzaPostsPath.toLowerCase().endsWith(".json")) {
//				System.err.println(piazzaPostsPath + " is not a JSON file");
//				System.exit(1);
//			}

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
			faker.execute(piazzaPostsFolder);
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
		File piazzaPostsFolder = (File) args;
		File[] files = piazzaPostsFolder.listFiles();
		File anonFolder = new File(piazzaPostsFolder, "Anon");
		if (!anonFolder.exists()) {
			anonFolder.mkdirs();
		}
		for (File file : files) {
			if (file.isDirectory()) {
				continue;
			}
			String fileName = file.getName();
			if (fileName.contains("ByAuthorPosts") && fileName.toLowerCase().endsWith(".json")) {
				anonymizeByAuthors(file, anonFolder);
			} else if (fileName.contains("AllPosts") && fileName.toLowerCase().endsWith(".json")) {
				anonymizeAllPosts(file, anonFolder);
			} else if (fileName.contains("Authors") && fileName.toLowerCase().endsWith(".txt")) {
				anonymizeAuthors(file, anonFolder);
			}
			
		}
	}
	
	public String getFakeAuthor(String author) {
		Matcher matcher = studentNamePattern.matcher(author);
		if (!matcher.matches()) {
			return "Cannot match author";
		}
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
			fakeName = fakeName.substring(0, fakeName.indexOf(','));
			fakeAuthor = fakeName + "(" + fakeName + "@live.unc.edu)";
		}
		return fakeAuthor;
	}
	
	public void anonymizeAuthors(File authors, File anonFolder) {
		String authorsString = readFile(authors).toString();
		String[] authorList = authorsString.split("\\R");
		StringBuilder sb = new StringBuilder();
		
		for (String author : authorList) {
			if (author.startsWith("Instructor")) {
				continue;
			}
			if (!authorToFakeAuthor.containsKey(author)) {
				authorToFakeAuthor.put(author, getFakeAuthor(author));
			}
			sb.append(authorToFakeAuthor.get(author) + System.lineSeparator());
		}
		File anonAuthors = new File(anonFolder, authors.getName().replace(".txt", "Anon.txt"));
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(anonAuthors))){
			bw.write(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void findAuthor(JSONObject post) {
		String author = post.getString("author");
		if (!author.contains("Instructor") && !authorToFakeAuthor.containsKey(author)) {
			authorToFakeAuthor.put(author, getFakeAuthor(author));
		}
		if (!post.has("children")) {
			return;
		}
		JSONArray children = post.getJSONArray("children");
		for (Object child : children) {
			findAuthor((JSONObject) child);
		}
	}
	
	public void anonymizeAllPosts(File allPosts, File anonFolder) {
		String allPostsString = readFile(allPosts).toString();
		JSONObject allPostsJson = new JSONObject(allPostsString);
		
		for (String post : allPostsJson.keySet()) {
			JSONObject postJson = allPostsJson.getJSONObject(post);
			findAuthor(postJson);
		}
		
		for (Entry<String, String> entry : authorToFakeAuthor.entrySet()) {
			allPostsString = allPostsString.replace(entry.getKey(), entry.getValue());
		}
		
		File anonAllPosts = new File(anonFolder, allPosts.getName().replace(".json", "Anon.json"));
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(anonAllPosts))){
			bw.write(allPostsString);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void anonymizeByAuthors(File piazzaPosts, File anonFolder) {
//		File piazzaPosts = (File) args;
		String piazzaPostsString = readFile(piazzaPosts).toString();
		JSONObject piazzaPostsJson = new JSONObject(piazzaPostsString);
		
		for (String author : piazzaPostsJson.keySet()) {
			if (author.startsWith("Instructor")) {
				continue;
			}
			if (!authorToFakeAuthor.containsKey(author)) {
				authorToFakeAuthor.put(author, getFakeAuthor(author));
			}
//			Matcher matcher = studentNamePattern.matcher(author);
//			String firstName = matcher.group(1);
//			String lastName = matcher.group(2);
//			String onyen = matcher.group(3);
//			String fakeName = CommentsIdenMap.get(onyen);
//			String fakeAuthor = "";
//			if (fakeName == null) {
//				String fakeFirstName = faker.name().firstName();
//				String fakeLastName = faker.name().lastName();
//				String fakeOnyen = fakeFirstName + " " + fakeLastName;
//				newPairs.put(concat(onyen, firstName, lastName), concat(fakeOnyen, fakeFirstName, fakeLastName));
//				fakeAuthor = fakeFirstName + " " + fakeLastName + "(" + fakeOnyen + "@live.unc.edu)";
//			} else {
//				fakeName = fakeName.substring(0, fakeName.indexOf(','));
//				fakeAuthor = fakeName + "(" + fakeName + "@live.unc.edu)";
//			}
			piazzaPostsString = piazzaPostsString.replace(author, authorToFakeAuthor.get(author));
		}
		File anonPiazzaPosts = new File(anonFolder, piazzaPosts.getName().replace(".json", "Anon.json"));
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(anonPiazzaPosts))){
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
