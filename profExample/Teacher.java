package server;

public class Teacher extends MyThread {
	
	public Teacher ()
	{
		super("The teacher");
	}
	
	public Teacher (Student [] students)
	{
		super("The teacher", students);
	}//constructor
	
	public void run ()
	{
		synchronized (goHome)
		{
			wakeUp();
			startClass(0);
			takeABreak();
			startClass(1);
			takeABreak();
			startClass(2);
			goHome();
		}	
	}//run
	
	public void startClass (int classNumber)
	{
		msg("arrives to class and lets the students in.");
		for (int i=0; i<SubServerThread.numStudents; i++)
		{
			synchronized (SubServerThread.students[i])
			{
				if (SubServerThread.students[i].isInClass())
					SubServerThread.students[i].notify();
			}//students	
		}//for
		inClass = true;
		
		try
		{
			sleep(2000);
		}//try
		catch (InterruptedException e) {}
		
		
		for (int i=0; i<SubServerThread.numStudents; i++)
		{
			if (SubServerThread.students[i].getAttendedClass(classNumber)) 
			{
				synchronized (SubServerThread.students[i]) {
					SubServerThread.students[i].notify();
					//SubServerThread.students[i].interrupt();
				}
			}//if
		}	
		inClass = false;
		
		synchronized (currentClass[classNumber])
		{
			attendedClass[classNumber] = true;
			currentClass[classNumber].notifyAll();
		}//synchronized
		
		msg("finished teaching the class!");
		
		takeABreak();
	}//startClass
	
	public void takeABreak ()
	{
		try
		{
			sleep(350);
		}//try
		catch (InterruptedException e) {}
	}//takeABreak
	
	private void printAttendance ()
	{
		System.out.println("\n");
		
		for (int i=0; i<SubServerThread.numStudents; i++) 
		{
			String answer = "";
			int count = 0;
			for (int j=0; j<3; j++)
				if (SubServerThread.students[i].getAttendedClass(j)) 
				{
					count++;
					answer+= (answer!="") ? ", " + j : " " + j; 
				}//if
			System.out.println("Student " + i + "\t" + count + " classes attended\t Class Numbers:" + answer);
		}//for
	}//printAttedance
	
	public void goHome ()
	{
		try {
			synchronized (goHome)
			{
				if (SubServerThread.students[SubServerThread.numStudents-1].isAlive() || !goneHome[SubServerThread.numStudents-1])
					goHome.wait();
			}	
		}//try
		catch (InterruptedException e) {}
		msg("went home.");
		printAttendance();
	}//goHome
	
}//class
