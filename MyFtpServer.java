import java.util.*;
import java.io.*;
import java.net.*;

public class MyFtpServer {

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
    String userPath, currentPath;
    boolean running = true;

	public ClientThread(Socket clientSocket) {
		this.mySocket = clientSocket;
		currentPath = System.getProperty("user.dir");

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

	private synchronized void parseCommand(String input) {
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
				if (userPath == null || userPath.equals(".")) {
					//Do nothing
					os.println("success");
				} 
				//Checks for "cd .."
				else if (userPath.equals("..")) {

					File parentDirectory = new File(currentPath);
					currentPath = parentDirectory.getAbsoluteFile().getParent();
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
				File file = new File(currentPath + "/" + userPath);

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
				if (list == null) {
					os.println("");
				} else {
					String childs[] = list.list();
					String output = "";
					for(String child: childs){
            			output += child + "<&&newline&&>";
        			}
        			os.println(output);
				}
	        	break;

			case "mkdir":

				//Checks to see if there was a file path
				File dir = new File(currentPath + "/" + userPath);
				System.out.println(currentPath);

				//creates the new directory and returns success or failure
				boolean t = dir.mkdir();
				if (t) {
					os.println("success");
				} else {
					os.println("failure");
				}
				break;

			case "pwd":

				os.println(currentPath);
				break;

			case "quit":

				running = false;

			case "get":
				//Create new thread for cmd handling
				//Send threads id back to client
				//Start thread 
				//send hello from within the thread
				Thread commandThread = new Thread(new CommandThread(this.mySocket, "get"));
				os.println(commandThread.getId());
				System.out.println("THREAD ID " + commandThread.getId());
				try{
					commandThread.start();
					wait();

				} catch(Exception e) {
					System.out.println("INtereupted exception");
				}


			case "put":
		}
	}
}


class CommandThread implements Runnable {
	Socket mySocket;
	BufferedReader is;
    PrintStream os;
    boolean running = true;

	public CommandThread(Socket clientSocket, String cmd) {
		System.out.println("CREATED COMMAND THREAD");
		try {
			is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	    	os = new PrintStream(clientSocket.getOutputStream());
		} catch (IOException ex) {
			System.out.println(ex);
		}
	}

	public void run() {
		System.out.println("RUNNING COMMAND THREAD");
		String input;
		try {
			System.out.println("PRINTING FROM COMMAND THREAD");
			os.println("Hello from the get thread");
		} catch (Exception ex) {
			System.out.println(ex);
		}
		finally {
			notifyAll();
		}
	}
}







