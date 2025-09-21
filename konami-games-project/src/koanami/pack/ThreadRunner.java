/**
 * Java Sample Program
 Jerum Lee Hubbert
 1700 Toltec Cir
 Henderson, NV 89014
 Cell: 210-995-4393
 
 Requirments are:
 
 Java Coding assignment for Konami Games.
 You should be able to complete this task if you understand 
 1) the Java language, 2) the Java API's swing GUI / Tables 
 / Socket Communication and 3) Parsing an xml file. 
  * Please make the sample apps are compatible with JDK 1.6 
  * and be able to run on Windows or Linux.  For the extra 
  * credit portion, you will need to understand threads and 
  * how they interact with the GUI.
	 **/

package koanami.pack;

// This is the main thread class

public class ThreadRunner {

	public static void main(String[] args) {
		
//Objects to be pasted into Thread Object.
		
		Main ma = 	new Main();
		MainOut mo = new MainOut();
		
//The first threads are created to run the program.
		
		Thread runServer = new Thread(ma);
		runServer.setName("Server Thread");
		
		Thread runClient = new Thread(mo);
		runClient.setName("Client Thread");
//Starting Threads		
		runServer.start();
		runClient.start();
		

	}

}
