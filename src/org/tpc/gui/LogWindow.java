package org.tpc.gui;

import org.tpc.Language;
import org.tpc.Main;
import org.tpc.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LogWindow extends DefaultWindow {

    public static JTextPane taLog;
    public static JCheckBox openOnStart;
    public LogWindow(){
        super(Language.getString("title.console"), 600, 300, false);
        setBackground(Color.BLACK);
        setLocation(50,100);
    }

    @Override
    protected void addContent() {

        JPanel contentPane = new JPanel();contentPane.setLayout(new BorderLayout());
        contentPane.setBackground(Color.BLACK);
        setContentPane(contentPane);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.BLACK);

        taLog = new JTextPane();
        taLog.setEditable(false);
        taLog.setFont(new Font("Lucida Console", 0, 14));
        taLog.setBackground(Color.BLACK);

        JScrollPane spPane = new JScrollPane(taLog);

        contentPane.add(spPane);

        openOnStart = new JCheckBox(Language.getString("log.startup"), Main.settings.shouldOpenLogOnStartUp());
        openOnStart.setForeground(Color.WHITE);
        openOnStart.setBackground(Color.BLACK);
        openOnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    Main.settings.setOpenLogOnStartUp(openOnStart.isSelected());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (NoSuchFieldException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        contentPane.add(openOnStart, BorderLayout.SOUTH);
    }
}
