package koanami.pack;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.UnknownHostException;
import javax.swing.*;

@SuppressWarnings("serial")
public class GUIClient extends JFrame implements ActionListener, Runnable {

	private JButton buttonSend;
	private JTextField textIp, textPort;
	private JTextArea textAreaOut, textAreaRespond;
	private ServerAccess serverAccess;

	public GUIClient(String windowName) {
		super(windowName);
		initUI();
	}

	private void initUI() {
		// North panel
		JPanel panelNorth = new JPanel();
		buttonSend = new JButton("Send Message");
		panelNorth.setBackground(Color.LIGHT_GRAY);
		panelNorth.add(buttonSend);

		// Center panel
		JPanel panelCenter = new JPanel();
		panelCenter.setBackground(Color.GRAY);

		textAreaRespond = new JTextArea(15, 30);
		textAreaRespond.setLineWrap(true);
		textAreaRespond.setEditable(false);
		textAreaRespond.setText("Respond Section:");
		JScrollPane scrollPaneRespond = new JScrollPane(textAreaRespond);

		textAreaOut = new JTextArea(15, 30);
		textAreaOut.setLineWrap(true);
		textAreaOut.setText("Output Section : <place XML data here>");
		JScrollPane scrollPaneOutput = new JScrollPane(textAreaOut);

		panelCenter.add(scrollPaneOutput);
		panelCenter.add(scrollPaneRespond);

		// South panel
		JPanel panelSouth = new JPanel(new BorderLayout());
		JPanel panelIntern = new JPanel(new GridLayout(2, 2, 5, 5));
		panelIntern.setBackground(Color.GRAY);

		JLabel labelIp = new JLabel("Enter IP");
		JLabel labelPort = new JLabel("Enter Port");
		textIp = new JTextField(15);
		textPort = new JTextField(15);

		panelIntern.add(labelIp);
		panelIntern.add(textIp);
		panelIntern.add(labelPort);
		panelIntern.add(textPort);

		panelSouth.add(panelIntern, BorderLayout.CENTER);

		// Add panels to frame
		setLayout(new BorderLayout());
		add(panelNorth, BorderLayout.NORTH);
		add(panelCenter, BorderLayout.CENTER);
		add(panelSouth, BorderLayout.SOUTH);

		// Action listeners
		buttonSend.addActionListener(this);
		textIp.addActionListener(this);
		textPort.addActionListener(this);

		// Frame settings
		setSize(900, 400);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.BLACK);
		setVisible(true);
	}

	public JTextArea getTextAreaOut() {
		return textAreaOut;
	}

	public JTextArea getTextAreaRespond() {
		return textAreaRespond;
	}

	@Override
	public void run() {
		String ipAddress = textIp.getText().trim();
		String portText = textPort.getText().trim();

		if (textAreaOut.getText().trim().isEmpty()) {
			textAreaRespond.setText("Error: missing <xml> data in Output field");
			return;
		}
		if (portText.isEmpty()) {
			textAreaRespond.setText("Error: missing port number data in Port Number field");
			return;
		}
		if (ipAddress.isEmpty()) {
			textAreaRespond.setText("Error: missing IP address in IP Address field");
			return;
		}

		int portNumber;
		try {
			portNumber = Integer.parseInt(portText);
		} catch (NumberFormatException e) {
			textAreaRespond.setText("Error: Invalid port number");
			return;
		}

		try {
			serverAccess = new ServerAccess(ipAddress, portNumber, this);
			serverAccess.out(textAreaOut.getText());
			textAreaRespond.append("\n" + serverAccess.getmyServerString());
			new Thread(serverAccess).start();
		} catch (UnknownHostException e) {
			textAreaRespond.setText("Error: Unknown host");
		} catch (IOException e) {
			textAreaRespond.setText("Error: IO Exception");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == buttonSend || e.getSource() == textIp || e.getSource() == textPort) {
			new Thread(this, "ClientThread").start();
		}
	}
}
