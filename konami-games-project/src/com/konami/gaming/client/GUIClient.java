package com.konami.gaming.client;

import com.konami.gaming.common.NetworkConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Modern GUI Client implementation with improved user interface
 * and error handling.
 */
public class GUIClient extends JFrame implements ActionListener, Runnable {
    
    private static final Logger LOGGER = Logger.getLogger(GUIClient.class.getName());
    private static final long serialVersionUID = 1L;
    
    // GUI Components
    private JButton sendButton;
    private JTextField ipTextField;
    private JTextField portTextField;
    private JLabel ipLabel;
    private JLabel portLabel;
    private JTextArea outputTextArea;
    private JTextArea responseTextArea;
    private JPanel centerPanel;
    private JPanel southPanel;
    private JPanel northPanel;
    private JPanel inputPanel;
    
    // Client connection
    private ServerConnector serverConnector;
    
    /**
     * Constructor for GUI Client
     * @param windowName The title for the client window
     */
    public GUIClient(String windowName) {
        super(windowName);
        initializeComponents();
    }
    
    /**
     * Initialize the client GUI
     */
    public void initialize() {
        setupLayout();
        setupEventListeners();
        configureWindow();
        populateDefaultXml();
        setVisible(true);
        LOGGER.info("Client GUI initialized and ready");
    }
    
    /**
     * Initialize all GUI components
     */
    private void initializeComponents() {
        // Button
        sendButton = new JButton("Send Message");
        
        // Text fields
        ipTextField = new JTextField(30);
        portTextField = new JTextField(30);
        
        // Labels
        ipLabel = new JLabel("Server IP Address:");
        portLabel = new JLabel("Server Port:");
        
        // Text areas
        responseTextArea = new JTextArea(15, 30);
        responseTextArea.setLineWrap(true);
        responseTextArea.setWrapStyleWord(true);
        responseTextArea.setEditable(false);
        responseTextArea.setText("Response Section: (Server responses will appear here)");
        responseTextArea.setBackground(Color.WHITE);
        responseTextArea.setBorder(BorderFactory.createTitledBorder("Server Response"));
        
        outputTextArea = new JTextArea(15, 30);
        outputTextArea.setLineWrap(true);
        outputTextArea.setWrapStyleWord(true);
        outputTextArea.setText("");
        outputTextArea.setBackground(Color.WHITE);
        outputTextArea.setBorder(BorderFactory.createTitledBorder("XML Message to Send"));
        
        // Panels
        northPanel = new JPanel();
        centerPanel = new JPanel();
        southPanel = new JPanel();
        inputPanel = new JPanel();
    }
    
