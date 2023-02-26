package org.tpc.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import org.tpc.Version;

public class AboutWindow extends JFrame {

	public AboutWindow ()
	{
		setSize(300,200);
		setMinimumSize(new Dimension(300,200));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("About");
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(10, 10));
		setResizable(false);
		
        JSeparator sep1 = createSeperator();
        JSeparator sep2 = createSeperator();
        JSeparator sep3 = createSeperator();
        
		JPanel topPanel = new JPanel();
		JPanel bottomPanel = new JPanel();
		JPanel leftPanel = new JPanel();
		JPanel rightPanel = new JPanel();
		
//		topPanel.setBackground(Color.red);
//		bottomPanel.setBackground(Color.green);
//		leftPanel.setBackground(Color.yellow);
//		rightPanel.setBackground(Color.magenta);
		
		topPanel.setPreferredSize(new Dimension(50, 100));
		bottomPanel.setPreferredSize(new Dimension(50, 100));
		leftPanel.setPreferredSize(new Dimension(150, 100));
		rightPanel.setPreferredSize(new Dimension(150, 100));
		
		topPanel.setLayout(new GridLayout(0,1));
		bottomPanel.setLayout(new GridLayout(0,1));
		leftPanel.setLayout(new GridLayout(0,1));
		rightPanel.setLayout(new GridLayout(0,1));
		
		add(topPanel, BorderLayout.NORTH);
		add(bottomPanel, BorderLayout.SOUTH);
		add(leftPanel, BorderLayout.WEST);
		add(rightPanel, BorderLayout.EAST);
		
		JLabel title = addlabel("Texture Pack Converter", Font.BOLD, 12);
		JLabel version = addlabel("Version: " + Version.getCurrentVersion(false), Font.PLAIN, 12);
			
		JLabel credits = addlabel("Credits", Font.BOLD, 12);
		JLabel sock = addlabel("Sockthing", Font.PLAIN, 12);
		JLabel zhil = addlabel("Zhil", Font.PLAIN, 12);
		JLabel obj = addlabel("jBTW2Obj", Font.PLAIN, 12);
		
		JLabel code = addlabel("Code", Font.BOLD, 12);
		JLabel source = addlabel("Source Code:", Font.PLAIN, 12);
		JLabel url = addlabel("github.com/BTW-Community/TexturePackConverter/", Font.ITALIC, 10);
		
		JLabel year = addlabel( "2023", Font.PLAIN, 12);
		JLabel licence = addlabel( "Licence here", Font.PLAIN, 12);
		
		
		topPanel.add(title);
		topPanel.add(version);
		
//		leftPanel.add(sep1);		
		leftPanel.add(credits);
		leftPanel.add(sock);
		leftPanel.add(zhil);
		leftPanel.add(obj);
		
//		rightPanel.add(sep2);
		rightPanel.add(code);
		rightPanel.add(source);
		rightPanel.add(url);
		
//		bottomPanel.add(sep3);
		bottomPanel.add(year);
		bottomPanel.add(licence);
		
		pack();
//		setVisible(true);
	}

	private JSeparator createSeperator() {
		JSeparator sep = new JSeparator();
        sep.setOrientation(SwingConstants.HORIZONTAL);
		return sep;
	}

	private JLabel addlabel( String text, int style, int size) {
		JLabel label = new JLabel(text);
		label.setFont(new Font("Dialog", style, size));
		label.setHorizontalAlignment(JLabel.CENTER);
		return label;
	}
}
