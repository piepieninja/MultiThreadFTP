all:
	javac CommandThread.java
	javac ClientThread.java
	javac ClientManager.java
	javac TerminateManager.java
	javac TerminateThread.java
	javac BackgroundThread.java
	javac MyFtpServer.java
	javac MyFtpClient.java
	javac RWLock.java
server:
	javac MyFtpServer.java
client:
	javac MyFtpClient.java
clean:
	rm *~
	clear
nuke:
	rm *.class
myftpserver:
	java MyFtpServer 5555 5556
myftpclient:
	java MyFtpClient localhost 5555 5556
