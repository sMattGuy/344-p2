import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.Random;

class ID_Checker implements Runnable{
	//local variables
	private String name;
	private ID_Check line;
	
	public Tracker tracker;
	
	public ID_Checker(String name, ID_Check line, Tracker tracker){
		this.name = name;
		this.line = line;
		this.tracker = tracker;
		new Thread(this).start();
	}
	
	public void run(){
		try{
			//loops until all voters are done, then leaves
			while(this.tracker.lineVotersRemaining != 0){
				this.msg("Ready to help next voter");
				if(line.startHelping(this.name)){break;};	//if more helpers than voters, exit early
				this.msg("Helping a voter");
				this.wasteTime(1000,2000);	//slight delay to add variance
			}
			this.msg("Done helping voters, leaving (exiting)");
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