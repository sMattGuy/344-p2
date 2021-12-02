import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

class ServerTestHelper implements Runnable{
	private Socket connection;
	private String name;
	
	private Random rand = new Random(System.currentTimeMillis());
	private static long time = System.currentTimeMillis();
	
	public ServerTestHelper(Socket connection, int counter){
		this.name = "ServerTestHelper_"+counter;
		this.connection = connection;
		new Thread(this).start();
	}
	
	public void run(){
		try{
			msg("ServerTestHelper started");
			//set up input and output connection
			DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
			DataInputStream dis = new DataInputStream(connection.getInputStream());
			msg("Data Input/Output Stream created");
			String threadType = dis.readUTF();
			msg("Client has declared they are type "+threadType);
			String methodToRun = dis.readUTF();
			msg("Client has requested to run method "+methodToRun);
			wasteTime(2000,3000);
			msg("Done helping, Responding to Client");
			dos.writeUTF(this.name+" is complete");
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