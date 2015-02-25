import java.util.*;
import java.io.*;
import java.net.*;

/**
* The ClientThread is what is used to handle the individual
* sessions of each client machine that connects with the
* ftp server it should spawn off command threads to execute
* get and put as well as background tasks
*/
public class ClientThread implements Runnable {
	Socket mySocket;
	BufferedReader is;
    PrintStream os;
    String currentPath;
    boolean running = true;

    /**
     * The constructor for the ClientThread class
     * @param clientSocket the socket used to send/recv from 
     * @return an instance of ClientThread instantiated with the current path and configured IO streams
     */
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

	/**
	 *Overridden method to allow us to use this class in a thread
	 */
	public void run() {
		String input = "";
		try {
			while (running) {
				input = is.readLine();
				System.out.println(input);
				routeCommand(input);
			}
		} catch (IOException ex) {
			System.out.println(ex);
		}
	}

	/**
     * Checks for a valid command then attempts to navigate to that directory
     * @param destPath, the destination directory
     */
	private void changeDirectory(String destPath) {
		String absolutePath = System.getProperty("user.dir");
		if (destPath.substring(0, 1).equals("/")) {
				File directory = new File(destPath);
			//Check if directory exists
			if (!directory.exists()) {
				os.println("no directory");
			} else if (directory.exists()) {
				currentPath = destPath;
				os.println("success");
			} else {
				os.println("not a directory");
			}
		} else {
			//Checks for just "cd"
			if (destPath == null || destPath.equals(".")) {
				//Do nothing
				os.println("success");
			} 
			//Checks for "cd .."
			else if (destPath.equals("..")) {
				File parentDirectory = new File(currentPath);
				currentPath = parentDirectory.getAbsoluteFile().getParent();
				os.println("success");
			}
			//All other paths
			else {
				File directory = new File(destPath);
				//Check if directory exists
				if (!directory.exists()) {
					os.println("no directory");
				} else if (directory.exists()) {
					currentPath = currentPath + "/" + destPath;
					os.println("success");
				} else {
					os.println("not a directory");
				}
			}
		}
	}

	/**
     * Checks for a valid command then attempts to delete to that file
     * @param destPath, the file desired to be deleted
     */
	private void deleteFile(String destPath){
		File file = new File(currentPath + "/" + destPath);
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
	}

	/**
	 * Lists all the files in the present working directory
	 * @param destPath, the destination directory
     */
	private void listFiles(String destPath){
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
	}

	/**
     * Checks for a valid command then attempts to make that directory
     * @param destPath, the new desired name of the directory
     */
	private void makeDirectory(String destPath){
				//Checks to see if there was a file path
				File dir = new File(currentPath + "/" + destPath);
				System.out.println(currentPath);
				//creates the new directory and returns success or failure
				boolean t = dir.mkdir();
				if (t) {
					os.println("success");
				} else {
					os.println("failure");
				}
	}

	/**
	* Spins off a sperate thread to handle get and put requests
	* @param inputs, an array containing the command and its arguments
	*/
	 public void startCommandThread(String[] inputs) {
	 	System.out.println("Starting command thread");
		Thread commandThread = new Thread(new CommandThread(this.mySocket, inputs, currentPath));
		System.out.println("2) thread id is " + commandThread.getId());
		os.println(commandThread.getId());
		commandThread.start();
		try{
			commandThread.join();
		} catch (InterruptedException consumed) {
			System.out.println("Command thread Interrupted");
		}
		 System.out.println("6) Resuming ClientThread");
		 //System.out.println("7) Started command thread: there are " + Thread.activeCount() + " threads active");
	 }

	/**
     * Determines which method needs to be executed given the clients input
     * @param input the command provided by the client
     * @return an instance of ClientThread instantiated with the current path and configured IO streams
     */
	private synchronized void routeCommand(String input) {
		if (input == null) { //DO we need this anymore?
			return;
		}
		String[] inputs = input.split(" ");
		String command = inputs[0];
		String destPath = null; 

		if(inputs.length > 1) {
			destPath = inputs[1];
		} 

				System.out.println("Routing inputs " + inputs.length  + " command " + command);

		switch(command) {
			case "cd":
				changeDirectory(destPath);
				break;
			case "delete":
				deleteFile(destPath);
				break;
			case "ls":
				listFiles(destPath);
	        	break;
			case "mkdir":
				makeDirectory(destPath);
				break;
			case "pwd":
				os.println(currentPath);
				break;
			case "quit":
				running = false;
				break;
			case "put":
			case "get":
				startCommandThread(inputs);
				break;
		}
	}
}