package test.Konami;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;


@SuppressWarnings("serial")
public class GUIClient extends JFrame implements ActionListener {
	
	// instance variables
	
	private JButton 	JBSend;
	
	private JTextField 	JTip;
	private JTextField 	JTport;
	
	private JLabel		JLip;
	private JLabel		JLport;
	
	private JTextArea 	JTareaOut;
	private JTextArea 	JTareaRespond;
	private JPanel 		JPcenter;
	private JPanel		JPsouth;
	private JPanel		JPnorth;
	private JPanel		JPintern;
	int counter = 1;
	
	
	public GUIClient (String windowName) {
		
		this.setTitle(windowName);
		}
		
	public void go(){
		
		//North panel of the GUI.
		JPnorth							= new JPanel();
		JBSend							= new JButton("Send Message");
		
		JPnorth.setBackground(Color.lightGray);
		JPnorth.add(JBSend);
		
		//Center panel of the GUI.
		JPcenter 						= new JPanel();
		JPcenter.setBackground(Color.GRAY);
		
		JTareaRespond 					= new JTextArea(20,30);
		JScrollPane scrollPaneRespond 	= new JScrollPane(JTareaRespond);
		JTareaRespond.setLineWrap(true);
		JTareaRespond.setEditable(false);
		JTareaRespond.setText("Respond Section:");
		

		JTareaOut 						= new JTextArea(20,30);
		JScrollPane scrollPaneOutput 	= new JScrollPane(JTareaOut);
		JTareaOut.setLineWrap(true);
		JTareaOut.setText("Output Section : <place XML data here>");
		
	
		JPcenter.add(scrollPaneOutput);
		JPcenter.add(scrollPaneRespond);
		
		//South panel of the GUI.
		JPsouth 		= new JPanel();
		JPintern		= new JPanel();
		JPintern.setBackground(Color.GRAY);
		
		JTip 			= new JTextField(30);
		JTport			= new JTextField(30);
		
		
		JLip			= new JLabel("Enter IP");
		JLport			= new JLabel("Enter Port");
		
		
		JPintern.add(JLip,BorderLayout.NORTH);
		JPintern.add(JTip, BorderLayout.NORTH);
		
		JPintern.add(JLport, BorderLayout.SOUTH);
		JPintern.add(JTport, BorderLayout.SOUTH);
		
		JPsouth.add(JPintern,BorderLayout.CENTER);
		
		// Adding to my main JFrame.
		
		this.add(JPnorth, BorderLayout.NORTH);
		this.add(JPcenter, BorderLayout.CENTER);
		this.add(JPsouth, BorderLayout.SOUTH);
		
		//Adding action to buttons and text fields.
		
		JBSend.addActionListener(this);
		JTip.addActionListener(this);
		JTport.addActionListener(this);
		
		//Settings for the main JFame.
		
		this.setSize (new Dimension(900,700));
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBackground(Color.BLACK);
		this.setVisible(true);
		
		}
	
	public JTextArea getJTareaOut() {
		return JTareaOut;
	}

	public void setJTareaOut(JTextArea jTareaOut) {
		JTareaOut = jTareaOut;
	}

	public JTextArea getJTareaRespond() {
		return JTareaRespond;
	}

	public void setJTareaRespond(JTextArea jTareaRespond) {
		JTareaRespond = jTareaRespond;
	}
	
	// Method for common action to this class.
	
	private void buttonAction(){
		
		String ipAddress = JTip.getText();
		int portNumber = 100;
	
		if (getJTareaOut().getText().isEmpty()) {
			getJTareaRespond().setText("Error: missing <xml> data in " +
					"Output field");
			return;}
		
		else if (JTport.getText().isEmpty()) {
			getJTareaRespond().setText("Error: missing port number data in " +
					"Port Number field");
			return;}
		
		else if (JTip.getText().isEmpty()) {
			getJTareaRespond().setText("Error: missing IP address in " +
					"IP Address field");
			return;}
		
		// Tries to parse input to port number to a Integer.
		
					try	{
			 				portNumber = Integer.parseInt(JTport.getText());}
			
					catch (NumberFormatException e){
			
							getJTareaRespond().setText("Error: Invaild port number");
							return;}
	
			
		getJTareaRespond().setText(getJTareaOut().getText());
				
		ServerAccess SA = new ServerAccess(ipAddress, portNumber);
					
					try {
						
							SA.connect(this);}
					
					catch (IOException e) {
						
							e.printStackTrace();
							return;}
					
		System.out.println("completed " + counter);
		counter++;}
		
	

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == JBSend){
			
					this.buttonAction();
			}
		
		else if(e.getSource() == JTip){
			
					this.buttonAction();
			}
		
		else if(e.getSource() == JTport){
			
					this.buttonAction();
			}
	}
}
