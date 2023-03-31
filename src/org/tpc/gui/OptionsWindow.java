package org.tpc.gui;

import com.google.gson.*;
import org.tpc.JsonHelper;
import org.tpc.Language;
import org.tpc.Main;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OptionsWindow extends DefaultWindow {


    JTabbedPane tabs;

    public static HashMap<String, JButton> enableMap;
    ArrayList<JCheckBox> optionBoxes;
    HashMap<String, ArrayList<JCheckBox>> hashMap;
    HashMap<String, Boolean> toggle;

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
            configs = configObj.get("configs").getAsJsonArray();
            options = configObj.get("options").getAsJsonObject();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        tabs = new JTabbedPane();
        hashMap = new HashMap<>();
        enableMap = new HashMap<>();
        toggle = new HashMap<>();

        for (JsonElement conf : configs) {

            JPanel tabContent = new JPanel(new BorderLayout(0,0));

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(0,2));
            panel.setBorder(new BevelBorder(BevelBorder.LOWERED));
            String configStr = conf.getAsString();

            JPanel enablePanel = new JPanel();
            enablePanel.setPreferredSize(new Dimension(100,50));

            enableButton = new JButton();
            enableButton.setText(Language.getString("options.toggle"));
            enableButton.addActionListener(e -> enableOptions());

//            JCheckBox enableCheckbox = new JCheckBox();
//            enableCheckbox.setText(Language.getString("options.toggle"));
//            enableCheckbox.setSelected(true);
//            enableCheckbox.addActionListener(e -> enableOptions());

            enableMap.put(configStr, enableButton);

            enablePanel.add(enableButton);

            configOptions =  configObj.get(configStr).getAsJsonObject();
            Map<String,JsonElement> map = configOptions.asMap();
            optionBoxes = new ArrayList<>();

            for(Map.Entry entry : map.entrySet())
            {
                JCheckBox checkBox = new JCheckBox();
                checkBox.setSize(new Dimension(100,25));
                checkBox.setHorizontalAlignment(JCheckBox.LEFT);
                checkBox.setText(entry.getKey().toString());
                JsonPrimitive primitive = (JsonPrimitive) entry.getValue();
                checkBox.setSelected(primitive.getAsBoolean());

                panel.add(checkBox);
                optionBoxes.add(checkBox);
            }

            tabContent.add(panel, BorderLayout.CENTER);
            tabContent.add(enablePanel, BorderLayout.SOUTH);
            tabs.addTab(configStr, tabContent);
            hashMap.put(configStr, optionBoxes);
            toggle.put(configStr, false);
        }

        centerPanel.add(tabs, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();

        Map<String,JsonElement> mapOptions = options.asMap();


        for(Map.Entry entry : mapOptions.entrySet())
        {
            JCheckBox checkBox = new JCheckBox();
            checkBox.setText(entry.getKey().toString());
            JsonPrimitive primitive = (JsonPrimitive) entry.getValue();
            checkBox.setSelected(primitive.getAsBoolean());

            bottomPanel.add(checkBox);
        }

        centerPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void enableOptions() {
        //Select and deselect all checkboxes in the current selected tab
        int index = tabs.getSelectedIndex();
        String tabName = tabs.getTitleAt(index);
        ArrayList<JCheckBox> tab = hashMap.get(tabName);

        if (!toggle.get(tabName))
        {
            for(JCheckBox box : tab)
            {
                box.setSelected(false);
            }

            toggle.replace(tabName, true);
        }
        else if (toggle.get(tabName))
        {
            for(JCheckBox box : tab)
            {
                box.setSelected(true);
            }

            toggle.replace(tabName, false);
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        MainWindow.optionsBtn.setEnabled(true);
    }
}
