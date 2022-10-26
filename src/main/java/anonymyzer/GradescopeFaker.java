package anonymyzer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
			UpdateNameMap.main(args);
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
		File anonGrades = new File(gradescopeGrades.getPath().replace(".csv", "Anon.csv"));
		if (anonGrades.exists()) {
			anonGrades.delete();
		}
		if (lines[0].contains("Total Lateness")) {
			anonymizeSemeter(lines, anonGrades);
		} else {
			anonymizeAssignment(lines, anonGrades);
		}
	}
	
	public void anonymizeSemeter(String[] lines, File anonGrades) {
		List<String> nextLine = new ArrayList<>();
		int emailIdx = -1;
		int firstNameIdx = -1;
		int lastNameIdx = -1;
		int fullNameIdx = -1;
		List<Integer> assigns = new ArrayList<>();
		List<String> assignNames = new ArrayList<>();
		List<Integer> exams = new ArrayList<>();
		List<String> examNames = new ArrayList<>();

		String[] firstLine = lines[0].split(",");

		for (int i = 0; i < firstLine.length; i++) {
			if (firstLine[i].equals("Email")) {
				emailIdx = i;
			}
			if (firstLine[i].equals("First Name")) {
				firstNameIdx = i;
			}
			if (firstLine[i].equals("Last Name")) {
				lastNameIdx = i;
			}
			if (firstLine[i].equals("Name")) {
				fullNameIdx = i;
			}
			if (firstLine[i].contains("Assignment") && !firstLine[i].contains("Lateness") && !firstLine[i].contains("Max Points") && !firstLine[i].contains("Submission Time")) {
				assignNames.add(firstLine[i]);
				assigns.add(i);
			}
			if ((firstLine[i].contains("Midterm") || firstLine[i].contains("Final")) && !firstLine[i].contains("Lateness") && !firstLine[i].contains("Max Points") && !firstLine[i].contains("Submission Time")) {
				examNames.add(firstLine[i]);
				exams.add(i);
			}
		}
		if (emailIdx == -1) {
			System.err.println("Cannot find email, using name matching");
		}
		List<String> headers = new ArrayList<>(Arrays.asList(HEADERS));
		headers.remove(headers.size()-1);
		headers.addAll(assignNames);
		headers.addAll(examNames);
		assigns.addAll(exams);
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(anonGrades))) {
			bw.write(String.join(",", headers) + System.lineSeparator());
			for (int i = 1; i < lines.length; i++) {
				String[] line = lines[i].split(",");
				if (line[emailIdx].isEmpty()) {
					continue;
				}
				
				String[] fakeNames = getFakeNames(line, emailIdx, firstNameIdx, lastNameIdx, fullNameIdx);
				nextLine.clear();
				nextLine.add(fakeNames[1]);
				nextLine.add(fakeNames[2]);
				nextLine.add(fakeNames[0]);
				for (int idx : assigns) {
					nextLine.add(line[idx]);
				}
				
				bw.write(String.join(",", nextLine) + System.lineSeparator());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void anonymizeAssignment(String[] lines, File anonGrades) {
		List<String> nextLine = new ArrayList<>();
		int emailIdx = -1;
		int firstNameIdx = -1;
		int lastNameIdx = -1;
		int fullNameIdx = -1;
		int statusIdx = -1;
		int gradeIdx = -1;
		String[] firstLine = lines[0].split(",");
		for (int i = 0; i < firstLine.length; i++) {
			if (firstLine[i].equals("Email")) {
				emailIdx = i;
			}
			if (firstLine[i].equals("First Name")) {
				firstNameIdx = i;
			}
			if (firstLine[i].equals("Last Name")) {
				lastNameIdx = i;
			}
			if (firstLine[i].equals("Name")) {
				fullNameIdx = i;
			}
			if (firstLine[i].equals("Status")) {
				statusIdx = i;
			}
			if (firstLine[i].equals("Total Score")) {
				gradeIdx = i;
			}
		}
		if (emailIdx == -1) {
			System.err.println("Cannot find email, using name matching");
		}
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(anonGrades))) {
			bw.write(String.join(",", HEADERS) + System.lineSeparator());
			for (int i = 1; i < lines.length; i++) {
				String[] line = lines[i].split(",");
				if (line[emailIdx].isEmpty() || line[statusIdx].equals("Missing")) {
					continue;
				}
				
				String[] fakeNames = getFakeNames(line, emailIdx, firstNameIdx, lastNameIdx, fullNameIdx);
				nextLine.clear();
				nextLine.add(fakeNames[1]);
				nextLine.add(fakeNames[2]);
				nextLine.add(fakeNames[0]);
				nextLine.add(line[gradeIdx]);
				bw.write(String.join(",", nextLine) + System.lineSeparator());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String[] getFakeNames(String[] line, int emailIdx, int firstNameIdx, int lastNameIdx, int fullNameIdx) {
		String onyen = line[emailIdx].substring(0, line[emailIdx].indexOf("@"));
		String fakeName = CommentsIdenMap.get(onyen);
		if (fakeName == null) {
			String fullName = firstNameIdx != -1 ? line[firstNameIdx] + " " + line[lastNameIdx] 
												 : line[fullNameIdx];
			boolean found = false;
			for (Entry<String, String> entry : nameToOnyen.entrySet()) {
				if (entry.getKey().contains(fullName)) {
					onyen = entry.getValue();
					fakeName = CommentsIdenMap.get(onyen);
					found = true;
					break;
				}
			}
			if (!found) {
				String fakeFirstName = faker.name().firstName();
				String fakeLastName = faker.name().lastName();
				String fakeOnyen = fakeFirstName + " " + fakeLastName + "?";
				newPairs.put(concat(onyen, fullName.substring(0, fullName.indexOf(" ")), fullName.substring(fullName.indexOf(" ")+1)), 
						concat(fakeOnyen, fakeFirstName, fakeLastName));
				fakeName = concat(fakeOnyen, fakeFirstName, fakeLastName);
			}
		}
		
		return fakeName.split(",");
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
