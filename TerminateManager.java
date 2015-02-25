import java.util.*;
import java.io.*;
import java.net.*;

public class TerminateManager implements Runnable {
	ServerSocket terminateSocket;
	int port;
	BufferedReader is;
    PrintStream os;
    String currentPath;
    boolean running = true;

	public TerminateManager(int tport) {
		this.port = tport;
		System.out.printf("TerminateManager created\n");
	}

	public void run() {
		try {
			//new stuff
			terminateSocket = new ServerSocket(port);
			while(true){
				System.out.println("Connected to Terminate Manage");
				Socket socket = terminateSocket.accept(); //ServerSocket returns a new socket on an unspecified port Client instantiates one	
				try {
					is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			    	os = new PrintStream(socket.getOutputStream());
				} catch (IOException ex) {
					System.out.println(ex);
				}

				long threadId = Long.parseLong(is.readLine().split(" ")[1]);
				
				for(Thread thread : Thread.getAllStackTraces().keySet()) {
					if(thread.getId() == threadId) {
						thread.interrupt();
					}
				}

			}

		} catch (Exception e) {
			System.out.printf("There was an error creating the socket");
		}
	}
}