package anonymyzer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SakaiGradebookFaker extends GeneralFaker {
	final String[] HEADERS = { "First Name", "Last Name", "Onyen" };

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.err.println("Enter path to Sakai gradebook file");
			System.exit(1);
		}

		File gradebook = new File(args[0]);
		if (!gradebook.exists()) {
			System.err.println("File " + args[0] + "does not exist");
			System.exit(1);
		}
		try {
			DownloadNameMap.main(args);
			SakaiGradebookFaker faker = new SakaiGradebookFaker();
			if (!faker.setNameMapAndNameFile()) {
				System.exit(1);
			}
			faker.execute(gradebook);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			UpdateNameMap.main(args);
		}
	}

	public SakaiGradebookFaker() throws IOException {
		super();
	}

	@Override
	public void anonymize(Object arg) {
		if (!(arg instanceof File)) {
			return;
		}
		File gradebook = (File) arg;
		String[] lines = readFile(gradebook).toString().split("\\R");
		List<String> nextLine = new ArrayList<>();
		List<Integer> gradesIdx = new ArrayList<>();
		List<String> assignNames = new ArrayList<>();

		String[] firstLine = maybeUnquote(lines[0]).split("\",\"");
		int onyenIdx = -1;
		int nameIdx = -1;

		for (int i = 0; i < firstLine.length; i++) {
			if (firstLine[i].equals("Student ID")) {
				onyenIdx = i;
			}
			if (firstLine[i].equals("Name")) {
				nameIdx = i;
			}
			if (firstLine[i].contains("[") && firstLine[i].contains("]")) {
				gradesIdx.add(i);
				assignNames.add("\"" + firstLine[i] + "\"");
			}
		}
		if (onyenIdx == -1) {
			System.err.println("Cannot find onyen, aborting");
			return;
		}
		List<String> headers = new ArrayList<>(Arrays.asList(HEADERS));
		headers.addAll(assignNames);
		try (BufferedWriter bw = new BufferedWriter(
				new FileWriter(new File(gradebook.getParent(), gradebook.getName().replace(".csv", "Anon.csv"))))) {
			bw.write(String.join(",", headers) + System.lineSeparator());
			for (int i = 1; i < lines.length; i++) {
				String line2 = lines[i];
				int j = i+1;
				while (j < lines.length && (lines[j].isEmpty() || lines[j].startsWith("\",\"") || !lines[j].startsWith("\""))) {
					line2 += lines[j];
					j++;
				}
				i = j-1;
				line2 = line2.substring(1).replace(",,", ",\"\",").replace(",,", ",\"\",");
				if (line2.endsWith(",")) {
					line2 += "\"0";
				}
				String[] line = line2.split("\",\"");
				if (line[onyenIdx].isEmpty()) {
					continue;
				}
				nextLine.clear();
				try {
					String[] fakeNames = getFakeNames(line[onyenIdx], line[nameIdx]);
					nextLine.add(fakeNames[1]);
					nextLine.add(fakeNames[2]);
					nextLine.add(fakeNames[0]);
					for (int idx : gradesIdx) {
						String s = maybeUnquote(line[idx]);
						if (s.isEmpty()) {
							s = "0";
						}
						nextLine.add(s);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					e.printStackTrace();
					continue;
				}
				bw.write(String.join(",", nextLine) + System.lineSeparator());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String[] getFakeNames(String onyen, String name) {
		String fakeName = CommentsIdenMap.get(onyen);
		if (fakeName == null) {
			String[] names = name.split(", ");
			String fakeFirstName = faker.name().firstName();
			String fakeLastName = faker.name().lastName();
			String fakeOnyen = fakeFirstName + " " + fakeLastName + "?";
			fakeName = concat(fakeOnyen, fakeFirstName, fakeLastName);
			newPairs.put(concat(onyen, names[1], names[0]), fakeName);
		}

		return fakeName.split(",");
	}
}
