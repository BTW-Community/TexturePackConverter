package org.tpc.gui;

import org.tpc.Language;
import org.tpc.Main;
import org.tpc.Utils;
import org.tpc.Version;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;

public class AboutWindow extends DefaultWindow implements HyperlinkListener {

    public AboutWindow() {
        super(Language.getString("title.about"), 450, 300, false);
        setLocationRelativeTo(Main.mainWindow);
        centerPanel.setLayout(new BorderLayout(0,0));
    }

    @Override
    protected void addContent() {
        ImageIcon logo = Utils.getImage(getClass(), "/assets/tpc_logo.png");
        ImageIcon imageIcon = new ImageIcon(logo.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));

        JPanel leftPanel = new JPanel();
        JPanel rightPanel = new JPanel();

        leftPanel.setPreferredSize(new Dimension(100,200));
        rightPanel.setPreferredSize(new Dimension(250,200));

        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        JLabel imgLabel = new JLabel();
        imgLabel.setIcon(imageIcon);

        String titleText = Main.mainWindow.getTitle() + " (" + Version.getCurrentVersion(true) + ")";
        JTextPane titlePane = new JTextPane();
        titlePane.setEditable(false);
        titlePane.setHighlighter(null);
        titlePane.setText(titleText);
        titlePane.setFont(new Font ("Segoe UI", Font.BOLD, 13) );

        String aboutText =
                "Contributers: " + "\n" +
                "Sockthing (@Socklessthing)" + "\n" +
                "\n" +
                "Credits: " + "\n" +
                "jMC2Obj" + "\n" +
                "GeeksForGeeks" + "\n" +
                "\n" +
                "Licence: " + "\n" +
                "CC BY-NC-SA 4.0";

        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setHighlighter(null);
        textPane.setText(aboutText);

        centerPanel.add(leftPanel, BorderLayout.WEST);
        centerPanel.add(rightPanel, BorderLayout.EAST);

        leftPanel.add(imgLabel);
        rightPanel.add(titlePane);
        rightPanel.add(textPane);

    }

    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {

    }
}
