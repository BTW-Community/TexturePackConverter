package org.tpc;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.tpc.gui.ConvertWindow;
import org.tpc.gui.MainWindow;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Converter extends SwingWorker {
    private final ConvertWindow window;
    public Converter (ConvertWindow window) throws FileNotFoundException {
        this.window = window;

    }

    @Override
    protected void done() {
        if (isCancelled()) {return;}

        if (!error)
        {
            ConvertWindow.label.setText(Language.getString("converter.done"));
            Log.success("Done");

            window.onDone();
        }
    }

    @Override
    protected void process(List string) {
        String received = (String) string.get(0);
        ConvertWindow.label.setText(received + " ...");
    }

    private boolean error = false;

    @Override
    protected Integer doInBackground() throws Exception {
        try {
            Log.debug("test1", true);
            //Create /temp/ folder
            createTempDir();

            Log.debug("test2", true);
            //delete contents of /temp/
            deleteTempFolder();

            Log.debug("test3", true);
            File inputFile = new File( MainWindow.inputLocation.getSelectedItem().toString() );

            if (inputFile.getName().contains(".zip"))
            {
                //Unzip
                unzip();
            }
            else if (inputFile.isDirectory())
            {
                unzippedDir = new File(inputFile.getAbsolutePath());
                Log.warning("File is a directory. Skipping Unzipping...");
                Log.warning("Using: " + unzippedDir.getPath());
            }

            Log.debug("test4", true);
            //Create output dir
            createOutputDir();

            Log.debug("test5", true);
            //Convert
            convert();

            Log.debug("test6", true);
            //Create zip
            zip();

            Log.debug("test7", true);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            ConvertWindow.label.setText(Language.getString("converter.error"));
            Log.error("An error occurred whilst converting: "+e.getMessage());
            error = true;
            return null;
        }
    }

    private void zip() throws FileNotFoundException {
        File inputMapping =  MainWindow.getInputMap();
        //File inputMapping = new File("mappings/" + MainWindow.inputMap.getName() + ".json");

        File configFile = MainWindow.getConfigFile();
        //File configFile = new File("configs/" + MainWindow.inputMap.getName() + ".json");

        JsonObject options = JsonHelper.getOptions(configFile).get("options").getAsJsonObject();
        boolean createZip = options.get("options.createZip").getAsBoolean();

        if (createZip)
        {
            List<File> files = Utils.getFilesForFolder(new File("temp/"+ exportDir.getName() +"/"));

            publish("zipping");

            try {
                Log.msg("Zipping ...");
                ZipHelper.zip(files,"temp/" + exportDir.getName() + ".zip");
                Log.success("Finished zipping!");
            } catch (IOException e) {
                Log.error("Didn't succeed in zipping!");
                throw new RuntimeException(e);
            }
        }
    }

    private void deleteTempFolder() throws FileNotFoundException {
        //File inputMapping = new File("mappings/" + MainWindow.inputMap.getName());
        File configFile = new File(MainWindow.config.getPath());

        JsonObject options = JsonHelper.getOptions(configFile).get("options").getAsJsonObject();
        boolean cleanTMP = options.get("options.cleanTMP").getAsBoolean();

        if (cleanTMP)
        {
            publish("Deleting Contents of /temp/");
            deleteFolder(new File("temp/"));
        }
    }

    private void createTempDir() {
        File temp = new File("temp");
        if (!temp.exists())
        {
            publish("Creating /temp/ dir");
            Log.msg("Creating temp dir");
            temp.mkdirs();
        }
    }

    private void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if(files!=null) { //some JVMs return null for empty dirs
            for(File f: files) {
                if(f.isDirectory() && !f.getName().equalsIgnoreCase("temp")) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

    private void convert() throws IOException {

        File inputMapping =  MainWindow.getInputMap();
        File outputMapping = MainWindow.getOutputMap();
        File configFile = MainWindow.getConfigFile();

        JsonObject configObj = null;
        JsonArray configs = null;
        JsonObject configOptions = null;

        try {
            configObj = JsonHelper.getOptions(configFile);
            configs = configObj.get("configs").getAsJsonArray();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        for (JsonElement conf : configs) {
            String configName = conf.getAsString();

            configOptions =  configObj.get(configName).getAsJsonObject();

            for(Map.Entry entry : configOptions.asMap().entrySet())
            {
                String optName = entry.getKey().toString();
                JsonPrimitive primitive = (JsonPrimitive) entry.getValue();
                Boolean optValue = primitive.getAsBoolean();

                Log.msg("Config " + "\"" + configName + "/" + optName + "\" = " + optValue);
                publish("Converting " + optName);
                if (optValue)
                {
                    //get input mapping json
                    JsonObject inputDir = JsonHelper.getMappings(inputMapping.getPath(), configName, "directory");
                    JsonElement inputPack = inputDir.get(optName);
                    String inputTextureDir = inputPack.getAsString();

                    JsonObject inputFiles = JsonHelper.getMappings(inputMapping.getPath(), configName, "files");
                    JsonArray inputTextureFiles = inputFiles.get(optName).getAsJsonArray();

                    //get output mapping json
                    JsonObject outputDir = JsonHelper.getMappings(outputMapping.getPath(), configName, "directory");
                    JsonElement outputPack = outputDir.get(optName);
                    String outputTextureDir = outputPack.getAsString();

                    JsonObject outputFiles = JsonHelper.getMappings(outputMapping.getPath(), configName, "files");
                    JsonArray outputTextureFiles = outputFiles.get(optName).getAsJsonArray();

                    //System.out.println(outputTextureFiles.size());

                    for(int j = 0; j < inputTextureFiles.size(); j++) {
                        publish("Converting " + optName + " " + j + "/" + inputTextureFiles.size());
                        JsonElement inputElement = inputTextureFiles.get(j);
                        JsonArray inputTexture = inputElement.getAsJsonArray();
                        String inputTextureName = inputTexture.get(0).getAsString();

                        JsonElement outputElement = outputTextureFiles.get(j);
                        JsonArray outputTexture = outputElement.getAsJsonArray();
                        String outputTextureName = outputTexture.get(0).getAsString();

                        String inputFormat = null;
                        String outputFormat = null;

                        if (outputTexture.size() > 1)
                        {
                            for (JsonElement outputObj : outputTexture) {
                                if (outputObj.getAsString().contains("format")) {
                                    String[] var = outputObj.getAsString().trim().split("=");

                                    if (var[0].equalsIgnoreCase("format")) {
                                        outputFormat = (String) var[1];
                                    }
                                }
                            }
                        }

                        if (outputFormat == null) outputFormat = "png";

                        if (inputTexture.size() > 1)
                        {
                            int index = 0;
                            for (JsonElement inputObj : inputTexture)
                            {
                                if (inputObj.isJsonPrimitive() && inputObj.getAsJsonPrimitive().isString())
                                {
                                    if (inputObj.toString().contains("texture") || inputObj.toString().contains("format"))
                                    {
                                        JsonElement arr = inputTexture.get(index);
                                        String[] var = arr.getAsString().trim().split("=");

                                        if (var[0].contains("format")) {
                                            inputFormat = var[1];
                                        }



                                        if (var[0].contains("texture")) {

                                            String texture = var[1];

                                            File file = new File(unzippedDir + inputTextureDir + texture + "." + inputFormat);
                                            if (file.exists()){
                                                inputTextureName = texture;
                                                Log.warning("Using alternative Texture: " + inputTextureName + "." + inputFormat);
                                            }
                                            else {
                                                Log.warning("Couldn't find alternative Texture: " + texture + "." + inputFormat + " Skipping ...");
                                            }
                                        }
                                    }


                                }
                                index++;
                            }
                        }

                        if (inputFormat == null) inputFormat = "png";

                        File inputFile = new File(unzippedDir + inputTextureDir + inputTextureName + "." + inputFormat);
                        File outputFile = new File(exportDir + outputTextureDir + outputTextureName + "." + outputFormat);

                        Log.msg(inputFile.getAbsolutePath() + " >>> " + outputFile.getAbsolutePath(), Color.GRAY);

                        //Copy Files
                        if (!inputFile.exists()) {
                            Log.warning("Source File: " + inputFile.getName() + " doesn't exist! Skipping ...");
                        }
                        else {
                            if (inputFormat.equalsIgnoreCase("png.mcmeta") && outputFormat.equalsIgnoreCase("txt"))
                            {
                                if (inputTextureName.equalsIgnoreCase("pack"))
                                {
                                    convertToPackTxt(inputFile, outputFile);
                                }
                                else convertToTxt(inputFile, outputFile);
                            }
                            else if (inputFormat.equalsIgnoreCase("txt") && outputFormat.equalsIgnoreCase("png.mcmeta") )
                            {
                                if (inputTextureName.equalsIgnoreCase("pack"))
                                {
                                    convertToPackMcMeta(inputFile, outputFile);
                                }
                                else convertToMcMeta(inputFile, outputFile);
                            }
                            else if (inputFormat.equalsIgnoreCase("mcmeta") && outputFormat.equalsIgnoreCase("txt"))
                            {
                                if (inputTextureName.equalsIgnoreCase("pack"))
                                {
                                    convertToPackTxt(inputFile, outputFile);
                                }
                                else Log.error("Unknown filename. Expected \"pack\"! Skipping ...");
                            }
                            else if (inputFormat.equalsIgnoreCase("txt") && outputFormat.equalsIgnoreCase("mcmeta") )
                            {
                                if (inputTextureName.equalsIgnoreCase("pack"))
                                {
                                    convertToPackMcMeta(inputFile, outputFile);
                                }
                                else Log.error("Unknown filename. Expected \"pack\"! Skipping ...");
                            }
                            else if (inputFormat.equals(outputFormat))
                            {
                                CopyFiles(inputFile, outputFile);
                            }
                            else
                            {
                                Log.error("Unknown Format! Skipping ...");
                            }
                        }

                        int index = 0;
                        for (JsonElement inputObj : inputTexture)
                        {
                            if (inputObj.isJsonArray()) {
                                for (JsonElement modifier : inputObj.getAsJsonArray()) {

                                    String[] var = modifier.getAsString().trim().split("=");

                                    String mod = var[0];
                                    String value = var[1];
                                    ApplyModifiers(mod, value, inputTextureDir, outputTextureDir, inputTextureName, outputTextureName, inputFormat, outputFormat);
                                }
                            }
                            index++;
                        }
                    }
                }
                publish("Converting " + optName);
            }
        }
    }

    private void ApplyModifiers(String modifier, String value, String inputTextureDir, String outputTextureDir, String inputTextureName, String outputTextureName, String inputFormat, String outputFormat ) throws IOException {
        File inputFile = new File(unzippedDir + inputTextureDir + inputTextureName + "." + inputFormat);
        File outputFile = new File(exportDir + outputTextureDir + outputTextureName + "." + outputFormat);

        String imagePath = null;
        BufferedImage myPicture = null;

        if (inputFormat.equalsIgnoreCase("png") && outputFormat.equalsIgnoreCase("png"))
        {
            imagePath = outputFile.getAbsolutePath();

            try(FileInputStream instream = new FileInputStream(imagePath)){
                myPicture = ImageIO.read(instream);

                if (myPicture != null) {

                    if (modifier.equalsIgnoreCase("tint"))
                    {
                        Log.msg("Tinting: " + outputFile.getName());
                        float tint_strength = 1.0F;
                        if (isColor(value))
                        {
                            int hexcolor = getColor(value);
                            myPicture = Utils.tintImage(myPicture, hexcolor);
                        }
                        else {
                            int hexcolor = Integer.parseInt(value, 16);
                            myPicture = Utils.tintImage(myPicture, hexcolor);
                        }
                    }
                    else if (modifier.equalsIgnoreCase("invert"))
                    {
                        Log.msg("Inverting Colors: " + outputFile.getName());
                        myPicture = Utils.invertColors(myPicture);
                    }
                    else if (modifier.equalsIgnoreCase("rotate"))
                    {
                        Log.msg("Rotating: " + outputFile.getName());
                        myPicture = Utils.rotateImage(myPicture, Integer.parseInt(value));
                    }
                    else if (modifier.equalsIgnoreCase("mirror"))
                    {
                        Log.msg("Mirroring " + outputFile.getName());
                        myPicture = Utils.mirrorImage(myPicture);
                    }
                    else if (modifier.equalsIgnoreCase("overlay"))
                    {
                        File overlayFile = new File(unzippedDir + inputTextureDir + value + "." + inputFormat);

                        try(FileInputStream instreamOverlay = new FileInputStream(overlayFile)) {
                            BufferedImage overlayImg = ImageIO.read(instreamOverlay);

                            if (overlayImg != null) {
                                Log.msg("Overlaying " + outputFile.getName());
                                myPicture = Utils.overlayImage(myPicture, overlayImg);
                            }
                        } catch (IOException e) {
                            Log.error("Couldn't overlay Image: " + overlayFile.getName() + " on to " + outputFile.getName());
                            throw new RuntimeException(e);
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            FileOutputStream outstream = null;

            try {
                outstream = new FileOutputStream(imagePath);
                ImageIO.write(myPicture, outputFormat, outstream);

                outstream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            outstream.close();

        }
    }

    private static boolean isColor(String tint){
        return getColor(tint) != -1;
    }
    private static int getColor(String tint){
        switch (tint)
        {
            default:
                return -1;
            case "white":
            case "WHITE":
                return 0xffffff;
            case "orange":
            case "ORANGE":
                return 0xf17716;
            case "magenta":
            case "MAGENTA":
                return 0xbe46b5;
            case "light_blue":
            case "LIGHT_BLUE":
                return 0x3cb0da;
            case "yellow":
            case "YELLOW":
                return 0xf9c629;
            case "lime":
            case "LIME":
                return 0x71ba1a;
            case "pink":
            case "PINK":
                return 0xe90ad;
            case "gray":
            case "grey":
            case "GRAY":
            case "GREY":
                return 0x3f4548;
            case "light_gray":
            case "light_grey":
            case "LIGHT_GRAY":
            case "LIGHT_GREY":
                return 0x8e8f87;
            case "cyan":
            case "CYAN":
                return 0x158a91;
            case "purple":
            case "PURPLE":
                return 0x7b2bad;
            case "blue":
            case "BLUE":
                return 0x353a9e;
            case "brown":
            case "BROWN":
                return 0x734829;
            case "green":
            case "GREEN":
                return 0x556e1c;
            case "red":
            case "RED":
                return 0xa12823;
            case "black":
            case "BLACK":
                return 0x16161b;
        }
    }

    private void convertToPackTxt(File inputFile, File outputFile) {
        JsonObject inputDir;
        try {
            inputDir = JsonHelper.getMcMeta(inputFile.getPath(), "pack");

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        int pack_format = inputDir.get("pack_format").getAsInt();
        String description = inputDir.get("description").getAsString();

        File newFile = CreateFile(outputFile.getAbsolutePath());

        try {
            FileWriter myWriter = new FileWriter(newFile);

            myWriter.write(description);

            myWriter.close();
            Log.success("Successfully wrote to the file: " + outputFile.getName() );
        } catch (IOException e) {
            Log.error("An error occurred while writing to the file:" + outputFile.getName());
            e.printStackTrace();
        }
    }
    private void convertToTxt(File inputFile, File outputFile) {
        JsonObject inputDir;
        try {
            inputDir = JsonHelper.getMcMeta(inputFile.getPath(), "animation");

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        JsonElement frametime = inputDir.get("frametime");
        JsonArray frames = inputDir.getAsJsonArray("frames");

        File newFile = CreateFile(outputFile.getAbsolutePath());

        try {
            FileWriter myWriter = new FileWriter(newFile);

            for (int i = 0; i < frames.size(); i++) {
                for (int j = 0; j < frametime.getAsInt(); j++) {
                    myWriter.write(frames.get(i).getAsString() + ",");
                }
                myWriter.write("\n");
            }

            myWriter.close();
            Log.success("Successfully wrote to the file: " + outputFile.getName() );
        } catch (IOException e) {
            Log.error("An error occurred while writing to the file:" + outputFile.getName());
            e.printStackTrace();
        }
    }

    private static FileData readFromFile(String fileName) throws IOException {
        List<String> lines;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            lines = br.lines().collect(Collectors.toList());
        }
        int[] array = null;
        int frameCount = 0;

        List<String> temp = lines;

        if (lines.contains("*"))
        {
            array = lines.stream()
                    .map(line -> {
                        String[] parts = line.split("\\*");
                        int count = Integer.parseInt(parts[1]);
                        return new int[count];
                    })
                    .flatMapToInt(Arrays::stream)
                    .toArray();

            for (int i = 0; i < array.length; i++) {
                // Check if the current entry is different from the previous one
                if (i > 0 && array[i] != array[i - 1]) {
                    frameCount = i;
                    break;  // Stop counting after the first different entry
                }
            }

        }
        else {
             array = lines.stream()
                    .map(line -> Arrays.stream(line.split(","))
                            .mapToInt(Integer::parseInt)
                            .toArray())
                    .flatMapToInt(Arrays::stream)
                    .toArray();

            for (int i = 0; i < array.length; i++) {
                // Check if the current entry is different from the previous one
                if (i > 0 && array[i] != array[i - 1]) {
                    frameCount = i;
                    break;  // Stop counting after the first different entry
                }
            }
        }

        long numLines = lines.size();

        return new FileData(array, frameCount, numLines);
    }

    private void convertToPackMcMeta(File inputFile, File outputFile) {
        File outputMapping =  MainWindow.getOutputMap();
        JsonObject outputMap = null;
        try {
            outputMap = JsonHelper.getOptions(outputMapping);

            int pack_format = outputMap.get("pack_format").getAsInt();
            String description = "";

            List<String> lines;
            try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
                lines = br.lines().collect(Collectors.toList());

                for (int i = 0; i < lines.size(); i++) {
                    if (i == lines.size()-1){ //last line
                        description += lines.get(i);
                    }
                    else description += lines.get(i) + " - ";
                }

            }

            PackMcMeta mcMeta = new PackMcMeta(
                    new PackMcMeta.Pack(
                            pack_format,
                            description
                    )
            );

            File f = new File(outputFile.getAbsolutePath());
            if (!f.exists() && !f.isDirectory()) {
                //write new Settings JSON
                try {
                    McMeta.writeJSON(mcMeta, outputFile.getAbsolutePath());
                    Log.success("Successfully wrote to the mcmeta file: " + outputFile.getName());
                } catch (IOException e) {
                    Log.error("An error occurred while writing to the mcmeta file:" + outputFile.getName());
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            Log.error("An error occurred while trying to read the mappings of:" + outputFile.getName());
            Log.error(e.toString());
            e.printStackTrace();
            //throw new RuntimeException(e);
        }


    }

    private void convertToMcMeta(File inputFile, File outputFile) {

        FileData fileData = null;
        try {
            fileData = readFromFile(inputFile.getAbsolutePath());

            int frametime = fileData.getFrameCount();
            int[] frames = new int[(int) fileData.getNumLines()];

            for (int i = 0; i < frames.length; i++) {
                frames[i] = i;
            }

            McMeta mcMeta = new McMeta(
                    new McMeta.Animation(
                            frametime,
                            frames
                    )
            );

            File f = new File(outputFile.getAbsolutePath());
            if (!f.exists() && !f.isDirectory()) {
                //write new Settings JSON
                try {
                    McMeta.writeJSON(mcMeta, outputFile.getAbsolutePath());
                    Log.success("Successfully wrote to the mcmeta file: " + outputFile.getName());
                } catch (IOException e) {
                    Log.error("An error occurred while writing to the mcmeta file:" + outputFile.getName());
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static File CreateFile(String path) {
        File myObj = null;
        try {
            myObj = new File(path);
            if (myObj.createNewFile()) {
                Log.success("File created: " + myObj.getAbsolutePath());
                return myObj;
            } else {
                Log.warning("File already exists:" + myObj.getAbsolutePath());
            }
        } catch (IOException e) {
            Log.error("An error occurred while creating the file");
            e.printStackTrace();
        }
        return myObj;
    }


    private static void CopyFiles(File inputFile, File outputFile) {
        //Create Parent dir
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
            Log.success("Created Parent dir: " + outputFile.getParentFile().getPath(), false);
        } else {
            Log.debug("Skipping Dir Creation: " + outputFile.getParentFile().getPath() + " already exists.", false);
        }
        //copy file
        if (inputFile.exists())
        {
            try {
                Path path = Files.copy(inputFile.toPath(), outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                Log.success("Copied file: " + outputFile.getName());
            } catch (IOException e) {
                Log.error(e.toString());
                e.printStackTrace();
            }
        }
        else {
            Log.warning("Didn't copy file: " + outputFile.getName());
        }
    }


    private void createOutputDir() {
        exportDir = new File( MainWindow.outputLocation.getSelectedItem().toString() );

        if (!exportDir.exists())
        {
            publish("Creating Output directory");
            Log.msg("Creating new dirs in temp/" + exportDir.getName() + "/ ...");
            exportDir.mkdirs();
        }
    }
    File unzippedDir;
    File exportDir;

    private void unzip() {
        Log.msg("Checking for unzipped dir...");

        ZipHelper zipHelper = new ZipHelper();
        File zippedDir = new File( MainWindow.inputLocation.getSelectedItem().toString() );
        unzippedDir = new File("temp/" + zippedDir.getName().replace(".zip", ""));

        publish("unzipping");

        if (!unzippedDir.exists()) {
            Log.msg("Unzipped dir doesn't exist. Unzipping...");

            try {
                zipHelper.unzip(zippedDir, unzippedDir);
                Log.msg("Finished unzipping.");
            } catch (IOException e) {
                Log.error("Couldn't unzip file: " + e.getMessage());
                publish(Language.getString("converter.error"));
                throw new RuntimeException(e);
            }

        } else {
            Log.warning("File already unzipped. Skipping Unzipping...");
        }
    }

    private static class FileData {
        private final int[] array;
        private final int frameCount;
        private final long numLines;

        public FileData(int[] array, int frameCount, long numLines) {
            this.array = array;
            this.frameCount = frameCount;
            this.numLines = numLines;
        }

        public int[] getArray() {
            return array;
        }

        public int getFrameCount() {
            return frameCount;
        }

        public long getNumLines() {
            return numLines;
        }
    }
}
