package org.tpc.gui;

import org.tpc.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class ConvertWindow extends DefaultWindow implements ConvertWindowListener {
    public static JLabel label;
    public static JProgressBar progressBar;
    public static JPanel topPanel;
    public static JPanel bottomPanel;
    public static JButton cancelBtn;
    public static JButton openBtn;
    private ConvertWindowListener listener;
    public ConvertWindow() {
        super(Language.getString("title.convert"), 300, 150, true);
        setLocationRelativeTo(Main.mainWindow);
        setListener(this);
        centerPanel.setLayout(new BorderLayout(0,0));
    }

    @Override
    protected void addContent() {

        topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(100, 30));
        //topPanel.setBackground(Color.red);

        bottomPanel = new JPanel();
        bottomPanel.setPreferredSize(new Dimension(100, 30));
        //bottomPanel.setBackground(Color.yellow);

        label = new JLabel();
        label.setText("Converting...");

        progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(80, 30));
        progressBar.setStringPainted(true);

        cancelBtn = new JButton();
        cancelBtn.setText("Cancel");
        cancelBtn.addActionListener(e -> cancel());

        centerPanel.add(topPanel,BorderLayout.NORTH);
        centerPanel.add(bottomPanel, BorderLayout.SOUTH);

        topPanel.add(label);
        //topPanel.add(progressBar);
        bottomPanel.add(cancelBtn);
    }

    @Override
    public void windowClosed(WindowEvent e) {
        Main.mainWindow.convertBtn.setEnabled(true);
    }

    public void onDone(){
        openBtn = new JButton();
        openBtn.setText("Open...");
        openBtn.addActionListener(e -> open());
        bottomPanel.add(openBtn);

        bottomPanel.remove(cancelBtn);
    }

    private void open(){
        try {
            Desktop.getDesktop().open(new File(MainWindow.outputLocation.getSelectedItem().toString()));
            this.dispose();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void cancel() {
        if (listener != null)
        {
            listener.conversionCancelled();
        }
    }

    public void setListener(ConvertWindowListener listener)
    {
        this.listener = listener;
    }
    SwingWorker<List<String>, Integer> worker;
    private void convert() {

        try {
            worker = new Converter(this);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        worker.execute();

    }
    @Override
    public void conversionCancelled() {

        if (worker != null)
        {
            worker.cancel(true);

            Log.warning("Conversion cancelled by user!");
            this.dispose();
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
        convert();
    }

    private static void convet() {
        /*
        File configFile = new File("configs/1.5.2.json");
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

            for(Map.Entry entry : configOptions.entrySet())
            {
                String optName = entry.getKey().toString();
                JsonPrimitive primitive = (JsonPrimitive) entry.getValue();
                Boolean optValue = primitive.getAsBoolean();

                System.out.println(optName + " : " + optValue);

                if (optValue == true)
                {
                    //get input
                    JsonObject dir = JsonHelper.getMappings("mappings/1.5.2.json", configName, "directory");
                    JsonElement pack = dir.get(optName);
                    String textureDirectory = pack.getAsString();

                    System.out.println(textureDirectory);

                    JsonObject map = JsonHelper.getMappings("mappings/1.5.2.json", configName, "files");
                    JsonArray textureFiles = map.get(optName).getAsJsonArray();

                    System.out.println(textureFiles);
                }
            }
        }

         */


        /*
        JsonObject dir = JsonHelper.getMappings("mappings/1.5.2.json", "vanilla", "directory");
        JsonElement pack = dir.get("pack");
        String textureDirectory = pack.getAsString();

        JsonObject map = JsonHelper.getMappings("mappings/1.5.2.json", "vanilla", "files");
        JsonArray packarr = map.get("pack").getAsJsonArray();

        for(JsonElement el : packarr) {
            JsonArray arr = el.getAsJsonArray();

            String textureName = arr.get(0).getAsString();

            String format = null;
            Integer rotate = null;
            Boolean mirror = null;
            Integer tint = null;

            String action = "NONE";
            Object value = null;

            String imagePath = null;
            BufferedImage myPicture = null;

            for (int i = 1; i < 5; i++)
            {
                if (arr.size() > i)
                {
                    try {
                        JsonElement arr2 = arr.get(i);

                        String[] var = arr2.getAsString().trim().split("=");

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
                            tint = Integer.parseInt(var[1],16);
                        }

                    }
                    catch (ArrayIndexOutOfBoundsException e)
                    {
                        continue;
                    }

                    //System.out.println(textureName +": " + action + " > " + value);

                }

                if (format == null) format = "png";
                if (mirror == null) mirror = false;

                imagePath = "temp/"  +  textureDirectory + textureName + "." + format;
            }

            System.out.println(imagePath);

            if (format.equalsIgnoreCase("png"))
            {
                try{
                    myPicture = ImageIO.read(new File(imagePath));
                }
                catch (IOException e)
                {
                    System.out.println("File not found");
                }

                if (myPicture != null)
                {
                    if (tint != null) myPicture = Utils.tintImage(myPicture, tint);
                    if (rotate != null) myPicture = Utils.rotateImage(myPicture, rotate);
                    if (mirror) myPicture = Utils.mirrorImage(myPicture);


                    File output_file = new File("temp/export" + "." + format);

                    ImageIO.write(myPicture, format, output_file);
                }
            }
        }

         */
    }


}
