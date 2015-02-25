import java.util.*;
import java.io.*;
import java.net.*;

public class BackgroundThread implements Runnable {

	BufferedReader is;
    PrintStream os;
    DataOutputStream dos;
    DataInputStream dis;
    BufferedReader normalIn;
	PrintWriter normalOut;
	String[] inputs;

    /**
     * The constructor for the ClientThread class
     * @param clientSocket the socket used to send/recv from 
     * @param cmd the command being processed by the command thread
     * @return an instance of ClientThread instantiated with the current path and configured IO streams
     */
	public BackgroundThread(Socket normalSocket, String userInput) {
		try {
			is = new BufferedReader(new InputStreamReader(normalSocket.getInputStream()));
	    	os = new PrintStream(normalSocket.getOutputStream());
	    	dos = new DataOutputStream(normalSocket.getOutputStream());
			dis = new DataInputStream(normalSocket.getInputStream());
			normalIn = new BufferedReader( new InputStreamReader(normalSocket.getInputStream())); //Input for each socket
			normalOut = new PrintWriter(normalSocket.getOutputStream(), true); //Output for each socket
			inputs = userInput.split(" ");		
			//System.out.println("User input is " + userInput);//right here user input is gucci	
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void getFile(String userInput) {
		try{
			normalOut.println(userInput);
			System.out.println("Get Command ID: " + normalIn.readLine());
			String i = normalIn.readLine();
			if (i.equals("file does not exist")) {
				System.out.println("ERROR: That file does not exist");
				normalOut.println("done");
			} else if (i.equals("file exists")) {
				normalOut.println("send file length");
				int fileSize = Integer.parseInt(normalIn.readLine());
				normalOut.println("send file");
				String fileName = userInput.split(" ")[1];
				FileOutputStream fStream = new FileOutputStream(new File(fileName));
		    	byte[] buffer = new byte[1000];
		    	int count = 0, rBytes = 0;
		    	while (rBytes < fileSize) {
		    		count = dis.read(buffer);
		    		fStream.write(buffer, 0, count);
		    		rBytes += count;
		    	}
		    	fStream.close();
			}
		} catch (Exception e) {

		}
		
	}
	

	public void putFile(String userInput){
		try{
			String fileName = System.getProperty("user.dir") + "/" + userInput.split(" ")[1];
			normalOut.println(userInput);
			File file = new File(fileName);
			//Check if directory exists
			if (!file.exists()) {
				System.out.println("ERROR: That file does not exist");
			} else if (file.exists()) {
				//receive command ID
				System.out.println("Put Command ID: " + normalIn.readLine());
				//send file length
				normalOut.println((int)file.length());
				//normalIn.readLine();
	    		int fileSize = (int)file.length();
	    		byte[] buffer = new byte[1000];
	    		BufferedInputStream fs = new BufferedInputStream(new FileInputStream(file));
	    		int count = 0;

	    		while((count = fs.read(buffer)) > 0) {
					dos.write(buffer, 0, count);
				}
				fs.close();
			}
		} catch (Exception e) {

		}
	
	}


	/**
	 * Overidden to implement the Runnable interface
	 */
	public void run() {
		//System.out.println("1) Running a BackgroundThread in the background");
		String cmd = this.inputs[0];
		String fileName = this.inputs[1];
		//Goes into an infinite loop if we use if else statements, why?
		switch(cmd) {
			case "get":
				getFile(fileName);
				break;
			case "put":
				putFile(cmd + " " +fileName);
				break;
		}
		//System.out.println("4) Terminating command in background");
	}
}
