import java.io.*;
import java.net.*;

public class MyFtpClient {

	private String IP, command;
	private int nport, tport;

	public MyFtpClient(String IP, int nport, int tport){
		this.IP = IP;
		this.nport = nport;
		this.tport = tport;

		try (
			//creates a socket to connect to the client port on the server
			Socket normalSocket = new Socket(IP, nport);
			
			//creates a socket to connect to the terminate port on the server
			Socket terminateSocket = new Socket(IP, tport);

			//Input for each socket
			BufferedReader normalIn = 
				new BufferedReader(
					new InputStreamReader(normalSocket.getInputStream()));
			BufferedReader terminateIn = 
				new BufferedReader(
					new InputStreamReader(terminateSocket.getInputStream()));

			//Output for each socket
			PrintWriter normalOut =
                new PrintWriter(normalSocket.getOutputStream(), true);
        	PrintWriter terminateOut =
                new PrintWriter(terminateSocket.getOutputStream(), true);

            //Input from the user
            BufferedReader stdIn =
                new BufferedReader(
                    new InputStreamReader(System.in))

		) {
			String userInput, data;
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
						if (command.equals("put")) {
							File file = new File(System.getProperty("user.dir") + "/" + userInput.substring(userInput.indexOf(' ') + 1));
							System.out.println(System.getProperty("user.dir") + "/" + userInput.substring(userInput.indexOf(' ') + 1));
							//Check if directory exists
							if (!file.exists()) {
								System.out.println("ERROR: That file does not exist");
							} else if (file.exists()) {
								// add stuff and things
								byte [] fileByteArray  = new byte [(int)file.length()];
								FileInputStream fis = new FileInputStream(file);
								BufferedInputStream bis = new BufferedInputStream(fis);
								bis.read(fileByteArray,0,fileByteArray.length);
								OutputStream os = normalSocket.getOutputStream();
								os.write(fileByteArray,0,fileByteArray.length);
		      					os.flush();
    							fis.close();
    							bis.close();
							} else {
								System.out.println("ERROR: That is not a file.");
							}
						}

						normalOut.println(userInput);

						break;
					default:
						System.err.println("error parsing input");
						System.exit(1);
						break;
				}

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
		} catch (Exception e) {
			System.out.println("There was an error creating the sockets\n" + e);
		}
	}

	public short parseInput(String str) {
		/*
		 * -1 : invalid command
		 *  0 : terminate the running command (get or put)
		 *  1 : valid command
		 */
		
		if (str.contains(" ")) {
			command = str.substring(0, str.indexOf(' '));
		} else {
			command = str;
		}


		if (command.equals("get") 	 ||   command.equals("put") 	||
			command.equals("delete") ||   command.equals("cd")		||
			command.equals("ls")  	 ||   command.equals("mkdir") 	||
			command.equals("pwd") 	 ||   command.equals("quit")) {
			return 1;
		} else if (command.equals("terminate")) {
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