    /**
     * Setup the layout for all panels
     */
    private void setupLayout() {
        // North panel setup
        northPanel.setBackground(Color.LIGHT_GRAY);
        northPanel.add(sendButton);
        
        // Center panel setup
        centerPanel.setBackground(Color.GRAY);
        centerPanel.setLayout(new GridLayout(1, 2, 10, 10));
        
        JScrollPane outputScrollPane = new JScrollPane(outputTextArea);
        JScrollPane responseScrollPane = new JScrollPane(responseTextArea);
        
        centerPanel.add(outputScrollPane);
        centerPanel.add(responseScrollPane);
        
        // South panel setup
        inputPanel.setBackground(Color.LIGHT_GRAY);
        inputPanel.setLayout(new GridLayout(2, 2, 5, 5));
        
        inputPanel.add(ipLabel);
        inputPanel.add(ipTextField);
        inputPanel.add(portLabel);
        inputPanel.add(portTextField);
        
        southPanel.add(inputPanel);
        
        // Add panels to frame
        add(northPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Setup event listeners
     */
    private void setupEventListeners() {
        sendButton.addActionListener(this);
        ipTextField.addActionListener(this);
        portTextField.addActionListener(this);
    }
    
    /**
     * Configure main window properties
     */
    private void configureWindow() {
        setSize(900, 500);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
    }
    
    /**
     * Populate default XML message
     */
    private void populateDefaultXml() {
        String defaultXml = 
            "<?xml version='1.0' encoding='UTF-8'?>\\n" +
            "<Message>\\n" +
            "  <Command>Print</Command>\\n" +
            "  <Data>\\n" +
            "     <Row>\\n" +
            "       <Description>\"Name\"</Description>\\n" +
            "       <Value>\"John Doe\"</Value>\\n" +
            "     </Row>\\n" +
            "     <Row>\\n" +
            "       <Description>\"Address\"</Description>\\n" +
            "       <Value>\"123 Main Street\"</Value>\\n" +
            "     </Row>\\n" +
            "  </Data>\\n" +
            "</Message>";
        
        outputTextArea.setText(defaultXml);
        
        // Set default connection values
        ipTextField.setText("localhost");
        portTextField.setText(String.valueOf(NetworkConstants.DEFAULT_PORT));
    }
    
    /**
     * Handle sending message to server
     */
    @Override
    public void run() {
        if (validateInput()) {
            sendMessageToServer();
        }
    }
    
    /**
     * Validate user input
     * @return true if input is valid, false otherwise
     */
    private boolean validateInput() {
        // Check XML data
        if (outputTextArea.getText().trim().isEmpty()) {
            showError("Please enter XML data to send");
            return false;
        }
        
        // Check port
        if (portTextField.getText().trim().isEmpty()) {
            showError("Please enter a port number");
            return false;
        }
        
        // Check IP
        if (ipTextField.getText().trim().isEmpty()) {
            showError("Please enter an IP address");
            return false;
        }
        
        // Validate port number
        try {
            int port = Integer.parseInt(portTextField.getText().trim());
            if (port < NetworkConstants.MIN_PORT || port > NetworkConstants.MAX_PORT) {
                showError("Port number must be between " + 
                         NetworkConstants.MIN_PORT + " and " + 
                         NetworkConstants.MAX_PORT);
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Invalid port number format");
            return false;
        }
        
        return true;
    }
    
    /**
     * Send message to server
     */
    private void sendMessageToServer() {
        try {
            String ipAddress = ipTextField.getText().trim();
            int portNumber = Integer.parseInt(portTextField.getText().trim());
            String xmlMessage = outputTextArea.getText();
            
            responseTextArea.append("\\n[" + java.time.LocalTime.now() + "] Connecting to " + 
                                  ipAddress + ":" + portNumber + "...\\n");
            
            serverConnector = new ServerConnector(ipAddress, portNumber, this);
            serverConnector.sendMessage(xmlMessage);
            
            // Start response listening thread
            Thread responseThread = new Thread(serverConnector, "Client-Response-Thread");
            responseThread.start();
            
            LOGGER.info("Message sent to server: " + ipAddress + ":" + portNumber);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error sending message to server", e);
            showError("Failed to send message: " + e.getMessage());
        }
    }
    
    /**
     * Append response from server
     * @param response The response text
     */
    public void appendResponse(String response) {
        SwingUtilities.invokeLater(() -> {
            responseTextArea.append("\\n[" + java.time.LocalTime.now() + "] Server: " + response);
            responseTextArea.setCaretPosition(responseTextArea.getDocument().getLength());
        });
    }
    
    /**
     * Show error message
     * @param message The error message to display
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    // Getters for text areas
    public JTextArea getOutputTextArea() {
        return outputTextArea;
    }
    
    public JTextArea getResponseTextArea() {
        return responseTextArea;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        
        if (source == sendButton || source == ipTextField || source == portTextField) {
            responseTextArea.append("\\n[" + java.time.LocalTime.now() + "] Preparing to send message...\\n");
            
            Thread clientThread = new Thread(this, "Client-Send-Thread");
            clientThread.start();
        }
    }
}