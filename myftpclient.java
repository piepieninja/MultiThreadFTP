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
		}

	

	public static void printHello() {
		System.out.println(" ___ _____ ___   ___ ___ _____   _____ ___ \n| __|_   _| _ \\ / __| __| _ \\ \\ / / __| _ \\\n| _|  | | |  _/ \\__ \\ _||   /\\ V /| _||   /\n|_|   |_| |_|   |___/___|_|_\\ \\_/ |___|_|_\\\n                                           ");
	}

	public static void main (String[] args) throws IOException{
	
		// if (args.length != 3){
		// 	System.err.println("Please use the correct syntax.\nUSAGE: MyFtpClient <SERVER IP> <NORMAL PORT> <TERMINATION PORT>");
		// 	System.exit(1);
		// }
	
		printHello();
		//FTPClient ftp = new FTPClient("test", 1, 2);

	}

}
