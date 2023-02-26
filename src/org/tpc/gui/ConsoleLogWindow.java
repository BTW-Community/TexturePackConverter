//https://github.com/BTW-Community/j-btw-2-obj/blob/master/src/org/jmc/gui/GUIConsoleLog.java

package org.tpc.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import org.json.simple.parser.ParseException;
import org.tpc.Version;

@SuppressWarnings("serial")
public class ConsoleLogWindow extends JFrame{	
		
		private JScrollPane spPane;
		private JTextPane taLog;

		public ConsoleLogWindow(){
			setLayout(new BorderLayout());
			setBackground(Color.BLACK);
			setSize(600, 300);
			setPreferredSize(new Dimension(600,300));
			setTitle("Console log");
			
	        addWindowListener(new WindowAdapter() {
	            @Override
	            public void windowClosing(WindowEvent e) {
//	                System.out.println("WindowClosingDemo.windowClosing");
	            	MainWindow.consoleItem.setEnabled(true);
	            }
	        });
			
			JPanel contentPane = new JPanel();contentPane.setLayout(new BorderLayout());
			contentPane.setBackground(Color.BLACK);
			setContentPane(contentPane);
			
			JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
			mainPanel.setBackground(Color.BLACK);
			
			taLog = new JTextPane();
			taLog.setEditable(false);
			taLog.setFont(new Font("Lucida Console", 0, 14));
			taLog.setBackground(Color.BLACK);

			spPane = new JScrollPane(taLog);
			
			contentPane.add(spPane);
			
			final JCheckBox openOnStart = new JCheckBox("Open Console On Startup", MainWindow.settings.shouldLogOpenOnStartUp());
			openOnStart.setForeground(Color.WHITE);
			openOnStart.setBackground(Color.BLACK);
			openOnStart.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
//					MainWindow.settings.getPreferences().putBoolean("OPEN_CONSOLE_ON_START", openOnStart.isSelected());
					try {
						System.out.println(openOnStart.isSelected());
						MainWindow.settings.writeSettings("openLogStartUp", openOnStart.isSelected());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
			
			contentPane.add(openOnStart, BorderLayout.SOUTH);
			
			pack();
//			setVisible(true);
		}
		
		public void log(String msg, boolean isError) {		
			try {
				Style color = taLog.addStyle("color", null);
				StyleConstants.setForeground(color, isError ? Color.RED : Color.WHITE);
				taLog.getStyledDocument().insertString(taLog.getDocument().getLength(), msg + "\n", color);
				taLog.setCaretPosition(taLog.getDocument().getLength());
				
				System.out.println(msg);
			} catch (BadLocationException e) { /* don't care */	}
		}
}
