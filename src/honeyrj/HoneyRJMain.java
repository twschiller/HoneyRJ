package honeyrj;

import gui.HoneyRJGUI;
import lowinteraction.LIModule;
import lowinteraction.LIProtocol;
import protocol.FtpProtocol;
import protocol.IrcProtocol;

public class HoneyRJMain {
	private static HoneyRJ honeyrj;
	private static HoneyRJGUI gui;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launchRJ();
		createSampleProtocols();
		
		/* sample code for adding your own protocol */
		/*LIProtocol yourProtocol = new FtpProtocol();
		
		LIModule yourModule = new LIModule(yourProtocol);
		
		gui.AddModule(yourModule); */

	}
	
	private static void launchRJ() {
		honeyrj = null;
		
		
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
