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
			nThread = new ClientManager(nport);
			nThread.start();
			//starts thread for dealing with the terminiate command
			tThread = new TerminateManager(tport);
			tThread.start();
		} catch (Exception e) {
			System.out.printf("There was an error\n");
		}

		System.out.printf("Thread Count: " + Thread.activeCount() + "\n");
	}

}


class ClientManager extends Thread {
	private ServerSocket normalSocket;

	public ClientManager(int nport) {
		System.out.printf("ClientManager created\n");
	}
}


class TerminateManager extends Thread {
	private ServerSocket terminateSocket;

	public TerminateManager(int tport) {
		System.out.printf("TerminateManager created\n");
	}
}