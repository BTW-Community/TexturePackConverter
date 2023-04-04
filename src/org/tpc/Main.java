package org.tpc;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.google.gson.*;
import org.tpc.gui.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Inet4Address;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class Main {
    public static LogWindow logWindow;
    public static AboutWindow aboutWindow;
    public static MainWindow mainWindow;
    public static BootWindow bootWindow;
    public static OptionsWindow optionsWindow;
    public static ConvertWindow convertWindow;
    public static Language lang;
    public static ImageIcon icon;
    public static Settings settings;

    public static UpdateWindow updateWindow;


    public static void main(String[] args) throws IOException {
        icon = Utils.getImage(Main.class, "/assets/tpc_icon.png");

        loadSettings();

        setLookAndFeel();

        lang = new Language(settings.getCurrentLanguage());

        logWindow = new LogWindow();

        Log.msg("Texture Pack Converter " + Version.getCurrentVersion(true));

        mainWindow = new MainWindow();

        aboutWindow = new AboutWindow();

        bootWindow = new BootWindow();
    }



    private static void setLookAndFeel() {
        if (settings.isDarkmode()){
            FlatDarkLaf.setup();
        }
        else FlatLightLaf.setup();
    }

    private static void loadSettings() throws IOException {
        settings = new Settings(
                "en_US",
                false,
                false,
                false,
                new ArrayList<String>(),
                new ArrayList<String>()
        );

        File f = new File("settings.json");
        if(!f.exists() && !f.isDirectory()) {
            //write new Settings JSON
            Settings.writeJSON(settings);
        }
        else if (f.exists() && !f.isDirectory())
        {
            //read Settings from JSON
            settings = Settings.readSettingsJSON("settings.json");
        }
    }
}