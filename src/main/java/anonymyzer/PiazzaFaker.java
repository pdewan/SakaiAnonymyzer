package anonymyzer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import anonymyzer.factories.KeywordFactory;
import anonymyzer.factories.LineReplacerFactory;
import anonymyzer.factories.StringReplacerFactory;

public class PiazzaFaker extends GeneralFaker {
//William Lucia Walker, William H.Lancaster
// William Jane Wilderman

//	private static final String PIAZZA_POSTS_PATH = "F:\\Hermes Data\\PiazzaOutput\\Comp301ss22";
	String logFileName = "piazza_faker_log";
//	Pattern onyenPattern = Pattern.compile(".*\\((.*)@.*\\)");
//	public static final Pattern fullNamePattern2 = Pattern.compile("\"(.*) (.*)\\((.*)@.*\\)\": \\[");
	public static final Pattern fullNamePattern2 = Pattern.compile("\"(.*) (.*)\\((.*)@.*\\)\":.*");

//	public static final Pattern fullNamePattern2 = Pattern.compile("(.*) (.*)\\((.*)@.*\\).*");

	public static final Pattern fullNamePattern = Pattern.compile("(.*) (.*)\\((.*)@.*\\)");
	Pattern firstNamePattern = Pattern.compile("(.*)\\((.*)@.*\\)");
//	Map<String, String> authorToFakeAuthor;
//	Map<String, String> emailToFakeAuthor;
//	Map<String, String> uidToFakeAuthor;
//	Map<String, String> fullNameToFakeFullName;
//	Map<String, String> firstNameToFakeFirstName;
//	Map<String, String> lastNameToFakeLastName;

//	Map<String, String> someNameToFakeAuthor;
//	List<String> originalNameList;
//	List<String> replacementNameList;

//	Map<String, String> firstNameToFakeFirstName;

//	Map<String, String> uidToAuthor;
	static final String NOT_MATCHED_FILE = "not matched authors.txt";

	public PiazzaFaker() throws IOException {
		super();
//		authorToFakeAuthor = new HashMap<>();
//		uidToAuthor = new HashMap<>();
//		uidToFakeAuthor = new HashMap<>();
//		emailToFakeAuthor = new HashMap<>();
//		fullNameToFakeFullName = new HashMap<>();
//		firstNameToFakeFirstName = new HashMap<>();
//		lastNameToFakeLastName = new HashMap<>();
	}

//	protected void processElements(Map<String, String> aMap) {
//		for (String aKey : aMap.keySet()) {
//			originalNameList.add(aKey);
//			String aValue = aMap.get(aKey);
//			replacementNameList.add(aValue);
//			someNameToFakeAuthor.put(aKey, aValue);
//		}
//	}
//
//	protected void replacementSetup() {
//		if (someNameToFakeAuthor == null) {
//			originalNameList = new ArrayList();
//			replacementNameList = new ArrayList();
//			someNameToFakeAuthor = new HashMap<>();
//			processElements(authorToFakeAuthor);
//			processElements(emailToFakeAuthor);
//			processElements(uidToFakeAuthor);
//			processElements(fullNameToFakeFullName);
//			processElements(firstNameToFakeFirstName);
//			processElements(lastNameToFakeLastName);
//			try {
//				specificLogger.write(authorToFakeAuthor.toString() + "/n");
//				specificLogger.flush();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			// first add
//		}
//	}

//	public static void main(String[] args) throws IOException {
//		if (args.length != 1) {
//			System.err.println("Enter main args: path to Piazza posts files folder");
//			System.exit(1);
//		}
//
//		try {
//			String piazzaPostsFolderPath = parseArg(args[0]);
////			String piazzaPostsFolderPath = PIAZZA_POSTS_PATH;
//
//			File piazzaPostsFolder = new File(piazzaPostsFolderPath);
//			if (!piazzaPostsFolder.exists()) {
//				System.err.println(piazzaPostsFolderPath + " folder does not exist.");
//				System.exit(1);
//			}
//			if (!piazzaPostsFolder.isDirectory()) {
//				System.err.println(piazzaPostsFolderPath + " is not a directory.");
//				System.exit(1);
//			}
//
//			DownloadNameMap.main(args);
//			PiazzaFaker faker = new PiazzaFaker();
//			if (!faker.setNameMapAndNameFile()) {
//				System.exit(1);
//			}
//
//			faker.execute(piazzaPostsFolder);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			UpdateNameMap.main(args);
//		}
//	}
	
