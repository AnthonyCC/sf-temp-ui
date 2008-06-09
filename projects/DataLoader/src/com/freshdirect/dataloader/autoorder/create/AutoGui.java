package com.freshdirect.dataloader.autoorder.create;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import com.freshdirect.dataloader.autoorder.create.util.IConstants;
import com.freshdirect.framework.util.DurationFormat;
import com.michaelbaranov.microba.calendar.DatePicker;
import com.michaelbaranov.microba.common.CommitEvent;
import com.michaelbaranov.microba.common.CommitListener;

public class AutoGui extends JFrame implements ActionListener {
	
	private final DurationFormat TIME_FORMATTER = new DurationFormat();
		
	//private Color skyblue = new Color(135, 206, 235);

	private String curDir = System.getProperty("user.dir") + File.separator;;
	private String sourceName = "";
	
	private File sourceFile = new File(sourceName);
	private File reportFile = new File(curDir);

	// declare file area components
	private JLabel sourceLabel = new JLabel("Delivery Date: ");
	private JTextField sourceField = new JTextField(14);
	private JButton sourceBrow = new JButton("Browse...");
	private JPanel sPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	private JLabel reportLabel = new JLabel("Item File:  ");
	private JTextField reportField = new JTextField(14);
	private JButton reportBrow = new JButton("...");
	private JPanel rPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	private JPanel fileZone = new JPanel(new GridLayout(3, 3, 2, 2));
	
	private JPanel cPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
	// declare button components
	private JButton runButton = new JButton("Run For Order");
	private JButton runCustButton = new JButton("Run For Customer");
	private JButton helpButton = new JButton("Help");
	private JButton aboutButton = new JButton("About");
	private JButton exitButton = new JButton("Close");
	private JPanel buttons = new JPanel(new GridLayout(4, 1, 2, 2));

	// main is split into 2 grid regions
	private JPanel main = new JPanel(new BorderLayout());
	private JPanel bottom = new JPanel(new GridLayout(1,1,2,2));
	private JScrollPane scrollPane = new JScrollPane();
	static JTextArea area = new JTextArea(5, 20);
	private JProgressBar progressBar;
	private Timer timer;
	private TesterTask task;
	private JPanel infoPanel = new JPanel(new GridLayout(4,1,2,2));
	private JPanel fileSubPanel = new JPanel(new GridLayout(2,1,2,2));
	
	DatePicker calendarControl = new DatePicker();
	
	private JLabel customerLabel = new JLabel("Customers: ");
	private JTextField customerField = new JTextField(5);
	
