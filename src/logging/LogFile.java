package logging;

import honeyrj.HoneyRJ;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;

import lowinteraction.LIModuleThread;


/**
 * a container for multiple log records
 * @author Eric Peter
 * @see LogRecord
 *
 */
public class LogFile {
	
	private PrintStream _logFilePrinter;
	private ArrayList<LogRecord> _logRecords;
	private String start;
	private String end;
	private Date started;
	private Date stopped;
	
	public LogFile(LIModuleThread loggingForThread) throws LoggingException {
		_logRecords = new ArrayList<LogRecord>();

		String filename = loggingForThread.getLoggingDirectory().getPath() + "\\" + loggingForThread.getProtocol() + "_" +loggingForThread.getStartedTime().getTime() + ".log";
		try {
			_logFilePrinter = new PrintStream(new FileOutputStream(filename, false));
		} catch (FileNotFoundException e) {
			throw new LoggingException("failed to create log file");
		}
	}
	
	/**
	 * call upon the beginning of a connection to put basic information into the start of the log record
	 * records a started logging time
	 * @param start String to describe the start of the connection
	 */
	public void setBeginningLogInfo(String start) {
		started = new Date();
		this.start = start;
		
		writeLogLine("***********************************************");
		writeLogLine("*****Started at: " + started + "*******");
		writeLogLine("TIMESTAMP,SRC_IP:PRT,DST_IP:PRT,PACKET");
	
	}

	/**
	 * add a LogRecord as part of the log
	 * @param logToAdd LogRecord to add
	 */
	public void add(LogRecord logToAdd) {
		_logRecords.add(logToAdd);
		writeLogLine(logToAdd.toString());
	}

	/**
	 * call upon the end of a connection to put basic information into the end of the log record
	 * records a stopped logging time
	 * @param end String to describe the end of the connection
	 */
	public void setEndingLogInfo(String end) {
		this.end = end;
		this.stopped = new Date();
		writeLogLine(end);
		writeLogLine("*****Stopped at: " + stopped + "*******");
		writeLogLine("***********************************************");
		closeLogFile();
	}

	/**
	 * return the Date when the log was started
	 * @return the Date when the log was started
	 */
	public Date getStartedDate() {
		return started;
	}

	/**
	 * print log record out to the console
	 */
	public void LogToConsole() {
		LogToStream(System.out);
	}

	/**
	 * print the LogRecord to the given PrintStream
	 * @param out
	 */
	public void LogToStream(PrintStream out) {
		if(started == null) { //sanity check if the thread crashed before logging started
			out.println("Connection failed before logging could start.");
			return;
		}
		
		out.println("***********************************************");
		out.println("*****Started at: " + started + "*******");
		out.println(start);
		out.println("TIMESTAMP,                   SRC_IP:PRT,    DST_IP:PRT,    PACKET");
							
		
		for (LogRecord r : _logRecords) {
			out.println(r);
		}
		
		if(end != null)		out.println(end); //sanity check if connection failed before logging ended
		else				out.println("Connection terminated early due to failure");
		if(stopped != null)	out.println("*****Stopped at: " + stopped + "*******");
		out.println("***********************************************");
	}

	private void writeLogLine(String s) {
		_logFilePrinter.println(s);
		if(HoneyRJ.LOG_TO_CONSOLE) System.out.println(s);
	}

	private void closeLogFile() {
		_logFilePrinter.flush();
		_logFilePrinter.close();
	}
	
	/*public void writeToFile() throws IOException {
		writeToFile("", HoneyRJ.DEFAULT_LOG_DIR);
	}
	
	public void writeToFile(String filename) throws IOException {
		writeToFile(filename, HoneyRJ.DEFAULT_LOG_DIR);
	}
	
	public void writeToFile(String filename, String directory) throws IOException {
		FileOutputStream fos = new FileOutputStream(directory + filename + getStartedDate().getTime() + ".log" , false);
		//FileWriter f = new FileWriter(directory + getStartedDate().getTime() + filename , false);
		//PrintStream pw = new PrintStream(new BufferedOutputStream(f));
		LogToStream(new PrintStream(fos));
	}*/
}
