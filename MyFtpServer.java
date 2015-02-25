import java.util.*;
import java.io.*;
import java.net.*;

public class MyFtpServer {

	private Thread nThread, tThread;
	
	/**
	 * The constructor for MyFtpServer, create an instance of an FTP server
	 * @param nport the normal port for client server communication to execute commands
	 * @param tport the port used for signaling the killing of a command being executed on a CommandThread
	 * @return an instance of MyFtpServer
	 */
	MyFtpServer(int nport, int tport) {	
		try {
			nThread = new Thread(new ClientManager(nport));
			nThread.start();
			tThread = new Thread(new TerminateManager(tport));
			tThread.start();
		} catch (Exception e) {
			System.out.printf("There was an error\n");
		}
		System.out.printf("Thread Count: " + Thread.activeCount() + "\n");
	}

	/**
	 * The entry point for our server
	 */
	public static void main (String[] args) {
		if (args.length != 2){
			System.out.println("ERROR: Please enter 2 ports.\nUSAGE: java MyFtpServer <NPORT> <TPORT>");
			System.exit(1);
		} else if (args[0] == args[1]){
			System.out.println("ERROR: Please enter different ports.\nUSAGE: java MyFtpServer <NPORT> <TPORT>");
			System.exit(1);
		} 
		// Defaults:
		int nport = Integer.parseInt(args[0]);
		int tport = Integer.parseInt(args[1]);
		MyFtpServer server = new MyFtpServer(nport, tport);
	}

}