	public static void main(String[] args) throws IOException {
//		if (args.length != 1) {
//			System.err.println("Enter main args: path to Piazza posts files folder");
//			System.exit(1);
//		}
		
		if (args.length != 1 && args.length != 2) {
			System.err.println("Enter main args: path to piazza postsfolder and grades.csv for the class");
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
			
			PiazzaFaker faker = new PiazzaFaker();

			
			File gradesCsv = faker.getGradesCSV(args, piazzaPostsFolderPath);
			if (gradesCsv == null) {
				System.exit(1);
			}
			faker.loadNameToOnyenMap(gradesCsv);

			DownloadNameMap.main(args);
//			PiazzaFaker faker = new PiazzaFaker();
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

	protected void processExecuteArg(Object args) {
		if (!(args instanceof File)) {
			return;
		}
		File piazzaPostsFolder = (File) args;
		try {
			createSpecificLoggerAndMetrics(piazzaPostsFolder, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void anonymize(Object args) {
		if (!(args instanceof File)) {
			return;
		}
		File piazzaPostsFolder = (File) args;
//		try {
//			createSpecificLoggerAndMetrics(piazzaPostsFolder, false);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		File[] files = piazzaPostsFolder.listFiles();
		File anonFolder = new File(piazzaPostsFolder, "Anon");
		if (!anonFolder.exists()) {
			anonFolder.mkdirs();
		}
//		anonymizeInAnyOrder(anonFolder, files);
		anonymizeByAuthorsFirst(anonFolder, files);

//		for (File file : files) {
//			if (file.isDirectory()) {
//				continue;
//			}
//			String fileName = file.getName();
//			if (fileName.contains("ByAuthorPosts") && fileName.toLowerCase().endsWith(".json")) {
//				anonymizeByAuthors(file, anonFolder);
//			} else if (fileName.contains("AllPosts") && fileName.toLowerCase().endsWith(".json")) {
//				anonymizeAllPosts(file, anonFolder);
//			} else if (fileName.contains("Authors") && fileName.toLowerCase().endsWith(".txt")) {
//				anonymizeAuthors(file, anonFolder);
//			}
//		}
	}

	public void anonymizeInAnyOrder(File anonFolder, File[] anOriginalFiles) {

//		File piazzaPostsFolder = (File) args;
//		File[] files = piazzaPostsFolder.listFiles();
//		File anonFolder = new File(piazzaPostsFolder, "Anon");
//		if (!anonFolder.exists()) {
//			anonFolder.mkdirs();
//		}
		for (File file : anOriginalFiles) {
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

// need to get the uids first from by authors
	public void anonymizeByAuthorsFirst(File anonFolder, File[] anOriginalFiles) {

//	File piazzaPostsFolder = (File) args;
//	File[] files = piazzaPostsFolder.listFiles();
//	File anonFolder = new File(piazzaPostsFolder, "Anon");
//	if (!anonFolder.exists()) {
//		anonFolder.mkdirs();
//	}
		List<File> anAuthorPostsFiles = new ArrayList();
		List<File> anAllPostsFiles = new ArrayList();
		List<File> anAuthorsFiles = new ArrayList();
		for (File file : anOriginalFiles) {
			if (file.isDirectory()) {
				continue;
			}
			String fileName = file.getName();
			if (fileName.contains("ByAuthorPosts") && fileName.toLowerCase().endsWith(".json")) {
//			anonymizeByAuthors(file, anonFolder);
				anAuthorPostsFiles.add(file);

			} else if (fileName.contains("AllPosts") && fileName.toLowerCase().endsWith(".json")) {
//			anonymizeAllPosts(file, anonFolder);
				anAllPostsFiles.add(file);
			} else if (fileName.contains("Authors") && fileName.toLowerCase().endsWith(".txt")) {
//			anonymizeAuthors(file, anonFolder);
				anAuthorsFiles.add(file);
			}
		}
		for (File anAuthorsPostsFile : anAuthorPostsFiles) {
			try {
				specificLogger.write("Anonymyzing all posts:" + anAuthorsPostsFile + "\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			anonymizeByAuthors(anAuthorsPostsFile, anonFolder);

		}
		for (File anAllPostsFile : anAllPostsFiles) {
			try {
				specificLogger.write("Anonymyzing all posts:" + anAllPostsFile + "\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			anonymizeAllPosts(anAllPostsFile, anonFolder);

		}
		for (File anAuthorsFile : anAuthorsFiles) {
			try {
				specificLogger.write("Anonymyzing authors:" + anAuthorsFile + "\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			anonymizeAuthors(anAuthorsFile, anonFolder);

		}
//		if (anAuthorPosts != null) {
//			anonymizeByAuthors(anAuthorPosts, anonFolder);
//		}
//		if (anAllPosts != null) {
//			anonymizeAllPosts(anAllPosts, anonFolder);
//		}
//		if (anAuthors != null) {
//			anonymizeAllPosts(anAllPosts, anonFolder);
//		}
//		for (File file : anOriginalFiles) {
//			if (file.isDirectory()) {
//				continue;
//			}
//			String fileName = file.getName();
//			if (fileName.contains("ByAuthorPosts") && fileName.toLowerCase().endsWith(".json")) {
//				anonymizeByAuthors(file, anonFolder);
//			} else if (fileName.contains("AllPosts") && fileName.toLowerCase().endsWith(".json")) {
//				anonymizeAllPosts(file, anonFolder);
//			} else if (fileName.contains("Authors") && fileName.toLowerCase().endsWith(".txt")) {
//				anonymizeAuthors(file, anonFolder);
//			}
//		}
		try {
			String aMetricsString = assignmentMetrics.toString();
			specificLogger.write(aMetricsString);
			specificLogger.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getFakeAuthor(String author) {
		Matcher matcher = fullNamePattern.matcher(author);
		String firstName = null;
		String lastName = null;
		String onyen = null;
		if (matcher.matches()) {
			firstName = matcher.group(1);
			lastName = matcher.group(2);
			onyen = matcher.group(3);
		} else {
			matcher = firstNamePattern.matcher(author);
			if (matcher.matches()) {
				firstName = matcher.group(1);
				lastName = "";
				onyen = matcher.group(2);
			} else {
				File notMatched = new File(NOT_MATCHED_FILE);
				if (!notMatched.exists()) {
					try {
						notMatched.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				try (BufferedWriter bw = new BufferedWriter(new FileWriter(notMatched, true))) {
					bw.write(author + System.lineSeparator());
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("Cannot match " + author + " against regex: " + fullNamePattern);
				String fakeFirstName = faker.name().firstName();
				String fakeLastName = faker.name().lastName();
				String fakeOnyen = fakeFirstName + " " + fakeLastName;
				return fakeFirstName + " " + fakeLastName + "(" + fakeOnyen + "([email]))?";
			}
		}

//		String fakeName = CommentsIdenMap.get(onyen);
		String fakeName = getFakeOfNameOrPossiblyAlias(onyen);

		String fakeAuthor = "";

		if (fakeName == null || onyen.equals("instructor")) {
			String fakeFirstName = faker.name().firstName();
			String fakeLastName = faker.name().lastName();
			String fakeOnyen = fakeFirstName + " " + fakeLastName;
			if (fakeName == null) {
				putNamePair(concat(onyen, firstName, lastName), concat(fakeOnyen, fakeFirstName, fakeLastName));
//				newPairs.put(concat(onyen, firstName, lastName), concat(fakeOnyen, fakeFirstName, fakeLastName));
			}
			fakeAuthor = fakeFirstName + " " + fakeLastName + "(" + fakeOnyen + "[email])";
		} else {
//			fakeName = fakeName.substring(0, fakeName.indexOf(','));
//			fakeAuthor = fakeName + "(" + fakeName + "@live.unc.edu)";
			fakeAuthor = fakeName + "([email])";
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
//		String id = null;
//		if (post.has("u")) {
//			id = post.getString("u");
//		}

		if (author != null && !author.contains("Instructor") && !authorToFakeAuthor.containsKey(author)) {
			authorToFakeAuthor.put(author, getFakeAuthor(author));
		}
//		JSONArray log = null;
//
//		if (post.has("log")) {
//			log = post.getJSONArray("log");
//		}
//		JSONArray tag_endorse = null;

//		if (post.has("tag_endorse")) {
//			tag_endorse = post.getJSONArray("tag_endorse");
//		}
//		JSONArray feed_groups = null;
//		if (post.has("feed_groups")) {
//			feed_groups = post.getJSONArray("feed_groups");
//		}
		if (!post.has("children")) {
			return;
		}

		JSONArray children = post.getJSONArray("children");
		for (Object child : children) {
			findAuthor((JSONObject) child);
		}
	}

	protected String replace(String aString, Map<String, String> anOriginalToReplacement) {
		String retVal = aString;
		for (Entry<String, String> entry : anOriginalToReplacement.entrySet()) {
			String aKey = entry.getKey();
			String aValue = entry.getValue();
//			allPostsString = allPostsString.replace(entry.getKey(), entry.getValue());
			retVal = retVal.replace(aKey, aValue);
		}
		return retVal;
	}

	protected String normalizeString(String aString) {
		return aString;
	}

	protected String anonymyzeUsingLineReplacer(String aPiazzaPostsString) {
		String aNormalizedString = normalizeString(aPiazzaPostsString);
		String[] aCommaSplitPiazzaPostsStrings = aNormalizedString.split(",");
//		System.out.println("Num comma split strings:" + aCommaSplitPiazzaPostsStrings.length);
		StringBuffer retVal = new StringBuffer(aPiazzaPostsString.length());
//		System.out.println("anonymyze line replacer");
		boolean hasName = false;
		int aLineNumber = 0;
		for (int aCommaSplitIndex = 0; aCommaSplitIndex < aCommaSplitPiazzaPostsStrings.length; aCommaSplitIndex++) {

			if (aCommaSplitIndex != 0) {
				retVal.append(",");
			}
//			System.out.println("Commad split index:" + aCommaSplitIndex);
//			if (aCommaSplitIndex == 40016) {
//				System.out.println("found problematic index");
//			}

			String aCommaSplitSegment = aCommaSplitPiazzaPostsStrings[aCommaSplitIndex];
			String[] aParaSplitStrings = aCommaSplitSegment.split("<p>");
			
			for (int aParaSplitIndex = 0; aParaSplitIndex < aParaSplitStrings.length; aParaSplitIndex++) {

				if (aParaSplitIndex != 0) {
					retVal.append("<p>");
				}
				assignmentMetrics.numLinesProcessed++;

				String aParaSplitSegment = aParaSplitStrings[aParaSplitIndex];
				int numCharsInSegment = aParaSplitSegment.length();
				assignmentMetrics.numCharactersProcessed += numCharsInSegment;
				String aReplacedSegment = aParaSplitSegment;
				if (AnonUtil.hasName(aParaSplitSegment, originalNameList)) {
					hasName = true;
					assignmentMetrics.numLinesWithNames++;
					assignmentMetrics.numCharactersInLinesWithNames += numCharsInSegment;
					
					String aKeywordRegex = KeywordFactory.keywordsRegex(aParaSplitSegment);
					Matcher matcher = PiazzaFaker.fullNamePattern2.matcher(aParaSplitSegment);
//					if (aParaSplitSegment.contains("sun.nio")) {
//						System.out.println("found offending text");
//					}
					if (!aParaSplitSegment.contains("\\n") &&
							
							matcher.matches()) {
						aKeywordRegex = null;
					}
					
					aReplacedSegment = LineReplacerFactory.replaceLine(aLineNumber, aParaSplitSegment,
							messagesOutput, specificLogger, aKeywordRegex, originalNameList,
							replacementNameList, someNameToFakeAuthor, assignmentMetrics);
					aLineNumber++;
				}
				retVal.append(aReplacedSegment);
			}
		}
		if (hasName) {
			assignmentMetrics.numFilesWithNames++;
		}
		return retVal.toString();

//		return LineReplacerFactory.replaceLine(
//				0, 
//				aPiazzaPostsString, 
//				messagesOutput, 
//				specificLogger, 
//				KeywordFactory.keywordsRegex(), 
//				originalNameList, 
//				replacementNameList, 
//				someNameToFakeAuthor, 
//				assignmentMetrics);

	}

	protected String anonymyzeUsingStringReplace(String aPiazzaPostsString) {
		String retVal = aPiazzaPostsString;
		retVal = replace(retVal, authorToFakeAuthor);
		retVal = replace(retVal, emailToFakeAuthor);
		retVal = replace(retVal, uidToFakeAuthor);
		return retVal;
	}

	public void anonymizeAllPosts(File allPosts, File anonFolder) {
		assignmentMetrics.numFilesProcessed++;

		String allPostsString = readFile(allPosts).toString();
		JSONObject allPostsJson = new JSONObject(allPostsString);

		for (String post : allPostsJson.keySet()) {
			JSONObject postJson = allPostsJson.getJSONObject(post);
			findAuthor(postJson);
		}
		allPostsString = anonymyzeUsingLineReplacer(allPostsString);
//		replace(allPostsString, authorToFakeAuthor );
//		replace(allPostsString, emailToFakeAuthor);
//		replace(allPostsString, uidToFakeAuthor);

//		for (Entry<String, String> entry : authorToFakeAuthor.entrySet()) {
//			String aKey = entry.getKey();
//			String aValue = entry.getValue();
////			allPostsString = allPostsString.replace(entry.getKey(), entry.getValue());
//			allPostsString = allPostsString.replace(aKey, aValue);
//
//		}
//		for (Entry<String, String> entry : emailToFakeAuthor.entrySet()) {
//			String aKey = entry.getKey();
//			String aValue = entry.getValue();
////			allPostsString = allPostsString.replace(entry.getKey(), entry.getValue());
//			allPostsString = allPostsString.replace(aKey, aValue);
//
//		}

		File anonAllPosts = new File(anonFolder, allPosts.getName().replace(".json", "Anon.json"));
		writeFile(anonAllPosts, allPostsString);
	}

	protected String getEmail(String anAuthorName) {
		int aStartIndex = anAuthorName.indexOf('(');
		int anEndIndex = anAuthorName.indexOf(')');
		if (aStartIndex < 0 || anEndIndex < 0) {
			return null;
		}
		String retVal = anAuthorName.substring(aStartIndex + 1, anEndIndex);
		return retVal;
	}

	protected String getFullName(String anAuthorName) {
		int anEndIndex = anAuthorName.indexOf('(');
		if (anEndIndex < 0) {
			return null;
		}
		String retVal = anAuthorName.substring(0, anEndIndex);
		return retVal;
	}

	protected String getUIDFromPost(JSONArray anAuthorPosts) {
		if (anAuthorPosts == null) {
			return null;
		}
		String retVal = null;
		for (Object anObjectPost : anAuthorPosts) {
			JSONObject aJSONPost = (JSONObject) anObjectPost;
			if (aJSONPost.has("uid")) {
				retVal = aJSONPost.getString("uid");
				return retVal;
			}
		}

		return null;
	}

	protected String getUIDFromLog(JSONArray anAuthorPosts) {
		if (anAuthorPosts == null) {
			return null;
		}
		String retVal = null;
		for (Object anObjectPost : anAuthorPosts) {
			JSONObject aJSONPost = (JSONObject) anObjectPost;
			if (aJSONPost.has("log")) {
				JSONArray aLog = aJSONPost.getJSONArray("log");
				for (Object aLogEntry : aLog) {
					JSONObject aJSONLogEntry = (JSONObject) aLogEntry;
					if (aJSONLogEntry.has("u")) {
						String aU = aJSONLogEntry.getString("u");
						if (aJSONLogEntry.has("n")) {
							String anN = aJSONLogEntry.getString("n");
							if (anN.equals("create")) {
								return aU;
							}
						}

					}
				}
			}
		}
		return null;
	}

//	protected void nonDuplicatePut(Map<String, String> aMap, String aKey, String aValue) {
//		if (aKey.contains("nstructor")) {
//			return;
//		}
//		String anExistingValue = aMap.get(aKey);
//		if (anExistingValue != null && !aValue.equals(anExistingValue)) {
//			aMap.put(aKey, HIDDEN_NAME);
//
//			try {
//				specificLogger.write("Key:" + aKey + " Duplicate Values:" + anExistingValue + "," + aValue + "\n");
//				specificLogger.flush();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		} else {
//			aMap.put(aKey, aValue);
//			aMap.put(aKey.toLowerCase(), aValue);
//		}
//	}

	protected boolean setUpDone = false;

	protected void replacementSetup() {
//		if (someNameToFakeAuthor == null) {
		if (!setUpDone) {
			setUpDone = true;
//			originalNameList = new ArrayList();
//			replacementNameList = new ArrayList();
//			someNameToFakeAuthor = new HashMap<>();
			processElementsOfAllMaps();
//			processElements(authorToFakeAuthor);
//			processElements(emailToFakeAuthor);
//			processElements(uidToFakeAuthor);
//			processElements(fullNameToFakeFullName);
//			processElements(firstNameToFakeFirstName);
//			processElements(lastNameToFakeLastName);
			try {
				specificLogger.write(authorToFakeAuthor.toString() + "/n");
				specificLogger.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// first add
		}
	}

	protected String extractFakeName(String aMapValue) {
		return aMapValue.split(",")[0];
	}

	public static void add(JSONArray aContainer, JSONArray aContained, int aStartIndex) {
		for (int anIndex = aStartIndex; anIndex < aContained.length(); anIndex++) {
			aContainer.put(aContained.get(anIndex));
		}

	}

	public static JSONArray merge(JSONArray first, JSONArray second) {
		if (second.length() == 0) {
			return first;
		}
		if (first.length() == 0) {
			return second;
		}
		int index1 = 0;
		int index2 = 0;
		JSONObject anElement1 = first.getJSONObject(index1);
		JSONObject anElement2 = second.getJSONObject(index2);

		JSONArray retVal = new JSONArray();
		long aTime1 = anElement1.getLong("time");
		long aTime2 = anElement2.getLong("time");

		while (true) {
			
			if (aTime1 <= aTime2) {
				retVal.put(anElement1);
				index1++;
				if (index1 < first.length()) {
					anElement1 = first.getJSONObject(index1);
					aTime1 = anElement1.getLong("time");
				} else {
					add(retVal, second, index2);
					return retVal;
				}

			} else {
				retVal.put(anElement2);
				index2++;
				if (index2 < second.length()) {
					anElement2 = second.getJSONObject(index2);
					aTime2 = anElement2.getLong("time");
				} else {
					add(retVal, first, index1);
					return retVal;
				}

			}

		}
	}

	public JSONObject removeDuplicates(JSONObject piazzaPostsJson) {
		Map<String, String> anIdToAuthor = new HashMap<String, String>();
		Map<String, String> aFullNameToAuthor = new HashMap<String, String>();
		Set<String> aDeletedAuthors = new HashSet<String>();
		for (String author : piazzaPostsJson.keySet()) {
			JSONArray anAuthorArray = (JSONArray) piazzaPostsJson.getJSONArray(author);
//			anAuthorArray.put(index, value)
			String anEmail = getEmail(author);
			String anId = anEmail.split("@")[0];
			String aFullName = getFullName(author).trim();

			String anExistingIdAuthor = aFullNameToAuthor.get(aFullName);
			String anExistingFullNameAuthor = aFullNameToAuthor.get(aFullName);
			String anExistingAuthor = null;
			JSONArray anExistingJSONArray = null;

			if (anExistingIdAuthor != null) {
				String aFakeNameDetails = CommentsIdenMap.get(anId);
				if (aFakeNameDetails != null) {
					anExistingAuthor = anExistingIdAuthor;
					anExistingJSONArray = piazzaPostsJson.getJSONArray(anExistingIdAuthor);
				}
			}
			if (anExistingJSONArray == null && anExistingFullNameAuthor != null) {
				anExistingAuthor = anExistingFullNameAuthor;
				anExistingJSONArray = piazzaPostsJson.getJSONArray(anExistingFullNameAuthor);

			}
			if (anExistingJSONArray != null && anExistingAuthor != null) {
				JSONArray aMergedRecord = merge(anExistingJSONArray, anAuthorArray);
				piazzaPostsJson.put(anExistingAuthor, aMergedRecord);
				aDeletedAuthors.add(author);
			} else {
				anIdToAuthor.put(anId, author);
				aFullNameToAuthor.put(aFullName, author);
			}

		}
		for (String aDeletedAuthor : aDeletedAuthors) {
			piazzaPostsJson.remove(aDeletedAuthor);
		}
		return piazzaPostsJson;
	}

	public void anonymyzeByAuthor(JSONObject piazzaPostsJson, String author) {
		if (author.startsWith("Instructor")) {
			return;
		}
		if (author.startsWith("Anonymous")) {
			return;
		}
		if (!authorToFakeAuthor.containsKey(author)) {
//			if (author.contains("William H")) {
//				System.out.println("found william h");
//			}

			JSONArray anAuthorPosts = piazzaPostsJson.getJSONArray(author);

			String aUid = getUIDFromPost(anAuthorPosts);
			if (aUid == null) {
				aUid = getUIDFromLog(anAuthorPosts);
			}
			String anEmail = getEmail(author);
//			String[] anEmailComponents = author.split("@");
			String anOnyen = anEmail.split("@")[0];

			String aFullName = getFullName(author).trim();
//				if (aFullName.contains("White")) {
//				System.out.println(" found offending text");
//			}
			String[] aNames = aFullName.split(" ");
			String aFirstName = aNames[0];
			String aLastName = aNames[aNames.length - 1];
			String aMiddleName = null;
//			if (aNames.length == 3) {
//				aMiddleName = aNames[1];
//			}
			String aFakeAuthor = null;
			String aFakeEmail = anEmail;
			String aFakeFullName = null;
			boolean isInstructorEmail = false;
			String aKey = anOnyen;
			if (anOnyen.contains("instructor")) {
//				aFakeEmail= anEmail;
//			 aFakeAuthor = getFakeAuthor(author);
//			 aFakeEmail = getEmail(aFakeAuthor);
				aKey = aFullName;
				anOnyen = storedNamesToOnyen.get(aFullName);
				if (anOnyen == null) {
				anOnyen = (aFirstName + aLastName).toLowerCase();
				}

//				String anEntry = getFakeOfNameOrPossiblyAlias(aFullName).trim();
//				if (anEntry != null) {
//					
//			
//					aFakeFullName = extractFakeName(anEntry);
//				}

			} else {
//				String anEntry = getFakeOfNameOrPossiblyAlias(anOnyen).trim();
//				if (anEntry != null) {
//					aFakeFullName = extractFakeName(anEntry);
//
//				}
//				aFakeEmail = aFakeFullName;
			}
			String anEntry = getFakeOfNameOrPossiblyAlias(aKey);
			if (anEntry == null) {
				anEntry = getFakeOfNameOrPossiblyAlias(aFullName);
			}
			if (anEntry != null) {
//				aFakeFullName = extractFakeName(anEntry);
				aFakeFullName = anEntry;

			} else {
//				String[] aNames = aFullName.split(" ");
//				String aFirstName = aNames[0];
//				String aLastName = aNames[aNames.length - 1];
				String fakeFirstName = faker.name().firstName();
				String fakeLastName = faker.name().lastName();
				aFakeFullName = fakeFirstName + " " + fakeLastName;
				aFakeEmail = aFakeFullName;

//				onyenToFakeName.put(anOnyen, aFakeFullName);
//				CommentsIdenMap.put(anOnyen, aFakeFullName);
//				fullNameToFakeFullName.put(anOnyen, aFakeFullName);

				fullNameToFakeFullName.put(aFirstName + " " + aLastName, aFakeFullName);
				specificLogLine(anOnyen + "(" + aFirstName + " " + aLastName + ")->"+ aFakeFullName);
//				newPairs.put(concat(anOnyen, aFirstName, aLastName),
//						concat(aFakeFullName, fakeFirstName, fakeLastName));
//				
//				specificLogLine(anOnyen + "(" + aFirstName + " " + aLastName + ")->"+ aFakeFullName);
				putNamePair(concat(anOnyen, aFirstName, aLastName),
						concat(aFakeFullName, fakeFirstName, fakeLastName));

			}
			aFakeEmail = aFakeFullName;

//			String aFakeFullName = getFullName(aFakeAuthor).trim();

//			String[] aFakeNames = aFakeFullName.split(" ");
//			String aFakeFirstName = aFakeNames[0];
//			String aFakeLastName = aFakeNames[1];
			
//			if (//aFakeFullName.contains("Lucia") || 
//					aFakeFullName.contains("Jane")) {
//				System.out.println("found Jane:" + aFullName);
//			}
			if (!originalNameList.contains(aFullName)) { // somebody who dropped the course
				putFullNameComponentsAndAliases(fullNameToFakeFullName, anOnyen, aFakeFullName, true, true);

				putFullNameComponentsAndAliases(fullNameToFakeFullName, aFullName, aFakeFullName, false, true);

			}
// wehy do we need this
//			putFullNameAndAliases(fullNameToFakeFullName, aFullName, aFakeFullName, false);
//			putFullNameComponentsAndAliases(fullNameToFakeFullName, aFullName, aFakeFullName, false);

			
	//			putFullNameAndAliases(fullNameToFakeFullName, anEmail, aFakeFullName, true);

//			if (aFakeAuthor != null) {
			// this will be longer than existing names
			authorToFakeAuthor.put(author, aFakeFullName);
			originalNameList.add(0, author);
			replacementNameList.add(0, aFakeFullName);
//			}

			if (aUid != null) {
				nonDuplicatePut(uidToFakeAuthor, aUid, aFakeFullName);
			} else {
				System.err.println("Could not find uid");
			}
			if (anEmail != null) {
//				if (anEmail.contains("ajwortas")) {
//					System.out.println("found offending text");
//				}
				putFullNameAndAliases(emailToFakeAuthor, anEmail, aFakeFullName, true);

//				nonDuplicatePut(emailToFakeAuthor, anEmail, aFakeFullName);

			}
//			else {
//				System.err.println("Could not find email");
//
//			}
		}
	}

	public void anonymizeByAuthors(File piazzaPosts, File anonFolder) {
		String piazzaPostsOriginalString = readFile(piazzaPosts).toString();
		assignmentMetrics.numFilesProcessed++;
		JSONObject piazzaPostsJson = new JSONObject(piazzaPostsOriginalString);
		removeDuplicates(piazzaPostsJson);
		String piazzaPostsString = piazzaPostsJson.toString();
		for (String author : piazzaPostsJson.keySet()) {
//			System.out.println("Author:" + author);
//			if (author.contains("Andrew")) {
//				System.out.println("found offending text");
//			}
			anonymyzeByAuthor(piazzaPostsJson, author);
//			if (author.startsWith("Instructor")) {
//				continue;
//			}
//			if (!authorToFakeAuthor.containsKey(author)) {
//
//				JSONArray anAuthorPosts = piazzaPostsJson.getJSONArray(author);
//
//				String aUid = getUIDFromPost(anAuthorPosts);
//				if (aUid == null) {
//					aUid = getUIDFromLog(anAuthorPosts);
//				}
//				String anEmail = getEmail(author);
//				String aFullName = getFullName(author).trim();
//// 				if (aFullName.contains("White")) {
////					System.out.println(" found offending text");
////				}
//				String[] aNames = aFullName.split(" ");
//				String aFirstName = aNames[0];
//				String aLastName = aNames[aNames.length - 1];
//				String aMiddleName = null;
//				if (aNames.length == 3) {
//					aMiddleName = aNames[1];
//				}
//
//				String aFakeAuthor = getFakeAuthor(author);
//				String aFakeEmail = getEmail(aFakeAuthor);
//				String aFakeFullName = getFullName(aFakeAuthor).trim();
//
//				String[] aFakeNames = aFakeFullName.split(" ");
//				String aFakeFirstName = aFakeNames[0];
//				String aFakeLastName = aFakeNames[1];
//
//				putFullNameAndAliases(fullNameToFakeFullName, aFullName, aFakeFullName, false);
//
////				putFullName(fullNameToFakeFullName, 
////						aFullName, aFakeFullName);
////				putAliases(fullNameToFakeFullName, aFullName, aFakeFullName);
////				
////				if (aMiddleName != null) {
////					String aFakeFirstLastName = aFirstName + " " + aLastName;
////					nonDuplicatePut(fullNameToFakeFullName, aFakeFirstLastName, aFakeFullName);
////					nonDuplicatePut(fullNameToFakeFullName, aMiddleName, aFakeFirstName);
////					nonDuplicatePut(fullNameToFakeFullName, aMiddleName + " " + aLastName, aFakeFullName);
////  
////				}
//
//				authorToFakeAuthor.put(author, aFakeAuthor);
////				nonDuplicatePut(fullNameToFakeFullName, aFullName, aFakeFullName);
////				nonDuplicatePut(firstNameToFakeFirstName, aFirstName, aFakeFirstName);
////				nonDuplicatePut(lastNameToFakeLastName, aLastName, aFakeLastName);
////
////				nonDuplicatePut(fullNameToFakeFullName, aFullName.toLowerCase(), aFakeFullName);
////				nonDuplicatePut(firstNameToFakeFirstName, aFirstName.toLowerCase(), aFakeFirstName);
////				nonDuplicatePut(lastNameToFakeLastName, aLastName.toLowerCase(), aFakeLastName);
////
////				if (aMiddleName != null) {
////					nonDuplicatePut(firstNameToFakeFirstName, aMiddleName, HIDDEN_NAME);
////				}
////				nonDuplicatePut(lastNameToFakeLastName, aLastName, aFakeLastName);
//
//				if (aUid != null) {
////					uidToFakeAuthor.put(aUid, aFakeAuthor);
//					nonDuplicatePut(uidToFakeAuthor, aUid, aFakeAuthor);
//				} else {
//					System.err.println("Could not find uid");
//				}
//				if (anEmail != null) {
////					emailToFakeAuthor.put(anEmail, aFakeAuthor);
//					nonDuplicatePut(emailToFakeAuthor, anEmail, aFakeEmail);
//
//				} else {
//					System.err.println("Could not find email");
//
//				}
//
////				if (anAuthorPost.has("uid")) {
////					aUid = anAuthorPost.getString("uid");
////					authorToFakeAuthor.put(aUid, aFakeAuthor);
////
////				}
//
//			}
//
////			piazzaPostsString = piazzaPostsString.replace(author, authorToFakeAuthor.get(author));
////			anEmail = emailToFakeAuthor.get(anEmail);
////			if (anEmail != null) {
////				piazzaPostsString = piazzaPostsString.replace(anEmail, emailToFakeAuthor.get(anEmail));
////
////			} if (aUid != null) {
////				piazzaPostsString = piazzaPostsString.replace(anEmail,uidToFakeAuthor.get(aUid));
////
////			}
////			aUid = uidToFakeAuthor.get(aUid);
		}
//		replace(piazzaPostsString, authorToFakeAuthor);
//		replace(piazzaPostsString, emailToFakeAuthor);
//		replace(piazzaPostsString, uidToFakeAuthor);
		replacementSetup();

		piazzaPostsString = anonymyzeUsingLineReplacer(piazzaPostsString);

		File anonPiazzaPosts = new File(anonFolder, piazzaPosts.getName().replace(".json", "Anon.json"));
		writeFile(anonPiazzaPosts, piazzaPostsString);
	}
}
