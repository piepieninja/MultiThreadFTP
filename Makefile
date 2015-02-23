all:
	javac MyFtpServer.java
	javac MyFtpClient.java
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