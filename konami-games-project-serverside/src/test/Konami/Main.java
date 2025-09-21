package test.Konami;

public class Main extends Thread {
	
	
	public static void main(String [] args){
	
	GUIServer sGui = new GUIServer("Server Side Program");
	sGui.go();
	}
}
