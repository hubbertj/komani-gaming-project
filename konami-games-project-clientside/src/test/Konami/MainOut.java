package test.Konami;

public class MainOut extends Thread {
	
	public static void main(String[] args) {
		
		GUIClient GUI = new GUIClient("Client Side");
		
		GUI.go();	

	}

}
