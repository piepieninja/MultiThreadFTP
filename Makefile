all:
	perl local_cache.pl
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
	perl local_cache.pl
	java MyFtpServer 5555 5556
myftpclient:
	perl local_cache.pl
	java MyFtpClient localhost 5555 5556
yourlocal_cache:
	perl local_cache.pl