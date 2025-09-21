package koanami.pack;

public class MainOut extends Thread {
	
//Class is used to extend Thread	
	
	public void run() {
		
		GUIClient GUI = new GUIClient("Client Side Program");
		
		GUI.go();	

	}

}
