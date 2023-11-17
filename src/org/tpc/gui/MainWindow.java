package org.tpc.gui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import org.tpc.*;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

public class MainWindow extends DefaultWindow {
    ArrayList<JMenuItem> languages;
    ArrayList<JMenuItem> themes;
    private JMenuBar menuBar;

    public static JMenuItem lightItem;

    public static JMenuItem darkItem;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JPanel midPanel;

    public static JComboBox inputMapping;
    public static JComboBox inputLocation;

    public static JComboBox outputMapping;
    public static JComboBox outputLocation;

    public static JButton optionsBtn;
    public static JButton convertBtn;
    public static File inputMap;
    public static File outputMap;
    public static File inputFile;
    public static File outputFile;

    public static JPanel inputPanel;
    public static String inputText;
    public static JPanel outputPanel;
    public static String outputText;
    public static File config;
    public static JButton openButton;
    public static JButton saveButton;

    public MainWindow() {
        super("Texture Pack Converter", 600,300,false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    @Override
    public void windowOpened(WindowEvent e) {
        Main.bootWindow.dispose();

        Main.logWindow.setVisible(Main.settings.shouldOpenLogOnStartUp());
        if (Main.settings.shouldCheckUpdateOnStartUp()) new UpdateWindow();
    }


    @Override
    protected void addContent() {
        addMenuBar();
        addCenterPanels();

        Color color = Main.settings.isDarkmode() ? Color.lightGray : Color.black;

        // --- CENTER PANEL --- //

        northPanel.setPreferredSize(new Dimension(100,45));
        centerPanel.setLayout(new BorderLayout(0, 0));

        topPanel.setPreferredSize(new Dimension(100, 75));
        midPanel.setPreferredSize(new Dimension(100, 75));
        bottomPanel.setPreferredSize(new Dimension(100, 50));

        centerPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(midPanel, BorderLayout.CENTER);
        centerPanel.add(bottomPanel, BorderLayout.SOUTH);


        // --- INPUT --- //
        inputText = Language.getString("main.input");
        inputPanel = new JPanel(new BorderLayout(10,10));
        inputPanel.setPreferredSize(new Dimension(525, 50));
        inputPanel.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null),
                " " + inputText + " ", TitledBorder.CENTER, TitledBorder.TOP, null,
                color));
        //inputPanel.setBackground(Color.orange);
        topPanel.add(inputPanel);



        final File folder = new File("mappings/");
        ArrayList<String> versions = listFilesForFolder(folder);

        int margin = 5;


        inputMapping = new JComboBox();
        for(String s : versions)
        {
            inputMapping.addItem(s);
        }
        inputMapping.setToolTipText(Language.getString("main.input.mapping"));
        inputMapping.setPreferredSize(new Dimension(75, 25));
        inputMapping.addActionListener(e -> {
            inputMap = new File ("mappings/" + inputMapping.getSelectedItem().toString() + ".json");
            Log.msg("Input mappings \"" + inputMap.getName() + "\" selected.");

            config = new File("configs/" + inputMap.getName());
            Log.msg("Loaded Config: " + config.getPath());

            if (Main.optionsWindow != null)
            {
                Main.optionsWindow.dispose();
            }

            optionsBtn.setEnabled(true);

        });

        inputMap = new File ("mappings/" + inputMapping.getSelectedItem().toString() + ".json");
        Log.msg("Input mappings \"" + inputMap.getName() + "\" selected.");

        //dropdownInput.setBorder(new EmptyBorder(margin, margin, margin, margin));

        String selectedFile = new File("mappings/" + inputMapping.getSelectedItem().toString() + ".json").getPath();

        inputLocation = new JComboBox();
        inputLocation.setPreferredSize(new Dimension(350, 25));
        inputLocation.setEditable(true);
        //inputText.setBorder(new EmptyBorder(margin, margin, margin, margin));

        if (Main.settings.getFiles_input().size() > 0)
        {
            for(String input : Main.settings.getFiles_input())
            {
                inputLocation.addItem(input);
            }
        }
        inputLocation.setSelectedItem(null);

