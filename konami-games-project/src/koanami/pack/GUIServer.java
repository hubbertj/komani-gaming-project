package koanami.pack;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.*;


@SuppressWarnings("serial")
public class GUIServer extends JFrame implements ActionListener, Runnable {
	
	private JButton 	startButton;
	private JButton 	stopButton;
	private JPanel 		jpNorth;
	private JPanel 		jpCenter;
	private JPanel 		jpSouth;
	private JTextField	ipField;
	private JTextField	portField;
	private JLabel		ipFieldLabel;
	private JLabel		portFieldLabel;
	private int 		serverPortNumber;
	private GridLayout	xmlLayout;
	private JTextField 	grid1;
	private JTextField 	grid2;
	private JTextField 	grid3;
	private JTextField 	grid4;
	private JTextField 	grid5;
	private JTextField 	grid6;
	private JTextField 	grid7;
	private JTextField 	grid8;
	private JTextField 	grid9;
	
	public SocketLis 	serverCon;
	
	
	
public GUIServer(String titleName){
	
	this.setTitle(titleName);
}


public int getServerPortNumber() {
	return serverPortNumber;
}

public void setServerPortNumber(int serverPortNumber) {
	this.serverPortNumber = serverPortNumber;
}

public void go(){
	
//configuring buttons and adding them to JFrame.
	
//north layout
	startButton = new JButton("Start Server");
	stopButton = new JButton("Stop Server");
	
	jpNorth = new JPanel();
	jpNorth.setBackground(Color.WHITE);
	
	
	stopButton.setEnabled(false);
	jpNorth.add(startButton, BorderLayout.WEST);
	jpNorth.add(stopButton, BorderLayout.EAST);
	
//center layout , making fields for the grid.
	
	xmlLayout = new GridLayout(3,3);
	jpCenter = new JPanel(xmlLayout);
	
	grid1 = new JTextField(); 
	grid2 = new JTextField(); 	
	grid3 = new JTextField();	
	grid4 = new JTextField();	
	grid5 = new JTextField();	
	grid6 = new JTextField();
	grid7 = new JTextField();
	grid8 = new JTextField();
	grid9 = new JTextField();
	
	jpCenter.add(grid1).setBackground(Color.DARK_GRAY);
	jpCenter.add(grid2).setBackground(Color.DARK_GRAY);
	jpCenter.add(grid3).setBackground(Color.DARK_GRAY);
	jpCenter.add(grid4).setBackground(Color.DARK_GRAY);
	jpCenter.add(grid5).setBackground(Color.DARK_GRAY);
	jpCenter.add(grid6).setBackground(Color.DARK_GRAY);
	jpCenter.add(grid7).setBackground(Color.DARK_GRAY);
	jpCenter.add(grid8).setBackground(Color.DARK_GRAY);
	jpCenter.add(grid9).setBackground(Color.DARK_GRAY);
	
	grid1.setVisible(false); 
	grid2.setVisible(false); 	
	grid3.setVisible(false); 	
	grid4.setVisible(false); 
	grid5.setVisible(false); 	
	grid6.setVisible(false); 
	grid7.setVisible(false); 
	grid8.setVisible(false); 
	grid9.setVisible(false); 
	
	this.add(jpCenter, BorderLayout.CENTER);
	
//south layout
	ipField = new JTextField(20);
	portField = new JTextField(20);
	ipFieldLabel = new JLabel("IP Address: ");		
	portFieldLabel = new JLabel("Port Number: ");		
	jpSouth = new JPanel();
	
	jpSouth.add(ipFieldLabel, BorderLayout.WEST);
	jpSouth.add(ipField, BorderLayout.WEST);
	
//sets the Field to static so it cannot be modified.
	ipField.setEditable(false);
	jpSouth.add(portFieldLabel, BorderLayout.EAST);
	jpSouth.add(portField, BorderLayout.EAST);
	
	jpSouth.setBackground(Color.WHITE);
	
	
	
	this.add(jpNorth, BorderLayout.NORTH);
	this.add(jpSouth, BorderLayout.SOUTH);
	
	
//adding action Listeners.
	
	startButton.addActionListener(this);
	stopButton.addActionListener(this);
	portField.addActionListener(this);
	
	
	
//define for main JFrame Object.
	
	this.setSize(900, 500);
	this.setBackground(Color.lightGray);
	this.setResizable(false);
	this.setVisible(true);
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	
	getIp();
	
}
	
public void getIp(){
	
	byte[] ipAddr = null;
	
	try {
	    InetAddress addr = InetAddress.getLocalHost();
	    
// Get IP Address from your local machine
	    
	    ipAddr = addr.getAddress(); 
	} catch (UnknownHostException e) { 
		e.printStackTrace();
	}
	ipField.setText(ipAddr[0]+"."+ipAddr[1]+"."+ipAddr[2]+"."+ipAddr[3]);
}

public void setmyLayout(String[] names, String[] addresses, String xmlCommand){

//Grid layout output.	
	
	grid1.setVisible(true);
	grid2.setVisible(true);
	grid3.setVisible(true);
	grid4.setVisible(true);
	grid5.setVisible(true);
	grid6.setVisible(true);
	grid7.setVisible(true);
	grid8.setVisible(true);
	grid9.setVisible(true);
	
	grid1.setEditable(false);
	grid2.setEditable(false);
	grid3.setEditable(false);
	grid4.setEditable(false);
	grid5.setEditable(false);
	grid6.setEditable(false);
	grid7.setEditable(false);
	grid8.setEditable(false);
	grid9.setEditable(false);
	
	grid1.setText(names[0]);
	grid2.setText(addresses[1]);
	grid3.setText(xmlCommand);
	grid4.setText(names[2]);
	grid5.setText(addresses[3]);
	grid6.setText("Command option");
	grid7.setText(names[4]);
	grid8.setText(addresses[5]);
	grid9.setText("Command option");
	
}

public void run(){
	
// main action method thread
	
	if (portField.getText().isEmpty()){
		portField.setText("Error: please provide a value port number");
		return;
		}
			try	{
				serverPortNumber = Integer.parseInt(portField.getText());
			}catch (NumberFormatException e){
				portField.setText("Error: please provide a value port number");
				return;
			}		
			 
	startButton.setEnabled(false);
	stopButton.setEnabled(true);
	serverCon = new SocketLis(this);
	
//Starts the reading and writing Loop process.			
	Thread runMe = new Thread(serverCon);
	runMe.start();
}
public void clear(){
	
	grid1.setVisible(false);
	grid2.setVisible(false);
	grid3.setVisible(false);
	grid4.setVisible(false);
	grid5.setVisible(false);
	grid6.setVisible(false);
	grid7.setVisible(false);
	grid8.setVisible(false);
	grid9.setVisible(false);
	
	grid1.setText(null);
	grid2.setText(null);
	grid3.setText(null);
	grid4.setText(null);
	grid5.setText(null);
	grid6.setText(null);
	grid7.setText(null);
	grid8.setText(null);
	grid9.setText(null);
}

public void stop(){
	
	grid1.setVisible(false);
	grid2.setVisible(false);
	grid3.setVisible(false);
	grid4.setVisible(false);
	grid5.setVisible(false);
	grid6.setVisible(false);
	grid7.setVisible(false);
	grid8.setVisible(false);
	grid9.setVisible(false);
	
	grid1.setText(null);
	grid2.setText(null);
	grid3.setText(null);
	grid4.setText(null);
	grid5.setText(null);
	grid6.setText(null);
	grid7.setText(null);
	grid8.setText(null);
	grid9.setText(null);
 
	this.stopButton.setEnabled(false); 
	this.startButton.setEnabled(true);  
	
		try {
			serverCon.close();
		} catch (IOException e) {
			System.out.println("Error from stop()");
			return;
		}
}
	
@Override
public void actionPerformed(ActionEvent e) {
	Thread tServer;
	
		if (e.getSource() == startButton){
			 tServer = new Thread(this);
			 tServer.setName("ServerProcess");
			 tServer.start();
			 
			}
		else if (e.getSource() == stopButton){
			this.stop();
			}
		else if (e.getSource() == portField){
			tServer = new Thread(this);
			tServer.setName("ServerProcess");
			tServer.start();
			
			}
		
	}

}
