all:
	ruby mom.rb
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
	ruby mom.rb
	java MyFtpServer 5555 5556
myftpclient:
	ruby mom.rb
	java MyFtpClient localhost 5555 5556
yourmom:
	ruby mom.rb