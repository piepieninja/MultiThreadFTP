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
    RWLock rwLock;

    /**
     * The constructor for the ClientThread class
     * @param clientSocket the socket used to send/recv from 
     * @param cmd the command being processed by the command thread
     * @return an instance of ClientThread instantiated with the current path and configured IO streams
     */
	public CommandThread(Socket clientSocket, String[] inputs, String path, RWLock rwLock) {
		this.rwLock = rwLock;
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
	public synchronized void getFile(String fileName) {
		rwLock.reads++;
		try{
			is.readLine(); 
			File file = new File(currentPath + "/" + fileName);
			BufferedInputStream fs = new BufferedInputStream(new FileInputStream(file));
			if (!file.exists()) {
				os.println("no file");
				fs.close();
				file.delete();
			} else if (file.exists()) {
				String response = "exists " + file.length();
				os.println(response);
				byte[] buffer = new byte[1000];
	    		int count = 1;
				while((count = fs.read(buffer)) > 0) {
	    			os.println("running");
	    			is.readLine();
					dos.write(buffer, 0, count);

	    			if(Thread.interrupted()){
		    			os.println("terminate");
		    			fs.close();
		    			return;	
	    			}
	    		}
				fs.close();
				System.out.println("4) completed getFile");
			}
			rwLock.reads--;
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
		System.out.println(rwLock.writes);
		while (rwLock.writes != 0) {
			try {
				System.out.println("This thread is waiting");
				wait();
			} catch (InterruptedException e) {
				System.out.println("This thread is resuming the write");
			}
		}
		try{

			String[] s = is.readLine().split(" ");
			if (s[0].equals("exists")) {
				int fileSize = Integer.parseInt(s[1]);
				File newFile = new File(currentPath + "/" + fileName);
		    	FileOutputStream fStream = new FileOutputStream(newFile);
		    	byte[] buffer = new byte[1000];
		    	int count = 0, rBytes = 0;
		    	while (rBytes < fileSize) {
		    		if(Thread.interrupted()){
				    	os.println("terminate");
			    		fStream.close();
			    		newFile.delete();
						return;
			    	}
		    		os.println("running");
		    		count = dis.read(buffer);
		    		fStream.write(buffer, 0, count);
		    		rBytes += count;
			    }
		    	fStream.close();
	    		System.out.println("4) completed Put File");
	    	}
	    	if (rwLock.writes > 0) {
	    		rwLock.writes--;
	    	} 
	    	notifyAll();
		} catch(IOException e) {
			System.out.println("Interrupted putting your file");
			return;
		}
	}
}
