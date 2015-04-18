package edu.wustl.honeyrj.lowinteraction;

import java.util.Vector;

/**
 * Interface that defines a protocol that can be plugged into a HoneyRJ
 * @author Eric Peter
 *
 */
public interface LIProtocol {
	
	/**
	 * The return value of this method specifies if the server sends the first message in the protocol (SVR_FIRST) or if the client does (CLIENT_FIRST)
	 * The first message is defined as the first person to transmit a String over the network after the TCP handshake/Java socket setup.
	 * 
	 * @return SVR_FIRST if the server sends the first message (ie SMTP)
	 * @return CLIENT_FIRST if the client sends the first message (ie HTTP)
	 */
	TALK_FIRST whoTalksFirst();
	
	/**
	 * Each time a message is received on the socket from the client, this method is called.
	 * The messageFromClient should be processed as needed and a Vector of String messages should be returned.
	 * Each message will be sent as a separate line to the client.
	 * @see LIHelper#vectorFromString(String)
	 * @param messageFromClient String received on the socket from the client
	 * @return Vector<String> to send to the client on the socket.
	 */
	Vector<String> processInput(String messageFromClient);
	
	/**
	 * Return the port the protocol listens for connections on
	 * ie 80 for HTTP
	 * @return int that is the port this protocol listens on
	 */
	int getPort();
	 
    /**
     * return a textual representation of the protocol name use for use in logs 
     * ie HTTP, FTP, etc
     * @return String containing the name of the protocol as it should appear in a log file.
     */
    String toString();
    
    /**
     * return true if the server believes the connection to be over for whatever reason
     * @return true if the protocol is done talking to the client and does not want to receive or send any more messages
     * @return false if the protocol is still expecting input from the client.
     */
    boolean isConnectionOver();
}

