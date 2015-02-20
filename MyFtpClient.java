// Caleb Was Here

import java.io.*;
import java.net.*;

public class MyFtpClient {

	private String IP;
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
						normalOut.println(userInput);
						break;
					default:
						System.err.println("error parsing input");
						System.exit(1);
						break;
				}
			}
		} catch (Exception e) {
			System.out.println("There was an error creating the sockets");
		}
	}

	public short parseInput(String str){
		/*
		 * -1 : invalid command
		 *  0 : kill the session
		 *  1 : valid command
		 */
		// quick check
		//if (!(str.charAt(0) == 'g') || !(str.charAt(0) == 'p') || !(str.charAt(0) == 'd') || !(str.charAt(0) == 'l') || !(str.charAt(0) == 'm') || !(str.charAt(0) == 'p') || !(str.charAt(0) == 'q') || !(str.charAt(0) == 't'))
		//	return -1;
		// long check
		if (str.contains("get") || str.contains("put") || str.contains("delete") || str.contains("ls") || str.contains("mkdir") || str.contains("pwd") || str.contains("quit") || str.contains("terminate")) 
			return -1;
		if (str.equals("terminate"))
			return 0;
		return 1;
	}

	public static void printHello() {
		System.out.println(" ___ _____ ___   ___ ___ _____   _____ ___ \n| __|_   _| _ \\ / __| __| _ \\ \\ / / __| _ \\\n| _|  | | |  _/ \\__ \\ _||   /\\ V /| _||   /\n|_|   |_| |_|   |___/___|_|_\\ \\_/ |___|_|_\\\n                                           ");
	}

	public static void main (String[] args) throws IOException{
	
		// if (args.length != 3){
		// 	System.err.println("Please use the correct syntax.\nUSAGE: MyFtpClient <SERVER IP> <NORMAL PORT> <TERMINATION PORT>");
		// 	System.exit(1);
		// }
	
		MyFtpClient ftp = new MyFtpClient("localhost", 5555, 5556);

	}

}
