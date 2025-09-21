package koanami.pack;

import java.net.*;
import java.io.*;

public class SocketLis implements Runnable {

// SocketLis is used to make a socket object for connections	
	
	public String readIn = "";
	public Socket sock;
	public ServerSocket serverConnect;
	public String XML = "";
	public PrintWriter printS;
	private boolean controlVar;
	GUIServer gs;
	BufferedReader inside;
	
	public SocketLis(GUIServer gs){
		
		super();
		this.gs = gs;
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
	
	public void run(){
		controlVar = true;
		
		try{
		serverConnect = new 
				ServerSocket(gs.getServerPortNumber());
		
		}catch(IOException e){
			e.printStackTrace();
		}
		
//This should write out to the client and read from it infinite amount of times.	
		
		for(;;){
		try {
			sock = serverConnect.accept();
			inside = new 
					BufferedReader(new InputStreamReader(sock.getInputStream()));
			printS = new PrintWriter(sock.getOutputStream(), true);
				while (controlVar){
					readIn = inside.readLine();
// I use "END" to mark my EOF
						if(readIn.equals("END")){
							controlVar = false;
							gs.clear();
							printS.println("Confirmed: Message has been recieved, come again!");
							printS.println("END");
							
						}else{
							XML += readIn;
						}

						
					}
// Processes the XML				
				XmlReceived xml = new XmlReceived(XML,gs);
				xml.process();
				gs.setmyLayout(xml.getMyStrings(), xml.getMyAdd(), xml.getxmlCommand());
				
//Resets all looping variables				
				controlVar = true;
				readIn = "";
				XML = "";
				
				
		
		}catch(IOException e){
			
		}
		
		
		}
	}
}
