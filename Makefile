all:
	javac myftpserver.java
	javac myftpclient.java
server:
	javac myftpserver.java
	java myftpserver
client:
	javac myftpclient.java
	java myftpclient
clean:
	rm *~
	clear
