import java.io.*;
import java.net.Socket;
import java.util.*;

class ClientDriver{
	public static void main(String args[]){
		if(args.length != 2){
			System.out.println("supply address and port");
			System.exit(0);
		}
		
		//default values for program
		int numVoters = 20;
		int numIDHelpers = 3;
		int numKiosks = 3;
		int numScanMachines = 4;
		int numScanHelpers = 2;
		
		int port = Integer.parseInt(args[1]);
		String address = args[0];
		for(int i=0;i<numIDHelpers;i++){
			new IDCheckerClient(address, port, i);
		}
		for(int i=0;i<numKiosks;i++){
			new KioskHelperClient(address, port, i);
		}
		for(int i=0;i<numVoters;i++){
			new VoterClient(address, port, i);
		}
	}
}