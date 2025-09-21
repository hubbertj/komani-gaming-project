package test.Konami;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import javax.swing.*;

@SuppressWarnings("serial")
public class GUIClient extends JFrame implements ActionListener {

	private JButton buttonSend;
	private JTextField textIp;
	private JTextField textPort;
	private JLabel labelIp;
	private JLabel labelPort;
	private JTextArea textAreaOut;
	private JTextArea textAreaRespond;

	public GUIClient(String windowName) {
		setTitle(windowName);
		initComponents();
	}

	private void initComponents() {
		// North panel
		JPanel panelNorth = new JPanel(new FlowLayout(FlowLayout.LEFT));
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

		labelIp = new JLabel("Enter IP");
		labelPort = new JLabel("Enter Port");
		textIp = new JTextField(15);
		textPort = new JTextField(15);

		panelIntern.add(labelIp);
		panelIntern.add(textIp);
		panelIntern.add(labelPort);
		panelIntern.add(textPort);

		panelSouth.add(panelIntern, BorderLayout.CENTER);

		// Add panels to frame
		add(panelNorth, BorderLayout.NORTH);
		add(panelCenter, BorderLayout.CENTER);
		add(panelSouth, BorderLayout.SOUTH);

		// Add action listeners
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

	public void setTextAreaOut(JTextArea textAreaOut) {
		this.textAreaOut = textAreaOut;
	}

	public JTextArea getTextAreaRespond() {
		return textAreaRespond;
	}

	public void setTextAreaRespond(JTextArea textAreaRespond) {
		this.textAreaRespond = textAreaRespond;
	}

	private void buttonAction() {
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

		ServerAccess serverAccess = new ServerAccess(ipAddress, portNumber);
		try {
			serverAccess.connect(this, textAreaOut.getText());
			Thread.sleep(4000);
			serverAccess.connectClose();
		} catch (UnknownHostException e) {
			textAreaRespond.setText("Error: Unknown host");
		} catch (IOException e) {
			textAreaRespond.setText("Error: IO Exception");
		} catch (InterruptedException e) {
			textAreaRespond.setText("Error: Interrupted");
			Thread.currentThread().interrupt();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if (src == buttonSend || src == textIp || src == textPort) {
			textAreaRespond.setText("Message going out ..\n" + textAreaOut.getText());
			buttonAction();
		}
	}

	// Entry point for testing
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new GUIClient("Konami GUI Client"));
	}
}
