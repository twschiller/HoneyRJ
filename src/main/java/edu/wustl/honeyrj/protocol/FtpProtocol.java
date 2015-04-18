package edu.wustl.honeyrj.protocol;

import java.util.Vector;
import java.util.regex.Pattern;

import edu.wustl.honeyrj.lowinteraction.LIHelper;
import edu.wustl.honeyrj.lowinteraction.LIProtocol;
import edu.wustl.honeyrj.lowinteraction.TALK_FIRST;

/**
 * FTP protocol implementation for HoneyRJ
 * @author Todd Schiller
 *
 */
public class FtpProtocol implements LIProtocol {
	private final static int NO_CONN = 0;
	private final static int READY = 1;
	private final static int KILLED = 2;
	private final static int USER = 3;
	private final static int LOGGED_IN = 4;
	
	private int connectionState = NO_CONN;

	private int cmdTryCnt = 0;
	private final static int cmdTryMax = 0;

	
	public int getPort() {
		return 21;
	}

	public boolean isConnectionOver() {
		return connectionState == KILLED;
	}
	
	public Vector<String> processInput(String msg) {
		
		switch(connectionState){
		
		case NO_CONN:
			if (msg == null){
				connectionState = READY;
				return LIHelper.vectorFromString("220 Service ready for new user.");
			}else{
				connectionState = KILLED;
				return LIHelper.vectorFromString("421 Service not available, closing control connection.");
			}
		case READY:
			if (msg != null && Pattern.matches("^QUIT\\s?", msg)){
				connectionState = KILLED;	
				return new Vector<String>();
			}else if (msg != null && Pattern.matches("^USER (\\w)+$", msg)){
				connectionState = USER;
				return LIHelper.vectorFromString("331 User name ok, need password.");
			}else if(msg != null && Pattern.matches("^USER\\s?", msg)){
				return LIHelper.vectorFromString("530 Not logged in.");
			}else if(msg != null && Pattern.matches("^USER.*", msg)){
				return LIHelper.vectorFromString("501 Syntax error in parameters or arguments");
			}else{
				return LIHelper.vectorFromString("332 Need account for login.");
			}
		case USER:
			if (msg != null && Pattern.matches("^PASS (\\S)+$", msg)){
				connectionState = LOGGED_IN;
				return LIHelper.vectorFromString("230 User logged in.");
			}else if(msg != null && Pattern.matches("^PASS.*", msg)){
				connectionState = READY;
				return LIHelper.vectorFromString("501 Syntax error in parameters or arguments");
			}else{
				connectionState = KILLED;
				return LIHelper.vectorFromString("221 Service closing control connection.");
			}
		case LOGGED_IN:
			if (msg != null && Pattern.matches("^QUIT\\s?", msg)){
				connectionState = KILLED;	
				return new Vector<String>();
			}else if (msg != null && cmdTryCnt < cmdTryMax){
				cmdTryCnt++;
				return LIHelper.vectorFromString("502 Command not implemented.");
			}else{
				connectionState = KILLED;
				return LIHelper.vectorFromString("221 Service closing control connection.");
			}
		default:
			connectionState = KILLED;
			return LIHelper.vectorFromString("421 Service not available, closing control connection.");
		}
	}

	public TALK_FIRST whoTalksFirst() {
		return TALK_FIRST.SVR_FIRST;
	}

	public String toString(){
		return "FTP";		
	}
}
