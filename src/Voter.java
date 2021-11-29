import java.util.Random;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

class Voter implements Runnable{
	//local variables
	public Tracker tracker;	//tabs on voters left per step
	private String name;	//unique id for voter
	//monitors for the voters
	private ID_Check line;	
	private Vector<Kiosk> kiosks;
	private ScanMachine scanner;
	//random and time to use for artificial delay
	private Random rand = new Random(System.currentTimeMillis());
	private static long time = System.currentTimeMillis();
	//constructor
	public Voter(String name, ID_Check line, Tracker tracker, Vector<Kiosk> kiosks, ScanMachine scanner){
		this.name = name;
		this.line = line;
		this.tracker = tracker;
		this.kiosks = kiosks;
		this.scanner = scanner;
		//auto start thread
		new Thread(this).start();
	}
	
	public void run(){
		try{
			/*
				between every movement or big action a delay is applied
				stripped down flow is
					delay -> enter section -> perform action -> leave section
			*/
			//id section
			this.wasteTime(1000,2000);				//delay when starting
			this.msg("Entering the ID line");	
			line.enterLine(this.name);				//enter line and wait
			this.msg("Getting ID checked");			//id check notified voter, 
			this.wasteTime(2000,5000);				//time taken to finish getting id checked
			line.exitLine(this.name);				//exit line, update tracker and notify helper
			
			//kiosk section
			this.msg("Moving on to Kiosk");		
			this.wasteTime(1000,3000);				//delay when entering kiosk
			this.msg("Looking for shortest kiosk line");
			//checks the status of every kiosk and picks the shortest line
			int chosenKiosk = 0;
			for(int i=0;i<kiosks.size();i++){
				if(kiosks.elementAt(i).lineSize() < kiosks.elementAt(chosenKiosk).lineSize()){
					chosenKiosk = i;
				}
			}
			this.msg("Selected kiosk "+chosenKiosk);
			kiosks.elementAt(chosenKiosk).enterLine(this.name);	//voter enters line for specific kiosk
			this.wasteTime(2000,5000);							//time spent voting at kiosk
			kiosks.elementAt(chosenKiosk).exitLine(this.name);	//exit and alert helper that theyre done
			
			//scan machine section
			this.msg("Moving on to Scan Machine");
			this.wasteTime(2000,5000);
			this.msg("entering scan line");
			scanner.enterScanLine(this.name);	//enters area for scanner and waits for a group to form
			this.wasteTime(1000,2000);
			//decide if this voter needs help inserting paper into a machine
			if(rand.nextFloat() < 0.75){
				//voter needs help
				this.msg("Needs help, getting a helper");
				scanner.getHelp();				//enter a help queue
				this.msg("Being helped");		//was notified
				this.wasteTime(3000,5000);		//artificial delay
				this.msg("done being helped");
				scanner.gotHelp();				//release helper that was helping voter
			}
			//voter exits the scan machine squad
			scanner.leaveGroup(this.name);
			this.msg("Done, leaving the voting area (exiting, patriotically)");
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