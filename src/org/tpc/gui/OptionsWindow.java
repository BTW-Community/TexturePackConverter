package org.tpc.gui;

import com.google.gson.*;
import org.tpc.JsonHelper;
import org.tpc.Language;
import org.tpc.Log;
import org.tpc.Main;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OptionsWindow extends DefaultWindow {


    JTabbedPane tabs;

    public static HashMap<String, JButton> enableMap;
    ArrayList<JCheckBox> optionBoxes;
    HashMap<String, ArrayList<JCheckBox>> hashMap;
    //HashMap<String, Boolean> toggle;

    JButton enableButton;

    public OptionsWindow() {
        super(Language.getString("title.options"), 400, 600, true);
        setLocationRelativeTo(Main.mainWindow);
    }

    @Override
    protected void addContent() {

        centerPanel.setLayout(new BorderLayout(0, 10));
        //centerPanel.setBackground(Color.blue);

        File config = MainWindow.config;
        JsonObject configObj = null;
        JsonArray configs = null;
        JsonObject configOptions = null;
        JsonObject options = null;
        try {
            configObj = JsonHelper.getOptions(config);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        configs = configObj.get("configs").getAsJsonArray();
        options = configObj.get("options").getAsJsonObject();

        tabs = new JTabbedPane();
        hashMap = new HashMap<>();
        enableMap = new HashMap<>();
        //toggle = new HashMap<>();

        for (JsonElement conf : configs) {

            JPanel tabContent = new JPanel(new BorderLayout(0,10));

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(0,2));
            panel.setBorder(new BevelBorder(BevelBorder.LOWERED));
            String configStr = conf.getAsString();

            JsonObject mapObj = null;
            try {
                mapObj = JsonHelper.getMappings("mappings/" + MainWindow.inputMap.getName(), configStr);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            JPanel enablePanel = new JPanel();
            enablePanel.setPreferredSize(new Dimension(100, 30));

            enableButton = new JButton();
            enableButton.setText(Language.getString("options.toggle"));
            enableButton.addActionListener(e -> enableOptions());

            enableMap.put(configStr, enableButton);

            enablePanel.add(enableButton);

            configOptions =  configObj.get(configStr).getAsJsonObject();
            Map<String,JsonElement> map = configOptions.asMap();
            optionBoxes = new ArrayList<>();

            for(Map.Entry entry : map.entrySet())
            {
                JCheckBox checkBox = new JCheckBox();
                checkBox.setSize(new Dimension(100,30));
                checkBox.setHorizontalAlignment(JCheckBox.LEFT);
                checkBox.setText(entry.getKey().toString());

                //TODO: remove, when txt > png.mcmeta is fixed. Error with 0*2
                if (checkBox.getText().equalsIgnoreCase("animation")){
                    checkBox.setEnabled(false);
                }


                JsonPrimitive primitive = (JsonPrimitive) entry.getValue();
                checkBox.setSelected(primitive.getAsBoolean());
                checkBox.addActionListener(e -> onOptionChanged(checkBox, configStr, entry.getKey().toString()));
                panel.add(checkBox);
                optionBoxes.add(checkBox);

            }

            tabContent.add(panel, BorderLayout.CENTER);
            tabContent.add(enablePanel, BorderLayout.SOUTH);

            if (mapObj.has("experimental") && mapObj.get("experimental").getAsBoolean())
            {
                String tabName = "⚠ " + configStr + " (" + Language.getString("options.experimental") + ") ⚠";

                tabs.addTab(tabName, tabContent);


            }
            else tabs.addTab(configStr, tabContent);

            hashMap.put(configStr, optionBoxes);
            //toggle.put(configStr, false);
        }

        centerPanel.add(tabs, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();

        Map<String,JsonElement> mapOptions = options.asMap();


        for(Map.Entry entry : mapOptions.entrySet())
        {
            JCheckBox checkBox = new JCheckBox();
            String optionName = entry.getKey().toString();
            //System.out.println(optionName);
            checkBox.setText(Language.getString(optionName));
            JsonPrimitive primitive = (JsonPrimitive) entry.getValue();
            checkBox.setSelected(primitive.getAsBoolean());

            checkBox.addActionListener(e -> onOptionChanged(checkBox, "options", entry.getKey().toString()));
            bottomPanel.add(checkBox);

            if (optionName.equalsIgnoreCase("options.cleanTMP")) {
                checkBox.setText(Language.getString("options.cleanTMP"));
            }
            if (optionName.equalsIgnoreCase("options.createZip")) {
                checkBox.setText(Language.getString("options.createZip"));
                checkBox.setSelected(false); //TODO: remove when zip is fixed
                checkBox.setEnabled(false); //TODO: remove when zip is fixed
            };
        }

        centerPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void onOptionChanged(JCheckBox checkBox,String confString, String option) {
        boolean boo = checkBox.isSelected();
        try {
            JsonHelper.setOptions(MainWindow.config, confString, option, boo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private void enableOptions() {
        //Select and deselect all checkboxes in the current selected tab
        int index = tabs.getSelectedIndex();
        String tabName = tabs.getTitleAt(index);
        ArrayList<JCheckBox> tab = hashMap.get(tabName);

        for(JCheckBox box : tab)
        {
            if (box.isEnabled())
            {
                box.setSelected(!box.isSelected());

                //write Json
                boolean boo = box.isSelected();
                try {
                    JsonHelper.setOptions(MainWindow.config, tabName, box.getText(), boo);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchFieldException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        MainWindow.optionsBtn.setEnabled(true);
    }
}
