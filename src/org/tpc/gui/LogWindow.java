package org.tpc.gui;

import org.tpc.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LogWindow extends DefaultWindow {

    public static JTextPane taLog;

    public LogWindow(){
        super("Console Log", 600, 300, false);
        setBackground(Color.BLACK);

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

        JCheckBox openOnStart = new JCheckBox("Open Console On Startup", Settings.shouldOpenLogOnStartUp());
        openOnStart.setForeground(Color.WHITE);
        openOnStart.setBackground(Color.BLACK);
        openOnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*
                try {
                    Settings.setOpenLogOnStartUp(openOnStart.isSelected());
                } catch (IOException | NoSuchFieldException | SecurityException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                 */
            }
        });

        contentPane.add(openOnStart, BorderLayout.SOUTH);

        pack();
    }
}
