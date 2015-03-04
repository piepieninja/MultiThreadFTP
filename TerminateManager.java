import java.util.*;
import java.io.*;
import java.net.*;

public class TerminateManager implements Runnable {
	ServerSocket terminateSocket;
	Socket normalSocket;
	int port;
	BufferedReader is;
    PrintStream os;
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

			while(true) {
				//normalSocket = terminateSocket.accept(); //ServerSocket returns a new socket on an unspecified port Client instantiates one
				is = new BufferedReader(new InputStreamReader(normalSocket.getInputStream()));
	    		os = new PrintStream(normalSocket.getOutputStream());
	    		userInput = is.readLine();
	    		System.out.println(userInput);
				(new Thread(new TerminateThread(normalSocket, userInput))).start(); //Spawn new thread using our newly created socket
			}
		} catch (Exception e) {
			System.out.printf("There was an error creating the socket");
		}
	}
}