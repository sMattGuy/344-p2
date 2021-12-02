import java.util.Random;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.io.*;
import java.net.Socket;
import java.util.*;
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
	
	//constructor
	public Kiosk(int num, Tracker tracker){
		this.tracker = tracker;
		this.kioskNum = num;
	}
	
	//essentially same code as in ID_Check, except that there will only ever be one helper per kiosk
	//service methods for voter
	public void enterLine(String name){
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
	public boolean startHelping(String name){
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
			return false;
		}
		else{
			//line empty, wait
			if(this.tracker.kioskVotersRemaining <= 6 && this.waitingVoters.size() == 0){
				return true;
			}
			synchronized(helperConvey){
				while(true){
					try{
						helperConvey.wait();
						break;
					}
					catch(InterruptedException e){
						continue;
					}
				}
			}
			return false;
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