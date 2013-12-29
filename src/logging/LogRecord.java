package logging;

import java.net.InetAddress;
import java.util.Date;

/**
 * a (dumb) container for all information (we care) about a point to point transmission
 * Logging format
 * TIMESTAMP,SRC_IP:SRC_PORT,DST_IP:DST_PORT,PACKET
 * @author Eric Peter
 * @see LogFile	
 */
public class LogRecord {
	private Date Timestamp;
	private InetAddress srcIP;
	private InetAddress dstIP;
	private int srcPort;
	private int dstPort;
	private String Packet;
	
	
	public String toString() {
		return Timestamp + "," + srcIP.getHostAddress() + ":" + srcPort + "," + dstIP.getHostAddress() + ":" + dstPort + "," + Packet; 
	}

	public void setTimestamp(Date timestamp) {
		this.Timestamp = timestamp;
	}

	public void setSrcIP(InetAddress srcIP) {
		this.srcIP = srcIP;
	}

	public void setDstIP(InetAddress dstIP) {
		this.dstIP = dstIP;
	}

	public void setSrcPort(int srcPort) {
		this.srcPort = srcPort;
	}

	public void setDstPort(int dstPort) {
		this.dstPort = dstPort;
	}

	public void setPacket(String packet) {
		this.Packet = packet;
	}
	
	
	
	
}
