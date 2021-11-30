import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class IDCheckerClient implements Runnable{
	private String name;
	private Socket connection;
	
	private String address;
	private int port;
	
	private Random rand = new Random(System.currentTimeMillis());
	private static long time = System.currentTimeMillis();
	
	public IDCheckerClient(String address, int port, int counter){
		this.name = "IDChecker_"+counter;
		this.address = address;
		this.port = port;
		new Thread(this).start();
	}
	
	public void run(){
		try{
			wasteTime(2000,5000);
			connection = new Socket(address, port);
			msg("Connection Established");
			DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
			DataInputStream dis = new DataInputStream(connection.getInputStream());
			msg("Data Input/Output Streams Established.");
			
			//this is how each method will be executed
			/*
				helpVoter for id			1
			*/
			msg("Sending Thread type to Server");
			dos.writeUTF("ID_Checker");
			msg("Type sent. Awaiting confirmation.");
			String input = dis.readUTF();
			msg("Recieved: "+input);
			
			msg("Sending Name.");
			dos.writeUTF(this.name);
			msg("Name sent. Awaiting confirmation.");
			input = dis.readUTF();
			msg("Recieved: "+input);
			
			while(!input.equals("DONE")){
				msg("Sending method 1 to Server");
				dos.writeUTF(""+1);
				msg("Method number sent. Awaiting server to respond");
				input = dis.readUTF();
				msg("Recieved: "+input);
				wasteTime(2000,5000);
			}
			dos.writeUTF("DONE");
			msg("Done, leaving the voting area (exiting, patriotically)");
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
	
	private void msg(String m){
		System.out.println("["+(System.currentTimeMillis()-time)+"] "+this.name+": "+m);
	}
	private void wasteTime(int min,int max) throws InterruptedException{
		TimeUnit.MILLISECONDS.sleep(rand.nextInt(max-min)+min);
	}
}