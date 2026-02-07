package koanami.pack;

import java.net.*;
import java.io.*;

public class SocketLis implements Runnable {

	private volatile boolean running = true;
	private String readIn = "";
	private String XML = "";
	private Socket sock;
	private ServerSocket serverConnect;
	private PrintWriter printS;
	private GUIServer gs;
	private BufferedReader inside;

	public SocketLis(GUIServer gs) {
		this.gs = gs;
	}

	// Close resources safely
	public void close() {
		running = false;
		try {
			if (sock != null && !sock.isClosed()) sock.close();
			if (serverConnect != null && !serverConnect.isClosed()) serverConnect.close();
			if (printS != null) printS.close();
			if (inside != null) inside.close();
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
		return XML;
	}

	public void setXML(String XML) {
		this.XML = XML;
	}

	@Override
	public void run() {
		try {
			serverConnect = new ServerSocket(gs.getServerPortNumber());
			while (running) {
				try (Socket clientSocket = serverConnect.accept();
					 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

					this.sock = clientSocket;
					this.inside = in;
					this.printS = out;
					StringBuilder xmlBuilder = new StringBuilder();

					while (running && (readIn = in.readLine()) != null) {
						if ("END".equals(readIn)) {
							gs.clear();
							out.println("Confirmed: Message has been received, come again!");
							out.println("END");
							break;
						} else {
							xmlBuilder.append(readIn);
						}
					}

					XML = xmlBuilder.toString();

					// Process the XML
					XmlReceived xml = new XmlReceived(XML, gs);
					xml.process();
					gs.setmyLayout(xml.getMyStrings(), xml.getMyAdd(), xml.getxmlCommand());

					// Reset variables
					readIn = "";
					XML = "";

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
}
