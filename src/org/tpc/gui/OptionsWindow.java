package org.tpc.gui;

import org.tpc.gui.MainWindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.simple.parser.ParseException;
import org.tpc.OptionsReader;

@SuppressWarnings("serial")
public class OptionsWindow extends JFrame {

	private OptionsReader options;

	public OptionsWindow(OptionsReader options) throws Exception {
		
		super("Options Window");
		
		this.options = options;
		
		setSize(400,250);
		setMinimumSize(new Dimension(250,400));
		setMaximumSize(getMinimumSize());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Options");
		setLocationRelativeTo(null);
		setResizable(false);
		setIconImage(MainWindow.icon.getImage());
		
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
//                System.out.println("WindowClosingDemo.windowClosing");
            	MainWindow.optionsButton.setEnabled(true);
            }
        });
        
        JPanel topPanel = new JPanel();
//        topPanel.setBackground(Color.green);
        topPanel.setPreferredSize(new Dimension(100, 30));
        topPanel.setLayout(new BorderLayout(25,25));
        add( topPanel, BorderLayout.NORTH);
        
        JPanel panel = new JPanel();
//        panel.setBackground(Color.red);
        panel.setPreferredSize(new Dimension(100, 100));
        panel.setLayout(new BorderLayout(50,50));
        add(panel);
        
        
        JLabel label = new JLabel();
        label.setText("Enable/Disable Conversion of:");
        label.setSize(350, 50);
        label.setMinimumSize(new Dimension(350,50));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        topPanel.add(label);
        
        JPanel optionsPanel = new JPanel();
//      panel.setBackground(Color.red);
        optionsPanel.setPreferredSize(new Dimension(100, 100));
        optionsPanel.setLayout(new GridLayout(10,3));
      	add(optionsPanel);
        
        OptionsReader reader = new OptionsReader();        
        Map map = reader.getOptions();
        
        Iterator<Map.Entry> itr1 = map.entrySet().iterator();
        while (itr1.hasNext()) {
            Map.Entry pair = itr1.next();
//            System.out.println(pair.getKey() + " : " + pair.getValue());            
            addCheckBox((String)pair.getKey(),(Boolean)pair.getValue(), optionsPanel);
        }
        
		pack();
		setVisible(true);
		
	}

	private void addCheckBox(String name, Boolean selected, JPanel panel) {
		JCheckBox box = new JCheckBox();
    	box.setText(name);
    	box.setSelected(selected);
    	box.setFocusable(false);
    	box.addActionListener(e -> {
			try {
				enabled(box);
			} catch (IOException | ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

    	if (box.isSelected())
    	{
    		box.setForeground(Color.darkGray);
    	}
    	else box.setForeground(Color.lightGray);
    	
    	panel.add(box);
	}

	private void enabled(JCheckBox box) throws IOException, ParseException {
		//MainWindow.consoleLog.log("Options: \"" + box.getText() + "\" set to " + box.isSelected(),false);
		
		options.writeOption((String) box.getText(), box.isSelected());
		
    	if (box.isSelected())
    	{
    		box.setForeground(Color.darkGray);
    	}
    	else box.setForeground(Color.lightGray);
	}
}
