package org.tpc;

import java.io.*;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class McMeta {

    Animation animation;

    public McMeta(Animation animation)
    {
        this.animation = animation;
    }


    public static void writeJSON(McMeta mcmeta, String filename) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        FileWriter writer = new FileWriter(filename);
        writer.write(gson.toJson(mcmeta));
        writer.close();
    }

    public static Settings readSettingsJSON(String jsonFile) throws FileNotFoundException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        BufferedReader bufferedReader = new BufferedReader(	new FileReader(jsonFile));

        Settings settings = gson.fromJson(bufferedReader, Settings.class);
        return settings;
    }

    public static class Animation {
        int frametime;
        int[] frames;
        public Animation(
                int frametime,
                int[] frames){
            this.frametime = frametime;
            this.frames = frames;
        }
    }
}
