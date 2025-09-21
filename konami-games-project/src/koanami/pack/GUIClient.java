package koanami.pack;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import javax.swing.*;


@SuppressWarnings("serial")
public class GUIClient extends JFrame implements ActionListener, Runnable {
	
// instance variables
	
	private JButton 	buttonSend;
	
	private JTextField 	textIp;
	private JTextField 	textPort;
	
	private JLabel		labelIp;
	private JLabel		labelPort;
	
	private JTextArea 	textAreaOut;
	private JTextArea 	textAreaRespond;
	private JPanel 		panelCenter;
	private JPanel		panelSouth;
	private JPanel		panelNorth;
	private JPanel		panelIntern;
	
	public ServerAccess SA;
	
	
	
	public GUIClient (String windowName) {
		
		this.setTitle(windowName);
}
		
	public void go(){
		
//North panel of the GUI.
		panelNorth	= new JPanel();
		buttonSend	= new JButton("Send Message");
		
		panelNorth.setBackground(Color.lightGray);
		panelNorth.add(buttonSend);
		
		//Center panel of the GUI.
		panelCenter = new JPanel();
		panelCenter.setBackground(Color.GRAY);
		
		textAreaRespond = new JTextArea(15,30);
		JScrollPane scrollPaneRespond = new JScrollPane(textAreaRespond);
		textAreaRespond.setLineWrap(true);
		textAreaRespond.setEditable(false);
		textAreaRespond.setText("Respond Section:");
		

		textAreaOut = new JTextArea(15,30);
		JScrollPane scrollPaneOutput = new JScrollPane(textAreaOut);
		textAreaOut.setLineWrap(true);
		textAreaOut.setText("Output Section : <place XML data here>");
		
	
		panelCenter.add(scrollPaneOutput);
		panelCenter.add(scrollPaneRespond);
		
//South panel of the GUI.
		panelSouth = new JPanel();
		panelIntern = new JPanel();
		panelIntern.setBackground(Color.GRAY);
		
		textIp = new JTextField(30);
		textPort = new JTextField(30);
		
		
		labelIp	= new JLabel("Enter IP");
		labelPort = new JLabel("Enter Port");
		
		
		panelIntern.add(labelIp,BorderLayout.NORTH);
		panelIntern.add(textIp, BorderLayout.NORTH);
		
		panelIntern.add(labelPort, BorderLayout.SOUTH);
		panelIntern.add(textPort, BorderLayout.SOUTH);
		
		panelSouth.add(panelIntern,BorderLayout.CENTER);
		
// Adding to my main JFrame		
		this.add(panelNorth, BorderLayout.NORTH);
		this.add(panelCenter, BorderLayout.CENTER);
		this.add(panelSouth, BorderLayout.SOUTH);
		
//Adding action to buttons and text fields.
		buttonSend.addActionListener(this);
		textIp.addActionListener(this);
		textPort.addActionListener(this);
		
//Settings for the main JFame.
		this.setSize (new Dimension(900,400));
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBackground(Color.BLACK);
		this.setVisible(true);
		
}
	
	public JTextArea gettextAreaOut() {
		return textAreaOut;
}

	public void settextAreaOut(JTextArea textAreaOut) {
		this.textAreaOut = textAreaOut;
}

	public JTextArea gettextAreaRespond() {
		return textAreaRespond;
}

	public void settextAreaRespond(JTextArea textAreaRespond) {
		this.textAreaRespond = textAreaRespond;
}
	
	
// Method for common action to this class.
	public void run() {
		
		
		String ipAddress = textIp.getText();
		int portNumber = 100;
		
//Checks input data on the client side		
	
		if (gettextAreaOut().getText().isEmpty()) {
			gettextAreaRespond().setText("Error: missing <xml> data in " +
					"Output field");
			return;
			}
		
		else if (textPort.getText().isEmpty()) {
			gettextAreaRespond().setText("Error: missing port number data in " +
					"Port Number field");
			return;
			}
		
		else if (textIp.getText().isEmpty()) {
			gettextAreaRespond().setText("Error: missing IP address in " +
					"IP Address field");
			return;
			}
		
//Tries to parse input to port number to a Integer.
		
					try	{
			 			portNumber = Integer.parseInt(textPort.getText());
			 		}catch (NumberFormatException e){
			 			gettextAreaRespond().setText("Error: Invaild port number");
						return;
					}
	
			
		
//connects to the server and sends / receives information.		
		try {
			SA = new ServerAccess(ipAddress, portNumber, this);
			SA.out(textAreaOut.getText());			
			textAreaRespond.append("\n"+SA.getmyServerString());
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Thread th = new Thread(SA);
		th.start();
		
		
}

	@Override
	public void actionPerformed(ActionEvent e) {
		
//action buttons		
		if (e.getSource() == buttonSend){
					
				Thread threadOne  = new Thread(this);
				threadOne.setName("ClientThread");
				threadOne.start();
				
				}
			}
}
