package test.Konami;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;

@SuppressWarnings("serial")
public class GUIClient extends JFrame implements ActionListener {

	private JButton sendButton;
	private JTextField ipField;
	private JTextField portField;
	private JTextArea outputArea;
	private JTextArea responseArea;
	private int counter = 1;

	public GUIClient(String windowName) {
		super(windowName);
		initUI();
	}

	private void initUI() {
		// North panel
		JPanel northPanel = new JPanel();
		sendButton = new JButton("Send Message");
		northPanel.setBackground(Color.LIGHT_GRAY);
		northPanel.add(sendButton);

		// Center panel
		JPanel centerPanel = new JPanel();
		centerPanel.setBackground(Color.GRAY);

		outputArea = new JTextArea(20, 30);
		outputArea.setLineWrap(true);
		outputArea.setText("Output Section : <place XML data here>");
		JScrollPane outputScroll = new JScrollPane(outputArea);

		responseArea = new JTextArea(20, 30);
		responseArea.setLineWrap(true);
		responseArea.setEditable(false);
		responseArea.setText("Respond Section:");
		JScrollPane responseScroll = new JScrollPane(responseArea);

		centerPanel.add(outputScroll);
		centerPanel.add(responseScroll);

		// South panel
		JPanel southPanel = new JPanel(new BorderLayout());
		JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
		inputPanel.setBackground(Color.GRAY);

		JLabel ipLabel = new JLabel("Enter IP");
		ipField = new JTextField(30);
		JLabel portLabel = new JLabel("Enter Port");
		portField = new JTextField(30);

		inputPanel.add(ipLabel);
		inputPanel.add(ipField);
		inputPanel.add(portLabel);
		inputPanel.add(portField);

		southPanel.add(inputPanel, BorderLayout.CENTER);

		// Add panels to frame
		setLayout(new BorderLayout());
		add(northPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);

		// Add listeners
		sendButton.addActionListener(this);
		ipField.addActionListener(this);
		portField.addActionListener(this);

		// Frame settings
		setSize(900, 700);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.BLACK);
		setVisible(true);
	}

	public JTextArea getOutputArea() {
		return outputArea;
	}

	public JTextArea getResponseArea() {
		return responseArea;
	}

	private void handleAction() {
		String ipAddress = ipField.getText().trim();
		String portText = portField.getText().trim();
		String xmlData = outputArea.getText().trim();

		if (xmlData.isEmpty()) {
			responseArea.setText("Error: missing <xml> data in Output field");
			return;
		}
		if (portText.isEmpty()) {
			responseArea.setText("Error: missing port number data in Port Number field");
			return;
		}
		if (ipAddress.isEmpty()) {
			responseArea.setText("Error: missing IP address in IP Address field");
			return;
		}

		int portNumber;
		try {
			portNumber = Integer.parseInt(portText);
		} catch (NumberFormatException e) {
			responseArea.setText("Error: Invalid port number");
			return;
		}

		responseArea.setText(xmlData);

		ServerAccess serverAccess = new ServerAccess(ipAddress, portNumber);
		try {
			serverAccess.connect(this);
		} catch (IOException e) {
			e.printStackTrace();
			responseArea.setText("Error: Could not connect to server");
			return;
		}

		System.out.println("completed " + counter++);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == sendButton || e.getSource() == ipField || e.getSource() == portField) {
			handleAction();
		}
	}
}
