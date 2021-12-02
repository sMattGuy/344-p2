import java.net.*;
import java.util.Vector;

public class Server{
	private static int port;
	private static String address;
	
	public Server(){
		try{
			//create monitors
			//default values for program
			int numVoters = 20;
			int numIDHelpers = 3;
			int numKiosks = 3;
			int numScanMachines = 4;
			int numScanHelpers = 2;
			//tracker that helps threads know when its time to close (shared variables essentially)
			Tracker tracker = new Tracker(numVoters);
			//creation of scan machine monitor
			ScanMachine scanner = new ScanMachine(tracker, numScanMachines, numScanHelpers);
			//create id line monitor
			ID_Check id_check = new ID_Check(tracker, numIDHelpers);
			//create kiosks monitors (and their helper threads)
			Vector<Kiosk> kiosks = new Vector<>();
			for(int i=0;i<numKiosks;i++){
				kiosks.addElement(new Kiosk(i,tracker));
			}
			
			//creation of server socket
			//creates a socket with a default backlog queue of 50
			ServerSocket server = new ServerSocket(port);
			InetAddress myAddress = InetAddress.getLocalHost();
			System.out.println("ServerSocket created on port "+port+" with address "+myAddress.getHostAddress());
			int counter = 0;
			while(true){
				Socket connection = server.accept();
				System.out.println("Connection accepted");
				new ServerClientHelper(connection, counter, id_check, kiosks, scanner);
				counter++;
			}
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
	
	public static void main(String args[]){
		port = ((int)(5000.0*Math.random())) + 5000;
		
		new Server();
	}
}