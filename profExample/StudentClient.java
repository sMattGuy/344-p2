

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class StudentClient extends Thread {
	private static final int numStudents = 
	private static final int portNumber = 
	private static final String address = 
	
	public void run ()
	{
		try 
		{
			// connect to server			
			// create input and output streams			
			int methodCount = 0;
			String line;
			
			while ((line = br.readLine()) != null)
			{
				if (methodCount <= 5) 
					pw.println("Student " + methodCount);
				else
				{
					
					break;
				}
				methodCount++;
			}
			
		}
		
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static void main (String [] args)
	{
		for (int i=0; i<numStudents; i++)
			new StudentClient().start();
	}
}
