package org.tpc;

import org.tpc.gui.LogWindow;
import org.tpc.gui.MainWindow;
import org.tpc.gui.OptionsWindow;
import org.tpc.gui.UpdateWindow;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Language {
    private static String currentLanguage;
    private static TreeMap<String, String> languageList;
    static Properties table = new Properties();

    public static TreeMap<String, String> getLanguages(){
        return languageList;
    }

    public Language(String lang){
        currentLanguage = lang;
        loadLanguageList();
        setLanguage(lang);
    }

    public static void updateTextFields() {
        //the following need to be updated when language is changed, as the window has already been created
        MainWindow.fileMenu.setText(getString("menu.file"));
        MainWindow.consoleItem.setText(getString("menu.console") + "...");
        MainWindow.langMenu.setText(getString("menu.lang") + "...");
        MainWindow.appearanceMenu.setText(getString("menu.appearance") + "...");
        MainWindow.lightItem.setText(getString("menu.appearance.light"));
        MainWindow.darkItem.setText(getString("menu.appearance.dark"));
        MainWindow.exitItem.setText(getString("menu.exit"));

        MainWindow.helpMenu.setText(getString("menu.help"));
        MainWindow.readmeItem.setText(getString("menu.readme") + "...");
        MainWindow.helpItem.setText(getString("menu.guide") + "...");
        MainWindow.updateItem.setText(getString("menu.updates") + "...");
        MainWindow.githubItem.setText(getString("menu.github") + "...");
        MainWindow.aboutItem.setText(getString("menu.about") + "...");

        Color color = Main.settings.isDarkmode() ? Color.lightGray : Color.black;

        MainWindow.inputPanel.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null),
                " " + getString("main.input") + " ", TitledBorder.CENTER, TitledBorder.TOP, null,
                color));
        MainWindow.inputMapping.setToolTipText(getString("main.input.mapping"));
        MainWindow.openButton.setToolTipText(getString("main.input.open"));

        MainWindow.outputPanel.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null),
                " " + getString("main.output") + " ", TitledBorder.CENTER, TitledBorder.TOP, null,
                color));
        MainWindow.outputMapping.setToolTipText(getString("main.output.mapping"));
        MainWindow.saveButton.setToolTipText(getString("main.output.save"));

        MainWindow.optionsBtn.setText(getString("main.options"));
        MainWindow.convertBtn.setText(getString("main.convert"));

        Main.logWindow.setTitle(getString("title.console"));
        LogWindow.openOnStart.setText(getString("log.startup"));

        if (Main.optionsWindow != null)
        {
            Main.optionsWindow.setTitle(getString("title.options"));

            for(Map.Entry entry : OptionsWindow.enableMap.entrySet())
            {
                JButton button = (JButton) entry.getValue();
                button.setText(Language.getString("options.toggle"));
            }
        }

        if(Main.updateWindow != null)
        {
            Main.updateWindow.setTitle(getString("title.update"));

            UpdateWindow.button.setText(getString("update.download"));
            UpdateWindow.openOnStart.setText(getString("update.startup"));

            int updateStatus = UpdateWindow.updateStatus;

            if (updateStatus == UpdateWindow.UPTODATE)
            {
                UpdateWindow.label.setText(getString("✔ " + getString("update.uptodate")));
            }
            else if (updateStatus == UpdateWindow.FOUND)
            {
                UpdateWindow.label.setText(getString("update.found") + ": " + UpdateWindow.versionFound);
            }
            else {
                UpdateWindow.label.setText(getString("update.check"));
            }

        }

    }

    public static void setLanguage(String lang) {
        currentLanguage = lang;

        Properties properties = new Properties();

        loadLanguage(properties, lang);

        table = properties;
    }

    private static void loadLanguage(Properties properties, String lang){
        if (languageList.containsKey(lang))
        {
            BufferedReader br = null;
            String line;

            try{
                //br = new BufferedReader(new FileReader("lang/" + lang + ".lang"));
                br = Utils.getFile(Language.class,"/lang/" + lang + ".lang");

                while ((line = br.readLine()) != null)
                {
                    if (line.startsWith("#"))
                    {
                        continue;
                    }

                    String[] var = line.trim().split("=");

                    if (var.length == 2)
                    {
                        properties.setProperty(var[0], var[1]);
                    }
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void loadLanguageList(){

        languageList = new TreeMap<>();

        BufferedReader br = null;
        String line;

        try{
            //br = new BufferedReader(new FileReader("lang/languages.txt"));
            br = Utils.getFile(Language.class, "/lang/languages.txt");
            while ((line = br.readLine()) != null)
            {
                if (line.startsWith("#"))
                {
                    continue;
                }

                String[] var = line.trim().split("=");

                if (var.length == 2)
                {
                    languageList.put(var[0], var[1]);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isCurrentLang(String s) {
        return currentLanguage.equalsIgnoreCase(s);
    }

    public static String getString(String string)
    {
        return table.getProperty(string, string);
    }
}
