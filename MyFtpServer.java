import java.util.*;
import java.io.*;
import java.net.*;

public class MyFtpServer {

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
	private Socket clientSocket;
	private int port;

	public ClientManager(int nport) {
		this.port = nport;
		System.out.printf("ClientManager created\n");
	}

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

class TerminateManager implements Runnable {
	private ServerSocket terminateSocket;
	private int port;

	public TerminateManager(int tport) {
		this.port = tport;
		System.out.printf("TerminateManager created\n");
	}

	public void run() {
		try {
			terminateSocket = new ServerSocket(port);

		} catch (Exception e) {
			System.out.printf("There was an error creating the socket");
		}
	}
}

class ClientThread implements Runnable {
	Socket mySocket;
	BufferedReader is;
    PrintStream os;

	public ClientThread(Socket clientSocket) {
		this.mySocket = clientSocket;

		try {
			is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	    	os = new PrintStream(clientSocket.getOutputStream());
		} catch (IOException ex) {
			System.out.println(ex);
		}
	}

	public void run() {
		String input;

		try {
			while (true) {
				input = is.readLine();
				os.println(input);
			}
		} catch (IOException ex) {
			System.out.println(ex);
		}
		
	}

}




