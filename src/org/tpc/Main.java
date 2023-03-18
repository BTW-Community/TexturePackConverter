package org.tpc;

import org.tpc.gui.*;
import javax.swing.*;

public class Main {
    public static LogWindow logWindow;
    public static AboutWindow aboutWindow;
    public static MainWindow mainWindow;

    //The Icon in the title bar
    public static ImageIcon icon = new ImageIcon("assets/tpc_icon.png");

    public static void main(String[] args) {
        logWindow = new LogWindow();
        aboutWindow = new AboutWindow();

        mainWindow = new MainWindow();
    }
}