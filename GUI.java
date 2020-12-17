import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class GUI extends JFrame {
	private static final long serialVersionUID = 4237738569124204876L;

	private final DefaultListModel<String> numbers = new DefaultListModel<String>();
	private final ArrayList<String> n = new ArrayList<String>(Arrays.asList("Raw Numbers", "Per 100", "Per 1,000"));
	private final DefaultListModel<String> cause = new DefaultListModel<String>();
	private final ArrayList<String> c = new ArrayList<String>(Arrays.asList("Total", "*** ALL CAUSES ***",
			"I. Certain infections and parasitic diseases", "II. Neoplasms",
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
			"XIX. External causes of morbidity"));
	private final DefaultListModel<String> years = new DefaultListModel<String>();
	private final ArrayList<String> y = new ArrayList<String>(
			Arrays.asList("Total", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018"));
	private final DefaultListModel<String> race = new DefaultListModel<String>();
	private final ArrayList<String> r = new ArrayList<String>(Arrays.asList("Total", "Black", "White", "Other"));
	private final DefaultListModel<String> sex = new DefaultListModel<String>();
	private final ArrayList<String> s = new ArrayList<String>(Arrays.asList("All", "Male", "Female", "Unknown"));

	private final String[] colNames = { "Causes of death", " Race and sex", "Total", "1 day", "1 week", "28 days",
			"1 year", "1 to 4 years", "5 to 9 years", "10 to 14 years", "15 to 19 years", "20 to 24 years",
			"25 to 34 years", "35 to 44 years", "45 to 54 years", "55 to 64 years", "65 to 74 years", "75 to 84 years",
			"85 years and over" };

	private final Data data2011;
	private final Data data2012;
	private final Data data2013;
	private final Data data2014;
	private final Data data2015;
	private final Data data2016;
	private final Data data2017;
	private final Data data2018;

	private final JScrollPane numbersPane;
	private final JScrollPane causePane;
	private final JScrollPane yearsPane;
	private final JScrollPane sexPane;
	private final JScrollPane racePane;
	private final JScrollPane tableScroll;

	private DefaultTableModel tableModel;

	private final JList numbersList;
	private final JList causeList;
	private final JList yearsList;
	private final JList sexList;
	private final JList raceList;

	private final JButton submit;
	private final JButton reset;
	private final JButton clearSelection;

	private final JTable tableView;
	private final TableRowSorter<TableModel> rowSorter;

	private final JPanel center;
	private final JPanel east;
	private final JPanel west;
	private final JPanel west2;

	public GUI(String title) throws Exception {
		super(title);

		this.numbersList = makeLists(numbers, n);
		this.causeList = makeLists(cause, c);
		this.yearsList = makeLists(years, y);
		this.sexList = makeLists(sex, s);
		this.raceList = makeLists(race, r);

		this.data2011 = new Data(Data.files[0]);
		this.data2012 = new Data(Data.files[1]);
		this.data2013 = new Data(Data.files[2]);
		this.data2014 = new Data(Data.files[3]);
		this.data2015 = new Data(Data.files[4]);
		this.data2016 = new Data(Data.files[5]);
		this.data2017 = new Data(Data.files[6]);
		this.data2018 = new Data(Data.files[7]);

		this.tableModel = new DefaultTableModel(Data.totalData, colNames) {
			private static final long serialVersionUID = 8569577698067869058L;

			// Setting the data types for each column
			@Override
			public Class getColumnClass(int column) {
				if (column < 2)
					return String.class;
				else
					return Double.class;
			}
		};

		setMinimumSize(new Dimension(1600, 900));
		setLocation(150, 75);
		setSize(1600, 900);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		setJMenuBar(myMenuBar());

		this.numbersPane = new JScrollPane(numbersList);
		this.causePane = new JScrollPane(causeList);
		this.yearsPane = new JScrollPane(yearsList);
		this.sexPane = new JScrollPane(sexList);
		this.racePane = new JScrollPane(raceList);

		this.submit = new JButton("Submit");
		this.reset = new JButton("Reset");
		this.clearSelection = new JButton("Clear Selection");

		// Adding a row sorter to the table, so columns can be sorted
		this.tableView = new JTable(tableModel);
		this.rowSorter = new TableRowSorter<TableModel>(tableView.getModel());
		this.tableView.setRowSorter(rowSorter);

		this.tableScroll = new JScrollPane(tableView);
		this.tableScroll.setPreferredSize(new Dimension(1025, 825));

		this.center = new JPanel();
		this.center.add(tableScroll);

		this.east = new JPanel();
		this.east.setPreferredSize(new Dimension(150, 0));
		this.east.setLayout(new BoxLayout(east, BoxLayout.Y_AXIS));
		this.east.add(Box.createVerticalStrut(20));
		this.east.add(new JLabel("Display (Choose One):"));
		this.east.add(numbersPane);
		this.east.add(Box.createVerticalStrut(20));
		this.east.add(new JLabel("Years (Choose One):"));
		this.east.add(yearsPane);
		this.east.add(Box.createVerticalStrut(20));
		this.east.add(new JLabel("Races:"));
		this.east.add(racePane);
		this.east.add(Box.createVerticalStrut(20));
		this.east.add(new JLabel("Sexes:"));
		this.east.add(sexPane);
		this.east.add(Box.createVerticalStrut(30));

		this.west = new JPanel();
		this.west.setPreferredSize(new Dimension(400, 0));
		this.west.setLayout(new BoxLayout(west, BoxLayout.Y_AXIS));
		this.west.add(Box.createVerticalStrut(10));
		this.west.add(new JLabel("Causes:"));
		this.west.add(causePane);

		this.west2 = new JPanel();
		this.west2.setLayout(new BoxLayout(west2, BoxLayout.X_AXIS));
		this.west2.add(Box.createVerticalStrut(20));
		this.west2.add(submit);
		this.west2.add(Box.createVerticalStrut(20));
		this.west2.add(reset);
		this.west2.add(Box.createVerticalStrut(20));
		this.west2.add(clearSelection);
		this.west2.add(Box.createVerticalStrut(200));
		this.west.add(west2);

		getContentPane().add(center, BorderLayout.CENTER);
		getContentPane().add(east, BorderLayout.WEST);
		getContentPane().add(west, BorderLayout.EAST);

		// Have this set to disabled at first, since Total is selected for race, which
		// would include every sex
		this.sexList.setEnabled(false);

		setupListListener();
		setupButtonListeners();
		setVisible(true);
	}

	// Setting up menu bar
	private JMenuBar myMenuBar() {
		JMenuBar myMenu = new JMenuBar();
		JMenu file = new JMenu("File");
		myMenu.add(file);
		JMenuItem save = new JMenuItem("Save Table");
		file.add(save);

		JMenu info = new JMenu("Info");
		myMenu.add(info);
		JMenuItem dataSource = new JMenuItem("Data Source");
		info.add(dataSource);
		JMenuItem myInfo = new JMenuItem("My Information");
		info.add(myInfo);

		ActionListener menuActions = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (ae.getSource().equals(save)) {
					saveResult();
				} else if (ae.getSource().equals(dataSource)) {
					try {
						showDataAndInfo(1);
					} catch (Exception ex) {
						System.out.println("Couldn't output data source.");
						ex.printStackTrace();
					}
				} else {
					try {
						showDataAndInfo(2);
					} catch (Exception ex) {
						System.out.println("Couldn't output info.");
						ex.printStackTrace();
					}
				}
			}
		};
		save.addActionListener(menuActions);
		dataSource.addActionListener(menuActions);
		myInfo.addActionListener(menuActions);

		return myMenu;
	}

	// Saving a tab delimited version of the table to a text file
	private void saveResult() {
		JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
			return;
		if (fileChooser.getSelectedFile() == null)
			return;

		File chosenFile = fileChooser.getSelectedFile();
		if (fileChooser.getSelectedFile().exists()) {
			String message = "File " + fileChooser.getSelectedFile().getName()
					+ " exists. Would you like to overwrite?";
			if (JOptionPane.showConfirmDialog(this, message) != JOptionPane.YES_OPTION)
				return;
		}

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(chosenFile));
			for (int row = 0; row < tableModel.getRowCount(); row++) {
				for (int col = 0; col < tableModel.getColumnCount(); col++) {
					writer.write(tableModel.getValueAt(row, col).toString());
					if (col < tableModel.getColumnCount() - 1) {
						writer.write("\t");
					}
				}
				writer.write("\n");
			}
			writer.flush();
			writer.close();
		} catch (Exception ex) {
			System.out.println("Could not write this file.");
			ex.printStackTrace();
		}
	}

	// Displaying message windows with my data source and my information, depending
	// on the value passed by the menu option chosen
	private void showDataAndInfo(int i) throws Exception {
		if (i == 1) {
			String message = "My data is from the North Carolina State Center for Health Statistics."
					+ "\nhttps://schs.dph.ncdhhs.gov/ - Detailed Mortality Statistics";
			JOptionPane.showMessageDialog(this, message);
		} else {
			String message = "Name: Robert Bennett\nSchool: UNC Charlotte\nProgram: Department of Bioinformatics and Genomics"
					+ "\nYear: Second year Masters\nProject: Final assignment for BINF 6380";
			JOptionPane.showMessageDialog(this, message);
		}
	}

	// Takes JLists with the declared lists and depending on their size, sets
	// certain lists to single options only
	private JList<String> makeLists(DefaultListModel<String> list, ArrayList<String> strings) {
		for (String s : strings)
			list.addElement(s);

		JList<String> returnList = new JList<String>(list);

		if (strings.size() == 3 || strings.size() == 9)
			returnList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		else
			returnList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		returnList.setSelectedIndex(0);

		return returnList;
	}

	// Setting up list listener for race total, so it disables sex list when only
	// total is selected and enables sex list when either total isn't selected or
	// isn't the only selection in race, since total is its own value that includes
	// all sexes
	private void setupListListener() {
		ListSelectionListener listListener = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (raceList.getSelectedIndex() == 0 & raceList.getSelectedValuesList().size() == 1) {
					sexList.setEnabled(false);
				} else {
					sexList.setEnabled(true);
				}
			}
		};
		this.raceList.addListSelectionListener(listListener);
	}

	// Setting up button listeners
	private void setupButtonListeners() {
		ActionListener buttonListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) { // Sorts data
				if (ae.getSource() == submit) {
					yearsActions();
					numbersActions();
					causeActions();
					raceActions();
					sexActions();
					submit.setEnabled(false);
					reset.setEnabled(true);
				} else if (ae.getSource() == reset) { // Resets table
					setTable(Data.totalData);
					submit.setEnabled(true);
					reset.setEnabled(false);
				} else { // Resets all selections to 0
					causeList.setSelectedIndex(0);
					numbersList.setSelectedIndex(0);
					raceList.setSelectedIndex(0);
					yearsList.setSelectedIndex(0);
					sexList.setSelectedIndex(0);
				}
			}
		};
		this.submit.addActionListener(buttonListener);
		this.reset.addActionListener(buttonListener);
		this.clearSelection.addActionListener(buttonListener);
	}

	// Replaces the data in the table with the data from each year (or total),
	// depending on the user's choice
	private void yearsActions() {
		int year = yearsList.getSelectedIndex();
		switch (year) {
		case 0:
			setTable(Data.totalData);
			break;
		case 1:
			setTable(data2011.data);
			break;
		case 2:
			setTable(data2012.data);
			break;
		case 3:
			setTable(data2013.data);
			break;
		case 4:
			setTable(data2014.data);
			break;
		case 5:
			setTable(data2015.data);
			break;
		case 6:
			setTable(data2016.data);
			break;
		case 7:
			setTable(data2017.data);
			break;
		case 8:
			setTable(data2018.data);
			break;
		}
	}

	// Does the math to find deaths per 100 or 1000
	private void numbersActions() {
		if (numbersList.getSelectedIndex() == 1) {
			Double totalDiv = (Double) tableModel.getValueAt(0, 2) / 100.0;
			for (int row = 0; row < tableModel.getRowCount(); row++) {
				for (int col = 2; col < tableModel.getColumnCount(); col++) {
					this.tableModel.setValueAt((((Double) tableModel.getValueAt(row, col)) / totalDiv), row, col);
				}
			}
		} else if (numbersList.getSelectedIndex() == 2) {
			Double totalDiv = (Double) tableModel.getValueAt(0, 2) / 1000.0;
			for (int row = 0; row < tableModel.getRowCount(); row++) {
				for (int col = 2; col < tableModel.getColumnCount(); col++) {
					this.tableModel.setValueAt((((Double) tableModel.getValueAt(row, col)) / totalDiv), row, col);
				}
			}
		}
	}

	// Removes rows that don't match the chosen causes
	private void causeActions() {
		// If total or nothing is selected, then it exits the method and defaults to the
		// total table that is already being used
		if (causeList.getSelectedIndex() == -1 || causeList.getSelectedIndex() == 0) {
			return;
		} else {
			// Simply iterating through the table with a for loop didn't work, I had to
			// calculate the accurate number of lines and wrap it in a while loop that
			// continued running the for loop, until the correct amount of lines were
			// reached
			int numSelection = causeList.getSelectedIndices().length * 10;
			while (tableModel.getRowCount() > numSelection) {
				for (int row = 0; row < tableModel.getRowCount(); row++) {
					String causeAtRow = tableModel.getValueAt(row, 0).toString();
					if (!(causeList.getSelectedValuesList().contains(causeAtRow))) {
						this.tableModel.removeRow(row);
					}
				}
			}
		}
	}

	// Removes rows that don't match the chosen race options
	private void raceActions() {
		int totalRows;
		char character;
		// If total is the only option selected, then only account for one index per
		// cause of death
		if (raceList.getSelectedValuesList().contains("Total") & raceList.getSelectedValuesList().size() == 1) {
			totalRows = tableModel.getRowCount() / 10;
			while (tableModel.getRowCount() > totalRows) {
				for (int row = 0; row < tableModel.getRowCount(); row++) {
					character = tableModel.getValueAt(row, 1).toString().charAt(0);
					if (tableModel.getValueAt(row, 1).toString().charAt(0) != 'T') {
						this.tableModel.removeRow(row);
					}
				}
			}
		} else {
			// Otherwise, account for the number of causes chosen
			int causeSize;
			// If/else block that does the preliminary math to setup the correct number of
			// rows, depending on choices made
			// If total causes is chosen, then account for all causes, otherwise get the
			// number of selected causes
			if (causeList.getSelectedValuesList().contains("Total")) {
				causeSize = causeList.getLastVisibleIndex();
				totalRows = causeSize * 3;
			} else {
				causeSize = causeList.getSelectedValuesList().size();
				totalRows = causeSize * 3;
			}
			// If/else block that does the last half of the calculation for correct number
			// of rows, depending on whether or not total race was selected
			// If so, subtract one selected races values to account for it and add the
			// number of causes chosen, otherwise multiply by number of races chosen
			if (raceList.getSelectedValuesList().contains("Total")) {
				totalRows = totalRows * (raceList.getSelectedValuesList().size() - 1) + causeSize;
			} else {
				totalRows = totalRows * raceList.getSelectedValuesList().size();
			}
			// Creates list that contains the first character of the chosen options, so it
			// can be used to check the indices of the table and remove rows
			ArrayList<String> values = new ArrayList<String>();
			for (Object item : raceList.getSelectedValuesList()) {
				values.add(String.valueOf(item.toString().charAt(0)));
			}
			// If the list doesn't contain the character at the first index of the checked
			// cell, then remove it
			while (tableModel.getRowCount() > totalRows) {
				for (int row = 0; row < tableModel.getRowCount(); row++) {
					character = tableModel.getValueAt(row, 1).toString().charAt(0);
					if (!(values.contains(String.valueOf(character)))) {
						this.tableModel.removeRow(row);
					}
				}
			}
		}
	}

	// Removes rows based on what sexes are chosen
	private void sexActions() {
		// If all is selected, exit method
		if (sexList.getSelectedValuesList().contains("All")) {
			return;
		} else if (sexList.isEnabled()) {
			// Creates list that contains the first character of the chosen options, so it
			// can be used to check the indices of the table and remove rows
			ArrayList<String> values = new ArrayList<String>();
			for (Object item : sexList.getSelectedValuesList()) {
				values.add(String.valueOf(item.toString().charAt(0)));
			}

			int totalRows = tableModel.getRowCount() / 3 * sexList.getSelectedValuesList().size();
			// If/else block that finishes the calculation for correct number of rows,
			// depending on whether total was selected in race or cause lists
			if (raceList.getSelectedValuesList().contains("Total")
					& causeList.getSelectedValuesList().contains("Total")) {
				totalRows += causeList.getLastVisibleIndex();
			} else if (raceList.getSelectedValuesList().contains("Total")) {
				totalRows += causeList.getSelectedValuesList().size();
			}

			while (tableModel.getRowCount() > totalRows) {
				for (int row = 0; row < tableModel.getRowCount(); row++) {
					char character = tableModel.getValueAt(row, 1).toString().charAt(2);
					// If total is selected in race and current cell being checked is TOTAL, do
					// nothing and leave it in the table
					// Otherwise, if list does not contain the character from the cell being
					// checked, remove it
					if (raceList.getSelectedValuesList().contains("Total") & character == 'T') {
					} else if (!(values.contains(String.valueOf(character)))) {
						this.tableModel.removeRow(row);
					}
				}
			}
		}
	}

	// Clears the current table and adds everything from the passed table
	// Used to switch between different years/total tables
	private void setTable(Object[][] passedTable) {
		while (tableModel.getRowCount() > 0) {
			for (int row = 0; row < tableModel.getRowCount(); row++) {
				this.tableModel.removeRow(row);
			}
		}
		for (int row = 0; row < Data.totalData.length; row++) {
			this.tableModel.addRow(passedTable[row]);
		}
	}
}