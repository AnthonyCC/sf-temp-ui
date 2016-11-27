package com.freshdirect.dataloader.geocodefilter.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
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

import com.freshdirect.framework.util.DurationFormat;

public class GFGui extends JFrame implements ActionListener {
	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final DurationFormat TIME_FORMATTER = new DurationFormat();
	
	private final String VALID_LABEL = "In Zone Adresses: ";
	private final String INVALID_LABEL = "Out Of Zone Adresses: ";
	private final String EXCEPTION_LABEL = "Invalid Adresses Exceptions: ";
	
	//private Color skyblue = new Color(135, 206, 235);

	private String curDir;
	private String sourceName = "";
	private String reportName = "report.txt";
	private File sourceFile = new File(sourceName);
	private File reportFile = new File(reportName);

	// declare file area components
	private JLabel sourceLabel = new JLabel("Source: ");
	private JTextField sourceField = new JTextField(14);
	private JButton sourceBrow = new JButton("Browse...");
	private JPanel sPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	private JLabel reportLabel = new JLabel("Report:  ");
	private JTextField reportField = new JTextField(14);
	private JButton reportBrow = new JButton("Browse...");
	private JPanel rPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	private JPanel fileZone = new JPanel(new GridLayout(3, 3, 2, 2));
	
	private JLabel validLabel = new JLabel(VALID_LABEL);
	private JLabel invalidLabel = new JLabel(INVALID_LABEL);
	private JLabel exceptionsLabel = new JLabel(EXCEPTION_LABEL);

	// declare button components
	private JButton runButton = new JButton("Run the Utility");
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
	private GFTask task;
	private JPanel infoPanel = new JPanel(new GridLayout(4,1,2,2));
	private JPanel fileSubPanel = new JPanel(new GridLayout(2,1,2,2));
	private JCheckBox checkBox = new JCheckBox("Filter restricted addresses", true);
	public GFGui() // the gui constructor and initializer
	{
		super("Geocode Filter Utility");
		setBounds(180, 150, 450, 400);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		curDir = System.getProperty("user.dir") + File.separator;
		// construct files/io zone
		sourceField.setEnabled(false);
		reportField.setEnabled(false);
		sourceField.setText(sourceName);
		reportField.setText(reportName);
		sourceLabel.setForeground(Color.black);
		sPanel.add(sourceLabel);
		sPanel.add(sourceField);
		sPanel.add(sourceBrow);
		//sPanel.setBackground(skyblue);
		reportLabel.setForeground(Color.black);
		rPanel.add(reportLabel);
		rPanel.add(reportField);
		rPanel.add(reportBrow);
		//rPanel.setBackground(skyblue);
        progressBar = new JProgressBar();
        infoPanel.setSize(200,200);
        infoPanel.add(progressBar);
	    infoPanel.add(validLabel);
	    infoPanel.add(invalidLabel);
	    infoPanel.add(exceptionsLabel);
	    //infoPanel.setBackground(skyblue);
	    infoPanel.setBorder(new TitledBorder("Process Info"));
	    infoPanel.setVisible(false);
        fileZone.add(sPanel);
		fileZone.add(rPanel);
		fileZone.add(checkBox);
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
		buttons.add(helpButton);
		buttons.add(aboutButton);
		//buttons.setBackground(skyblue);
		buttons.setBorder(new TitledBorder("RunTime Options"));
		checkWS(); // deactivate runtime until file selected
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
		
		//		Create a timer.
	    timer = new Timer(1000, new ActionListener() {
	        public void actionPerformed(ActionEvent evt) {
	            progressBar.setValue(task.getCurrent());
	            validLabel.setText(VALID_LABEL + task.getValids());
	            invalidLabel.setText(INVALID_LABEL + task.getInvalids());
	            exceptionsLabel.setText(EXCEPTION_LABEL + task.getInvalidExceptions());
	            
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
	                
	                JOptionPane.showMessageDialog(null,
	    					"Geocode Filter complete",
	    					"Success!", JOptionPane.PLAIN_MESSAGE);
	            }
	        }
	    });
	    
	    //add source FILE chooser
		sourceBrow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser chooser = new JFileChooser(curDir);
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.setSelectedFile(sourceFile); // set output default

				int option = chooser.showOpenDialog(GFGui.this);
				if (option == JFileChooser.APPROVE_OPTION) {
					sourceFile = chooser.getSelectedFile();
					try {
						sourceName = sourceFile.getCanonicalPath();
					} catch (IOException e) {
					}
					;
					sourceField.setText(sourceFile.getAbsolutePath()); // show on GUI
					checkWS(); // and adjust runtime button(s)
				} else {/* add cancelled message here */
				}
			}
		});
		// add report FILE chooser
		reportBrow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser chooser = new JFileChooser(curDir);
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.setSelectedFile(reportFile); // set output default
				int option = chooser.showOpenDialog(GFGui.this);
				if (option == JFileChooser.APPROVE_OPTION) {
					reportFile = chooser.getSelectedFile();
					try {
						reportName = reportFile.getCanonicalPath();
					} catch (IOException e) {
					}
					;
					reportField.setText(reportFile.getAbsolutePath()); // show it on
																// GUI
				} else {/* addcancelled message here */
				}
			}
		});
	}

	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if (source == runButton) {
	        task = new GFTask(sourceFile.getPath(), reportFile.getPath(), checkBox.isSelected());
			
			task.go();
			timer.start();
			
			runButton.setEnabled(false);
			area.setText("");
			
			progressBar.setValue(0);
	        progressBar.setMaximum(task.getLengthOfTask());
	        progressBar.setStringPainted(true); //get space for the string  
			infoPanel.setVisible(true);
		} // show something
		if (source == helpButton) {

		}
		if (source == aboutButton) {

		}
	
		if (source == exitButton) {
			System.exit(0);
		}
	}

	// method enables buttons when data present, disables on null
	public void checkWS() {
		boolean flag = true;
		if (sourceName == "") {
			flag = false;
		}
		runButton.setEnabled(flag);
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			
		} catch (Exception e) {
			;
		}
		GFGui gui = new GFGui();
	}
}
