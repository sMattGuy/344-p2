package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

public class SubServerThread extends Thread {
	private static final String STUDENT = "Student";
	private static final String TEACHER = "Teacher";
	public static Student [] students = new Student [100];
	private static Object lock = new Object();
	public static Object goHome = new Object();
	public static Teacher teacher;
	public static int numStudents = 0;
	private Socket socket;
	private BufferedReader br;
	private PrintWriter pw;
	private Student student;
	private String threadtype;
	
	// constructor
	
	public void run ()
	{
		// get input stream
		// get output stream
			
			//other code
					
			
			
			runMethod(methodNumber);
			pw.println("DONE");
			
			while (true) 
			{
				
				runMethod(methodNumber);
				
			}//while
			
		}//try
		catch (IOException e)
		...................
		}
	}
	
	public void runMethod (int methodNumber)
	{
		if (threadtype.equals(STUDENT))
		{
			switch (methodNumber)
			{
				case 0: 
							student.wakeUp();
							break;
				case 1:
							student.goToBathroom();
							break;
				case 2:
							student.goToClass(0);
							break;	
				case 3:
							student.goToClass(1);
							break;			
				case 4:
							student.goToClass(2);
							break;		
				case 5:
							student.goHome();
							break;
			}//switch		
		}//if
		
		else 
		{
			switch (methodNumber)
			{
				case 0: 
							teacher.wakeUp();
							break;
				case 1:
							teacher.startClass(0);
							break;
				case 2:
							teacher.startClass(1);
							break;			
				case 3:
							teacher.startClass(2);;
							break;					
				case 4:
							teacher.goHome();
							break;			
			}//else
		}//else
	}//runMethod
}
