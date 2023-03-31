package org.tpc.gui;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.tpc.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateWindow extends DefaultWindow {
    public static JButton button;
    public static JLabel label;
    public UpdateWindow() {
        super(Language.getString("title.update"), 300, 175, true);
        setLocationRelativeTo(null);
    }

    @Override
    protected void addContent() {
        JPanel topPanel = new JPanel();
        JPanel bottomPanel = new JPanel();

        topPanel.setPreferredSize(new Dimension(100, 30));
        bottomPanel.setPreferredSize(new Dimension(100, 50));

        centerPanel.setLayout(new BorderLayout(10,10));
        southPanel.setLayout(new BorderLayout(0,0));

        centerPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(bottomPanel, BorderLayout.SOUTH);

        label = new JLabel();
        label.setText(Language.getString("update.check") + "...");
//		label.setBounds(75, 50, 200, 50);
        label.setVerticalAlignment(JLabel.CENTER);

        String latestURL = "https://github.com/BTW-Community/TexturePackConverter/releases/latest";
        button = new JButton();
        button.setText(Language.getString("update.download"));
        button.addActionListener(e -> Utils.openSite(latestURL));
        button.setToolTipText(latestURL);
        button.setEnabled(false);
        button.setVerticalAlignment(JButton.CENTER);
//		button.setBounds(75, 100, 150, 25);

        JCheckBox openOnStart = new JCheckBox(Language.getString("update.startup"), Main.settings.shouldCheckUpdateOnStartUp());
        openOnStart.setHorizontalAlignment(JCheckBox.CENTER);
        openOnStart.addActionListener(e -> {
            try {
                Main.settings.setCheckUpdateOnStartUp(openOnStart.isSelected());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (NoSuchFieldException ex) {
                throw new RuntimeException(ex);
            }
        });

        topPanel.add(label, BorderLayout.SOUTH);
        bottomPanel.add(button, BorderLayout.CENTER);
        southPanel.add(openOnStart, BorderLayout.CENTER);
    }

    @Override
    public void windowClosed(WindowEvent e) {
        MainWindow.updateItem.setEnabled(true);
    }

    @Override
    public void windowOpened(WindowEvent e) {
        check();
    }
    public void check()
    {

        Log.msg("Update: Checking...");

        String version = checkForUpdate();

        if (version.length() > 0)
        {
            label.setText(Language.getString("update.found") + ": " + version);
            button.setEnabled(true);

            Log.msg("Update: Found " + version);
        }
        else
        {
            label.setText("✔ " + Language.getString("update.uptodate"));

            Log.msg("Update: No update found");
        }
    }





    private String checkForUpdate(){

        String newVersion = "";

        String urlString = "https://api.github.com/repos/BTW-Community/TexturePackConverter/releases/latest";

        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            int status = con.getResponseCode();
//			int status = 404;

            if (status != 200)
            {
                Log.error("Response Code " + status);
                return "";
            }

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            con.disconnect();

            JsonElement fileElement = JsonParser.parseString(content.toString());
            JsonObject fileObject = fileElement.getAsJsonObject();

            String version = fileObject.get("tag_name").getAsString();

            String currentVersion = Version.getCurrentVersion(true);

            if (version.equalsIgnoreCase(currentVersion))
            {
//				System.out.println("No Updates found");
//				System.out.println("Current verison: " + currentVersion);
            }
            else
            {
//				System.out.println("Update found: " + version);
//				System.out.println("Current verison: " + currentVersion);

                newVersion = version;
            }
        }
        catch (Exception e) {
            Log.msg("No latest release.");
            e.printStackTrace();
        }

        return newVersion;
    }
}
