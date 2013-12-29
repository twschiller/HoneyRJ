package protocol;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lowinteraction.LIProtocol;
import lowinteraction.TALK_FIRST;

/**
 * An implementation of IRC for HoneyRJ
 * @author Todd Schiller
 *
 */
public class IrcProtocol implements LIProtocol {

	private String myName = "hubbard.freenode.net";
	private String myFakeHostName = "freenode.club.cc.cmu.edu";
	private String nickName = null;
	private String userName = null;
	private String hostName = null;
	@SuppressWarnings("unused")
	private String serverName = null;
	@SuppressWarnings("unused")
	private String realName = null;
	
	
	private final int NO_CONN = 1;
	private final int GOOD_NICK = 2;
	private final int KILLED = 0;
	
	
	private int state = NO_CONN;
	
	private int cmdTryCnt = 0;
	private int cmdTryMax = 1;
	
	public int getPort() {
		return 6667;
	}

	public String toString(){
		return "IRC";
	}
	
	public boolean isConnectionOver() {
		return state == KILLED;
	}
	
	public Vector<String> processInput(String msg) {
		if (msg == null){
			state = KILLED;
			return new Vector<String>();
		}

		Vector<String> resp = new Vector<String>();
		
		switch(state){
			
		case NO_CONN:
			if (Pattern.matches("^NICK (\\S+)", msg)){
				Pattern nP = Pattern.compile("^NICK (\\S+)");
				Matcher nM = nP.matcher(msg);
				nM.matches();
				this.nickName = nM.group(1);
				
				Pattern uP = Pattern.compile("USER (\\S+) (\\S+) (\\S+) (.+)$");
				Matcher uM = uP.matcher(msg);
				boolean user = uM.matches();
				if (user){
					userName = uM.group(1);
					hostName = uM.group(2).replace("\"", "");
					serverName = uM.group(3).replace("\"", "");
					realName = uM.group(4);
				}
				
				resp.add("NOTICE AUTH :*** Looking up your hostname...");
				resp.add("NOTICE AUTH :*** Checking ident");
				resp.add("NOTICE AUTH :*** Found your hostname");
				resp.add("NOTICE AUTH :*** No identd (auth) response");
				
				resp.add("NOTICE " + nickName + " :*** Your host is " + myName + "[" + myFakeHostName + "/6667], running version hyperion-1.0.2b");
				//resp.add(":" + myName + " 251 " + nickName + " :There are 23792 listed and 21887 unlisted users on 38 servers");
				//resp.add(":" + myName + " 252 " + nickName + " 36 :flagged staff members");
				//resp.add(":" + myName + " 254 " + nickName + " 24966 :channels formed");
				resp.add(":" + myName + " 375 " + nickName + " :- " + myName + " Message of the Day - ");
				resp.add(":" + myName + " 372 " + nickName + " :- \"IT'S A TRAP!\" says Admiral Ackbar");
				resp.add(":" + myName + " 376 " + nickName + " :End of /MOTD command");
			
				state = GOOD_NICK;
				
				return resp;
			}
		case GOOD_NICK:
			if (Pattern.matches("^MODE (\\S+) ([\\+-][iwso])$", msg)){
				Pattern mP = Pattern.compile("^MODE (\\S+) ([\\+-][iwso])$");
				Matcher mM = mP.matcher(msg);
				mM.matches();
				resp.add(":" + nickName + " MODE " + mM.group(1) + " :" + mM.group(2));
				return resp;
			}else if(Pattern.matches("^USER\\s(\\S+)\\s(\\S+)\\s(\\S+)\\s(.+)",msg)){
				Pattern uP = Pattern.compile("USER\\s(\\S+)\\s(\\S+)\\s(\\S+)\\s(.+)");
				Matcher uM = uP.matcher(msg);
				uM.matches();
				userName = uM.group(1);
				hostName = uM.group(2).replace("\"", "");
				serverName = uM.group(3).replace("\"", "");
				realName = uM.group(4);

				return resp;
			}else if(Pattern.matches("^USERHOST (\\S+)", msg)){
				if (userName == null){
					state = KILLED;
					return resp;
				}
				
				Pattern mP = Pattern.compile("^USERHOST (\\S+)");
				Matcher mM = mP.matcher(msg);
				mM.matches();
		
				resp.add(":" + myName + " 302 " + nickName + ":" + mM.group(1) + "=+" + hostName);	
				return resp;
			}else if (Pattern.matches("^QUIT", msg)){
				state = KILLED;
				return resp;
			}else if (Pattern.matches("^(\\S+)", msg)){
				if (cmdTryCnt >= cmdTryMax){
					state = KILLED;
					return resp;
				}
				
				Pattern uP = Pattern.compile("^(\\S+)");
				Matcher uM = uP.matcher(msg);
				uM.matches();
				resp.add(":" + myName + " 421 " + uM.group(1) + " :Unknown command");
				
				cmdTryCnt++;
				return resp;
			}
		default:
			state = KILLED;
			return resp;
		}
	}

	public TALK_FIRST whoTalksFirst() {
		return TALK_FIRST.CLIENT_FIRST;
	}

}
