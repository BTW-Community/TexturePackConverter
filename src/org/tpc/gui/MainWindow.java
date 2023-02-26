package org.tpc.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.tpc.JSONReader;
import org.tpc.OptionsReader;
import org.tpc.SettingsReader;
import org.tpc.Version;

@SuppressWarnings({"serial", "unchecked", "rawtypes" })
public class MainWindow extends JFrame {

	/**
	 * Global main window reference. 
	 */
	public static MainWindow main;
	
	private JTextField inputText;
	private JTextField outputText;

	public boolean enabled = true;

	public static SettingsReader settings;
	public static ConsoleLogWindow consoleLog;
	public static AboutWindow aboutWindow;
	
	public static JButton optionsButton;
	
	public static JMenuItem consoleItem;
	
	public MainWindow() throws Exception {
		
		super("Main Window");
		
		main = this;
		
		setSize(650,350);
		setMinimumSize(new Dimension(650,350));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Texture Pack Converter " + Version.getCurrentVersion(true));
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(10, 10));
		setResizable(false);
		
		settings = new SettingsReader();		
		consoleLog = new ConsoleLogWindow();
		aboutWindow = new AboutWindow();
		


        JSeparator sep = new JSeparator();
        sep.setOrientation(SwingConstants.HORIZONTAL);
		
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		JMenu helpMenu = new JMenu("Help");

		consoleItem  = new JMenuItem("Show Console Log");
		JMenuItem exitItem  = new JMenuItem("Exit");
		
		JMenuItem aboutItem  = new JMenuItem("About");
				
		consoleItem.addActionListener(e -> openConsole());
		exitItem.addActionListener(e -> System.exit(EXIT_ON_CLOSE));
		aboutItem.addActionListener(e -> aboutWindow.setVisible(true));
		
		fileMenu.add(consoleItem);
		fileMenu.add(sep);
		fileMenu.add(exitItem);
		
		helpMenu.add(aboutItem);
		
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);
		
		setJMenuBar(menuBar);		
		
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		JPanel panel3 = new JPanel();
		JPanel panel4 = new JPanel();
		JPanel panel5 = new JPanel();
//		JPanel panel6 = new JPanel();
		
//		panel1.setBackground(Color.red);
//		panel2.setBackground(Color.green);
//		panel3.setBackground(Color.yellow);
//		panel4.setBackground(Color.magenta);
//		panel5.setBackground(Color.blue);
		
//		panel6.setBackground(Color.orange);
		
		panel1.setPreferredSize(new Dimension(100, 50));
		panel2.setPreferredSize(new Dimension(100, 100));
		panel3.setPreferredSize(new Dimension(50, 100));
		panel4.setPreferredSize(new Dimension(50, 100));
		panel5.setPreferredSize(new Dimension(100, 50));
//		panel6.setPreferredSize(new Dimension(200, 40));
		
		add(panel1, BorderLayout.NORTH);
		add(panel2, BorderLayout.SOUTH);
		add(panel3, BorderLayout.WEST);
		add(panel4, BorderLayout.EAST);
		add(panel5, BorderLayout.CENTER);
		
