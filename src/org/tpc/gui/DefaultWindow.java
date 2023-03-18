package org.tpc.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import org.tpc.*;

public class DefaultWindow extends JFrame implements WindowListener {

    public DefaultWindow(String title, int width, int height, boolean visible){
        setPreferredSize(new Dimension(width, height));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(title);
        setLayout(new BorderLayout(0, 0));
        setResizable(false);
        addWindowListener(this);
        setIconImage(Main.icon.getImage());

        // --- PANELS --- //

        JPanel northPanel = new JPanel();
        JPanel southPanel= new JPanel();
        JPanel westPanel = new JPanel();
        JPanel eastPanel = new JPanel();
        JPanel centerPanel = new JPanel();

        northPanel.setBackground(Color.red);
        southPanel.setBackground(Color.green);
        westPanel.setBackground(Color.yellow);
        eastPanel.setBackground(Color.cyan);
        centerPanel.setBackground(Color.blue);

        northPanel.setPreferredSize(new Dimension(100, 25));
        southPanel.setPreferredSize(new Dimension(100, 25));
        westPanel.setPreferredSize(new Dimension(25, 100));
        eastPanel.setPreferredSize(new Dimension(25, 100));
        centerPanel.setPreferredSize(new Dimension(100, 100));

        add(northPanel, BorderLayout.NORTH);
        add(southPanel, BorderLayout.SOUTH);
        add(westPanel, BorderLayout.WEST);
        add(eastPanel, BorderLayout.EAST);
        add(centerPanel, BorderLayout.CENTER);

        addContent();

        pack();
        setVisible(visible);
    }

    protected void addContent() {}

    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowClosing(WindowEvent e) {}

    @Override
    public void windowClosed(WindowEvent e) {}

    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowActivated(WindowEvent e) {}

    @Override
    public void windowDeactivated(WindowEvent e) {}
}
