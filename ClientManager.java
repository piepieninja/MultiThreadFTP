import java.util.*;
import java.io.*;
import java.net.*;

public class ClientManager implements Runnable {

	private ServerSocket normalSocket;
	private Socket clientSocket;
	private int port;

	 /**
     * The constructor for the ClientThread class
     * @param nport the "normal" port that is used for client server communication
     * @return an instance of ClientManager which is intended to be ran in a thread
     */
	public ClientManager(int nport) {
		this.port = nport;
		System.out.printf("ClientManager created\n");
	}

	/**
	 * Overidden to implement the Runnable interface
	 */
	public void run() {
		try {
			// creates the socket for accepting clients
			normalSocket = new ServerSocket(port);
			while(true){
				clientSocket = normalSocket.accept();
				(new Thread(new ClientThread(clientSocket))).start();
			}
		} catch (Exception e) {
			System.out.printf("There was an error creating the socket");
		}
	}

}