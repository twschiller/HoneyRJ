/**
 * 
 */
package protocol;

import java.util.Vector;

import lowinteraction.LIHelper;
import lowinteraction.LIProtocol;
import lowinteraction.TALK_FIRST;

/**
 * @author ecp1
 *
 */
public class SampleServerFirstProtocol implements LIProtocol {
	boolean connected;
	int State;
	static final int S1 = 1;
	static final int S2 = 2;
	
	@Override
	public String toString() {
		return "TestProtocol";
	}

	static final int S3 = 3;
	static final int S4 = 4;
	static final int S5 = 5;
	static final int S6 = 6;
	
	public SampleServerFirstProtocol() {
		connected = true;
		State = S1;
	}

	public int getPort() {
		return 65000;
	}


	public boolean isConnectionOver() {
		return !connected;
	}


	public Vector<String> processInput(String messageFromClient) {
		switch(State) {
		case S1:
			State = S2;
			return  LIHelper.vectorFromString("hello");
		case S2:
			if(messageFromClient.equals("hi"))	State = S3;
			if(messageFromClient.equals("cya"))	connected = false;
			return LIHelper.vectorFromString("hello - '" + messageFromClient + "'");
		case S3:
			connected = false;
			return  LIHelper.vectorFromString("bye bye");
		default:
			connected = false;
			return  LIHelper.vectorFromString("woops");
		
		}
	}


	public TALK_FIRST whoTalksFirst() {
		return TALK_FIRST.SVR_FIRST;
	}

}
