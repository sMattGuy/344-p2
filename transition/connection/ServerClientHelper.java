import java.io.*;
import java.net.Socket;
import java.util.*;

class ServerClientHelper implements Runnable{
	private Socket connection;
	private String name;
	
	private String threadType;
	
	public ServerClientHelper(Socket connection, int counter){
		this.name = "ServerClientHelper_"+counter;
		this.connection = connection;
		new Thread(this).start();
	}
	
	public void run(){
		try{
			msg("ServerClientHelper started");
			//set up input and output connection
			DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
			DataInputStream dis = new DataInputStream(connection.getInputStream());
			msg("Data Input/Output Stream created");
			
			msg("Getting Client Thread Type");
			String threadType = dis.readUTF();
			msg("Client has declared they are type "+threadType);
			
			//from here we can start getting the methods the client wants to run
			String methodToRun = dis.readUTF();
			msg("Client has requested to run method "+methodToRun);
			
			msg("Done helping, Responding to Client");
			dos.writeUTF(this.name+" is complete");
		}
		catch(Exception e){
			System.out.println(e);
		}
		
	}
	
	private void methodSelect(int methodID){
		/*
			similar method to example given
		*/
		if(threadType.equals("Voter")){
			switch(methodID){
				
			}
		}
		else if(threadType.equals("ID_Checker")){
			switch(methodID){
				
			}
		}
		else if(threadType.equals("Kiosk_Helper")){
			switch(methodID){
				
			}
		}
		else if(threadType.equals("Scan_Helper")){
			switch(methodID){
				
			}
		}
	}
	
	private void msg(String m){
		System.out.println("["+(System.currentTimeMillis()-time)+"] "+this.name+": "+m);
	}
}