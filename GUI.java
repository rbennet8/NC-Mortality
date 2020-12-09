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

public class GUI extends JFrame {
	private static final long serialVersionUID = 4237738569124204876L;

	private final Data data2011;
	private final Data data2012;
	private final Data data2013;
	private final Data data2014;
	private final Data data2015;
	private final Data data2016;
	private final Data data2017;
	private final Data data2018;

	private final DefaultListModel<String> numbers = new DefaultListModel<String>();
	private final ArrayList<String> n = new ArrayList<String>(Arrays.asList("Raw Numbers", "Per 100", "Per 1,000"));
	private final DefaultListModel<String> cause = new DefaultListModel<String>();
	private final ArrayList<String> c = new ArrayList<String>(Arrays.asList("Total",
			"Certain infections/parasitic diseases", "Neoplasms",
			"Diseases of the blood/blood-forming organs and certain disorders involving immune mechanisms",
			"Endocrine, nutritional, and metabolic diseases", "Mental, Behavioral, and Neurodevelopmental disorders",
			"Diseases of the nervous system", "Diseases of the eye/adnexa", "Diseases of the ear and mastoid process",
			"Diseases of the circulatory system", "Diseases of the respiratory system",
			"Diseases of the digestive system", "Diseases of the skin/subcutaneous tissue",
			"Diseases of the musculoskeletal system/connective tissue", "Diseases of the genitourinary system",
			"Pregnancy, childbirth, and the puerperium", "Certain conditions originating in the perinatal period",
			"Congenital malformations, deformations, and chromosomal abnormalities",
			"Symptoms, signs, and abnormal clinical/laboratory findings, NEC", "External causes of morbidity"));
	private final DefaultListModel<String> years = new DefaultListModel<String>();
	private final ArrayList<String> y = new ArrayList<String>(
			Arrays.asList("Total", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018"));
	private final DefaultListModel<String> age = new DefaultListModel<String>();
	private final ArrayList<String> a = new ArrayList<String>(
			Arrays.asList("Total", "1 day", "1 week", "28 days", "1 year", "1 to 4 years", "5 to 9 years",
					"10 to 14 years", "15 to 19 years", "20 to 24 years", "25 to 34 years", "35 to 44 years",
					"45 to 54 years", "55 to 64 years", "65 to 74 years", "75 to 84 years", "85 years and over"));
	private final DefaultListModel<String> sex = new DefaultListModel<String>();
	private final ArrayList<String> s = new ArrayList<String>(Arrays.asList("Total", "Male", "Female", "Unknown"));
	private final DefaultListModel<String> race = new DefaultListModel<String>();
	private final ArrayList<String> r = new ArrayList<String>(Arrays.asList("Total", "Black", "White", "Other"));
	private final String[] colNames = { "Causes of death", " Race and sex", "Total", "1 day", "1 week", "28 days",
			"1 year", "1 to 4 years", "5 to 9 years", "10 to 14 years", "15 to 19 years", "20 to 24 years",
			"25 to 34 years", "35 to 44 years", "45 to 54 years", "55 to 64 years", "65 to 74 years", "75 to 84 years",
			"85 years and over" };

	public GUI(String title) throws Exception {
		super(title);

		this.data2011 = new Data(Data.files[0]);
		this.data2012 = new Data(Data.files[1]);
		this.data2013 = new Data(Data.files[2]);
		this.data2014 = new Data(Data.files[3]);
		this.data2015 = new Data(Data.files[4]);
		this.data2016 = new Data(Data.files[5]);
		this.data2017 = new Data(Data.files[6]);
		this.data2018 = new Data(Data.files[7]);

		setMinimumSize(new Dimension(1600, 900));
		setLocation(150, 75);
		setSize(1600, 900);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		setJMenuBar(myMenuBar());

		JScrollPane numbersPane = new JScrollPane(makeLists(numbers, n));
		JScrollPane causePane = new JScrollPane(makeLists(cause, c));
		JScrollPane yearsPane = new JScrollPane(makeLists(years, y));
		JScrollPane agePane = new JScrollPane(makeLists(age, a));
		JScrollPane sexPane = new JScrollPane(makeLists(sex, s));
		JScrollPane racePane = new JScrollPane(makeLists(race, r));

		JTable tableView = new JTable(Data.totalData, colNames);

		JScrollPane tableScroll = new JScrollPane(tableView);
		tableScroll.setPreferredSize(new Dimension(1025, 825));

		JPanel center = new JPanel();
		center.add(tableScroll);

		JPanel east = new JPanel();
		east.setPreferredSize(new Dimension(150, 0));
		east.setLayout(new BoxLayout(east, BoxLayout.Y_AXIS));
		east.add(Box.createVerticalStrut(25));
		east.add(new JLabel("Display (Choose One):"));
		east.add(numbersPane);
		east.add(Box.createVerticalStrut(20));
		east.add(new JLabel("Years:"));
		east.add(yearsPane);
		east.add(Box.createVerticalStrut(20));
		east.add(new JLabel("Sexes:"));
		east.add(sexPane);
		east.add(Box.createVerticalStrut(20));
		east.add(new JLabel("Races:"));
		east.add(racePane);
		east.add(Box.createVerticalStrut(25));

		JPanel west = new JPanel();
		west.setPreferredSize(new Dimension(400, 0));
		west.setLayout(new BoxLayout(west, BoxLayout.Y_AXIS));
		west.add(Box.createVerticalStrut(20));
		west.add(new JLabel("Ages:"));
		west.add(agePane);
		west.add(Box.createVerticalStrut(10));
		west.add(new JLabel("Causes:"));
		west.add(causePane);
		west.add(Box.createVerticalStrut(20));

		getContentPane().add(center, BorderLayout.CENTER);
		getContentPane().add(east, BorderLayout.WEST);
		getContentPane().add(west, BorderLayout.EAST);

		setupListeners();
		setVisible(true);
	}

	private JMenuBar myMenuBar() {
		JMenuBar myMenu = new JMenuBar();
		JMenu file = new JMenu("File");
		myMenu.add(file);
		JMenuItem save = new JMenuItem("Save");
		file.add(save);

		JMenu info = new JMenu("Info");
		myMenu.add(info);
		JMenuItem dataSource = new JMenuItem("Data Source");
		info.add(dataSource);
		JMenuItem myInfo = new JMenuItem("My Information");
		info.add(myInfo);

		/*
		 * save.addActionListener(new ActionListener() { public void
		 * actionPerformed(ActionEvent e) { - - - Move this to a new method that setups
		 * up each/all menu item(s) saveResult(); } });
		 */

		return myMenu;
	}

	private JList<String> makeLists(DefaultListModel<String> list, ArrayList<String> strings) {
		for (String s : strings) {
			list.addElement(s);
		}

		JList<String> returnList = new JList<String>(list);
		if (strings.size() == 3) {
			returnList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		} else {
			returnList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		}
		returnList.setSelectedIndex(0);

		return returnList;
	}

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
			// writer.write(something something something);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			System.out.println("Could not write this file.");
			e.printStackTrace();
		}
	}

	private void setupListeners() {
		/*
		 * ActionListener buttonListener = new ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent ae) { if (ae.getSource() ==
		 * someButton) { Code } else if (ae.getSource() == someButton2) { Code } else {
		 * Code } } }; forward.addActionListener(buttonListener);
		 * backward.addActionListener(buttonListener);
		 * choose.addActionListener(buttonListener);
		 */
	}
}
