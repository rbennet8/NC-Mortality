import java.io.BufferedReader;
import java.io.FileReader;

public class Data {
	private static final String filePath = "C:\\Users\\rbenn\\JavaWorkspace\\MortalityProject\\src\\";
	public static final String[] files = { "NC2011.tsv", "NC2012.tsv", "NC2013.tsv", "NC2014.tsv", "NC2015.tsv",
			"NC2016.tsv", "NC2017.tsv", "NC2018.tsv" };
	public Object[][] data = new Object[200][19];
	public static Object[][] totalData = new Object[200][19];

	public Data(String fileName) throws Exception {
		BufferedReader readData = new BufferedReader(new FileReader(filePath + fileName));
		buildTable(readData);
		// print();
	}

	private void buildTable(BufferedReader file) throws Exception {
		data = emptyTable();
		String line = "";
		String[] lineEntries;
		boolean notFirst = false;
		int row = 0;
		while ((line = file.readLine()) != null) {
			if (notFirst) {
				lineEntries = line.split("\t");
				row = addLine(lineEntries, row);
			} else {
				notFirst = true;
			}
		}
		if (totalData[0][0] == null) {
			totalData = emptyTable();
		}
		addToTotal();
	}

	private Object[][] emptyTable() {
		Object[][] emptyTable = new Object[200][19];
		String[] raceAndSex = { "TOTAL", "W M", "W F", "W U", "B M", "B F", "B U", "O M", "O F", "O U" };
		int raceAndSexInc = 0;
		String[] causes = { "*** ALL CAUSES ***", "I. Certain infections and parasitic diseases", "II. Neoplasms",
				"III. Diseases of the blood and blood-forming organs and certain disorders involving the immune mechanism",
				"IV. Endocrine, nutritional and metabolic diseases",
				"V. Mental, Behavioral and Neurodevelopmental disorders", "VI. Diseases of the nervous system",
				"VII. Diseases of the eye and adnexa", "VIII. Diseases of the ear and mastoid process",
				"IX. Diseases of the circulatory system", "X. Diseases of the respiratory system",
				"XI. Diseases of the digestive system", "XII. Diseases of the skin and subcutaneous tissue",
				"XIII. Diseases of the musculoskeletal system and connective tissue",
				"XIV. Diseases of the genitourinary system", "XV. Pregnancy, childbirth and the puerperium",
				"XVI. Certain conditions originating in the perinatal period",
				"XVII. Congenital malformations, deformations and chromosomal abnormalities",
				"XVIII. Symptoms, signs and abnormal clinical and laboratory findings, NEC",
				"XIX. External causes of morbidity" };
		int causesInc = 0;
		for (int row = 0; row < emptyTable.length; row++) {
			if (raceAndSexInc >= raceAndSex.length) {
				raceAndSexInc = 0;
			}
			if (causesInc >= causes.length) {
				causesInc = 0;
			}
			for (int col = 0; col < emptyTable[col].length; col++) {
				if (col > 1) {
					emptyTable[row][col] = 0;
				} else if (col == 1) {
					emptyTable[row][col] = raceAndSex[raceAndSexInc];
					raceAndSexInc++;
				} else {
					emptyTable[row][col] = causes[causesInc];
				}
			}
			if (row > 0 & (row + 1) % 10 == 0) {
				causesInc++;
			}
		}

		return emptyTable;
	}

	private int addLine(String[] line, int row) {
		if (line[0].equals(data[row][0]) & line[1].equals(data[row][1])) {
			for (int col = 2; col < line.length; col++) {
				data[row][col] = Integer.parseInt(line[col]);
			}
			row++;
		} else {
			row = addLine(line, row + 1);
		}
		return row;
	}

	public void print() {
		for (Object[] x : totalData) {
			for (Object y : x) {
				System.out.print(y + " - ");
			}
			System.out.println();
		}
	}

	private void addToTotal() {
		for (int row = 0; row < totalData.length; row++) {
			for (int col = 2; col < totalData[row].length; col++) {
				System.out.println(totalData[row][col].toString());
				int t = ((Integer) totalData[row][col]).intValue();
				int d = ((Integer) data[row][col]).intValue();
				totalData[row][col] = Integer.sum(t, d);
				System.out.println(totalData[row][col].toString() + " - " + data[row][col].toString());
			}
		}
	}
}
