package server;

import java.io.IOException;
import java.net.ServerSocket;

public class MainServer {
	
	
	public MainServer ()
	{
		try {
						
			
			while (true)
			{
	  		// code for having it listen to a dedicated port           			// for incomming connections
			//spawn a helper,in here is named SubServerThread 	
				
			}//while
		}//try
		catch (IOException e)
		{
			System.out.println("Unable to listen to port.");
			e.printStackTrace();
		}//catch
	}//constructor
	
	public static void main (String [] args)
	{
		new MainServer();
	}//main
	
	
}//class
