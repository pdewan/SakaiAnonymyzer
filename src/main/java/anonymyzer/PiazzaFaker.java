package anonymyzer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

public class PiazzaFaker extends GeneralFaker {

//	private static final String PIAZZA_POSTS_PATH = "F:\\Hermes Data\\PiazzaOutput\\Comp301ss22";
	String logFileName = "piazza_faker_log";
//	Pattern onyenPattern = Pattern.compile(".*\\((.*)@.*\\)");
	Pattern studentNamePattern = Pattern.compile("(.*) (.*)\\((.*)@.*\\)");
	Map<String, String> authorToFakeAuthor;
	Map<String, String> uidToAuthor;

	
	public PiazzaFaker() throws IOException {
		super();
		authorToFakeAuthor = new HashMap<>();
		uidToAuthor = new HashMap<>();
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

			DownloadNameMap.main(args);
			PiazzaFaker faker = new PiazzaFaker();
			if (!faker.setNameMapAndNameFile()) {
				System.exit(1);
			}

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
			System.out.println("Cannot match " + author + " against regex: " + studentNamePattern);
			String fakeFirstName = faker.name().firstName();
			String fakeLastName = faker.name().lastName();
			String fakeOnyen = fakeFirstName + " " + fakeLastName;
			return fakeFirstName + " " + fakeLastName + "(" + fakeOnyen + "@live.unc.edu)?";
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
		writeFile(anonAuthors, sb.toString());
	}
	
	public void findAuthor(JSONObject post) {
		String author = null;
		if (post.has("author")) {
			author = post.getString("author");
		}
		if (author != null && !author.contains("Instructor") && !authorToFakeAuthor.containsKey(author)) {
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
		writeFile(anonAllPosts, allPostsString);
	}
	
	public void anonymizeByAuthors(File piazzaPosts, File anonFolder) {
		String piazzaPostsString = readFile(piazzaPosts).toString();
		JSONObject piazzaPostsJson = new JSONObject(piazzaPostsString);
		
		for (String author : piazzaPostsJson.keySet()) {
			if (author.startsWith("Instructor")) {
				continue;
			}
			if (!authorToFakeAuthor.containsKey(author)) {
				authorToFakeAuthor.put(author, getFakeAuthor(author));
			}

			piazzaPostsString = piazzaPostsString.replace(author, authorToFakeAuthor.get(author));
		}
		File anonPiazzaPosts = new File(anonFolder, piazzaPosts.getName().replace(".json", "Anon.json"));
		writeFile(anonPiazzaPosts, piazzaPostsString);
	}
}
