package org.tpc.gui;

import org.tpc.*;

import javax.swing.*;
import java.awt.event.WindowEvent;

public class MainWindow extends DefaultWindow {
    public MainWindow() {
        super("Texture Pack Converter", 600,400,true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    @Override
    public void windowOpened(WindowEvent e) {
        Main.logWindow.setVisible(true);
    }

    @Override
    protected void addContent() {
        addMenuBar();

        Log.msg("Testing");
        Log.warning("Testing");
        Log.error("Testing");
    }

    private void addMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenu helpMenu = new JMenu("Help");

        JMenuItem consoleItem = new JMenuItem("Show Console Log...");
        consoleItem.addActionListener(e -> Main.logWindow.setVisible(true));
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));

        JMenuItem updateItem = new JMenuItem("Check for Updates...");
        updateItem.addActionListener(e -> new UpdateWindow());
        JMenuItem readmeItem = new JMenuItem("Open README...");
        readmeItem.addActionListener(e -> Utils.openSite("https://github.com/BTW-Community/TexturePackConverter/blob/main/README.md"));
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> Main.aboutWindow.setVisible(true));

        fileMenu.add(consoleItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        helpMenu.add(readmeItem);
        helpMenu.addSeparator();
        helpMenu.add(updateItem);
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }
}
