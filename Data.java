import java.io.BufferedReader;
import java.io.FileReader;

public class Data {
	private static final String filePath = "C:\\Users\\rbenn\\JavaWorkspace\\MortalityProject\\src\\";

	// These 3 varaibles are accessed in the GUI class
	public static final String[] files = { "NC2011.tsv", "NC2012.tsv", "NC2013.tsv", "NC2014.tsv", "NC2015.tsv",
			"NC2016.tsv", "NC2017.tsv", "NC2018.tsv" };
	public Object[][] data = new Object[200][19];
	public static Object[][] totalData = new Object[200][19];

	// Since the process for building tables was so verbose, I broke it up into
	// separate methods, instead of housing all that in the constructor
	public Data(String fileName) throws Exception {
		if (totalData[0][0] == null) {
			totalData = emptyTable();
		}
		buildTable(fileName);
	}

	// Calls emptyTable to setup the 2D array, then fills all the appropriate
	// indices with values from the passed file
	private void buildTable(String fileName) throws Exception {
		BufferedReader readData = new BufferedReader(new FileReader(filePath + fileName));
		data = emptyTable();
		String line = "";
		String[] lineEntries;
		boolean notFirst = false; // I use this to skip the first line
		int row = 0;
		while ((line = readData.readLine()) != null) {
			if (notFirst) {
				lineEntries = line.split("\t");
				row = addLine(lineEntries, row);
			} else {
				notFirst = true;
			}
		}
		readData.close();
		addToTotal();
	}

	// This just creates a table full of zeros, to account for all the data the
	// files skipped, like male deaths during pregnancy/childbirth
	// I specifically wanted to to this, so all the tables would be the same size
	// and easily manageable
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
			// Since both of these have repeated values throughout the table, I use if
			// statements to check and reset them in necessary
			if (raceAndSexInc >= raceAndSex.length) {
				raceAndSexInc = 0;
			}
			if (causesInc >= causes.length) {
				causesInc = 0;
			}
			for (int col = 0; col < emptyTable[col].length; col++) {
				if (col > 1) {
					emptyTable[row][col] = 0.0;
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

	// Made this its own method for readability, since it got a little verbose
	private int addLine(String[] line, int row) {
		// Checking to see if the cause and race/sex indices match the line read in from
		// the file
		if (line[0].equals(data[row][0]) & line[1].equals(data[row][1])) {
			for (int col = 2; col < line.length; col++) {
				// Parse the values to a Double, so I can do normalization on them in the GUI
				// class and not have values truncated
				data[row][col] = Double.parseDouble(line[col]);
			}
			row++;
		} else {
			// Added this else statement, because some files were missing lines, as
			// mentioned in the comment for emptyTable
			// This just makes the method recursive, continuing to the next row, until it
			// finds the matching first and second indices
			row = addLine(line, row + 1);
		}
		return row;
	}

	// Adding the values from each table to the total table
	private void addToTotal() {
		for (int row = 0; row < totalData.length; row++) {
			for (int col = 2; col < totalData[row].length; col++) {
				Double t = ((Double) totalData[row][col]);
				Double d = ((Double) data[row][col]);
				totalData[row][col] = Double.sum(t, d);
			}
		}
	}
}