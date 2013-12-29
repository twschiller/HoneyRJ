package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import lowinteraction.LIModule;
/**
 * Provides the GUI for an individual module.  These are added into the LIModuleContainer
 * @author Eric Peter
 *@see LIModuleContainer
 */
public class LIModuleGUI extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6195602253907757865L;
	private LIModule _liModule;
	private JLabel _mainInfo;
	private JLabel _numConnections;
	private JButton _start;
	private JButton _stop;
	private JButton _pauseResume;
	
	/**
	 * Wrap an LIModule with a GUI
	 * @param liModule
	 */
	public LIModuleGUI(LIModule liModule) {
		
		this.setBackground(Color.RED);
		_liModule = liModule;
		_liModule.registerWithGUI(this);
		setPreferredSize(new Dimension(640,75));
		setMaximumSize(new Dimension(640,75));
		
		setBorder(BorderFactory.createRaisedBevelBorder());
		//label
		_mainInfo = new JLabel(_liModule.toString() + " (" + _liModule.getPort() +")");
		_mainInfo.setVerticalAlignment(JLabel.CENTER);
		_mainInfo.setHorizontalAlignment(JLabel.CENTER);
		
		//number of connections
		_numConnections = new JLabel("Hackers connected: 0");
		_numConnections.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		_numConnections.setVerticalAlignment(JLabel.CENTER);
		_numConnections.setHorizontalAlignment(JLabel.CENTER);
		_numConnections.setPreferredSize(new Dimension(150,25));
		
		//start/stop/pause buttons
		_start = new JButton("Start");
		_start.setActionCommand("start");
		_start.setToolTipText("Click to start the module");
		_start.addActionListener(this);
		
		_stop = new JButton("Stop");
		_stop.setActionCommand("stop");
		_stop.setToolTipText("Click to stop the module");
		_stop.addActionListener(this);
		_stop.setEnabled(false);
		
		_pauseResume = new JButton("Pause");
		_pauseResume.setActionCommand("pause");
		_pauseResume.setToolTipText("Click to pause listening for connections");
		_pauseResume.addActionListener(this);
		_pauseResume.setEnabled(false);
		
		add(_start);
		add(_stop);
		add(_mainInfo);
		add(_numConnections);
		add(_pauseResume);
		
		
	}

	/**
	 * Return the port used by the LIModule contained within
	 * @return the port used by the LIModule contained within
	 */
	public int getPort() {
		return _liModule.getPort();
	}

	/**
	 * Start this module and change the background color
	 */
	public void startInteractionModule() {
		try {
			_liModule.startInteractionModule();
			setBackground(Color.GREEN);
		} catch (IOException e) {
			setBackground(Color.ORANGE);
		}
		_stop.setEnabled(true);
		_start.setEnabled(false);
		_pauseResume.setEnabled(true);
	}

	/**
	 * Pause this module and change the background color
	 */
	public void pauseListeningForConnections() {
		_liModule.pauseListeningForConnections();
		setBackground(Color.YELLOW);
		_pauseResume.setText("Resume");
		_pauseResume.setActionCommand("resume");
		_pauseResume.setToolTipText("Click to resume listening for connections");
	}

	/**
	 * Resume this module and change the background color
	 */
	public void resumeListeningForConnections() {
		if(_liModule.resumeListeningForConnections())
			setBackground(Color.GREEN);
		else 
			setBackground(Color.ORANGE);
		_pauseResume.setText("Pause");
		_pauseResume.setActionCommand("pause");
		_pauseResume.setToolTipText("Click to pause listening for connections");
	}

	/**
	 * Stop this module and change the background color
	 */
	public void stopInteractionModule() {
		_liModule.stopInteractionModule();
		setBackground(Color.RED);
		_stop.setEnabled(false);
		_start.setEnabled(true);
		_pauseResume.setEnabled(false);
	}
	
	/**
	 * Respond to the start/stop/pause buttons
	 */
	public void actionPerformed(ActionEvent e) {
		if("start".equals(e.getActionCommand())) { //start button clicked
			_stop.setEnabled(true);
			_start.setEnabled(false);
			_pauseResume.setEnabled(true);
			startInteractionModule();
		} else if ("stop".equals(e.getActionCommand())) {
			_stop.setEnabled(false);
			_start.setEnabled(true);
			_pauseResume.setEnabled(false);
			stopInteractionModule();
		} else if ("pause".equals(e.getActionCommand())) {
			_pauseResume.setText("Resume");
			_pauseResume.setActionCommand("resume");
			_pauseResume.setToolTipText("Click to resume listening for connections");
			pauseListeningForConnections();
		} else if ("resume".equals(e.getActionCommand())) {
			_pauseResume.setText("Pause");
			_pauseResume.setActionCommand("pause");
			_pauseResume.setToolTipText("Click to pause listening for connections");
			resumeListeningForConnections();
		}
		
	}
	
	/**
	 * update the display to reflect the number of active connections
	 * @param newNum
	 */
	public void setNumberConnections(int newNum) {
		_numConnections.setText("Hackers connected: " + newNum);
	}
	
	
}
