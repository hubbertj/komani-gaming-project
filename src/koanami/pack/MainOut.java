package koanami.pack;

public class MainOut extends Thread {

	@Override
	public void run() {
		GUIClient guiClient = new GUIClient("Client Side Program");
		guiClient.go();
	}
}
