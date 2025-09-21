package test.Konami;

import java.io.*;
import java.net.*;

public class ServerAccess {

	private Socket connectSocket;
	private PrintWriter out;
	private BufferedReader myBR;
	private String serverIp;
	private int serverPort;
	private String myServerString;

	public ServerAccess(String serverIp, int serverPort) {
		this.serverIp = serverIp;
		this.serverPort = serverPort;
	}

	public String getMyServerString() {
		return myServerString;
	}

	public void setMyServerString(String myServerString) {
		this.myServerString = myServerString;
	}

	public void connect(String outPutText) throws IOException {
		connectSocket = new Socket(serverIp, serverPort);
		try (
			PrintWriter out = new PrintWriter(connectSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(connectSocket.getInputStream()))
		) {
			out.println(outPutText);
			myServerString = in.readLine();
		}
	}

	public void connectClose() throws IOException {
		if (connectSocket != null && !connectSocket.isClosed()) {
			connectSocket.close();
		}
	}
}
