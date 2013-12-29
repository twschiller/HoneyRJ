# HoneyRJ User Manual

**Eric Peter**, [epeter@wustl.edu][1] **Todd Schiller**, [tschiller@acm.org][2] (Under guidance of Prof. Raj Jain)

[Original Document](http://www.cs.wustl.edu/~jain/cse571-09/ftp/honey/manual.html "Permalink to HoneyRJ User Manual")

* * *

###  1 Introduction

This application, HoneyRJ, is an implementation of a low-interaction
honeypot. A low-interaction honeypot serves a number of limited
functionality protocols with the intent of capturing the source of
traffic coming to the honeypot. A honeypot is located on an IP address
that is used solely for the purpose of the honeypot and not for any
legitimate services; any connections to the software are presumed to
be malicious and are logged for later review. HoneyRJ was designed to
be extremely simple and easily extendable.

This first release can emulate both FTP and IRC servers. HoneyRJ logs
all interactions, allowing users to identify potential attack
vectors. This user manual shows how to set up and run HoneyRJ on your
computer.


###  2 System Requirements

In order to run HoneyRJ, the end user must have the following minimum software requirements:

  * Microsoft Windows XP Professional
  * Active software firewalls must be configured to allow incoming connections on the monitored ports
  * Java Runtime Environment version 1.5
  * Eclipse 3.4 (if you wish to reconfigure the application)

HoneyRJ also has to following minimum hardware requirements:

  * 1 GB of RAM
  * A configured network card
  * 50 MB hard disk space (or more if you expect to keep a large number of logs)

###  3 Application Setup

The following section outlines the process of software configuration and installation. HoneyRJ is unique in that all configuration is done in the source code and therefore must be done at compile time.

####  3.1 Configuration

HoneyRJ has four configurable options. If the defaults are acceptable
to you, you are encouraged to use the provided binary and not
re-compile the application.

  * Logging directory: `C:\tmp\`
  * Connection timeout: **2 minutes**
  * Waiting period between connections to a protocol: **5 seconds**
  * Output all logging output to the console: **no**

If you wish to configure these options, please import the provided
Eclipse project into Eclipse following the instructions available in
the Eclipse documentation and then follow these steps.

  1. Open the file `src\honeyrj\HoneyRJ.java`
  2. To adjust the connection timeout, find the following line and change the default value to your desired value in milliseconds.
    ```
    public static final int DEFAULT_TIME_OUT_MSEC = 120000;
    ```
  3. To adjust the logging direction, find the following line and change the default value to your desired logging directory (trailing slash is required). Due to the special nature of the `\` character in Java, you must represent a `\` with a `\\`.
    ```
    public static final String DEFAULT_LOG_DIR ="C:\\tmp\\";
    ```
  4. To set whether HoneyRJ writes all log messages to the console, find the following line and change the value to `true`. If you wish to keep the default configuration of not writing to the console, keep the value as `false`.
    ```
    public static final boolean LOG_TO_CONSOLE = false;
    ```
  5. To adjust the waiting period between connections, find the following line and change the default value to your desired value in milliseconds. We recommend keeping this a reasonable number (under 10 seconds) or clients will experience large timeouts attempting to connect.
    ```
    public static final int TIME_WAIT_CONNECTION = 5000;
    ```

Once you have made the desired changes, follow the [instructions][4] available in the Eclipse documentation to generate a runnable JAR file.

####  3.2 Installation

To install HoneyRJ, place the created JAR file (or the provided
HoneyRJ.jar file) in the desired installation directory.

###  4 Application Overview

The following section outlines the steps to launch HoneyRJ and
provides a brief overview of the the application GUI, in addition to
defining terminology specific to HoneyRJ. After reading this section,
you should have a good understanding of what features HoneyRJ
provides.

####  4.1 Running HoneyRJ

If you have reconfigured the application as described above, please
replace any reference to the provided file **HoneyRJ.jar** with your
exported JAR file.

To launch the application, double click **HoneyRJ.jar.** The
application will launch and you will see a window similar to Figure 1:

Figure 1. HoneyRJ main application window upon initial startup

![Figure 1. HoneyRJ main application view upon initial startup][5]

####  4.2 User Interface Overview

There are two main parts of the application - the top pane and the
module pane. The top pane contains a legend and buttons that affect
all installed modules. Below the top pane, is the module pane, which
contains a representation of each module that is a part of the
application.

In the **top pane**, you see four legend icons and three buttons. The legend icons are a key to the colors that represent the four states of a module.

  * Green - Started
  * Yellow - Paused
  * Red - Stopped
  * Orange - Error

The three buttons are used as follows:

  * Start All - Click this button to put all modules into the started state
  * Stop All - Click this button to put all modules into the stopped state
  * Pause All - Click this button to put all modules into the paused state

The **module pane** contains a section for each module loaded into the
application. Each module contains three buttons that have the same
functionality as those in the top header, but they only affect that
module. Each module lists the common name of its protocol and the port
on which it runs. Finally, each module displays the number of
currently connected clients.

####  4.2.1 What is a Module?

A module represents one protocol running within HoneyRJ. A module
provides the implementation of a protocol to allow HoneyRJ to
communicate with clients as if it were a server running that
protocol. A module has four states: stopped, paused, started and
error. HoneyRJ can run one or more modules simultaneously.

  * A **stopped module** is not running in a thread, is not bound to a port and is not listening for connections.
  * A **started module** is running in a thread, is bound to a port and is listening for connections.
  * A **paused module** is running in a thread, but is not bound to a port and is not listening for connections.
  * A module in the **error state** was unable to bind to the port specified by its protocol and thus is not listening for connections.

###  5 Application Workflow

The following section describes the usage of HoneyRJ in several common
tasks. You must have the application configured and running to use
this section. As mentioned in the prerequisites, please ensure you do
not have any firewalls running that would prevent clients from
connecting to your machine. After reading this section, you should
have an understanding of how to use the features HoneyRJ provides.

####  5.1 Starting/Stopping/Pausing Modules

To start all the modules in HoneyRJ, click the **Start All** button in
the top pane. You will see each module turn from red to green,
indicating the modules are now listening for connections.

If you only wish to start a subset of modules, click the **Start** button contained within each desired module's section. The module will turn from red to green, indicating that it is now listening for connections.

To verify that HoneyRJ is listening for connections, perform the following steps.

  1. Go to the Windows Start menu and choose **Run**
  2. In the Run dialogue, type `telnet localhost <PORT>` where `<PORT>` is the port number of the running module and press **Enter**. For example, to connect to the FTP protocol, type `telnet localhost 21`
  3. You will now see a Telnet session open, similar to what is shown in Figure 2, where you can interact with the protocol. If the window opens and immediately closes, refer to the troubleshooting section for steps to ensure the module is properly started.
  4. In the HoneyRJ module pane, you should see "Hackers connected: 1" in the module to which you connected, similar to Figure 3.
  5. Close the Telnet application window, you should see hackers connected change to 0

Figure 2. Telnet session showing interaction with HoneyRJ's FTP protocol

![Telnet session showing interaction with HoneyRJ's FTP protocol][6]

Figure 3. HoneyRJ application viewing showing one connection to the FTP protocol

![HoneyRJ application viewing showing one connection to the FTP protocol][7]

The steps for stopping modules are identical to the steps for starting a module. Replace any reference to **start** with **stop**.

The steps for pausing modules are identical to the steps for starting a module. Replace any reference to **start** with **pause**.

####  5.2 Viewing Log Files

HoneyRJ logs every connection into an individual file. When HoneyRJ launches, it creates a directory named with a timestamp in the configured logging directory (by default the directory is `c:\tmp\`). For example, the log files created while writing this documentation were stored in `C:\tmp\j_1238883728078_log\`.

Each connection to HoneyRJ creates a new text file within this directory, named by the protocol name, followed by a timestamp and given the extension log. An example of a log file name for the FTP protocol is `FTP_1238884135628.log`. Log files are updated in real time, that is, whenever a packet is sent or received, it is appended to the log file. If you wish to monitor the logs files in real time, we suggest an application similar to [Tail for Win32][8] [Tail4Win].

####  5.2.1 Log File Format

A log file consists of the following header lines, which describe the time the connection started, followed by a description of the logging format.
```
******************************************************
******Started at: Sat Apr 04 16:28:55 CST 2009********
TIMESTAMP,SRC_IP:PRT,DST_IP:PRT,PACKET
```

After the header lines, details of each sent or received packet is logged. The following information is stored about each packet, in the order that appears below.

  * Timestamp - timestamp that states when the packet was sent or received.
  * Source IP - the IP address the packet was sent from (the IP of the machine HoneyRJ is running for a sent packet or the IP of the client for a received packet)
  * Source Port - the port number the packet was sent from
  * Destination IP - the IP address on which the packet was received (the IP of the machine HoneyRJ is running for a received packet or the IP of the client for a sent packet)
  * Destination Port - the port number the packet was received from
  * Packet - the string contained within the packet

Each packet sent or received is logged on a separate line. An example of a line representing a sent packet is listed below:
```
Sat Apr 04 16:28:55 CST 2009,127.0.0.1:2595,127.0.0.1:21,220 Service ready for new user.
```
Immediately following the packet information are the footer lines, which describe how and when the connection terminated. The examples used below are from a FTP connection.

For a connection that closed due to a connection timeout, the following lines appear.
```
*****Protocol FTP TIMED OUT talking to /127.0.0.1 using local port 21, connection closed.****
*****Stopped at: Sat Apr 04 16:30:58 CST 2009*******
****************************************************
```

If the connection closed normally, the following lines appear instead:

```
*Protocol FTP is finished talking to /127.0.0.1 using local port 21****
*****Stopped at: Sat Apr 04 16:46:29 CST 2009*******
****************************************************
```

####  5.3 Recovering from an Error State

If a module cannot start listening on the port specified by its protocol, it will be in the error state. The error state is visually indicated by the module's section turning to orange. See Figure 4 for an example of the FTP module in the error state. An error happens when HoneyRJ cannot bind to the module's specified port. There are two reasons why the error state could happen:

  * **The user account running HoneyRJ does not have permissions to open the specified port.** \- Try to run the application as a user with Administrator privileges. You can either login to the system as a user with administrative privileges or use the Run As command to run HoneyRJ with elevated privileges. For instructions on use of the Run As command, please refer to [Microsoft Knowledge Base article #294676.][9]
  * **Another application is already listening on the port** \- If another application is listening specified the port, HoneyRJ will be unable to start listening on that port. For example, if a real FTP server is running on the same computer as HoneyRJ, this could happen. To verify that no application is listening on the port, use the **netstat -a** command to view all active connections on your system. Verify no application currently has the port open in the "LISTENING" state. If there is an application listening on the port, close that application. For more information on this command, please view the [Microsoft TechNet Article describing netstat usage.][10]

Figure 4. HoneyRJ application indicating the FTP protocol could not listen on port 21

![HoneyRJ application indicating the FTP protocol could not listen on port 21][11]

####  5.4 Closing HoneyRJ

To close the application, click the X button in the top right corner. All active connections will be closed.

####  5.5 Troubleshooting

If the application fails to start, ensure that your configured logging directory exists and is writeable. The application will attempt to create a directory within the configured logging directory and will exit upon failure.

   [1]: mailto:epeter@wustl.edu
   [2]: mailto:tschiller@acm.org
   [4]: http://help.eclipse.org/ganymede/topic/org.eclipse.jdt.doc.user/tasks/tasks-33.htm
   [5]: images/manual_fig1.png
   [6]: images/manual_fig2.png
   [7]: images/manual_fig3.png
   [8]: http://tailforwin32.sourceforge.net/
   [9]: http://support.microsoft.com/kb/294676
   [10]: http://technet.microsoft.com/en-us/library/bb490947.aspx
   [11]: images/manual_fig4.png
  