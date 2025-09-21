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
	private JTextField gridName;
	private JTextField gridAddress;
	private JTextField gridCommand;
	private JTextField gridExtra1;
	private JTextField gridExtra2;
	private JTextField gridExtra3;

	public SocketLis serverCon;

	public GUIServer(String titleName) {
		super(titleName);
		initUI();
	}

	private void initUI() {
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(900, 400);
		setResizable(false);
		setBackground(Color.BLACK);

		// North Panel
		startButton = new JButton("Start Server");
		stopButton = new JButton("Stop Server");
		stopButton.setEnabled(false);

		jpNorth = new JPanel(new BorderLayout());
		jpNorth.setBackground(Color.WHITE);
		jpNorth.add(startButton, BorderLayout.WEST);
		jpNorth.add(stopButton, BorderLayout.EAST);

		// Center Panel
		gridName = createGridField("Name Field: ");
		gridAddress = createGridField("Address Field: ");
		gridCommand = createGridField("Command Field: ");
		gridExtra1 = createGridField("Extra");
		gridExtra2 = createGridField("Extra");
		gridExtra3 = createGridField("Extra");

		jpCenter = new JPanel(new GridLayout(2, 3));
		jpCenter.add(gridName);
		jpCenter.add(gridAddress);
		jpCenter.add(gridCommand);
		jpCenter.add(gridExtra1);
		jpCenter.add(gridExtra2);
		jpCenter.add(gridExtra3);

		// South Panel
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

		// Action Listeners
		startButton.addActionListener(this);
		stopButton.addActionListener(this);
		portField.addActionListener(this);

		setVisible(true);
		setIpField();
	}

	private JTextField createGridField(String text) {
		JTextField field = new JTextField(text);
		field.setEditable(false);
		field.setBackground(Color.LIGHT_GRAY);
		return field;
	}

	private void setIpField() {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			ipField.setText(addr.getHostAddress());
		} catch (UnknownHostException e) {
			ipField.setText("Unknown");
		}
	}

	public int getServerPortNumber() {
		return serverPortNumber;
	}

	public void setServerPortNumber(int serverPortNumber) {
		this.serverPortNumber = serverPortNumber;
	}

	private void startServer() {
		String portText = portField.getText().trim();
		if (portText.isEmpty()) {
			gridExtra3.setText("Error: please provide a valid port number");
			return;
		}
		try {
			serverPortNumber = Integer.parseInt(portText);
		} catch (NumberFormatException e) {
			gridExtra3.setText("Error: please provide a valid port number");
			return;
		}

		startButton.setEnabled(false);
		stopButton.setEnabled(true);

		gridExtra3.setText(portText);
		serverCon = new SocketLis();
		try {
			serverCon.listen(this);
		} catch (IOException e) {
			gridExtra3.setText("IO error from GUIServer");
			return;
		}

		// Using XML data to update grid
		XmlReceived xml = new XmlReceived(serverCon.getXML(), this);
		xml.process();
		gridName.setText(xml.updateNameGrid());
		gridAddress.setText(xml.updateAddressGrid());
		gridCommand.setText(xml.updateCommandGrid());
	}

	private void stopServer() {
		gridName.setText("Name Field: ");
		gridAddress.setText("Address Field: ");
		gridCommand.setText("Command Field: ");
		gridExtra1.setText("Extra");
		gridExtra2.setText("Extra");
		gridExtra3.setText("Extra");
		stopButton.setEnabled(false);
		startButton.setEnabled(true);

		if (serverCon != null) {
			try {
				serverCon.close();
			} catch (IOException e) {
				gridExtra3.setText("Error stopping server");
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if (src == startButton || src == portField) {
			startServer();
		} else if (src == stopButton) {
			stopServer();
		}
	}

	// Getters and setters for grid fields if needed
	public JTextField getGridName() { return gridName; }
	public JTextField getGridAddress() { return gridAddress; }
	public JTextField getGridCommand() { return gridCommand; }
	public JTextField getGridExtra1() { return gridExtra1; }
	public JTextField getGridExtra2() { return gridExtra2; }
	public JTextField getGridExtra3() { return gridExtra3; }
	public void setGridName(JTextField gridName) { this.gridName = gridName; }
	public void setGridAddress(JTextField gridAddress) { this.gridAddress = gridAddress; }
	public void setGridCommand(JTextField gridCommand) { this.gridCommand = gridCommand; }
	public void setGridExtra1(JTextField gridExtra1) { this.gridExtra1 = gridExtra1; }
	public void setGridExtra2(JTextField gridExtra2) { this.gridExtra2 = gridExtra2; }
	public void setGridExtra3(JTextField gridExtra3) { this.gridExtra3 = gridExtra3; }
}