//		panel3.setLayout(new GridLayout(0, 1, 10, 15));
//		JButton b = new JButton("T");
//		b.setToolTipText("Test");
//		b.setBounds(0,0, 150, 50);
//		panel3.add(b);
//		
//		JButton b2 = new JButton("O");
//		b2.setBounds(0,0, 150, 50);
//		b2.setToolTipText("Options");
//		panel3.add(b2);
//		
//		JButton b3 = new JButton("A");
//		b3.setBounds(0,0, 150, 50);
//		b3.setToolTipText("About");
//		panel3.add(b3);
		
		JLabel label = new JLabel("Choose MC Version and Texture Pack:");
		label.setPreferredSize(new Dimension(500, 25));
		label.setHorizontalAlignment(JLabel.CENTER);
		panel5.add(label);
		
		final File folder = new File("./mappings/");
		String[] versions = listFilesForFolder(folder);
		
		JComboBox box = new JComboBox(versions);
		box.setToolTipText("Choose Mappings");
		box.setPreferredSize(new Dimension(75, 25));
		box.addActionListener(e -> {
			try {
				loadMapping(box.getSelectedItem().toString());
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		});
		loadMapping(box.getSelectedItem().toString());
		panel5.add(box);
		
		inputText = new JTextField();
		inputText.setPreferredSize(new Dimension(300, 25));
		panel5.add(inputText);
		
		JButton openButton = new JButton("...");
		openButton.setBounds(0,0, 100, 50);
		openButton.setToolTipText("Choose Zip File");
		openButton.addActionListener(e -> openDialoge());
		panel5.add(openButton);
		
		JLabel saveLabel = new JLabel("Choose export location and file name:");
		saveLabel.setPreferredSize(new Dimension(400, 25));
		saveLabel.setHorizontalAlignment(JLabel.CENTER);
		panel5.add(saveLabel);
		
		outputText = new JTextField();
		outputText.setPreferredSize(new Dimension(300, 25));
		panel5.add(outputText);
		
		panel2.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		
		JButton saveButton = new JButton("...");
		saveButton.setBounds(0,0, 100, 50);
		saveButton.setToolTipText("Choose Save Location");
		saveButton.addActionListener(e -> saveDialoge());
		panel5.add(saveButton);
		
		optionsButton = new JButton("Options");
		optionsButton.setBounds(0,0, 100, 50);
		openButton.setToolTipText("Options");
		optionsButton.setEnabled(true);
		optionsButton.addActionListener(e -> {
			try {
				optionsWindow();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		panel2.add(optionsButton);
		
		JButton exportButton = new JButton("Convert");
		exportButton.setBounds(0,0, 100, 50);
//		exportButton.addActionListener(e -> saveDialoge());
		panel2.add(exportButton);
		
//		JProgressBar bar = new JProgressBar(0, 100);
//		bar.setValue(50);
//		bar.setStringPainted(true);
//		panel2.add(bar);
		
		if(settings.shouldLogOpenOnStartUp() == true) { //settings.getPreferences().getBoolean("OPEN_CONSOLE_ON_START", true)){
			consoleLog.setVisible(true);
//			consoleItem.setEnabled(false);
		}
		
		pack();
		setVisible(true);
	}

	private void openConsole() {
		consoleLog.setVisible(true);
//		consoleItem.setEnabled(false);
	}

	private void loadMapping(String name) throws Exception
	{
		JSONReader reader = new JSONReader();
		reader.parse("mappings/" + name + ".json");
	}

	
	private void optionsWindow() throws Exception  {
			
			optionsButton.setEnabled(false);
			OptionsReader reader = new OptionsReader();
			new OptionsWindow(reader);
	}

	File inputFile;
	
	private void openDialoge() {
		JFileChooser fileChooser = new JFileChooser(".");
		int response = fileChooser.showOpenDialog(null);
		
		if (response == JFileChooser.APPROVE_OPTION)
		{
			inputFile = new File(fileChooser.getSelectedFile().getAbsolutePath());
			inputText.setText(inputFile.getAbsolutePath());			
		}
	}
	
	private void saveDialoge() {
		
		String loc = ".";
		
		if (inputText.getText().length() > 0)
		{
			loc = inputFile.getAbsolutePath();
		}
		
		JFileChooser fileChooser = new JFileChooser(loc);
		int response = fileChooser.showSaveDialog(null);
		
		if (response == JFileChooser.APPROVE_OPTION)
		{
			File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
			outputText.setText(file.getAbsolutePath());
		}
	}
	
	public String[] listFilesForFolder(final File folder) {
	    ArrayList<String> list = new ArrayList<String>();
		
		for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry);
	        } else {
	        	final int lastPeriodPos = fileEntry.getName().lastIndexOf('.');
	        	list.add(fileEntry.getName().substring(0, lastPeriodPos));
//	            System.out.println(fileEntry.getName());
	        }
	    }
		
		String[] arr = {};
		
		return list.toArray(arr);
	}

}
