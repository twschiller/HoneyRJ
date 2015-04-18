package edu.wustl.honeyrj.honeyrj;

import edu.wustl.honeyrj.gui.HoneyRJGUI;
import edu.wustl.honeyrj.lowinteraction.LIModule;
import edu.wustl.honeyrj.lowinteraction.LIProtocol;
import edu.wustl.honeyrj.protocol.FtpProtocol;
import edu.wustl.honeyrj.protocol.IrcProtocol;

public class HoneyRJMain {
	private static HoneyRJGUI gui;

	public static void main(String[] args) {
		launchRJ();
		createSampleProtocols();
		
		/* sample code for adding your own protocol */
		/*LIProtocol yourProtocol = new FtpProtocol();
		
		LIModule yourModule = new LIModule(yourProtocol);
		
		gui.AddModule(yourModule); */

	}
	
	private static void launchRJ() {
		HoneyRJ honeyrj = null;
		try {
			honeyrj = new HoneyRJ();
		} catch (HoneyRJException e) {
			System.err.println("Failed to create logging directory...Exiting");
			System.exit(-1);
		}
		
		gui = new HoneyRJGUI(honeyrj);
	}
	
	private static void createSampleProtocols() {
		LIProtocol ftpP = new FtpProtocol();
		LIModule ftpM = new LIModule(ftpP);
		LIProtocol ircP = new IrcProtocol();
		LIModule ircM = new LIModule(ircP);
		
		gui.AddModule(ftpM);
		gui.AddModule(ircM);
	}
}
