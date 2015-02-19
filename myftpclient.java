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
			while(true) {
				System.out.print("myftp>");
				userInput = stdIn.readLine();
			}
		} catch (Exception e) {
			System.out.println("There was an error creating the sockets");
		}
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
