import java.util.*;
import java.io.*;
import java.net.*;

public class ClientManager implements Runnable {

	private ServerSocket normalSocket;//Accepts a request, runs a function, then returns that to the requester
	private Socket clientSocket; //Traditional communication endpoint
	private int port;
	private RWLock rwLock;

	 /**
     * The constructor for the ClientThread class
     * @param nport the "normal" port that is used for client server communication
     * @return an instance of ClientManager which is intended to be ran in a thread
     */
	public ClientManager(int nport) {
		this.port = nport;
		System.out.printf("ClientManager created\n");
		rwLock = new RWLock();
	}

	/**
	 * Overidden to implement the Runnable interface
	 */
	public void run() {
		try {
			// creates the socket for accepting clients
			normalSocket = new ServerSocket(port);
			while(true){
				clientSocket = normalSocket.accept(); //ServerSocket returns a new socket on an unspecified port Client instantiates one
				(new Thread(new ClientThread(clientSocket, rwLock))).start(); //Spawn new thread using our newly created socket
			}
		} catch (Exception e) {
			System.out.printf("There was an error creating the socket");
		}
	}

}