package test.Konami;

import java.net.*;
import java.io.*;

public class SocketLis {

// SocketLis is used to make a socket object for connections	
	
	public String readIn = "";
	public Socket sock;
	public ServerSocket serverConnect;
	public String XML = "";
	public PrintWriter printS;
	private boolean controlVar;
	
	public SocketLis(){
		super();
	}
	
// Used for closing the socket
	
	public void close() throws IOException{
		sock.close();
		serverConnect.close();
		printS.close();
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

	public void setXML(String xML) {
		XML = xML;
	}

//main connection method
	
	public void listen(GUIServer gs) throws IOException{
		controlVar = true;
		serverConnect = new 
				ServerSocket(gs.getServerPortNumber());
		sock = serverConnect.accept();
		BufferedReader inside = new 
				BufferedReader(new InputStreamReader(sock.getInputStream()));
		
		while (controlVar){
			readIn = inside.readLine();
				if(readIn == null){
					controlVar = false;
				}else{
					XML += readIn;
				}
		}
	
//This should write out to the client.		
		try {
			Thread.sleep(2000);
			printS = new PrintWriter(sock.getOutputStream());
			printS.println("Confirmed: Message has been recieved");
			
		} catch (InterruptedException e) {
			System.out.println("Sleep Thread");
			
		}
	}
}
