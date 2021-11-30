import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

class ServerClientHelper implements Runnable{
	//connection
	private Socket connection;
	private String name;
	
	//client information
	private String threadType;
	private String threadName;
	private int chosenKiosk;
	private String sendDone = "OK";
	
	//monitors to work with
	private ID_Check line;	
	private Vector<Kiosk> kiosks;
	private ScanMachine scanner;
	
	//time
	private Random rand = new Random(System.currentTimeMillis());
	private static long time = System.currentTimeMillis();
	
	//constructor
	public ServerClientHelper(Socket connection, int counter, ID_Check line, Vector<Kiosk> kiosks, ScanMachine scanner){
		this.name = "ServerClientHelper_"+counter;
		this.connection = connection;
		this.line = line;
		this.kiosks = kiosks;
		this.scanner = scanner;
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
			threadType = dis.readUTF();
			dos.writeUTF("OK");
			
			msg("Client has declared they are type "+threadType+", getting name.");
			threadName = dis.readUTF();
			dos.writeUTF("OK");
			
			if(threadName.equals("Kiosk_Helper")){
				msg("Getting Kiosk Helper number");
				chosenKiosk = Integer.parseInt(dis.readUTF());
				dos.writeUTF("OK");
				msg("Kiosk Helper is in kiosk "+chosenKiosk);
			}
			
			msg("Client has declared they are "+threadName+", beginning routine");
			
			while(true){
				//from here we can start getting the methods the client wants to run
				msg("Getting method to run");
				String methodToRun = dis.readUTF();
				msg("Client has requested to run method "+methodToRun);
				if(methodToRun.equals("DONE")){
					msg("Client has signaled they are done, exiting");
					break;
				}
				methodSelect(Integer.parseInt(methodToRun));
				msg("Done helping with method, Responding to Client");
				dos.writeUTF(sendDone);
			}
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
				case 1:
					//enter id check line
					msg(threadName+" entering ID line");
					line.enterLine(threadName);
					msg(threadName+" getting ID checked");
					break;
				case 2:
					//exit id line
					msg(threadName+" exiting the ID line");
					line.exitLine(threadName);
					msg(threadName+" exited the ID line");
					break;
				case 3:
					//get shortest kiosk line
					msg(threadName+" looking for shortest kiosk line");
					getShortestLine();
					msg(threadName+" selected kiosk "+chosenKiosk);
					break;
				case 4:
					//enter kiosk line
					msg(threadName+" is entering the kiosk line");
					kiosks.elementAt(chosenKiosk).enterLine(threadName);
					msg(threadName+" is voting at a kiosk");
					break;
				case 5:
					//exit kiosk line
					msg(threadName+" is exiting the kiosk line");
					kiosks.elementAt(chosenKiosk).exitLine(threadName);
					msg(threadName+" exited kiosk line");
					break;
				case 6:
					//enter scan line
					msg(threadName+" is entering the scan machine line");
					scanner.enterScanLine(threadName);
					msg(threadName+" is scanning their ballot");
					break;
				case 7:
					//check if need help and get if necessary
					msg(threadName+" seeing if they need help");
					if(getScanHelp()){
						msg(threadName+" didn't need help");
					}
					break;
				case 8:
					//leave and exit
					msg(threadName+" Leaving scanning group");
					scanner.leaveGroup(threadName);
					msg(threadName+" left scanning group");
					break;
			}
		}
		else if(threadType.equals("ID_Checker")){
			switch(methodID){
				case 1:
					msg(threadName+" ready to help next voter");
					if(line.startHelping(threadName)){sendDone = "DONE"; break;};
					msg(threadName+" helping a voter");
					break;
			}
		}
		else if(threadType.equals("Kiosk_Helper")){
			switch(methodID){
				case 1:
					msg(threadName+" ready to help voter at kiosk");
				break;
			}
		}
		else if(threadType.equals("Scan_Helper")){
			switch(methodID){
				
			}
		}
	}
	
	private void getShortestLine(){
		chosenKiosk = 0;
		for(int i=0;i<kiosks.size();i++){
			if(kiosks.elementAt(i).lineSize() < kiosks.elementAt(chosenKiosk).lineSize()){
				chosenKiosk = i;
			}
		}
	}
	
	private boolean getScanHelp(){
		if(rand.nextFloat() < 0.75){
			//voter needs help
			this.msg(threadName+" Needs help scanning, getting a helper");
			scanner.getHelp();				//enter a help queue
			this.msg(threadName+" Being helped");		//was notified
			this.wasteTime(3000,5000);		//artificial delay
			this.msg(threadName+" done being helped");
			scanner.gotHelp();				//release helper that was helping voter
			return false;
		}
		return true;
	}
	
	private void msg(String m){
		System.out.println("["+(System.currentTimeMillis()-time)+"] "+this.name+": "+m);
	}
	private void wasteTime(int min,int max){
		try{
			TimeUnit.MILLISECONDS.sleep(rand.nextInt(max-min)+min);
		}catch(Exception e){
			System.out.println(e);
		}
	}
}