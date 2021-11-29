import java.util.Random;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
/*
	plan:
	each kiosk will run individually and have its own line
	the voter can decide where it goes and stick to that monitor
	then the kiosk will have its own thread to help the voters
	once there are no voters left to help in line, the thread will terminate
	a good idea would be to have a check for an active helper, that way if a voter enters late, and the 
	kiosk helper thought it was done, it can re activate
*/
class Kiosk{
	//name for the kiosk
	private int kioskNum = 0;
	//this is the kiosk line
	private Vector<Object> waitingVoters = new Vector<>();
	//tracker to keep a number on how many voters are left
	public Tracker tracker;
	
	//kiosk thread
	private Object helperConvey = new Object();
	private boolean busy = false;
	private boolean first = true;
	private KioskHelper helper;
	
	//thread that is only for that kiosk
	/*
		since each kiosk only has one helper, it is better to just let it exist within its monitor
		this makes it much easier to manage, since its restricted to only its monitor
	*/
	private class KioskHelper implements Runnable{
		private String name;
		private Kiosk kiosk;
		
		public KioskHelper(String name, Kiosk kiosk){
			this.name = name;
			this.kiosk = kiosk;
		}
		
		public void run(){
			try{
				while(kiosk.waitingVoters.size() != 0){
					this.msg("Ready to help next voter to kiosk");
					//loop helpers job until voters are all gone
					kiosk.startHelping(this.name);
					this.msg("Waiting for voter to finish at kiosk");
					this.wasteTime(1000,2000);
				}
				this.kiosk.first = true;
				this.msg("Done helping voters at kiosk, leaving (exiting until more voters arrive)");
			}
			catch(InterruptedException e){
				System.out.println(e);
			}
		}
		public static long time = System.currentTimeMillis();
	
		public void msg(String m) {
			System.out.println("["+(System.currentTimeMillis()-time)+"] "+this.name+": "+m);
		}
		
		private void wasteTime(int min,int max) throws InterruptedException{
			Random rand = new Random(System.currentTimeMillis());
			TimeUnit.MILLISECONDS.sleep(rand.nextInt(max-min)+min);
		}
	}
	//constructor
	public Kiosk(int num, Tracker tracker){
		this.tracker = tracker;
		helper = new KioskHelper("KioskHelper_"+num,this);
	}
	
	//essentially same code as in ID_Check, except that there will only ever be one helper per kiosk
	//service methods for voter
	public void enterLine(String name){
		if(first){
			first = false;
			new Thread(helper).start();
		}
		//object that thread will wait on
		Object convey = new Object();
		synchronized(convey){
			//voter enters line
			waitingVoters.addElement(convey);
			while(true){
				try{
					alertHelpers();
					convey.wait();
					break;
				}
				catch(InterruptedException e){
					continue;
				}
			}
		}
		//voter exits, sleeps, and calls exitLine
	}
	//called when a voter is done, this updates the tracker and alerts the helper
	public synchronized void exitLine(String name){
		synchronized(this.tracker){
			this.tracker.kioskVotersRemaining--;
		}
		alertBusyHelper();
	}
	//called by helpers to kick things off
	public void startHelping(String name){
		if(!waitingVoters.isEmpty()){
			//assist voter
			alertVoters();
			//wait for voter to end
			synchronized(helperConvey){
				while(true){
					try{
						busy = true;
						helperConvey.wait();
						break;
					}
					catch(InterruptedException e){
						continue;
					}
				}
			}
		}
	}
	//methods to release threads from their queues
	private synchronized void alertBusyHelper(){
		if(busy){
			synchronized(helperConvey){
				helperConvey.notify();
				busy = false;
			}
		}
	}
	private synchronized void alertVoters(){
		if(!waitingVoters.isEmpty()){
			synchronized(waitingVoters.elementAt(0)){
				waitingVoters.elementAt(0).notify();
				waitingVoters.removeElementAt(0);
			}
		}
	}
	private synchronized void alertHelpers(){
		if(!busy){
			synchronized(helperConvey){
				helperConvey.notify();
			}
		}
	}
	//helper function so voters can make decisions on which line to enter
	public int lineSize(){
		return waitingVoters.size();
	}
	//debug information, not shown in final version
	public String toString(){
		return "Remaining voters:"+this.tracker.kioskVotersRemaining+" Current Voter Line:"+this.waitingVoters.size()+" Current Busy Helpers:"+this.busy;
	}
}