package anonymyzer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import com.github.javafaker.Faker;

public class FakeNameGenerator extends GeneralFaker {
	
//	protected static final String NAME_MAP = "name map.csv";
//	protected static final String NAME_FILE = "name.yml";
//	protected static final String NAME_MAP_ID = "1_L9fs9Oy5-0-IJeoJTyu8fY6lccek0Ox";
//	protected static final String NAME_FILE_ID = "1VN317S6CkfnTknVBuwbFbwqEjpXoCeRg";
//	protected static final String TOKEN = "tokens" + File.separator + "StoredCredential";
//	File nameMapCSV;
//	String nameMapPath;
//	Faker faker;
//	HashMap<String, String> CommentsIdenMap = new HashMap<String, String>();
//	HashMap<String, String> newPairs = new HashMap<>();
//	HashSet<String> fakeNameSet = new HashSet<>();
//	File log_file;
	String logFileName = "fake_name_generator_log";
//	FileWriter logger;
	
	public FakeNameGenerator() throws IOException {
		super();
	}
	public Faker getFaker() {
		if (faker == null) {
			faker = new Faker();
		}
		return faker;
	}
	
	public static void main(String[] args) throws IOException {
		if (args.length != 3) {
			System.err.println("Enter main args: onyen/email firstName lastName");
			System.exit(1);
		}
		
		FakeNameGenerator faker = new FakeNameGenerator();
		if (!faker.setNameMapAndNameFile()) {
			System.exit(1);
		}
		try {
//			for (int i = 0; i < args.length; i++) {
//				args[i] = parseArg(args[i]);
//			}
			args = parseArgs(args);
			
//			File namemap = new File(NAME_MAP);
//			if (!namemap.exists()) {
//				System.err.println("name map.csv not found, please download required files first");
//				System.exit(1);
//			}
//			faker.setNameMap(namemap);
//			File nameFile = new File(NAME_FILE);
//			if (!nameFile.exists()) {
//				System.err.println("name.yml not found, please download name map.csv first");
//				System.exit(1);
//
//			}
//			((FakeValuesGrouping)faker.getFaker().fakeValuesService().getFakeValueList().get(0))
//				.setSpecifiedFileName("name", nameFile.getPath());
			

			faker.execute(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
//	
//	public void setNameMap(String path) throws IOException {
//		nameMapPath = path;
//		nameMapCSV = new File(path);
//	}
//
//	
//	public void execute(Object arg) throws IOException, InterruptedException {
//		loadNameMap();
//		anonymize(arg);
//		updateNameMap();
//	}

	public void anonymize(Object arg) {
		if (!(arg instanceof String[])) {
			return;
		}
		String[] args = (String[]) arg;
		String[] tokens = getTokens(args[1], args[2], args[0]);
		System.out.println(concatFirst3(tokens));
	}
//	
//	public void setNameMap(File file) {
//		nameMapPath = file.getPath();
//		nameMapCSV = file;
//	}
//	
//	public void loadNameMap() throws IOException {
//		if (!nameMapCSV.exists()) {
//			logger.write(nameMapPath + " not found, creating name map: " + nameMapPath);
//			nameMapCSV.createNewFile();
//			logger.write("Name map created: " + nameMapPath);
//			return;
//		}
//		logger.write("Loading name map: " + nameMapPath);
//		try (BufferedReader br = new BufferedReader(new FileReader(nameMapCSV))) {
//			String line = "";
//			while ((line = br.readLine()) != null) {
//				String[] vals = line.split(",");
//				loadAnonNameMap(vals);
//			}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} 
//	}
//	
//	protected void loadAnonNameMap(String[] vals) {
//		CommentsIdenMap.put(vals[0], concat(vals[3], vals[4], vals[5]));
//		fakeNameSet.add(vals[3]);
//	}
//	
//	public void updateNameMap() throws IOException {
//		if (!nameMapCSV.exists()) {
//			logger.write("Default name map not found, creating default name map: " + NAME_MAP);
//			nameMapCSV.createNewFile();
//			logger.write("Default name map created: " + NAME_MAP);
//		}
//		logger.write("Updating name map: " + nameMapCSV.getPath());
//		try (BufferedWriter bw = new BufferedWriter(new FileWriter(nameMapCSV, true))) {
//			for (Entry<String, String> entry: newPairs.entrySet()) {
//				bw.write(entry.getKey() + "," + entry.getValue());
//				bw.newLine();
//			}
//		} 
//		newPairs.clear();
////		DriveAPI.updateFile(NAME_MAP_ID, nameMapPath);
//	}

	protected String getFakeName(String lastName, String firstName, String onyen) {
		String[] tokens = getTokens(firstName, lastName, onyen);
		if (tokens == null) {
			return "Failed to get fake name";
		}
		return concatFirst3(tokens);
	}
	
	protected String[] getTokens(String firstName, String lastName, String onyen) {
		String fake = CommentsIdenMap.get(onyen);
		if (fake != null) {
			return fake.split(",");
		}
		String[] tokens = new String[3];
		do {
			tokens[1] = getFaker().name().firstName();
			tokens[2] = getFaker().name().lastName();
			tokens[0] = tokens[1] + " " + tokens[2];
		} while (fakeNameSet.contains(tokens[2]));
		CommentsIdenMap.put(onyen, concatFirst3(tokens));
		fakeNameSet.add(tokens[2]);
		newPairs.put(concat(onyen, firstName, lastName), concatFirst3(tokens));
		return tokens;
	}
//	
//	protected String concatFirst3(String[] tokens) {
//		return concat(tokens[0], tokens[1], tokens[2]);
//	}
//	
//	protected String concat(String onyen, String firstName, String lastName) {
//		return onyen+","+firstName+","+lastName;
//	}
//		
//	public static String parseArg(String arg) {
//		if (arg.startsWith("'") && arg.endsWith("'")) {
//			return arg.substring(1, arg.length()-1);
//		}
//		return arg;
//	}
}
