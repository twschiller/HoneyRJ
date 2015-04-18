package edu.wustl.honeyrj.lowinteraction;


import edu.wustl.honeyrj.gui.LIModuleGUI;
import edu.wustl.honeyrj.honeyrj.HoneyRJ;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Date;
import java.util.TreeMap;

import edu.wustl.honeyrj.logging.LogFile;

/**
 * An LIModule listens on a port specified by a LIProtocol and launches threads to handle client connections.
 * LIModules are part of a HoneyRJ
 * @see HoneyRJ
 * @see LIModuleThread
 * @author Eric Peter
 *
 */
public class LIModule implements Runnable {
	/**
	 * a reference to the protocol that this Module should run
	 */
	LIProtocol _protocol;
	/**
	 * the socket that listens for connections
	 */
	private ServerSocket _server;
	
	/**
	 * return the port this Module listens on (as defined by the protocol)
	 * @see LIProtocol#getPort()
	 * @return the port this Module listens on (as defined by the protocol)
	 */
	public int getPort() {
		return _protocol.getPort();
	}

	/**
	 * stores all the logRecords generated, sorted by timestamp
	 * @see LogFile
	 */
	private TreeMap<Date,LogFile> _logFiles; 
	
	/**
	 * a reference to the containing HoneyRJ
	 * @see HoneyRJ
	 */
	private HoneyRJ _parent;
	
    private int numberConnections; //might be used later to limit # of connections at once.
	
    /**
     * a internal reference to the thread this module is running in
     * @see Thread
     */
	private volatile Thread _thread; //we start ourselves in a thread object so we can "safely" stop later (Java claims this is the right way)
	/**
	 * true if the module is currently listening for client connections
	 * NOTE: this is limited in the fact that if accept has already been called, that socket will still connect but another socket will not be created until listening = true.
	 */
	private volatile boolean listening; 
	
	/**
	 * reference to GUI container
	 */
	private LIModuleGUI _gui;

	/**
	 * Define which GUI object we are contained in for updates on number of connection
	 * @param _gui LIModuleGUI to register with
	 * @see LIModuleGUI
	 */
	public void registerWithGUI(LIModuleGUI gui) {
		this._gui = gui;
	}

	/**
	 * Create the module from the given protocol
	 * @param protocol LIProtcol object to implement here
	 * @see LIProtocol 
	 */
	public LIModule(LIProtocol protocol) {
		_protocol = protocol;
		_logFiles = new TreeMap<Date, LogFile>();
		_thread = null;
        numberConnections = 0; //might be used later.
	}

	/**
	 * Called once a HoneyRJ adds this module to itself.
	 * Allows this module to be created in memory but not yet registered to a HoneyRJ
	 * @see HoneyRJ#RegisterService(LIModule)
	 * @param honey parent HoneyRJ
	 */
	public void registerParent(HoneyRJ honey) {
		_parent = honey;
		
	}
	
	/**
	 * thread run method 
	 * start listening on the socket and launch sub-threads to handle connections when clients connect
	 */
	public void run() {
		Thread thisThread = Thread.currentThread(); //by storing this, we can safely stop ourselves later
		while(_thread == thisThread) { //while running (so we can stop later)
			while (listening) { //allow us to temporary stop listening subject to limitation mentioned above
				try {
					new Thread(new LIModuleThread(_server.accept(), this)).start();
                   // numberConnections++;
				} catch (SocketException e) {
					//let us die because someone stopped us
					//e.printStackTrace();
				} catch (IOException e1) {
					//let us die and retry loop
					//e1.printStackTrace();
				} catch (LIModuleException e) {
					// try again
					//e.printStackTrace();
				}
				try {
					Thread.sleep(HoneyRJ.TIME_WAIT_CONNECTION);
				} catch (InterruptedException ignored) {
					//prevent a DOS attack and high cpu during pause state
				}
			}
			try {
				Thread.sleep(HoneyRJ.TIME_WAIT_CONNECTION);
			} catch (InterruptedException ignored) {
				//prevent a DOS attack and high cpu during pause state
			}
		}
		
		//if we got here, the thread is dead, let us die
		
	}

	/**
	 * instruct the module to begin listening
	 * does nothing if module is already started
	 */
	public boolean resumeListeningForConnections() {
		
		if(_server == null)
			try {
				_server = new ServerSocket(_protocol.getPort());
			} catch (IOException e) { return false; } //create socket, let our caller deal with an exception
		listening = true;
		return true;
	}
	
	/**
	 * 
	 * @return HoneyRJ that is our parent
	 */
	public HoneyRJ getParent() {
		return _parent;
	}

	/**
	 * Stop listening for client connections temporarily
	 */
	public void pauseListeningForConnections() {
		listening = false;
		try {
			if(_server != null) {
				_server.close();
			}
		} catch (IOException ignored) {

		}
		_server = null;
	}

	/**
	 * Launch into a thread and start listening for client connections
	 * @throws IOException if the socket can't be started
	 */
	public void startInteractionModule() throws IOException {
		listening = true;
		if(_server == null) //create the socket
			_server = new ServerSocket(_protocol.getPort()); //create socket, let our caller deal with an exception
//			_server.set
		
		if (_thread == null) { //create the thread
			_thread = new Thread(this);
			_thread.start();
		}
	}
	
	/**
	 * Stops listening, stores the log into the HoneyRJ parent and stops the thread.
	 */
	public void stopInteractionModule() {
		listening = false;
		
		try {
			if(_server != null) {
				_server.close();
			}
		} catch (IOException ignored) {

		}

		_thread = null;
		_server = null;
	}

	

	/**
	 * @return the protocol
	 */
	public LIProtocol getProtocol() {
		return _protocol;
	}

    @Override
    public String toString() {
        return "Module for: " + _protocol.toString();
    }

    /**
     * Has our thread been created/socket started?
     * @return True if yes.
     */
    public boolean isStarted() {
        return (_thread != null && _server != null);
    }

    /**
     * A child thread that is communicating with a client should call this method upon closing the connection to store the log
     * @param file
     */
	protected void receiveLogFileFromThread(LogFile file) {
		//file.LogToConsole(); //TODO debug method only
		_logFiles.put(file.getStartedDate(),file);
		_parent.storeLogFiles(this, file);
		numberConnections--;
		if(_gui != null)	_gui.setNumberConnections(numberConnections);
	}

	public File getLoggingDirectory() {
		return _parent.getLoggingDirectory();
	}

	/**
	 * Increment the number of active connections by 1
	 */
	public void incrementConnections() {
		numberConnections++;
		if(_gui != null)	_gui.setNumberConnections(numberConnections);
		
	}
	
	/**
	 * return the number of active threads
	 * @return the number of active threads
	 */
	public int getNumberOfActiveConnections()
	{
		return numberConnections;
	}



}
