package org.tpc;

import org.tpc.gui.LogWindow;

import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import java.awt.*;

import static org.tpc.gui.LogWindow.taLog;

public class Log {
    public static void msg(String msg) {
        try {
            log(msg, Color.WHITE);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

    public static void success(String msg) {
        try {
            log(msg, Color.GREEN);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }
    public static void success(String msg, Boolean outputToScreen) {
        try {
            log(msg, Color.GREEN, outputToScreen);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

    public static void error(String error) {
        try {
            error = "ERROR: " + error;
            log(error, Color.RED);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

    public static void warning(String warning) {
        try {
            warning = "WARNING: " + warning;
            log(warning, Color.YELLOW);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

    public static void warning(String warning, boolean outputToScreen) {
        try {
            warning = "WARNING: " + warning;
            log(warning, Color.YELLOW, outputToScreen);
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

    private static void log(String msg, Color textColor) throws BadLocationException {
        Style color = taLog.addStyle("color", null);
        StyleConstants.setForeground(color, textColor);
        taLog.getStyledDocument().insertString(taLog.getDocument().getLength(), msg + "\n", color);
        taLog.setCaretPosition(taLog.getDocument().getLength());

        System.out.println(msg);
    }

    private static void log(String msg, Color textColor, Boolean outputToScreen) throws BadLocationException {

        if (outputToScreen) {
            Style color = taLog.addStyle("color", null);
            StyleConstants.setForeground(color, textColor);
            taLog.getStyledDocument().insertString(taLog.getDocument().getLength(), msg + "\n", color);
            taLog.setCaretPosition(taLog.getDocument().getLength());
        }

        System.out.println(msg);
    }
}
