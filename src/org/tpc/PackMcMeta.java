package org.tpc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class PackMcMeta {
    Pack pack;

    public PackMcMeta(Pack pack)
    {
        this.pack = pack;
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

    public static class Pack {
        int pack_format;
        String description;
        public Pack(int pack_format,  String description)
        {
            this.pack_format = pack_format;
            this.description = description;
        }
    }
}
