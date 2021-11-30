import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.Random;

class ScanHelper implements Runnable{
	//local variables
	private String name;
	private ScanMachine scanner;
	
	public Tracker tracker;
	
	public ScanHelper(String name, ScanMachine scanner, Tracker tracker){
		this.name = name;
		this.scanner = scanner;
		this.tracker = tracker;
		new Thread(this).start();
	}
	
	public void run(){
		try{
			while(tracker.scannerVotersRemaining != 0){
				this.msg("Ready to help next voters");
				scanner.startHelping(this.name);	//all decisions are controlled in this one function
				this.wasteTime(1000,2000);
			}
			this.msg("Done with helping voters scan, leaving (exiting)");
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