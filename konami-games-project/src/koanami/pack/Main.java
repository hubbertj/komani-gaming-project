package koanami.pack;

public class Main extends Thread {
	
//Class is used to extend Thread
	
	public void run(){
	
	GUIServer sGui = new GUIServer("Server Side Program");
	sGui.go();
	}
}
