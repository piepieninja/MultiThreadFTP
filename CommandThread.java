import java.util.*;
import java.io.*;
import java.net.*;

public class CommandThread implements Runnable {

	BufferedReader is;
    PrintStream os;
    DataOutputStream dos;
    DataInputStream dis;
    String[] inputs;
    String currentPath;

    /**
     * The constructor for the ClientThread class
     * @param clientSocket the socket used to send/recv from 
     * @param cmd the command being processed by the command thread
     * @return an instance of ClientThread instantiated with the current path and configured IO streams
     */
	public CommandThread(Socket clientSocket, String[] inputs, String path) {
		System.out.println("1) created command thread");
		try {
			is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	    	os = new PrintStream(clientSocket.getOutputStream());
	    	dos = new DataOutputStream(clientSocket.getOutputStream());
			dis = new DataInputStream(clientSocket.getInputStream());
			this.inputs = inputs;
			this.currentPath = path;		
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Overidden to implement the Runnable interface
	 */
	public void run() {
		try{
			System.out.println("3) Started command thread: there are " + Thread.activeCount() + " threads active");
			String cmd = this.inputs[0];
			String fileName = this.inputs[1];
			//Goes into an infinite loop if we use if else statements, why?
			switch(cmd) {
				case "get":
					getFile(fileName);
					break;
				case "put":
					putFile(fileName);
					break;
			}
			System.out.println("5) Terminating command thread");
		} catch (Exception consumed) {
			//Thread.currentThread().interrupt();
			//System.out.println("INterrupted thread woo hoo");
			//return;
		}
	}

	/**
	* Gets a remote file from the server and gives it to the client
	* @param destPath, the destination directory
	*/
	public void getFile(String fileName){
		try{
			File file = new File(currentPath + "/" + fileName);
			BufferedInputStream fs = new BufferedInputStream(new FileInputStream(file));
			if (!file.exists()) {
				os.println("file does not exist");
			} else if (file.exists()) {
				os.println("file exists");
			}
			String i = is.readLine();
			if (i.equals("done")) {
				return;
			} else if (i.equals("send file length")) {
				os.println((int)file.length());
				is.readLine(); //client sends "send file"
				byte[] buffer = new byte[1000];
				//BufferedInputStream fs = new BufferedInputStream(new FileInputStream(file));
	    		int count = 0;
	    		try{
	    			Thread.sleep(10000);
	    			while((count = fs.read(buffer)) > 0) {
					dos.write(buffer, 0, count);
					}
	    		} catch (InterruptedException terminate){
	    			fs.close();
	    			return;
	    		}
				fs.close();
				System.out.println("4) completed getFile");
			}
		} catch(IOException e) {
			System.out.println("there was an error getting your file");
			//fs.close();
			return;
		} 
	}

	/**
	* Put a file from the client onto the server
	* @param fileName, the name of the file to put
	* @param destPath, the destination directory
	*/
	private void putFile(String fileName) {
		try{
			//os.println("received put command");
			int fileSize = Integer.parseInt(is.readLine());
			//os.println("received file size");
			File newFile = new File(currentPath + "/" + fileName);
	    	FileOutputStream fStream = new FileOutputStream(newFile);
	    	byte[] buffer = new byte[1000];
	    	int count = 0, rBytes = 0;
	    	try {
		    	Thread.sleep(10000);
		    	while (rBytes < fileSize) {
		    		os.println("running");
		    		count = dis.read(buffer);
		    		fStream.write(buffer, 0, count);
		    		rBytes += count;
		    	}	
	    	} catch (InterruptedException terminate) {
	    		os.println("terminate");
	    		fStream.close();
	    		newFile.delete();
				return;
	    	}
	    	fStream.close();
	    	System.out.println("4) completed puteFile");	
		} catch(IOException e) {
			System.out.println("INterrupted putting your file");
			return;
		}
	}
}
