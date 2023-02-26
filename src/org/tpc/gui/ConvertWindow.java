package org.tpc.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ConvertWindow extends JFrame {

	public ConvertWindow() {
		setSize(400,200);
		setMinimumSize(new Dimension(400,200));
		setMaximumSize(getMinimumSize());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Converting...");
		setLocationRelativeTo(null);
		setResizable(false);
		setLayout(new BorderLayout(10,10));
		setIconImage(MainWindow.icon.getImage());
		
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
//		panel.setBackground(Color.red);
		
		
		JPanel panel2 = new JPanel();
//		panel2.setBackground(Color.green);
		
		panel2.setBounds(20, 20, 350, 125);
		
		JLabel label = new JLabel();
		label.setPreferredSize(new Dimension(400,50));
		label.setText("Converting...");
		label.setHorizontalAlignment(JLabel.CENTER);
		
		JProgressBar progress = new JProgressBar();
		progress.setPreferredSize(new Dimension(350,25));
		progress.setValue(0);
		progress.setMaximum(100);
		progress.setStringPainted(true);
		
		
		JButton cancel = new JButton();
		label.setSize(100,50);
		cancel.setText("Cancel");
		cancel.setHorizontalAlignment(JButton.CENTER);
		
		
		this.add(panel);	
		panel.add(panel2);
		panel2.add(label, BorderLayout.NORTH);
		panel2.add(progress, BorderLayout.CENTER);
		panel2.add(cancel, BorderLayout.SOUTH);
		
		pack();
		setVisible(true);
	}
}
