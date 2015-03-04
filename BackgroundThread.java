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
	int iD;

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
			normalOut.println(userInput);	
			//System.out.println("User input is " + userInput);//right here user input is gucci	
		} catch (Exception e) {
			System.out.println(e);
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
				getFile(cmd + " " + fileName);
				break;
			case "put":
				putFile(cmd + " " +fileName);
				break;
		}
		//System.out.println("4) Terminating command in background");
	}

	public void getFile(String userInput) {
		try{
			File file = new File(userInput.split(" ")[1]);
			FileOutputStream fStream = new FileOutputStream(file);
			iD = Integer.parseInt(normalIn.readLine());
			normalOut.println("Does file exist?");
			String[] s = normalIn.readLine().split(" ");
			if (s[0].equals("exists")) {
				System.out.println("Put Command ID: " + iD);
				int fileSize = Integer.parseInt(s[1]);
		    	byte[] buffer = new byte[1000];
		    	int count = 0, rBytes = 0;
		    	while (rBytes < fileSize) {
		    		String status = normalIn.readLine();
		    		normalOut.println();
		    		if(status.equals("running")){
						count = dis.read(buffer);
						fStream.write(buffer, 0, count);
						rBytes += count;
		    		} else {
		    			fStream.close();
						file.delete();
		    			return;
		    		}
		    	}
		    	fStream.close();
			} else {
				System.out.println("ERROR: That file does not exist");
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void putFile(String userInput){
		try{
			iD = Integer.parseInt(normalIn.readLine());
			String fileName = System.getProperty("user.dir") + "/" + userInput.split(" ")[1];
			File file = new File(fileName);
			//Check if directory exists
			if (!file.exists()) {
				System.out.println("ERROR: That file does not exist");
				normalOut.println("no file");
			} else if (file.exists()) {
				System.out.println("Put Command ID: " + iD);
				int fileSize = (int)file.length();
				String response = "exists " + fileSize;
				normalOut.println(response);
	    		byte[] buffer = new byte[1000];
	    		BufferedInputStream fs = new BufferedInputStream(new FileInputStream(file));
	    		int count = 0;

	    		while((count = fs.read(buffer)) > 0) {
	    			String status = normalIn.readLine();
	    			if (status.equals("running")){
	    				dos.write(buffer, 0, count);
	    			} else {
	    				return;
	    			}
				}
				fs.close();
			}
		} catch (Exception e) { 
			System.out.println(e);
		}
	}
}
