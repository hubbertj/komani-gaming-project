package test.Konami;

import java.io.*;
import java.net.*;

public class ServerAccess {
	
	Socket connectSocket = null;
    PrintWriter out = null;
    BufferedReader in = null;
    String ServerIp;
    int ServerPort;
	
	public ServerAccess(String inputServerIp, int inputServerPort){
		
		this.ServerIp = inputServerIp;
		
		this.ServerPort = inputServerPort;
		
	}
		
	public void connect(GUIClient GUI) throws IOException{	

        try {
            connectSocket = new Socket(ServerIp, ServerPort);
            out = new PrintWriter(connectSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
                                        connectSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Cannot Connect to server");
            //System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                               + "the connection to: " + ServerIp.toString());
           // System.exit(1);
        }

	BufferedReader stdIn = new BufferedReader(
                                   new InputStreamReader(System.in));
	
	String clientInput = GUI.getJTareaOut().getText();

	while ((clientInput = stdIn.readLine())!= null) {
	    out.println(clientInput);
	    System.out.println("Writing: " + in.readLine());
	}

	out.close();
	in.close();
	stdIn.close();
	connectSocket.close();
	
    }
}
