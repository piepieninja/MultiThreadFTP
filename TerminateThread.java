import java.util.*;
import java.io.*;
import java.net.*;

public class TerminateThread implements Runnable {
	Socket mySocket;
	BufferedReader is;
    PrintStream os;
    String input;
    boolean running = true;

    public TerminateThread(Socket terminateSocket, String userInput) {
    	this.mySocket = terminateSocket;
    	this.input = userInput;
    }

    public void run() {
    	try {
    		long threadId = Long.parseLong(input.split(" ")[1]);
			System.out.println("Terminator got command");
			for(Thread thread : Thread.getAllStackTraces().keySet()) {
				System.out.println("Thread: " + thread.getId());
				if(thread.getId() == threadId) {
					System.out.println("Interrupted that shit!");
					thread.interrupt();
				}
			}
    	} catch (Exception e) {
    		//System.out.println(e + "here");
    	}
    }

}