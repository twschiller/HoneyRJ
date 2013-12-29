package gui;

import honeyrj.HoneyRJ;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import lowinteraction.LIModule;

/**
 * Provides the main application frame and creates the header and module pane.
 * @author Eric Peter
 *
 */
public class HoneyRJGUI extends JFrame implements ActionListener {

	private JButton startButton;
	private JButton stopButton;
	private JButton pauseResumeButton;
	private static final long serialVersionUID = -2936644671086250830L;
	private LIModuleContainer _moduleContainer;
	private HoneyRJ _honeyRJ;
	
	/**
	 * Creates the main GUI frame and populates it with the top panel and frame for modules.
	 * @param rj HoneyRJ to wrap.
	 * @author Eric Peter
	 */
	public HoneyRJGUI(HoneyRJ rj) {
		super("HoneyRJ");
		_honeyRJ = rj;

		createTopPane();
		
		//create module container
		_moduleContainer = new LIModuleContainer();
		JScrollPane moduleScroll = new JScrollPane(_moduleContainer);
		getContentPane().add(moduleScroll, BorderLayout.CENTER);
		
        //Display the window.
        pack();
        setVisible(true);

	}

	/**
	 * Add the given LIModule to the HoneyRJ application.
	 * @param toAdd LIModule to add
	 */
	public void AddModule(LIModule toAdd) {
		_honeyRJ.RegisterService(toAdd);
		_moduleContainer.AddModule(new LIModuleGUI(toAdd));
		pack();
	}

	private void createTopPane() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDefaultLookAndFeelDecorated(true);
		setSize(640,480);
		setPreferredSize(new Dimension(700,480));
		JLabel startLabel = new JLabel("Started");
		startLabel.setBackground(Color.GREEN);
		startLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		startLabel.setOpaque(true);
		startLabel.setPreferredSize(new Dimension(55,30));
		startLabel.setHorizontalAlignment(JLabel.CENTER);
		startLabel.setVerticalAlignment(JLabel.CENTER);
		
		JLabel stopLabel = new JLabel("Stopped");
		stopLabel.setBackground(Color.RED);
		stopLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		stopLabel.setOpaque(true);
		stopLabel.setPreferredSize(new Dimension(55,30));
		stopLabel.setHorizontalAlignment(JLabel.CENTER);
		stopLabel.setVerticalAlignment(JLabel.CENTER);
		
		JLabel pauseLabel = new JLabel("Paused");
		pauseLabel.setBackground(Color.YELLOW);
		pauseLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		pauseLabel.setOpaque(true);
		pauseLabel.setPreferredSize(new Dimension(55,30));
		pauseLabel.setHorizontalAlignment(JLabel.CENTER);
		pauseLabel.setVerticalAlignment(JLabel.CENTER);
		
		JLabel errorLabel = new JLabel("Error");
		errorLabel.setBackground(Color.ORANGE);
		errorLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		errorLabel.setOpaque(true);
		errorLabel.setPreferredSize(new Dimension(55,30));
		errorLabel.setHorizontalAlignment(JLabel.CENTER);
		errorLabel.setVerticalAlignment(JLabel.CENTER);
		
		JPanel legend = new JPanel();
		GridLayout legendGL = new GridLayout(0,2);
		legend.setLayout(legendGL);
		legendGL.setHgap(4);
		legendGL.setVgap(4);
		legend.add(startLabel);
		legend.add(pauseLabel);
		legend.add(stopLabel);
		legend.add(errorLabel);
		
		startButton = new JButton("Start All");
		startButton.setActionCommand("startall");
		startButton.setPreferredSize(new Dimension(100,75));
		startButton.addActionListener(this);
		startButton.setToolTipText("Click to start all modules listening for connections");
		stopButton = new JButton("Stop All");
		stopButton.setActionCommand("stopall");
		stopButton.setPreferredSize(new Dimension(100,75));
		stopButton.addActionListener(this);
		stopButton.setToolTipText("Click to stop all modules from listening for connections");
		stopButton.setEnabled(false);
		pauseResumeButton = new JButton("Pause All");
		pauseResumeButton.setPreferredSize(new Dimension(100,75));
		pauseResumeButton.setActionCommand("pauseall");
		pauseResumeButton.addActionListener(this);
		pauseResumeButton.setToolTipText("Click to pause all modules from listening for connections");
		pauseResumeButton.setEnabled(false);
		
		JPanel masterButtons = new JPanel();
		masterButtons.add(startButton);
		masterButtons.add(stopButton);
		masterButtons.add(pauseResumeButton);
		
		JPanel topPanel = new JPanel();
		topPanel.add(legend);
		topPanel.add(masterButtons);
		
		getContentPane().add(topPanel, BorderLayout.PAGE_START);

	}
	
	/**
	 * Respond to the start/stop/pause all buttons
	 */
	public void actionPerformed(ActionEvent e) {
		if("startall".equals(e.getActionCommand())) { //start button clicked
			startButton.setEnabled(false);
			stopButton.setEnabled(true);
			pauseResumeButton.setEnabled(true);
			_moduleContainer.startAll();
		} else if ("stopall".equals(e.getActionCommand())) {
			startButton.setEnabled(true);
			stopButton.setEnabled(false);
			pauseResumeButton.setEnabled(false);
			_moduleContainer.stopAll();
		} else if ("pauseall".equals(e.getActionCommand())) {
			pauseResumeButton.setText("Resume All");
			pauseResumeButton.setActionCommand("resumeall");
			pauseResumeButton.setToolTipText("Click to resume listening for connections");
			_moduleContainer.pauseAll();
		} else if ("resumeall".equals(e.getActionCommand())) {
			pauseResumeButton.setText("Pause All");
			pauseResumeButton.setActionCommand("pauseall");
			pauseResumeButton.setToolTipText("Click to pause listening for connections");
			_moduleContainer.resumeAll();
		}
		
	}

}
