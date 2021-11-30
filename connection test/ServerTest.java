import java.net.*;

public class ServerTest{
	private static int port;
	private static String address;
	
	public ServerTest(){
		try{
			//creation of server socket
			//creates a socket with a default backlog queue of 50
			ServerSocket server = new ServerSocket(port);
			InetAddress myAddress = InetAddress.getLocalHost();
			System.out.println("ServerSocket created on port "+port+" with address "+myAddress.getHostAddress());
			int counter = 0;
			while(true){
				Socket connection = server.accept();
				System.out.println("Connection accepted");
				new ServerTestHelper(connection, counter);
				counter++;
			}
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
	
	public static void main(String args[]){
		port = ((int)(5000.0*Math.random())) + 5000;
		new ServerTest();
	}
}