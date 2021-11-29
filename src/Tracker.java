/*
	This class is designed to keep track of the progress of voters through the various sections, once all voters finish a section, the helper threads will be able to exit as well.
*/
class Tracker{
	public int totalVoters;
	public int lineVotersRemaining;
	public int kioskVotersRemaining;
	public int scannerVotersRemaining;
	
	public Tracker(int voters){
		this.totalVoters = voters;
		this.lineVotersRemaining = voters;
		this.kioskVotersRemaining = voters;
		this.scannerVotersRemaining = voters;
	}
	public String toString(){
		return "Line voters:"+this.lineVotersRemaining+", Kiosk voters:"+this.kioskVotersRemaining;
	}
}