package anonymyzer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GradescopeFaker extends GeneralFaker {

	final String[] HEADERS = {"First Name", "Last Name", "Onyen", "Grade"}; 
	Map<String, String> nameToOnyen = new HashMap<>();

	
	public GradescopeFaker() throws IOException {
		super();
	}
	
	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			System.err.println("Enter main args: path to Gradescope grades file, path to grades.csv file");
			System.exit(1);
		}

		try {
			GradescopeFaker faker = new GradescopeFaker();

			String gradescopeGradesPath = parseArg(args[0]);
//			String piazzaPostsFolderPath = PIAZZA_POSTS_PATH;

			File gradescopeGrades = new File(gradescopeGradesPath);
			if (!gradescopeGrades.exists()) {
				System.err.println(gradescopeGradesPath + " folder does not exist.");
				System.exit(1);
			}
			
			String gradesCsvPath = args.length == 2 ? parseArg(args[1]) : "";
			if (gradesCsvPath.isEmpty()) {
				System.err.println("Path for grades.csv is missing and cannot be found in the folder provided");
				System.exit(1);
			}

			DownloadNameMap.main(args);
			if (!faker.setNameMapAndNameFile()) {
				System.exit(1);
			}
			
			File[] files = {gradescopeGrades, new File(gradesCsvPath)};

			faker.execute(files);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			UpdateNameMap.main(args);
		}
	}

	@Override
	public void anonymize(Object arg) {
		if (!(arg instanceof File[])) {
			return;
		}
		File[] files = (File[]) arg;
		File gradescopeGrades = files[0];
		File gradesCsv = files[1];
		loadNameToOnyenMap(gradesCsv);
		String[] lines = readFile(gradescopeGrades).toString().split("\\R");
		List<String> nextLine = new ArrayList<>();
		File anonGrades = new File(gradescopeGrades.getPath().replace(".csv", "Anon.csv"));
		if (anonGrades.exists()) {
			anonGrades.delete();
		}
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(anonGrades))) {
//			anonGrades.createNewFile();
			bw.write(String.join(",", HEADERS) + System.lineSeparator());
			for (int i = 1; i < lines.length; i++) {
				String[] line = lines[i].split(",");
				if (line[8].equals("Missing")) {
					continue;
				}
				String onyen = line[3].substring(0, line[3].indexOf("@"));
//				if (onyen.equals("elad_ohana")) {
//					onyen = "eohana";
//				}
				String fakeName = CommentsIdenMap.get(onyen);
				if (fakeName == null) {
					String fullName = line[0] + " " + line[1];
					boolean found = false;
					for (String name : nameToOnyen.keySet()) {
						if (name.contains(fullName)) {
							onyen = nameToOnyen.get(name);
							fakeName = CommentsIdenMap.get(onyen);
							found = true;
							break;
						}
					}
					if (!found) {
						String fakeFirstName = faker.name().firstName();
						String fakeLastName = faker.name().lastName();
						String fakeOnyen = fakeFirstName + " " + fakeLastName + "?";
						newPairs.put(concat(onyen, line[0], line[1]), concat(fakeOnyen, fakeFirstName, fakeLastName));
						fakeName = concat(fakeOnyen, fakeFirstName, fakeLastName);
					}
				}
				
				String[] fakeNames = fakeName.split(",");
				nextLine.clear();
				nextLine.add(fakeNames[1]);
				nextLine.add(fakeNames[2]);
				nextLine.add(fakeNames[0]);
				nextLine.add(line[6]);
				bw.write(String.join(",", nextLine) + System.lineSeparator());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		newPairs.clear();
	}
	
	public void loadNameToOnyenMap(File gradesCsv) {
		String gradesCsvString = readFile(gradesCsv).toString();
		String[] lines = gradesCsvString.split("\\R");
		for (int i = 3; i < lines.length; i++) {
			String[] fields = lines[i].split(",");
			String onyen = unquote(fields[1]);
			String lastName = unquote(fields[2]);
			String firstName = unquote(fields[3]);
			nameToOnyen.put(firstName + " " + lastName, onyen);
		}
	}
	
	public String unquote(String s) {
		return s.substring(1, s.length() - 1);
	}
}
