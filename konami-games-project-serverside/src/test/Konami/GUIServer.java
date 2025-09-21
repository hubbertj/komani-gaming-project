package test.Konami;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.*;


@SuppressWarnings("serial")
public class GUIServer extends JFrame implements ActionListener {
	
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
	private JTextField 	gridName;
	private JTextField 	gridAddress;
	private JTextField 	gridCommand;
	private JTextField 	gridExtra1;
	private JTextField 	gridExtra2;
	private JTextField 	gridExtra3;
	
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
	
	
	gridName = new JTextField("Name Field: ");  
	gridAddress = new JTextField("Address Field: "); 	
	gridCommand = new JTextField("Command Field: "); 	
	gridExtra1 = new JTextField("Extra"); 	
	gridExtra2 = new JTextField("Extra"); 	
	gridExtra3 = new JTextField("Extra");
	
	xmlLayout = new GridLayout(2, 3);
	jpCenter = new JPanel(xmlLayout);
	
	gridName.setEditable(false);  
	gridAddress.setEditable(false);  	
	gridCommand.setEditable(false);  	
	gridExtra1.setEditable(false);  	
	gridExtra2.setEditable(false); 	
	gridExtra3.setEditable(false);
	
	gridName.setBackground(Color.LIGHT_GRAY);
	gridAddress.setBackground(Color.LIGHT_GRAY); 	
	gridCommand.setBackground(Color.LIGHT_GRAY);	
	gridExtra1.setBackground(Color.LIGHT_GRAY);	
	gridExtra2.setBackground(Color.LIGHT_GRAY);	
	gridExtra3.setBackground(Color.LIGHT_GRAY);
	
	jpCenter.add(gridName);
	jpCenter.add(gridAddress);
	jpCenter.add(gridCommand);
	jpCenter.add(gridExtra1);
	jpCenter.add(gridExtra2);
	jpCenter.add(gridExtra3);
	
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
	
	this.setSize(900, 400);
	this.setBackground(Color.BLACK);
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
		System.out.println("Error from getIP()");
	}
	ipField.setText(ipAddr[0]+"."+ipAddr[1]+"."+ipAddr[2]+"."+ipAddr[3]);
}

public void allAction(){
	
// main action method
	
	if (portField.getText().isEmpty()){
		gridExtra3.setText("Error: please provide a value port number");
		return;
		}
			try	{
				serverPortNumber = Integer.parseInt(portField.getText());
			}catch (NumberFormatException e){
				System.out.println("Error from getIP()");
				gridExtra3.setText("Error: please provide a value port number");
				return;
			}
			
	startButton.setEnabled(false);
	stopButton.setEnabled(true);
	
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				System.out.println("sleep thread");
			}		
	
	gridExtra3.setText(portField.getText());
	serverCon = new SocketLis();
			try {
				serverCon.listen(this);
			} catch (IOException e) {
				System.out.println("IO error from GUIServer");
				return;
			}
			
//Using XML data to update grid
			
	XmlReceived xml = new XmlReceived(serverCon.getXML(),this);
	xml.process();
	gridName.setText(xml.updateNameGrid());
	gridAddress.setText(xml.updateAddressGrid());
	gridCommand.setText(xml.updateCommandGrid());
	
}

public JTextField getGridName() {
	return gridName;
}


public void setGridName(JTextField gridName) {
	this.gridName = gridName;
}


public JTextField getGridAddress() {
	return gridAddress;
}


public void setGridAddress(JTextField gridAddress) {
	this.gridAddress = gridAddress;
}


public JTextField getGridCommand() {
	return gridCommand;
}


public void setGridCommand(JTextField gridCommand) {
	this.gridCommand = gridCommand;
}


public JTextField getGridExtra1() {
	return gridExtra1;
}


public void setGridExtra1(JTextField gridExtra1) {
	this.gridExtra1 = gridExtra1;
}


public JTextField getGridExtra2() {
	return gridExtra2;
}


public void setGridExtra2(JTextField gridExtra2) {
	this.gridExtra2 = gridExtra2;
}


public JTextField getGridExtra3() {
	return gridExtra3;
}


public void setGridExtra3(JTextField gridExtra3) {
	this.gridExtra3 = gridExtra3;
}


public void stop(){
	
	this.gridName.setText("Name Field: ");
	this.gridAddress.setText("Addres Field: ");
	this.gridCommand.setText("Command Field: ");
	this.gridExtra1.setText("Extra");  	
	this.gridExtra2.setText("Extra");  
	this.gridExtra3.setText("Extra");  
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
	
		if (e.getSource() == startButton){
			this.allAction();
			}
		else if (e.getSource() == stopButton){
			this.stop();
			}
		else if (e.getSource() == portField){
			this.allAction();
			}
		
	}

}
