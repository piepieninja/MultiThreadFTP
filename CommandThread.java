import java.util.*;
import java.io.*;
import java.net.*;


public class CommandThread implements Runnable {

	Socket mySocket;
	BufferedReader is;
    PrintStream os;
    boolean running;


    /**
     * The constructor for the ClientThread class
     * @param clientSocket the socket used to send/recv from 
     * @param cmd the command being processed by the command thread
     * @return an instance of ClientThread instantiated with the current path and configured IO streams
     */
	public CommandThread(Socket clientSocket, String cmd, BufferedReader is, PrintStream os) {
		System.out.println("CREATED COMMAND THREAD");
		this.is = is;
		this.os = os;
		this.running = true;
		// try {
		// 	is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	 //    	os = new PrintStream(clientSocket.getOutputStream());
		// } catch (IOException ex) {
		// 	System.out.println(ex);
		// }
	}

	/**
	 * Overidden to implement the Runnable interface
	 */
	public void run() {
		System.out.println("RUNNING COMMAND THREAD: " + "Threads" + Thread.activeCount() + "active");
		String input;
		synchronized(this){
			try {
				System.out.println("PRINTING FROM COMMAND THREAD");
				os.println("Hello from the get thread");
			} catch (Exception ex) {
				System.out.println(ex);
			}
			finally {
				System.out.println("Command Finally");
				running = false;
					///notifyAll();
			}
		}
	}
}