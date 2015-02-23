import java.util.*;
import java.io.*;
import java.net.*;

public class TerminateManager implements Runnable {
	private ServerSocket terminateSocket;
	private int port;

	public TerminateManager(int tport) {
		this.port = tport;
		System.out.printf("TerminateManager created\n");
	}

	public void run() {
		try {
			terminateSocket = new ServerSocket(port);

		} catch (Exception e) {
			System.out.printf("There was an error creating the socket");
		}
	}
}