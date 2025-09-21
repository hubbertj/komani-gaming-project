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

	private JButton startButton;
	private JButton stopButton;
	private JPanel jpNorth;
	private JPanel jpCenter;
	private JPanel jpSouth;
	private JTextField ipField;
	private JTextField portField;
	private JLabel ipFieldLabel;
	private JLabel portFieldLabel;
	private int serverPortNumber;
	private JTextField[] gridFields = new JTextField[9];
	public SocketLis serverCon;

	public GUIServer(String titleName) {
		super(titleName);
		initUI();
	}

	public int getServerPortNumber() {
		return serverPortNumber;
	}

	public void setServerPortNumber(int serverPortNumber) {
		this.serverPortNumber = serverPortNumber;
	}

	private void initUI() {
		setLayout(new BorderLayout());

		// North panel
		startButton = new JButton("Start Server");
		stopButton = new JButton("Stop Server");
		stopButton.setEnabled(false);

		jpNorth = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jpNorth.setBackground(Color.WHITE);
		jpNorth.add(startButton);
		jpNorth.add(stopButton);

		// Center panel (Grid)
		jpCenter = new JPanel(new GridLayout(3, 3));
		for (int i = 0; i < 9; i++) {
			gridFields[i] = new JTextField();
			gridFields[i].setBackground(Color.DARK_GRAY);
			gridFields[i].setVisible(false);
			gridFields[i].setEditable(false);
			jpCenter.add(gridFields[i]);
		}

		// South panel
		ipField = new JTextField(20);
		ipField.setEditable(false);
		portField = new JTextField(20);

		ipFieldLabel = new JLabel("IP Address: ");
		portFieldLabel = new JLabel("Port Number: ");

		jpSouth = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jpSouth.setBackground(Color.WHITE);
		jpSouth.add(ipFieldLabel);
		jpSouth.add(ipField);
		jpSouth.add(portFieldLabel);
		jpSouth.add(portField);

		// Add panels to frame
		add(jpNorth, BorderLayout.NORTH);
		add(jpCenter, BorderLayout.CENTER);
		add(jpSouth, BorderLayout.SOUTH);

		// Add action listeners
		startButton.addActionListener(this);
		stopButton.addActionListener(this);
		portField.addActionListener(this);

		setSize(900, 500);
		setBackground(Color.LIGHT_GRAY);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

		setIpField();
	}

	private void setIpField() {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			ipField.setText(addr.getHostAddress());
		} catch (UnknownHostException e) {
			ipField.setText("Unknown Host");
		}
	}

	public void setGridLayout(String[] names, String[] addresses, String xmlCommand) {
		for (JTextField field : gridFields) {
			field.setVisible(true);
			field.setEditable(false);
		}
		// Example mapping, adjust as needed
		gridFields[0].setText(names.length > 0 ? names[0] : "");
		gridFields[1].setText(addresses.length > 1 ? addresses[1] : "");
		gridFields[2].setText(xmlCommand != null ? xmlCommand : "");
		gridFields[3].setText(names.length > 2 ? names[2] : "");
		gridFields[4].setText(addresses.length > 3 ? addresses[3] : "");
		gridFields[5].setText("Command option");
		gridFields[6].setText(names.length > 4 ? names[4] : "");
		gridFields[7].setText(addresses.length > 5 ? addresses[5] : "");
		gridFields[8].setText("Command option");
	}

	@Override
	public void run() {
		String portText = portField.getText().trim();
		if (portText.isEmpty()) {
			portField.setText("Error: please provide a valid port number");
			return;
		}
		try {
			serverPortNumber = Integer.parseInt(portText);
		} catch (NumberFormatException e) {
			portField.setText("Error: please provide a valid port number");
			return;
		}

		startButton.setEnabled(false);
		stopButton.setEnabled(true);
		serverCon = new SocketLis(this);
		new Thread(serverCon).start();
	}

	public void clearGrid() {
		for (JTextField field : gridFields) {
			field.setVisible(false);
			field.setText(null);
		}
	}

	public void stopServer() {
		clearGrid();
		stopButton.setEnabled(false);
		startButton.setEnabled(true);
		if (serverCon != null) {
			try {
				serverCon.close();
			} catch (IOException e) {
				System.out.println("Error from stopServer()");
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if (src == startButton || src == portField) {
			new Thread(this, "ServerProcess").start();
		} else if (src == stopButton) {
			stopServer();
		}
	}
}
