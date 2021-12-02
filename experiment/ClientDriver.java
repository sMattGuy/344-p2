import java.io.*;
import java.net.Socket;
import java.util.*;

class ClientDriver{
	public static void main(String args[]){
		if(args.length != 2){
			System.out.println("supply address and port");
			System.exit(0);
		}
		int port = Integer.parseInt(args[1]);
		String address = args[0];
		for(int i=0;i<10;i++){
			new VoterClient(address, port, i);
		}
	}
}