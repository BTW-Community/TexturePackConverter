package org.tpc;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.io.*;

public class JsonHelper {

    public static JsonObject getMcMeta(String file, String map) throws FileNotFoundException {
        File input = new File(file);
        JsonElement fileElement = JsonParser.parseReader(new FileReader(input));
        JsonObject fileObject = fileElement.getAsJsonObject();
        JsonObject infoObj = fileObject.get(map).getAsJsonObject();


        return infoObj;
    }

    public static JsonObject getMappings(String file,String map, String folderOrTextures) throws FileNotFoundException {
        File input = new File(file);
        JsonElement fileElement = JsonParser.parseReader(new FileReader(input));
        JsonObject fileObject = fileElement.getAsJsonObject();
        JsonObject infoObj = fileObject.get(map).getAsJsonObject();
        JsonObject locObj = infoObj.get(folderOrTextures).getAsJsonObject();

        return locObj;
    }

    public static JsonObject getOptions(File file) throws FileNotFoundException {
        JsonElement fileElement = JsonParser.parseReader(new FileReader(file));
        JsonObject fileObject = fileElement.getAsJsonObject();

        return fileObject;
    }
    public static JsonElement getOption(String file) throws FileNotFoundException {
        File input = new File(file);
        JsonElement fileElement = JsonParser.parseReader(new FileReader(input));

        return fileElement;
    }

}
