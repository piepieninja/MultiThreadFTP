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
			
				Socket socket = terminateSocket.accept(); //ServerSocket returns a new socket on an unspecified port Client instantiates one	
				try {
					is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			    	os = new PrintStream(socket.getOutputStream());
				} catch (IOException ex) {
					System.out.println(ex);
				}

				long threadId = Long.parseLong(is.readLine().split(" ")[1]);
				System.out.println("Terminator got command");
				for(Thread thread : Thread.getAllStackTraces().keySet()) {
					System.out.println("Thread: " + thread.getId());
					if(thread.getId() == threadId) {
						System.out.println("Interrupted that shit!");
						thread.interrupt();
					}
				}
				socket.close();

			}

		} catch (Exception e) {
			System.out.printf("There was an error creating the socket");
		}
	}
}