	private JLabel prefixLabel = new JLabel("Prefix: ");
	private JTextField prefixField = new JTextField(5);
	
	
	public AutoGui() // the gui constructor and initializer
	{
		super("Tester");
		setBounds(180, 150, 450, 400);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		sourceField.setEnabled(false);
		reportField.setEnabled(false);
		reportField.setText(curDir);
		
		sourceField.setText(sourceName);
		
		sourceLabel.setForeground(Color.black);
		
		sPanel.add(sourceLabel);
		//sPanel.add(sourceField);
		//sPanel.add(sourceBrow);
		
		calendarControl.setPreferredSize(new Dimension(100,25));
		calendarControl.setFieldEditable(false);
		
		sPanel.add(calendarControl);
		
		cPanel.add(customerLabel);
		cPanel.add(customerField);
		
		cPanel.add(prefixLabel);
		cPanel.add(prefixField);
		
		//sPanel.setBackground(skyblue);
		reportLabel.setForeground(Color.black);
		rPanel.add(reportLabel);
		rPanel.add(reportField);
		rPanel.add(reportBrow);
		//rPanel.setBackground(skyblue);
        progressBar = new JProgressBar();
        infoPanel.setSize(200,200);
        infoPanel.add(progressBar);
	    
        	    	    
	    infoPanel.setBorder(new TitledBorder("Process Info"));
	    infoPanel.setVisible(false);
        fileZone.add(sPanel);
		fileZone.add(rPanel);
		fileZone.add(cPanel);
		//fileZone.setBackground(skyblue);
		fileZone.setBorder(new TitledBorder("File Settings"));
	    fileSubPanel.add(fileZone);
	    fileSubPanel.add(infoPanel);
	    //fileSubPanel.setBackground(skyblue);
		scrollPane.getViewport().add( area );
	    bottom.add(scrollPane);		
		// construct controls zone
	    
	    //hide buttons that im not currently using
	    aboutButton.setVisible(false);
	    helpButton.setVisible(false);
	    
		buttons.add(runButton);
		buttons.add(exitButton);
		buttons.add(runCustButton);
		buttons.add(aboutButton);
		//buttons.setBackground(skyblue);
		buttons.setBorder(new TitledBorder("RunTime Options"));
		
		// complete the panel assembly
		main.add("Center", fileSubPanel);
		main.add("East", buttons);
		main.add("South", bottom);
		this.getContentPane().add("Center", main);
		setVisible(true);
		// now register button events
		runButton.addActionListener(this);
		helpButton.addActionListener(this);
		aboutButton.addActionListener(this);
		exitButton.addActionListener(this);
		runCustButton.addActionListener(this);
		
		//		Create a timer.
	    timer = new Timer(1000, new ActionListener() {
	        public void actionPerformed(ActionEvent evt) {
	        	progressBar.setMaximum(task.getLengthOfTask());
	            progressBar.setValue(task.getCurrent());
	            	            
	            String s = task.getMessage();
	            if (s != null) {
	                area.append(s + "[" + TIME_FORMATTER.format(task.getElapsedTime()) + "]" + "\n");
	                area.setCaretPosition(area.getDocument().getLength());
	            }
	            if (task.isDone()) {
	            	progressBar.setValue(task.getLengthOfTask());
	                Toolkit.getDefaultToolkit().beep();
	                timer.stop();
	                runButton.setEnabled(true);
	                area.append("Process Complete " + "[" + TIME_FORMATTER.format(task.getElapsedTime()) + "]");
	                
	                JOptionPane.showMessageDialog(AutoGui.this,
	    					"Processing Complete",
	    					"Success!", JOptionPane.PLAIN_MESSAGE);
	            }
	        }
	    });
	    
	    //add source FILE chooser
	    calendarControl.addCommitListener(new CommitListener() {
			public void commit(CommitEvent ae) {
				//checkWS(); 				
			}
		});
		// add report FILE chooser
		reportBrow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser chooser = new JFileChooser(curDir);
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.setSelectedFile(reportFile); // set output default
				int option = chooser.showOpenDialog(AutoGui.this);
				if (option == JFileChooser.APPROVE_OPTION) {
					reportFile = chooser.getSelectedFile();
					reportField.setText(reportFile.getAbsolutePath()); // show it on
																// GUI
				} else {/* addcancelled message here */
				}
			}
		});
	}

	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		String type=null;
		if (source == runButton ||source == runCustButton) {	
			if(source == runButton) {               
				type=IConstants.CREATE_ORDERS;
            } else {
            	type=IConstants.CREATE_CUSTOMER;
            }
			try {
		        task = new TesterTask(calendarControl.getDate(), reportFile.getPath()
		        			, customerField.getText(), prefixField.getText(), type);
				
				task.go();
				timer.start();
				
				runButton.setEnabled(false);
				area.setText("");
				
				progressBar.setValue(0);
		        progressBar.setMaximum(10000);
		        progressBar.setStringPainted(true); //get space for the string  
				infoPanel.setVisible(true);
			} catch(Exception e) {
				JOptionPane.showMessageDialog(AutoGui.this,
    					e.getMessage(),
    					"Error!", JOptionPane.ERROR_MESSAGE);
			}
		} // show something
		if (source == helpButton) {

		}
		if (source == aboutButton) {

		}
	
		if (source == exitButton) {
			System.exit(0);
		}
	}

	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			
		} catch (Exception e) {
			;
		}
		AutoGui gui = new AutoGui();
	}
}

