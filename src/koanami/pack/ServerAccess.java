package koanami.pack;

import java.io.*;
import java.net.*;

public class ServerAccess implements Runnable {

	private Socket connectSocket;
	private PrintWriter out;
	private BufferedReader in;
	private final String serverIp;
	private final int serverPort;
	private final GUIClient gui;
	private volatile boolean running = true;

	public ServerAccess(String serverIp, int serverPort, GUIClient gui) {
		this.serverIp = serverIp;
		this.serverPort = serverPort;
		this.gui = gui;
	}

	public synchronized void send(String message) throws IOException {
		if (connectSocket == null || connectSocket.isClosed()) {
			connectSocket = new Socket(serverIp, serverPort);
			out = new PrintWriter(connectSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(connectSocket.getInputStream()));
		}
		out.println(message);
		out.println("END");
	}

	@Override
	public void run() {
		try {
			if (connectSocket == null || connectSocket.isClosed()) {
				connectSocket = new Socket(serverIp, serverPort);
				out = new PrintWriter(connectSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(connectSocket.getInputStream()));
			}
			StringBuilder serverResponse = new StringBuilder();
			String line;
			while (running && (line = in.readLine()) != null) {
				if ("END".equals(line)) {
					gui.gettextAreaRespond().append("\n" + serverResponse.toString());
					serverResponse.setLength(0);
				} else {
					serverResponse.append(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeConnections();
		}
	}

	public void stop() {
		running = false;
		closeConnections();
	}

	private void closeConnections() {
		try {
			if (out != null) out.close();
			if (in != null) in.close();
			if (connectSocket != null && !connectSocket.isClosed()) connectSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
