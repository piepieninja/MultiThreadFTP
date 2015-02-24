import java.io.*;
import java.net.*;
import java.util.*;

public class MyFtpClient {

	private String IP;
	private int nport, tport;
	private static Socket normalSocket;	
	private static Socket terminateSocket;
	private static BufferedReader normalIn;
	private static BufferedReader terminateIn;
	private static PrintWriter normalOut;
	private static PrintWriter terminateOut;
    private static BufferedReader stdIn;

	public MyFtpClient(String IP, int nport, int tport){
		this.IP = IP;
		this.nport = nport;
		this.tport = tport;
		beginCommunication();
	}

	public void clientPutFile(String userInput) throws Exception {
		String fileName = System.getProperty("user.dir") + "/" + userInput.substring(userInput.indexOf(' ') + 1);
		File file = new File(fileName);
		//Check if directory exists
		if (!file.exists()) {
			System.out.println("ERROR: That file does not exist");
		} else if (file.exists()) {
			//send command
			normalOut.println(userInput.split(" ")[0]);
			normalIn.readLine();
			//send file name and length
			normalOut.println((int)file.length());
			normalIn.readLine();

			int length = (int)file.length();
			byte[] fileBytes = new byte[(int) length];
			FileInputStream fis = new FileInputStream(file);
    		BufferedInputStream bis = new BufferedInputStream(fis);
    		BufferedOutputStream out = new BufferedOutputStream(normalSocket.getOutputStream());
    		int count = 0;

    		while ((count = bis.read(fileBytes)) > 0) {
        		out.write(fileBytes, 0, count);
    		}

    		out.flush();
    		out.close();
    		fis.close();
    		bis.close();
		}
	}

	public void clientGetFile(String userInput) throws Exception {
		System.out.println("Executing get command client side");
		normalOut.println(userInput);
		System.out.println(normalIn.readLine());
	}


	public void routeCommand(String userInput) throws Exception {
		normalOut.println(userInput);
		String command = userInput.split(" ")[0];
		String data;

    	if (command.equals("pwd")) {
			//prints out the current directory
			System.out.println(normalIn.readLine());
		} else if (command.equals("ls")) {
			//prints out all of the files in the current directory
			data = normalIn.readLine();
			//check if there are no files or directories in current directory
			if (data.equals("")) {
				System.out.print("");
			} else {
				data = data.replace("<&&newline&&>", "\n");
				System.out.println(data.substring(0, data.length() - 1));
			}
		} else if (command.equals("cd")) {
			//changes the directory to the specified directory
			data = normalIn.readLine();
			if (data.equals("no directory")) {
				System.out.println("ERROR: Directory does not exist");
			} else if (data.equals("not a directory")) {
				System.out.println("ERROR: That is not a directory");
			}
		} else if (command.equals("mkdir")) {
			//creates a directory in the current directory
			data = normalIn.readLine();
			if (data.equals("failure")) {
				System.out.println("ERROR: Cannot create directory");
			} 
		} else if (command.equals("delete")) {
			//deletes the specified file
			data = normalIn.readLine();

			if (data.equals("no file")) {
				System.out.println("ERROR: File does not exist");
			} else if (data.equals("failure")) {
				System.out.println("ERROR: That is not a file");
			}
		} else if (command.equals("quit")) {
			//close all streams and exit
			normalIn.close();
			terminateIn.close();
			normalOut.close();
			terminateOut.close();
			stdIn.close();
			System.exit(0);
		}
	}

	public void sendCommand(String userInput ) throws Exception {
		String command = userInput.split(" ")[0];
		if (command.equals("put")) {
			clientPutFile(userInput);
		} else if (command.equals("get")) {
			clientGetFile(userInput);
		} else {
			routeCommand(userInput);
		}
	}

	private void setupSocket() throws Exception{
		 normalSocket = new Socket(IP, nport); //creates a socket to connect to the client port on the server
		 terminateSocket = new Socket(IP, tport); //creates a socket to connect to the terminate port on the server
		 normalIn = new BufferedReader( new InputStreamReader(normalSocket.getInputStream())); //Input for each socket
		 terminateIn = new BufferedReader(new InputStreamReader(terminateSocket.getInputStream()));
		 normalOut = new PrintWriter(normalSocket.getOutputStream(), true); //Output for each socket
    	 terminateOut = new PrintWriter(terminateSocket.getOutputStream(), true);
         stdIn = new BufferedReader(new InputStreamReader(System.in)); //Input from the user
	}

	public void beginCommunication(){
		try {
			setupSocket();
			String userInput;
			short sh;
			while(true) {
				System.out.print("myftp> ");
				userInput = stdIn.readLine();

				sh = parseInput(userInput);
				switch (sh) {
					case -1:
						System.out.println("ERROR: invalid command");
						break;
					case 0:
						terminateOut.println("terminate");
						break;
					case 1:
						sendCommand(userInput);
						break;
					default:
						System.err.println("error parsing input");
						System.exit(1);
						break;
				}
			}
		} catch (Exception e) {
			System.out.println("There was an error creating the sockets\n" + e);
		}
	}

	public short parseInput(String str) {
		ArrayList<String> commands = new ArrayList<String>(Arrays.asList("get", "delete", "ls", "pwd", "put", "cd", "mkdir", "quit"));
		String cmd = str.split(" ")[0];
		if (commands.contains(cmd)) {
			return 1;
		} else if (cmd.equals("terminate")) {
			return 0;
		} else {
			return -1;
		}
	}

	public static void printHello() {
		System.out.println(" ___ _____ ___   ___ ___ _____   _____ ___ \n| __|_   _| _ \\ / __| __| _ \\ \\ / / __| _ \\\n| _|  | | |  _/ \\__ \\ _||   /\\ V /| _||   /\n|_|   |_| |_|   |___/___|_|_\\ \\_/ |___|_|_\\\n                                           ");
	}

	public static void main (String[] args) throws IOException{
		if (args.length != 3){
			System.out.println("Please use the correct syntax.\nUSAGE: MyFtpClient <SERVER IP> <NORMAL PORT> <TERMINATION PORT>");
		 	System.exit(1);
		} else if (args[1] == args[2]){
			System.out.println("ERROR: Please enter different ports.\nUSAGE: java MyFtpClient <IP> <NPORT> <TPORT>");
			System.exit(1);
		} 
	
		String IP = args[0];
		int nport = Integer.parseInt(args[1]);
		int tport = Integer.parseInt(args[2]);

		printHello();

		MyFtpClient ftp = new MyFtpClient("localhost", 5555, 5556);
	}

}
