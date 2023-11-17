package org.tpc;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

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

    public static JsonObject getMappings(String file,String map) throws FileNotFoundException {
        File input = new File(file);
        JsonElement fileElement = JsonParser.parseReader(new FileReader(input));
        JsonObject fileObject = fileElement.getAsJsonObject();
        JsonObject infoObj = fileObject.get(map).getAsJsonObject();

        return infoObj;
    }

    public static JsonObject getOptions(File file) throws FileNotFoundException {
        JsonElement fileElement = JsonParser.parseReader(new FileReader(file));
        JsonObject fileObject = fileElement.getAsJsonObject();

        return fileObject;
    }

    public static void setOptions(File configFile, String conf, String option, boolean boo) throws IOException, NoSuchFieldException {
        JsonObject options = getOptions(configFile);
        JsonObject vanilla = options.get(conf).getAsJsonObject();

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();

        JsonElement element = new JsonPrimitive(boo);
        vanilla.asMap().put(option, element);

        FileWriter writer = new FileWriter(configFile);
        writer.write(gson.toJson(options));
        writer.close();

        Log.msg("Option: \"" + option + "\" in: " + conf + " set to: " + boo);
    }

}
