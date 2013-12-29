package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
/**
 * Provides the module pane, containing any number of modules
 * @author Eric Peter
 *
 */
public class LIModuleContainer extends JPanel {

	private static final long serialVersionUID = -8729526289292675328L;
	
	private ArrayList<LIModuleGUI> _modules;
	/**
	 * Create a container for multiple LIModules
	 */
	public LIModuleContainer() {
		_modules = new ArrayList<LIModuleGUI>();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(Color.LIGHT_GRAY);
		setVisible(true);
	}
	
	/**
	 * Add a LIModuleGUI to this container
	 * @param toAdd
	 */
	public void AddModule(LIModuleGUI toAdd) {
		add(Box.createRigidArea(new Dimension(640,10))); //add spacer
		_modules.add(toAdd);
		add(toAdd);
		toAdd.setVisible(true);
	}

	/**
	 * Pause all modules.
	 */
	public void pauseAll() {
		for(LIModuleGUI li : _modules) {
			li.pauseListeningForConnections();
		}
		
	}

	/**
	 * resume all modules
	 */
	public void resumeAll() {
		for(LIModuleGUI li : _modules) {
			li.resumeListeningForConnections();
		}
		
	}

	/**
	 * stop all modules
	 */
	public void stopAll() {
		for(LIModuleGUI li : _modules) {
			li.stopInteractionModule();
		}
		
	}

	/**
	 * start all modules
	 */
	public void startAll() {
		for(LIModuleGUI li : _modules) {
			li.startInteractionModule();
		}
		
	}
}
