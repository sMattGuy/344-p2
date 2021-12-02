import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class KioskHelperClient implements Runnable{
	private String name;
	private Socket connection;
	private int kioskNumber;
	
	private String address;
	private int port;
	
	private static long time = System.currentTimeMillis();
	private Random rand = new Random(System.currentTimeMillis());
	
	public KioskHelperClient(String address, int port, int counter){
		this.name = "KioskHelper_"+counter;
		this.kioskNumber = counter;
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
			
			msg("Sending Thread type to Server");
			dos.writeUTF("Kiosk_Helper");
			msg("Type sent. Awaiting confirmation.");
			String input = dis.readUTF();
			msg("Recieved: "+input);
			
			//special to kiosk helper, send the number associated with the kiosk
			msg("Sending Associated Kiosk Number");
			dos.writeUTF(""+kioskNumber);
			
			msg("Sending Name.");
			dos.writeUTF(this.name);
			msg("Name sent. Awaiting confirmation.");
			input = dis.readUTF();
			msg("Recieved: "+input);
			
			while(!input.equals("DONE")){
				this.msg("Sending method 1 to server");
				dos.writeUTF(""+1);
				msg("Method number sent. Awaiting server to respond");
				input = dis.readUTF();
				msg("Recieved: "+input);
				wasteTime(2000,5000);
			}
			dos.writeUTF("DONE");
			msg("Done, leaving the voting area");
		}
		catch(Exception e){
			System.out.println(e);
		}
	}

	public void msg(String m) {
		System.out.println("["+(System.currentTimeMillis()-time)+"] "+this.name+": "+m);
	}
	
	private void wasteTime(int min,int max) throws InterruptedException{
		TimeUnit.MILLISECONDS.sleep(rand.nextInt(max-min)+min);
	}
}