// Caleb Was Here

import java.io.*;
import java.net.*;

public class myftpclient {

	public class FTPClient{

		String IP;
		int nport, tport;

		public FTPClient(String IP, int nport, int tport){

		}

	}

	public static void main (String[] args) throws IOException{
	
		if (args.length != 3){
			System.err.println("Please use the correct syntax.\nUSAGE: myftpclient <SERVER IP> <NORMAL PORT> <TERMINATION PORT>");
			System.exit(1);
		}
	
		printHello();
		FTPClient ftp = new FTPClient("test", 1, 2);

	}

	public static void printHello() {
		System.out.println(" ___ _____ ___   ___ ___ _____   _____ ___ \n| __|_   _| _ \\ / __| __| _ \\ \\ / / __| _ \\\n| _|  | | |  _/ \\__ \\ _||   /\\ V /| _||   /\n|_|   |_| |_|   |___/___|_|_\\ \\_/ |___|_|_\\\n                                           ");
	}

}