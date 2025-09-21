package koanami.pack;

import java.io.*;
import java.net.*;

public class ServerAccess implements Runnable {
	
	public Socket connectSocket;
	public PrintWriter out;
    private String serverIp;
    private int serverPort;
    public GUIClient gui;
    String myServerString = "";
    BufferedReader myBR;
    
    

	public ServerAccess(String inputserverIp, int inputserverPort, GUIClient gui) throws UnknownHostException, IOException{
		
		this.serverIp = inputserverIp;	
		this.serverPort = inputserverPort;
		this.gui = gui;
		
		
}
	public String getmyServerString (){
		return myServerString;
}

//connection objects for writing and reading from the server 
	public void out(String outPutText) throws IOException{	
		connectSocket = new Socket(serverIp, serverPort);
		out = new PrintWriter(connectSocket.getOutputStream(), true);
		out.println(outPutText);
	    out.println("END");
	    
//Receiving information from the server.	    		     
} 
	
	public void run(){
	boolean br = true;
	String tmp = "";
	
	
//My write and read loop
	
	for (;;){
	try{
		myBR = new BufferedReader(new InputStreamReader(connectSocket.getInputStream())); 
					while(br){
						tmp = myBR.readLine();
//I used "END" to mark my EOF						
						if(tmp.equals("END")){
							br = false;
						}else{
							myServerString += tmp;	
							
						}
					}
					
// Writes to responds text area and resets Looping variables.
					
			gui.gettextAreaRespond().append("\n" + myServerString);
			br = true;
			tmp = "";
			myServerString = "";
				
			
		
		}catch(IOException e){
			e.printStackTrace();
		}
		
	}	
		
	}
			
// used for closing the socket	
	public void connectClose() throws IOException{
		connectSocket.close();
		out.close();
		myBR.close();
			
	}
 }
