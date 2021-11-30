import java.util.Vector;

class ScanMachine{
	//this is set to the number of voters we will process, once it is 0, the helpers can exit
	public Tracker tracker;
	//machine and helper count
	private int machineCount;
	private int helperCount;
	//voter queues
	private int votersWaiting = 0;
	private Vector<Object> helpVoter = new Vector<>();
	//helper queues
	private Vector<Object> waitingHelpers = new Vector<>();
	private Vector<Object> busyHelpers = new Vector<>();
	
	//group information
	private Object groupConvey = new Object();
	private boolean groupActive = false;
	private int groupMembersLeft = 0;
	
	//constructor for monitor
	public ScanMachine(Tracker tracker, int machineCount, int helperCount){
		this.tracker = tracker;
		this.machineCount = machineCount;
		this.helperCount = helperCount;
	}
	
	public void enterScanLine(String name){
		//voters enter group and wait to be alerted
		synchronized(groupConvey){
			while(true){
				try{
					votersWaiting++;
					if(votersWaiting >= machineCount || (tracker.scannerVotersRemaining == votersWaiting && tracker.totalVoters / machineCount < machineCount)){
						//check if ok to send in next group
						alertHelpers();
					}
					groupConvey.wait();
					break;
				}
				catch(InterruptedException e){
					continue;
				}
			}
			votersWaiting--;
		}
	}
	
	public synchronized void leaveGroup(String name){
		/*
			this is what voters call when they're done, it will increase the progress,
			decrement the remaining voters in the tracker, and alert the helpers that theyre done
		*/
		groupMembersLeft--;
		tracker.scannerVotersRemaining--;
		if(groupMembersLeft == 0){
			//all members of this group are done
			groupActive = false;
			alertHelpers();
		}
	}
	
	public void startHelping(String name){
		//we can check the help voter queue for any stuck voters, and handle them
		if(!helpVoter.isEmpty()){
			//assist the voter with their problem
			//alert voter
			//wait in busy queue
			Object convey = new Object();
			synchronized(convey){
				busyHelpers.addElement(convey);
				while(true){
					try{
						alertVoter();
						convey.wait();
						break;
					}
					catch(InterruptedException e){
						continue;
					}
				}
			}
			//get released when voter is done
		}
		else{
			//helpers will enter and wait until voters alert that they have enough, then if conditions are met, a new group can enter
			if(!groupActive && (votersWaiting >= machineCount || (tracker.scannerVotersRemaining == votersWaiting && tracker.totalVoters / machineCount < machineCount))){
				//ok to let in next group
				releaseGroup();
			}
			//helpers wait here
			else if(this.tracker.scannerVotersRemaining > helperCount){
				//helpers will enter the waiting queue
				Object convey = new Object();
				synchronized(convey){
					waitingHelpers.addElement(convey);
					while(true){
						try{
							convey.wait();
							break;
						}
						catch(InterruptedException e){
							continue;
						}
					}
				}
			}
			//when a helper is released, its most likely to help a voter.
			//if nothing, and still voters left, thread will loop in its run function
		}
	}
	
	public void getHelp(){
		Object convey = new Object();
		synchronized(convey){
			//voter enters line
			helpVoter.addElement(convey);
			while(true){
				try{
					//alert helpers and wait
					alertHelpers();
					convey.wait();
					break;
				}
				catch(InterruptedException e){
					continue;
				}
			}
		}
		//helper released voter, stall and then release helper
	}
	public void gotHelp(){
		//release helper from busy queue
		alertBusyHelper();
	}
	
	private synchronized void releaseGroup(){
		synchronized(groupConvey){
			if(!groupActive){
				groupActive = true;
				for(int i=0;i<machineCount;i++){
					groupConvey.notify();
				}
				groupMembersLeft = this.machineCount;
			}
		}
	}
	private synchronized void alertBusyHelper(){
		if(!busyHelpers.isEmpty()){
			synchronized(busyHelpers.elementAt(0)){
				busyHelpers.elementAt(0).notify();
				busyHelpers.removeElementAt(0);
			}
		}
	}
	private synchronized void alertHelpers(){
		if(!waitingHelpers.isEmpty()){
			synchronized(waitingHelpers.elementAt(0)){
				waitingHelpers.elementAt(0).notify();
			}
			waitingHelpers.removeElementAt(0);
		}
	}
	private synchronized void alertVoter(){
		if(!helpVoter.isEmpty()){
			synchronized(helpVoter.elementAt(0)){
				helpVoter.elementAt(0).notify();
			}
			helpVoter.removeElementAt(0);
		}
	}
	public String toString(){
		return "Remaining voters:"+this.tracker.scannerVotersRemaining+" Voters Waiting:"+this.votersWaiting+" Current Help Voter:"+this.helpVoter.size()+" Current Busy Helpers:"+this.busyHelpers.size()+" Current Waiting Helpers:"+waitingHelpers.size()+" Group Active:"+groupActive+" Group remaining:"+groupMembersLeft;
	}
}