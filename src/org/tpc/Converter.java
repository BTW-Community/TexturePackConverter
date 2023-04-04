package org.tpc;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.tpc.gui.ConvertWindow;
import org.tpc.gui.MainWindow;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

public class Converter extends SwingWorker {
    private final ConvertWindow window;
    public Converter (ConvertWindow window) throws FileNotFoundException {
        this.window = window;
    }

    @Override
    protected void done() {
        if (isCancelled()) return;

        ConvertWindow.label.setText("Done!");
        Log.success("Done");

        window.onDone();
    }


    @Override
    protected void process(List string) {
        String received = (String) string.get(0);
        ConvertWindow.label.setText(received + " ...");
    }

    @Override
    protected Object doInBackground() throws Exception {
        //List<String> recievedList = new ArrayList<>();

        //Create /temp/ folder
        createTempDir();

        //delete contents of /temp/
        deleteTempFolder();

        //Unzip
        unzip();

        //Create output dir
        createOutputDir();

        //Convert
        convert();

        //Create zip
        zip();
        return null;
    }

    private void zip() throws FileNotFoundException {
        File inputMapping = new File("mappings/" + MainWindow.inputMap.getName() + ".json");
        File configFile = new File("configs/" + inputMapping.getName());

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
        File inputMapping = new File("mappings/" + MainWindow.inputMap.getName() + ".json");
        File configFile = new File("configs/" + inputMapping.getName());

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

        File inputMapping = new File("mappings/" + MainWindow.inputMap.getName() + ".json");
        File outputMapping = new File("mappings/" + MainWindow.outputMap.getName());
        File configFile = new File("configs/" + inputMapping.getName());

        //Log.msg(inputMapping.getAbsolutePath());
        //Log.msg(outputMapping.getAbsolutePath());
        //Log.msg(configFile.getAbsolutePath());

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

                if (optValue)
                {
                    Log.msg(optName + " : " + optValue);

                    //get input mapping json
                    JsonObject inputDir = JsonHelper.getMappings(inputMapping.getAbsolutePath(), configName, "directory");
                    JsonElement inputPack = inputDir.get(optName);
                    String inputTextureDir = inputPack.getAsString();

                    JsonObject inputFiles = JsonHelper.getMappings(inputMapping.getAbsolutePath(), configName, "files");
                    JsonArray inputTextureFiles = inputFiles.get(optName).getAsJsonArray();

                    //get output mapping json
                    JsonObject outputDir = JsonHelper.getMappings(outputMapping.getAbsolutePath(), configName, "directory");
                    JsonElement outputPack = outputDir.get(optName);
                    String outputTextureDir = outputPack.getAsString();

                    JsonObject outputFiles = JsonHelper.getMappings(outputMapping.getAbsolutePath(), configName, "files");
                    JsonArray outputTextureFiles = outputFiles.get(optName).getAsJsonArray();

                    System.out.println(outputTextureFiles.size());

                    for(int j = 0; j < inputTextureFiles.size(); j++) {

                        JsonElement inputElement = inputTextureFiles.get(j);
                        JsonArray inputTexture = inputElement.getAsJsonArray();
                        String inputTextureName = inputTexture.get(0).getAsString();

                        JsonElement outputElement = outputTextureFiles.get(j);
                        JsonArray outputTexture = outputElement.getAsJsonArray();
                        String outputTextureName = outputTexture.get(0).getAsString();

                        String inputFormat = null;

                        String format = null;
                        Integer rotate = null;
                        Boolean mirror = null;
                        Integer tint = null;

                        String imagePath = null;
                        BufferedImage myPicture = null;

                        String action = "NONE";
                        Object value = null;

                        //get input format
                        for (int i = 1; i < 5; i++) {
                            if (inputTexture.size() > i) {
                                try {
                                    JsonElement arr = inputTexture.get(i);

                                    String[] var = arr.getAsString().trim().split("=");

                                    if (var[0].equalsIgnoreCase("format")) {
                                        action = "FORMAT";
                                        inputFormat = (String) var[1];
                                    }

                                } catch (ArrayIndexOutOfBoundsException e) {
                                    Log.error(e.getStackTrace().toString());
                                    continue;
                                }
                            }

                            if (inputFormat == null) inputFormat = "png";
                        }

                        //analyse output texture
                        for (int i = 1; i < 5; i++) {
                            if (outputTexture.size() > i) {
                                try {
                                    JsonElement arr = outputTexture.get(i);

                                    String[] var = arr.getAsString().trim().split("=");

                                    if (var[0].equalsIgnoreCase("format")) {
                                        action = "FORMAT";
                                        format = (String) var[1];
                                    } else if (var[0].equalsIgnoreCase("rotate")) {
                                        action = "ROTATE";
                                        rotate = Integer.parseInt(var[1]);
                                    } else if (var[0].equalsIgnoreCase("mirror")) {
                                        action = "MIRROR";
                                        mirror = Boolean.parseBoolean(var[1]);

                                    } else if (var[0].equalsIgnoreCase("tint")) {
                                        action = "TINT";
                                        tint = Integer.parseInt(var[1], 16);
                                    }

                                } catch (ArrayIndexOutOfBoundsException e) {
                                    continue;
                                }
                            }

                            if (format == null) format = "png";
                            if (mirror == null) mirror = false;
                        }

                        File inputFile = new File(unzippedDir + inputTextureDir + inputTextureName + "." + inputFormat);
                        File outputFile = new File(exportDir + outputTextureDir + outputTextureName + "." + format);

                        Log.msg(inputFile.getAbsolutePath() + " >>> " + outputFile.getAbsolutePath());

                        //Copy Files
                        if (!inputFile.exists()) {
                            Log.warning("Source File: " + inputFile.getName() + " doesn't exist! Skipping ...");
                        }
                        else {
                            if (!outputFile.getParentFile().exists()) {
                                outputFile.getParentFile().mkdirs();
                                Log.success("Created Parent dir: " + outputFile.getParentFile().getPath(), false);
                            } else {
                                Log.warning("Skipping Dir Creation: " + outputFile.getParentFile().getPath() + " already exists.", false);
                            }
                            try {
                                Path path = Files.copy(inputFile.toPath(), outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                                Log.success("Copied file: " + inputFile.getName());
                            } catch (IOException e) {
                                Log.error(e.getMessage());
                                e.printStackTrace();
                                Log.error("Didn't copy file: " + inputFile.getName());
                                continue;
                            }
                        }

                        //Manipulate File
                        if (format.equalsIgnoreCase("png"))
                        {
                            imagePath = outputFile.getAbsolutePath();

                            try(FileInputStream instream = new FileInputStream(imagePath)){
                                myPicture = ImageIO.read(instream);

                                if (myPicture != null) {

                                    if (tint != null) {
                                        Log.msg("Tinting " + outputFile.getName());
                                        myPicture = Utils.tintImage(myPicture, tint);
                                    }
                                    if (rotate != null) {
                                        Log.msg("Rotating " + outputFile.getName());
                                        myPicture = Utils.rotateImage(myPicture, rotate);
                                    }
                                    if (mirror) {
                                        Log.msg("Mirroring " + outputFile.getName());
                                        myPicture = Utils.mirrorImage(myPicture);
                                    }
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            FileOutputStream outstream = null;
                            try {
                                outstream = new FileOutputStream(imagePath);
                                ImageIO.write(myPicture, format, outstream);

                                outstream.close();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            outstream.close();

                        }
                    }
                }
            }
        }
    }


    private void createOutputDir() {
        exportDir = new File( MainWindow.outputLocation.getSelectedItem().toString() );

        if (!exportDir.exists())
        {
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
                Log.error("Couldn't unzip file!");
                throw new RuntimeException(e);
            }

        } else {
            Log.msg("File already unzipped. Skipping Unzipping...");
        }
    }
}
