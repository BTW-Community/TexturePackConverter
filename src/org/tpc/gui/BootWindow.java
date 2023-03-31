package org.tpc.gui;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.tpc.Log;
import org.tpc.Main;
import org.tpc.Utils;
import org.tpc.Version;

public class BootWindow extends JFrame implements WindowListener {

    public BootWindow ()  {
        setSize(300,200);
        setMinimumSize(new Dimension(300,200));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Booting...");
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);
        setUndecorated(true);
        addWindowListener(this);
        setIconImage(Main.icon.getImage());

        Color color = getBackground();

        ImageIcon logo = Utils.getImage(getClass(), "/assets/tpc_logo.png");

        Container container = this.getContentPane();
        container.setLayout(new BorderLayout(10,10));
        container.setBackground(color);

        JPanel centerPanel = new JPanel();
        JPanel southPanel = new JPanel();

        centerPanel.setPreferredSize(new Dimension(200, 200));
        southPanel.setPreferredSize(new Dimension(100, 75));

        centerPanel.setBackground(color);
        southPanel.setBackground(color);

        container.add(centerPanel, BorderLayout.CENTER);
        container.add(southPanel, BorderLayout.SOUTH);

        JLabel imgLabel = new JLabel();
        imgLabel.setIcon(logo);
        imgLabel.setPreferredSize(new Dimension(144,200));
        imgLabel.setVerticalAlignment(JLabel.CENTER);

        JLabel labelV = new JLabel("Version: " + Version.getCurrentVersion(false));
        labelV.setFont(new Font("Dialog",  Font.PLAIN, 12));
        labelV.setVerticalAlignment(JLabel.CENTER);

        JLabel labelD = new JLabel("Attribution-NonCommercial-ShareAlike 4.0 International");
        labelD.setFont(new Font("Dialog",  Font.PLAIN, 10));
        labelD.setVerticalAlignment(JLabel.CENTER);

        JLabel labelD2 = new JLabel("(CC BY-NC-SA 4.0)");
        labelD2.setFont(new Font("Dialog",  Font.PLAIN, 10));
        labelD2.setVerticalAlignment(JLabel.CENTER);

        centerPanel.add(imgLabel, BorderLayout.CENTER);
        southPanel.add(labelV, BorderLayout.NORTH);
        southPanel.add(labelD, BorderLayout.CENTER);
        southPanel.add(labelD2, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    @Override
    public void windowOpened(WindowEvent e) {


        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }

        try {
            Main.mainWindow.setVisible(true);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

    @Override
    public void windowClosing(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowClosed(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowIconified(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowActivated(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        // TODO Auto-generated method stub

    }
}