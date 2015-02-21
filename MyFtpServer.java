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
	final Runtime rt = Runtime.getRuntime();
	Socket mySocket;
	BufferedReader is;
    PrintStream os;
    String userPath, currentPath;
    boolean running = true;

	public ClientThread(Socket clientSocket) {
		this.mySocket = clientSocket;

		try {
			is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	    	os = new PrintStream(clientSocket.getOutputStream());
		} catch (IOException ex) {
			System.out.println(ex);
		}

		currentPath = System.getProperty("user.dir");
	}

	public void run() {
		String input;
		try {
			while (running) {
				input = is.readLine();
				parseCommand(input);
			}
		} catch (IOException ex) {
			System.out.println(ex);
		}
	}

	private void parseCommand(String input) {
		String command;
		if (input.contains(" ")) {
			command = input.substring(0, input.indexOf(' '));
			userPath = input.substring(input.indexOf(' '));
			userPath = userPath.trim();
		} else {
			command = input;
		}

		switch(command){
			case "cd":

				//Checks for just "cd"
				if (userPath == null) {
					//Do nothing
					os.println("success");
				} 
				//Checks for "cd .."
				else if (userPath.equals("..")) {

					//
					File parentDirectory = new File(System.getProperty("user.dir"));
					currentPath = parentDirectory.getAbsoluteFile().getParent();
					System.out.println(currentPath);
					os.println("success");
				}
				//All other paths
				else {
					File directory = new File(userPath);
					//Check if directory exists
					if (!directory.exists()) {
						os.println("no directory");
					} else if (directory.exists()) {
						currentPath = System.getProperty("user.dir") + "/" + userPath;
						os.println("success");
					} else {
						os.println("not a directory");
					}
				}
				break;

			case "delete":

				//Checks to see if there was a file path
				File file; 
				if (userPath == null) {
					file = new File(currentPath);
				} else {
					file = new File(userPath);
				}

				if (!file.exists()) {
					os.println("no file");
				} else {
					boolean t = file.delete();
					if (t) {
						os.println("success");
					} else {
						os.println("failure");
					}
				}
				break;

			case "ls":

				File list = new File(currentPath);
				String childs[] = list.list();
				String output = "";
				for(String child: childs){
	            	output += child + "<&&newline&&>";
	        	}
	        	os.println(output);
	        	break;

			case "mkdir":

				//Checks to see if there was a file path
				File dir;
				if (userPath == null) {
					dir = new File(currentPath);
				} else {
					dir = new File(userPath);
				}

				//creates the new directory and returns success or failure
				boolean t = dir.mkdir();
				if (t) {
					os.println("success");
				} else {
					os.println("failure");
				}
				break;

			case "pwd":

				os.println(System.getProperty("user.dir"));
				break;

			case "quit":

				running = false;

			case "get":

			case "put":
		}
	}

}




