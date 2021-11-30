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
			/*
				enterLine for id			1
				exitLine for id				2
				
				get shortest kiosk line		3
				enter kiosk					4
				exit kiosk					5
				
				enter scan line				6
				determine if needs help 	7
					get help if needed		
					done getting help		
				leave and exit				8
			*/
			msg("Sending Thread type to Server");
			dos.writeUTF("Voter");
			msg("Type sent. Awaiting confirmation.");
			String input = dis.readUTF();
			msg("Recieved: "+input);
			
			msg("Sending Name.");
			dos.writeUTF(this.name);
			msg("Name sent. Awaiting confirmation.");
			input = dis.readUTF();
			msg("Recieved: "+input);
			
			for(int i=1;i<3;i++){
				msg("Sending method "+i+" to Server");
				dos.writeUTF(""+i);
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