        openButton = new JButton("...");
        openButton.setBounds(0,0, 75, 25);
        openButton.setToolTipText(Language.getString("main.input.open") + "...");
        openButton.addActionListener(e -> openDialog());
        //openButton.setBorder(new EmptyBorder(margin, margin, margin, margin));

        inputPanel.add(inputMapping, BorderLayout.WEST);
        inputPanel.add(inputLocation, BorderLayout.CENTER);
        inputPanel.add(openButton, BorderLayout.EAST);

        // --- OUTPUT --- //
        outputText = Language.getString("main.output");
        outputPanel = new JPanel(new BorderLayout(10,10));
        outputPanel.setPreferredSize(new Dimension(525, 50));
        outputPanel.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null),
                " " + outputText + " ", TitledBorder.CENTER, TitledBorder.TOP, null,
                color));
        //outputPanel.setBackground(Color.orange);

        midPanel.add(outputPanel);

        outputMapping = new JComboBox(versions.toArray());
        outputMapping.setToolTipText(Language.getString("main.output.mapping"));
        outputMapping.setPreferredSize(new Dimension(75, 25));
        outputMapping.addActionListener(e -> {
            outputMap = new File ("mappings/" + outputMapping.getSelectedItem().toString() + ".json");
            Log.msg("Output mappings \"" + outputMap.getName() + "\" selected.");
        });
        outputMapping.setSelectedItem(0);

        outputMap = new File ("mappings/" + outputMapping.getSelectedItem().toString() + ".json");
        Log.msg("Output mappings \"" + outputMap.getName() + "\" selected.");

        outputLocation = new JComboBox();
        outputLocation.setPreferredSize(new Dimension(350, 25));
        outputLocation.setEditable(true);
        if (Main.settings.getFiles_output().size() > 0)
        {
            for(String output : Main.settings.getFiles_output())
            {
                outputLocation.addItem(output);
            }
        }
        outputLocation.setSelectedItem(null);

        saveButton = new JButton("...");
        saveButton.setBounds(0,0, 75, 25);
        saveButton.setToolTipText(Language.getString("main.output.save") + "...");
        saveButton.addActionListener(e -> saveDialog());

        outputPanel.add(outputMapping, BorderLayout.WEST);
        outputPanel.add(outputLocation, BorderLayout.CENTER);
        outputPanel.add(saveButton, BorderLayout.EAST);

        // --- BUTTONS --- //

        JPanel btnPanel = new JPanel(new BorderLayout(5,5));
        //inputPanel.setPreferredSize(new Dimension(700, 200));
        //inputPanel.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null),
        //        "Choose MC Version and Texture Pack", TitledBorder.CENTER, TitledBorder.TOP, null,
        //        new Color(0, 0, 0)));
        //btnPanel.setBackground(Color.orange);

        bottomPanel.add(btnPanel);

        optionsBtn = new JButton();
        optionsBtn.setText(Language.getString("main.options"));
        optionsBtn.setVerticalAlignment(JButton.CENTER);
        optionsBtn.addActionListener(e ->
        {
            Main.optionsWindow = new OptionsWindow();
            optionsBtn.setEnabled(false);
        });
        btnPanel.add(optionsBtn, BorderLayout.WEST);

        convertBtn = new JButton();
        convertBtn.setText(Language.getString("main.convert"));
        convertBtn.setVerticalAlignment(JButton.CENTER);
        convertBtn.addActionListener(e -> checkFiles());
        btnPanel.add(convertBtn, BorderLayout.EAST);

        inputMap = new File(inputMapping.getItemAt(0).toString());

        config = new File("configs/" + inputMap + ".json");
        Log.msg("Loaded Config: " + config.getPath());


    }

    private void addCenterPanels() {
        topPanel = new JPanel();
        bottomPanel = new JPanel();
        midPanel = new JPanel();

        if (debug)
        {
            topPanel.setBackground(new Color(0x880000));
            bottomPanel.setBackground(new Color(0x008800));
            midPanel.setBackground(new Color(0x000088));
        }

        topPanel.setPreferredSize(new Dimension(100, 125));
        bottomPanel.setPreferredSize(new Dimension(100, 50));
        midPanel.setPreferredSize(new Dimension(100, 125));

        centerPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(bottomPanel, BorderLayout.SOUTH);
        centerPanel.add(midPanel, BorderLayout.CENTER);
    }

    public static JMenu fileMenu;
    public static JMenu helpMenu;
    public static JMenuItem consoleItem;
    public static JMenu langMenu;
    public static JMenu appearanceMenu;
    public static JMenuItem exitItem;
    public static JMenuItem updateItem;
    public static JMenuItem readmeItem;
    public static JMenuItem helpItem;
    public static JMenuItem githubItem;
    public static JMenuItem aboutItem;
    int DARK = 0;
    int LIGHT = 1;
    private void addMenuBar() {
        menuBar = new JMenuBar();

        fileMenu = new JMenu(Language.getString("menu.file"));
        //fileMenu.setMnemonic(KeyEvent.VK_F);

        helpMenu = new JMenu(Language.getString("menu.help"));
        //helpMenu.setMnemonic(KeyEvent.VK_H);

        consoleItem = new JMenuItem(Language.getString("menu.console") + "...");
        consoleItem.addActionListener(e -> Main.logWindow.setVisible(true));
        setIcon(consoleItem, "log");

        langMenu = new JMenu(Language.getString("menu.lang") + "...");
        //setIcon(langMenu, "lang");

        appearanceMenu = new JMenu(Language.getString("menu.appearance") + "...");

        exitItem = new JMenuItem(Language.getString("menu.exit"));
        exitItem.addActionListener(e -> System.exit(0));
        setIcon(exitItem, "exit");

        updateItem = new JMenuItem(Language.getString("menu.updates") + "...");
        updateItem.addActionListener(e -> openUpdateWindow());
        setIcon(updateItem, "update");

        readmeItem = new JMenuItem(Language.getString("menu.readme") + "...");
        readmeItem.addActionListener(e -> Utils.openSite("https://github.com/BTW-Community/TexturePackConverter/blob/main/README.md"));

        aboutItem = new JMenuItem(Language.getString("menu.about") + "...");
        aboutItem.addActionListener(e -> Main.aboutWindow.setVisible(true));

        helpItem = new JMenuItem(Language.getString("menu.guide") + "...");
        setIcon(helpItem, "help");

        helpItem.addActionListener(e -> Utils.openSite("https://github.com/BTW-Community/TexturePackConverter/blob/main/FORMATTING.md"));
        githubItem = new JMenuItem(Language.getString("menu.github") + "...");
        githubItem.addActionListener(e -> Utils.openSite("https://github.com/BTW-Community/TexturePackConverter/"));
        setIcon(githubItem, "github");

        languages = new ArrayList<JMenuItem>();
        TreeMap<String, String> list = Language.getLanguages();

        for(String str : list.keySet())
        {
            JMenuItem lang = new JMenuItem(list.get(str));
            lang.setEnabled(!Main.lang.isCurrentLang(str));
            lang.addActionListener(e -> onLanguageChanged(str, lang));
            if (!lang.isEnabled()) setIcon(lang, "check");
            langMenu.add(lang);
            languages.add(lang);
        }

        lightItem = new JMenuItem(Language.getString("menu.appearance.light"));
        lightItem.setEnabled(Main.settings.isDarkmode());
        lightItem.addActionListener(e -> onAppearanceChanged(lightItem, LIGHT));
        if (!lightItem.isEnabled()) setIcon(lightItem, "check");
        appearanceMenu.add(lightItem);

        darkItem = new JMenuItem(Language.getString("menu.appearance.dark"));
        darkItem.setEnabled(!Main.settings.isDarkmode());
        darkItem.addActionListener(e -> onAppearanceChanged(darkItem, DARK));
        if (!darkItem.isEnabled()) setIcon(darkItem, "check");
        appearanceMenu.add(darkItem);

        themes = new ArrayList<JMenuItem>();
        themes.add(lightItem);
        themes.add(darkItem);


        fileMenu.add(consoleItem);
        fileMenu.addSeparator();
        fileMenu.add(langMenu);
        fileMenu.add(appearanceMenu);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        helpMenu.add(readmeItem);
        helpMenu.add(helpItem);
        helpMenu.addSeparator();
        helpMenu.add(updateItem);
        helpMenu.addSeparator();
        helpMenu.add(githubItem);
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void onAppearanceChanged(JMenuItem item, int mode) {
        boolean darkmode = mode == DARK;

        try {
            if (mode == DARK)
            {
                UIManager.setLookAndFeel(FlatDarkLaf.class.getName());
            }
            else  UIManager.setLookAndFeel(FlatLightLaf.class.getName());

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }

        FlatLaf.updateUI();

        try {
            Main.settings.setDarkmode(darkmode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for(JMenuItem menu : themes) {
            menu.setEnabled(true);
            menu.setIcon(null);
        }
        if (item == lightItem)
        {
            item.setEnabled(Main.settings.isDarkmode());
            setIcon(item, "check");
        }
        else if (item == darkItem)
        {
            item.setEnabled(!Main.settings.isDarkmode());
            setIcon(item, "check");
        }

        updateIcons();

        Color color = Main.settings.isDarkmode() ? Color.lightGray : Color.black;

        inputPanel.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null),
                " " + Language.getString("main.input") + " ", TitledBorder.CENTER, TitledBorder.TOP, null,
                color));

        outputPanel.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null),
                " " + Language.getString("main.output") + " ", TitledBorder.CENTER, TitledBorder.TOP, null,
                color));
    }

    private void updateIcons() {
        setIcon(consoleItem, "log");
        setIcon(exitItem, "exit");
        setIcon(helpItem, "help");
        setIcon(updateItem, "update");
        setIcon(githubItem, "github");

        for(JMenuItem item : languages) {
            if (item.getIcon() != null)
            {
                setIcon(item, "check");
            }
        }

        for(JMenuItem item : themes) {
            if (item.getIcon() != null)
            {
                setIcon(item, "check");
            }
        }

    }

    private void openUpdateWindow() {
        Main.updateWindow = new UpdateWindow();
        updateItem.setEnabled(false);
    }

    private void onLanguageChanged(String lang, JMenuItem item) {
        Main.lang.setLanguage(lang);
        try {
            Main.settings.setCurrentLanguage(lang);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for(JMenuItem langItem : languages) {
            langItem.setEnabled(true);
            langItem.setIcon(null);
        }
        item.setEnabled(!Main.lang.isCurrentLang(lang));
        if (!item.isEnabled()) setIcon(item, "check");

        Language.updateTextFields();
    }

    public void setIcon(JMenuItem menu, String image) {
        String mode = Main.settings.isDarkmode() ? "dark" : "light";
        ImageIcon icon = Utils.getImage(Main.class, "/assets/" + image + "_" + mode + ".png");

        menu.setIcon(icon);
    }

    private void openDialog() {
        JFileChooser fileChooser = new JFileChooser("./");
    	FileNameExtensionFilter filter = new FileNameExtensionFilter("Zip Files", "zip");
    	fileChooser.setFileFilter(filter);

        int response = fileChooser.showOpenDialog(null);

        if (response == JFileChooser.APPROVE_OPTION)
        {
            inputFile = new File(fileChooser.getSelectedFile().getAbsolutePath());
            inputLocation.setSelectedItem(inputFile);

            try {
                addInputFile(inputFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void addInputFile(File inputFile) throws IOException {
        if (Main.settings.addInputFile(inputFile.getAbsolutePath()))
        {
            inputLocation.insertItemAt(inputFile, 0);
            if (this.inputLocation.getItemCount() > 5)
            {
                this.inputLocation.removeItemAt(5);
            }

        }
    }

    private void saveDialog() {

        String loc = "./";

        JFileChooser fileChooser = new JFileChooser(loc);

        int response = fileChooser.showSaveDialog(null);

        if (response == JFileChooser.APPROVE_OPTION)
        {
            outputFile = new File(fileChooser.getSelectedFile().getAbsolutePath());
            try {
                addOutputFile(outputFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            outputLocation.setSelectedItem(outputFile);
        }
    }
    private void addOutputFile(File file) throws IOException {
        if (Main.settings.addOutputFile(file.getAbsolutePath()))
        {
            outputLocation.insertItemAt(file, 0);
            if (this.outputLocation.getItemCount() > 5)
            {
                this.outputLocation.removeItemAt(5);
            }
        }
    }

    private void checkFiles() {

        Object input = inputLocation.getSelectedItem();

        if (inputLocation.getSelectedItem() == null ||  input.toString().length() == 0 )
        {
            showMessageDialog(Language.getString("dialog.file.input"), Language.getString("dialog.file.input.title"));
        }
        else {
            Object output = outputLocation.getSelectedItem();
            File inputFile = new File(input.toString());
            File outputFile = null;

            if (!inputFile.exists())
            {
                showMessageDialog(Language.getString("dialog.file.notfound"), Language.getString("dialog.file.input.title"));
            }
            else {
                if (!inputFile.getName().contains(".zip"))
                {
                    showMessageDialog(Language.getString("dialog.file.nozip"), Language.getString("dialog.file.input.title"));
                }
                else {

                    if (output == null) {
                        showMessageDialog(Language.getString("dialog.file.output"), Language.getString("dialog.file.output.title"));
                    }
                    else {
                        outputFile = new File(output.toString());
                        //check output
                        if (outputFile.exists())
                        {
                            //Override name of output zip?
                            int override = JOptionPane.showOptionDialog(this,
                                    Language.getString("dialog.file.override"),
                                    Language.getString("dialog.file.override.title"),
                                    JOptionPane.OK_CANCEL_OPTION,
                                    JOptionPane.WARNING_MESSAGE,
                                    null,
                                    new Object[] {Language.getString("dialog.yes"), Language.getString("dialog.cancel")},
                                    Language.getString("dialog.cancel"));

                            Log.debug("Override Response: " + override, true);

                            if (override == 0) //Yes
                            {
                                checkMappings();
                            }


                        }
                        else {
                            //success
                            checkMappings();
                        }
                    }
                }
            }
        }
    }
    private void showMessageDialog(String msg, String title) {
        JOptionPane.showMessageDialog(this,
                msg,
                title,
                JOptionPane.ERROR_MESSAGE);
    }

    private void checkMappings() {
        Object in = inputMapping.getSelectedItem();
        Object out = outputMapping.getSelectedItem();

        String input = in.toString();
        String output = out.toString();

        if (input.equalsIgnoreCase(output) || output.equalsIgnoreCase(input))
        {
            int response = JOptionPane.showOptionDialog(this,
                    Language.getString("dialog.mapping"),
                    Language.getString("dialog.mapping.title"),
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    new Object[] { Language.getString("dialog.yes"),  Language.getString("dialog.cancel")},
                    Language.getString("dialog.cancel"));

            Log.msg("Mapping Response: " + response);

            if (response == 0) //Yes
            {
                openConvertWindow();
            }
        }
        else{
            openConvertWindow(); //??
        }
    }

    private void openConvertWindow() {
       Main.convertWindow = new ConvertWindow();
       Main.mainWindow.convertBtn.setEnabled(false);
    }

    public ArrayList<String> listFilesForFolder(final File folder) {
        ArrayList<String> list = new ArrayList<String>();

        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                final int lastPeriodPos = fileEntry.getName().lastIndexOf('.');
                list.add(fileEntry.getName().substring(0, lastPeriodPos));
            }
        }
        return list;
    }

    public static File getInputMap() {
        return inputMap;
    }

    public static File getOutputMap() {
        return outputMap;
    }

    public static File getConfigFile() {
        return config;
    }
}
