package edu.wustl.honeyrj.honeyrj;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

import edu.wustl.honeyrj.logging.LogFile;
import edu.wustl.honeyrj.lowinteraction.LIModule;

/**
 * 
 * HoneyRJ coordinates a number of LIModules, which provide faux services to hackers.
 * HoneyRJ is the final destination for all log files created by the LIModules.
 * @author Eric Peter
 * @see LIModule
 */
public class HoneyRJ {
	private File _logging_dir;
	
	public File getLoggingDirectory() {
		return _logging_dir;
	}

	/**
	 * HashMap of all service modules
	 */
	private HashMap<Integer, LIModule> _services;
	/**
	 * all the logs we have collected
	 */
	private HashMap<Integer, TreeMap<Date, LogFile>> _logs; //this storage sucks dick but will be improved
	/**
	 * The default time out for connections.  Any connection active after this period will be closed and a log entry will be created.
	 */
	public static final int DEFAULT_TIME_OUT_MSEC = 120000;
	/**
	 * Default logging directory
	 */
	public static final String DEFAULT_LOG_DIR="C:\\tmp\\";

	/**
	 * log file debugging
	 */
	public static final boolean LOG_TO_CONSOLE = false;
	/**
	 * Interval between connections for one protocol (in msec)
	 */
	public static final int TIME_WAIT_CONNECTION = 5000;
	/** 
	 * constructor
	 * @throws HoneyRJException if the log directory can't be created
	 */
	public HoneyRJ() throws HoneyRJException {
		this(DEFAULT_LOG_DIR);
	}
	
	/**
	 * 
	 * @param logDir logging directory to use -  C:\\tmp\\ is the default
	 * @throws HoneyRJException if the log directory can't be created
	 */
	public HoneyRJ(String logDir) throws HoneyRJException {
		_services = new HashMap<Integer, LIModule>();
		_logs = new HashMap<Integer, TreeMap<Date,LogFile>>();
		
		_logging_dir = new File(logDir + "rj_" + new Date().getTime() + "_log");
		//File loggingDir = 
		if(!_logging_dir.exists()) {
			if(!_logging_dir.mkdir())		throw new HoneyRJException("Failed to create log directory");
		}
	}
	
	/**
	 * attempt to start the module running on the given port
	 * @param portToStart int
	 * @return true on success, false if it fails to start or does not exist
	 */
	public boolean startPort(int portToStart) {
		if(!_services.containsKey(portToStart))	return false; //sanity
		try {
			_services.get(portToStart).startInteractionModule();
		} catch (IOException e) {
			return false; //FAILED TO START THREAD/server
		} 
		return true;
	}
	
	/**
	 * grabs the port # from the module and starts the local copy
	 * @see HoneyRJ#startPort(int)
	 */
	public boolean startModule(LIModule moduleToStart) {
		return startPort(moduleToStart.getProtocol().getPort());
	}
	
	/**
	 * register a given LIModule with this HoneyRJ
	 * @param moduleToBeConnected
	 * @return true on success, false if there already exists a Module on the port of moduleToBeConnected
	 * @see LIModule#registerParent(HoneyRJ)
	 */
	public boolean RegisterService(LIModule moduleToBeConnected) {
		if(_services.containsKey(moduleToBeConnected.getPort())) { //already have a service registered on this port
			return false;
		} else {
			_services.put(moduleToBeConnected.getPort(), moduleToBeConnected); //add to list
			_logs.put(moduleToBeConnected.getPort(), new TreeMap<Date, LogFile>());
			moduleToBeConnected.registerParent(this); //register with this HoneyRJ
			return true;
		}
		
	}
	
	/**
	 * disconncts the given module from the HoneyRJ
	 * the module will attempt to send its logs back to us
	 * @param moduleToBeDisconnected
	 * @return true on succes, false if the module isn't in the HoneyRJ
	 */
	public boolean DeRegisterService(LIModule moduleToBeDisconnected) {
		int port = moduleToBeDisconnected.getPort();
		if(_services.containsKey(port) && moduleToBeDisconnected == _services.get(port)) {
			moduleToBeDisconnected.stopInteractionModule();
			_services.remove(port);
			return true;
		} else {
			return false;
		}
		
	}
	
	/**
	 * Pause the given module from accepting connections
	 * @param moduleToPause
	 * @return true on success, false if the module isn't in the HoneyRJ
	 */
	public boolean PauseNewConnections(LIModule moduleToPause) {
		if(_services.containsKey(moduleToPause.getPort()) && moduleToPause == _services.get(moduleToPause.getPort())) {
			moduleToPause.pauseListeningForConnections();
			return true;
		} else 	return false;
	}
	
	/**
	 * Resumes the given module to accepting connections
	 * @param moduleToResume
	 * @return true on succes, false if the module isn't in the HoneyRJ
	 */
	public boolean ResumeNewConnections(LIModule moduleToResume) {
		if(_services.containsKey(moduleToResume.getPort()) && moduleToResume == _services.get(moduleToResume.getPort())) {
			moduleToResume.resumeListeningForConnections();
			return true;
		} else	return false;
			
	}

	/**
	 * accept a collection of LogFiles from a module and store them
	 */
	public void storeLogFiles(LIModule from, LogFile file) {
		_logs.get(from.getPort()).put(file.getStartedDate(), file);
		
	}
	
	public void DebugServices() {
		for(Integer i: _services.keySet()) {
			System.out.println("Port " + i + ": " + _services.get(i).toString());
		}
	}

}
