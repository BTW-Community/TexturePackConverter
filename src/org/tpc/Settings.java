package org.tpc;

import java.io.*;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Settings {

    private String currentLanguage;
    private boolean darkmode;
    private boolean startUp_openLog;

    private boolean startUp_checkUpdate;

    private ArrayList<String> files_input;
    private ArrayList<String> files_output;

    public Settings(
            String currentLanguage,
            boolean darkmode,
            boolean startUp_openLog,
            boolean startUp_checkUpdate,
            ArrayList<String> files_input,
            ArrayList<String> files_output)
    {
        this.currentLanguage = currentLanguage;
        this.startUp_openLog = startUp_openLog;
        this.startUp_checkUpdate = startUp_checkUpdate;
        this.files_input = files_input;
        this.files_output = files_output;
    }

    public void setDarkmode(boolean darkmode) throws IOException {
        this.darkmode = darkmode;

        writeJSON(this);

        String mode = darkmode ? "Dark" : "Light";

        Log.msg("Settings: Set Theme to: " + mode + " Mode");

    }

    public boolean isDarkmode() {
        return darkmode;
    }

    public void setCurrentLanguage(String currentLanguage) throws IOException {
        this.currentLanguage = currentLanguage;

        writeJSON(this);

        Log.msg("Settings: Set Language to: " + Main.lang.getLanguages().get(currentLanguage));
    }

    public String getCurrentLanguage() {
        return currentLanguage;
    }

    public void setCheckUpdateOnStartUp(boolean boo) throws IOException, NoSuchFieldException, SecurityException {

        this.startUp_checkUpdate = boo;

        writeJSON(this);

        Log.msg("Settings: \"Check Updates on Startup\" set to: " + this.startUp_checkUpdate );
    }

    public boolean shouldCheckUpdateOnStartUp() {
        return startUp_checkUpdate;

    }

    public ArrayList<String> getFiles_input() {
        return files_input;
    }

    public boolean addInputFile(String file) throws IOException {

        if (!this.files_input.contains(file))
        {
            this.files_input.add(0, file);

            if (this.files_input.size() > 5)
            {
                this.files_input.remove(files_input.size() - 1);
            }

            writeJSON(this);

            Log.msg("Settings: Added new file to: \"files_input\"." );

            return true;
        }
        else {
            return false;
        }
    }

    public ArrayList<String> getFiles_output() {
        return files_output;
    }

    public boolean addOutputFile(String file) throws IOException {
        if (!this.files_output.contains(file))
        {
            this.files_output.add(0, file);

            if (this.files_output.size() > 5)
            {
                this.files_output.remove(files_output.size() - 1);
            }

            writeJSON(this);

            Log.msg("Settings: Added new file to: \"files_output\"." );

            return true;
        }
        else {
            return false;
        }
    }

    public boolean shouldOpenLogOnStartUp() {
        return startUp_openLog;
    }

    public void setOpenLogOnStartUp(boolean boo) throws IOException, NoSuchFieldException, SecurityException {

        this.startUp_openLog = boo;

        writeJSON(this);

        Log.msg("Settings: \"Open Console on Startup\" set to: " + this.startUp_openLog);

    }

    public static void writeJSON(Settings settings) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        FileWriter writer = new FileWriter("settings.json");
        writer.write(gson.toJson(settings));
        writer.close();
    }

    public static Settings readSettingsJSON(String jsonFile) throws FileNotFoundException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        BufferedReader bufferedReader = new BufferedReader(	new FileReader(jsonFile));

        Settings settings = gson.fromJson(bufferedReader, Settings.class);
        return settings;
    }
}
