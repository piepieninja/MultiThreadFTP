import java.util.*;
import java.io.*;
import java.net.*;

public class TerminateManager implements Runnable {
	ServerSocket terminateSocket;
	Socket normalSocket;
	BufferedReader is;
	PrintStream os;
	int port;
	String userInput = "";

	public TerminateManager(int tport) {
		this.port = tport;
		System.out.printf("TerminateManager created\n");
	}


	/**
	 * Overidden to implement the Runnable interface
	 */
	public void run() {
		try {
			// creates the socket for accepting clients
			terminateSocket = new ServerSocket(port);
			normalSocket = terminateSocket.accept();

			is = new BufferedReader(new InputStreamReader(normalSocket.getInputStream()));
	    	os = new PrintStream(normalSocket.getOutputStream());

			while(true) {
				//normalSocket = terminateSocket.accept(); //ServerSocket returns a new socket on an unspecified port Client instantiates one

	    		for (String userInput = is.readLine(); userInput != null; userInput = is.readLine()) {
					//Spawn new thread using our newly created socket
					(new Thread(new TerminateThread(normalSocket, userInput))).start();
				}
			}
		} catch (Exception e) {
			System.out.printf("There was an error creating the socket");
		}
	}
}