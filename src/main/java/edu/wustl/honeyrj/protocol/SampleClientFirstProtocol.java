package edu.wustl.honeyrj.protocol;

import java.util.Vector;

import edu.wustl.honeyrj.lowinteraction.LIHelper;
import edu.wustl.honeyrj.lowinteraction.LIProtocol;
import edu.wustl.honeyrj.lowinteraction.TALK_FIRST;

/**
 * @author Eric Peter
 */
public class SampleClientFirstProtocol implements LIProtocol {
	boolean connected;
	int State;
	static final int S1 = 1;
	static final int S2 = 2;
	static final int S3 = 3;
	static final int S4 = 4;
	static final int S5 = 5;
	static final int S6 = 6;
	
	public SampleClientFirstProtocol() {
		connected = true;
		State = S1;
	}

	public int getPort() {
		return 65001;
	}


	public boolean isConnectionOver() {
		return !connected;
	}


	public Vector<String> processInput(String messageFromClient) {
		switch(State) {
		case S1:
			if(messageFromClient.equals("toddgay"))		State = S2;
			return  LIHelper.vectorFromString("HELLO");
		case S2:
			if(messageFromClient.equals("cya"))	State = S3;
			if(messageFromClient.equals("hi"))	connected = false;
			return  LIHelper.vectorFromString("hello333 - '" + messageFromClient + "'");
		case S3:
			connected = false;
			return  LIHelper.vectorFromString("you said to me: " + messageFromClient);
		default:
			connected = false;
			return  LIHelper.vectorFromString("woops");
		
		}
	}

	@Override
	public String toString() {
		return "Test2Protocol";
	}

	public TALK_FIRST whoTalksFirst() {
		return TALK_FIRST.CLIENT_FIRST;
	}

}
