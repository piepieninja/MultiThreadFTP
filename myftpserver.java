import java.util.*;
import java.io.*;
import java.net.*;

public class myftpserver {

	private Thread nThread, tThread;

	public static void main (String[] args) {
		//////Deal with command line args	
		int nport = 5555;
		int tport = 5556;
		//////
		//////
		//////
		//////

		FTPServer server = new FTPServer(nport, tport);
	}

}

class FTPServer {

	private Thread nThread, tThread;
	
	FTPServer(int nport, int tport) {
		
		try {
			//starts thread for dealing with normal commands
			nThread = new Thread(new ClientManager(nport));
			nThread.start();
			//starts thread for dealing with the terminiate command
			tThread = new Thread(new TerminateManager(tport));
			tThread.start();
		} catch (Exception e) {
			System.out.printf("There was an error\n");
		}

		System.out.printf("Thread Count: " + Thread.activeCount() + "\n");
	}

}

class ClientManager implements Runnable {
	private ServerSocket normalSocket;

	public ClientManager(int nport) {
		System.out.printf("ClientManager created\n");

		try {
			normalSocket = new ServerSocket(nport);
		} catch (Exception e) {
			System.out.printf("There was an error creating the socket");
		}
	}

	public void run() {

	}
}

class TerminateManager implements Runnable {
	private ServerSocket terminateSocket;

	public TerminateManager(int tport) {
		System.out.printf("TerminateManager created\n");

		try {
			terminateSocket = new ServerSocket(tport);
		} catch (Exception e) {
			System.out.printf("There was an error creating the socket");
		}
	}

	public void run() {

	}
}