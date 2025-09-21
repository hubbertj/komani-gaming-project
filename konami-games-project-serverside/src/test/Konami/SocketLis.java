package test.Konami;

import java.net.*;
import java.io.*;

public class SocketLis {

	private String readIn = "";
	private Socket sock;
	private ServerSocket serverConnect;
	private StringBuilder xmlBuilder = new StringBuilder();
	private PrintWriter printS;
	private boolean controlVar;

	public SocketLis() {
		// Default constructor
	}

	// Used for closing the socket and streams
	public void close() {
		try {
			if (printS != null) printS.close();
			if (sock != null && !sock.isClosed()) sock.close();
			if (serverConnect != null && !serverConnect.isClosed()) serverConnect.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getReadIn() {
		return readIn;
	}

	public void setReadIn(String readIn) {
		this.readIn = readIn;
	}

	public String getXML() {
		return xmlBuilder.toString();
	}

	public void setXML(String xml) {
		xmlBuilder = new StringBuilder(xml);
	}

	// Main connection method
	public void listen(GUIServer gs) throws IOException {
		controlVar = true;
		serverConnect = new ServerSocket(gs.getServerPortNumber());
		try (Socket clientSocket = serverConnect.accept();
			 BufferedReader inside = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

			sock = clientSocket;
			printS = out;

			String line;
			while (controlVar && (line = inside.readLine()) != null) {
				readIn = line;
				xmlBuilder.append(line);
			}

			// Write out to the client
			try {
				Thread.sleep(2000);
				out.println("Confirmed: Message has been received");
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		} finally {
			close();
		}
	}
}
