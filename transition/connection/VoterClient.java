import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class VoterClient implements Runnable{
	private String name;
	private Socket connection;
	
	private String address;
	private int port;
	
	private Random rand = new Random(System.currentTimeMillis());
	private static long time = System.currentTimeMillis();
	
	public VoterClient(String address, int port, int counter){
		this.name = "Voter_"+counter;
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
			msg("Sending Thread name to Server");
			dos.writeUTF(this.name);
			msg("Name sent. Sending Method to run: 3");
			dos.writeUTF(""+3);
			msg("Method number sent. Awaiting server to respond");
			String input = dis.readUTF();
			msg("Recieved: "+input);
			
			msg("Done, exiting");
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