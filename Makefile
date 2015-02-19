all:
	javac myftpserver.java
	javac myftpclient.java
server:
	javac myftpserver.java
client:
	javac myftpclient.java
clean:
	rm *.class
	rm *~
	clear

