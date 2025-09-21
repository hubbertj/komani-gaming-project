package com.konami.gaming.server;

import com.konami.gaming.xml.XmlProcessor;
import com.konami.gaming.common.NetworkConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Modern GUI Server implementation using Swing components.
 * This class provides a user interface for the server-side application
 * with improved error handling and modern Java practices.
 */
public class GUIServer extends JFrame implements ActionListener, Runnable {
    
    private static final Logger LOGGER = Logger.getLogger(GUIServer.class.getName());
    private static final long serialVersionUID = 1L;
    
    // GUI Components
    private JButton startButton;
    private JButton stopButton;
    private JPanel northPanel;
    private JPanel centerPanel;
    private JPanel southPanel;
    private JTextField ipField;
    private JTextField portField;
    private JLabel ipFieldLabel;
    private JLabel portFieldLabel;
    
    // Server Configuration
    private int serverPortNumber;
    private volatile boolean isServerRunning = false;
    
    // Grid Layout Components
    private GridLayout xmlLayout;
    private JTextField[] gridFields;
    
    // Server Connection Handler
    private SocketListener serverConnection;
    
    /**
     * Constructor for GUI Server
     * @param titleName The title for the server window
     */
    public GUIServer(String titleName) {
        super(titleName);
        initializeComponents();
    }
    
    /**
     * Initialize all GUI components and layout
     */
    public void initialize() {
        setupLayout();
        setupEventListeners();
        configureWindow();
        detectLocalIP();
        setVisible(true);
        LOGGER.info("Server GUI initialized and ready");
    }
    
    /**
     * Initialize all GUI components
     */
    private void initializeComponents() {
        // Buttons
        startButton = new JButton("Start Server");
        stopButton = new JButton("Stop Server");
        stopButton.setEnabled(false);
        
        // Panels
        northPanel = new JPanel();
        centerPanel = new JPanel();
        southPanel = new JPanel();
        
        // Network configuration fields
        ipField = new JTextField(20);
        portField = new JTextField(20);
        ipFieldLabel = new JLabel("IP Address: ");
        portFieldLabel = new JLabel("Port Number: ");
        ipField.setEditable(false);
        
        // Grid fields for XML data display
        gridFields = new JTextField[9];
        xmlLayout = new GridLayout(3, 3);
        centerPanel.setLayout(xmlLayout);
        
        for (int i = 0; i < gridFields.length; i++) {
            gridFields[i] = new JTextField();
            gridFields[i].setEditable(false);
            gridFields[i].setVisible(false);
            gridFields[i].setBackground(Color.LIGHT_GRAY);
            centerPanel.add(gridFields[i]);
        }
    }
    
    /**
     * Setup the layout for all panels
     */
    private void setupLayout() {
        // North panel setup
        northPanel.setBackground(Color.WHITE);
        northPanel.add(startButton);
        northPanel.add(stopButton);
        
        // Center panel setup
        centerPanel.setBackground(Color.DARK_GRAY);
        
        // South panel setup
        southPanel.setBackground(Color.WHITE);
        southPanel.add(ipFieldLabel);
        southPanel.add(ipField);
        southPanel.add(portFieldLabel);
        southPanel.add(portField);
        
        // Add panels to frame
        add(northPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Setup event listeners for components
     */
    private void setupEventListeners() {
        startButton.addActionListener(this);
        stopButton.addActionListener(this);
        portField.addActionListener(this);
    }
    
    /**
     * Configure main window properties
     */
    private void configureWindow() {
        setSize(900, 500);
        setBackground(Color.LIGHT_GRAY);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
    }
    
    /**
     * Detect and display local IP address
     */
    private void detectLocalIP() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            byte[] ipAddr = addr.getAddress();
            
            // Convert bytes to IP string (handling unsigned bytes)
            StringBuilder ipString = new StringBuilder();
            for (int i = 0; i < ipAddr.length; i++) {
                if (i > 0) ipString.append(".");
                ipString.append(ipAddr[i] & 0xFF);
            }
            
            ipField.setText(ipString.toString());
            LOGGER.info("Detected local IP: " + ipString.toString());
            
        } catch (UnknownHostException e) {
            LOGGER.log(Level.WARNING, "Could not detect local IP address", e);
            ipField.setText("Unable to detect IP");
        }
    }
    
    /**
     * Update the grid layout with processed XML data
     * @param names Array of name values from XML
     * @param addresses Array of address values from XML
     * @param xmlCommand The command from XML
     */
    public void updateGridDisplay(String[] names, String[] addresses, String xmlCommand) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Make grid fields visible
                for (JTextField field : gridFields) {
                    field.setVisible(true);
                    field.setEditable(false);
                }
                
                // Populate grid with data
                if (names.length > 0 && names[0] != null) {
                    gridFields[0].setText(names[0]);
                }
                if (addresses.length > 1 && addresses[1] != null) {
                    gridFields[1].setText(addresses[1]);
                }
                if (xmlCommand != null) {
                    gridFields[2].setText(xmlCommand);
                }
                
                // Continue populating other fields as needed
                for (int i = 3; i < gridFields.length && i - 3 < names.length; i++) {
                    if (i % 3 == 0 && (i - 3) / 3 * 2 < names.length) {
                        gridFields[i].setText(names[(i - 3) / 3 * 2]);
                    } else if (i % 3 == 1 && (i - 3) / 3 * 2 + 1 < addresses.length) {
                        gridFields[i].setText(addresses[(i - 3) / 3 * 2 + 1]);
                    } else {
                        gridFields[i].setText("Command option");
                    }
                }
                
                LOGGER.info("Grid display updated with XML data");
                
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error updating grid display", e);
            }
        });
    }
    
    /**
     * Clear the grid display
     */
    public void clearGridDisplay() {
        SwingUtilities.invokeLater(() -> {
            for (JTextField field : gridFields) {
                field.setVisible(false);
                field.setText("");
            }
            LOGGER.info("Grid display cleared");
        });
    }
    
    /**
     * Start the server with the specified port
     */
    @Override
    public void run() {
        if (validatePortInput()) {
            startServer();
        }
    }
    
    /**
     * Validate port input from user
     * @return true if port is valid, false otherwise
     */
    private boolean validatePortInput() {
        String portText = portField.getText().trim();
        
        if (portText.isEmpty()) {
            showError("Please provide a valid port number");
            return false;
        }
        
        try {
            serverPortNumber = Integer.parseInt(portText);
            
            if (serverPortNumber < NetworkConstants.MIN_PORT || 
                serverPortNumber > NetworkConstants.MAX_PORT) {
                showError("Port number must be between " + 
                         NetworkConstants.MIN_PORT + " and " + 
                         NetworkConstants.MAX_PORT);
                return false;
            }
            
            return true;
            
        } catch (NumberFormatException e) {
            showError("Invalid port number format");
            return false;
        }
    }
    
    /**
     * Start the server
     */
    private void startServer() {
        try {
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            isServerRunning = true;
            
            serverConnection = new SocketListener(this);
            Thread serverThread = new Thread(serverConnection, "Server-Socket-Thread");
            serverThread.start();
            
            LOGGER.info("Server started on port: " + serverPortNumber);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error starting server", e);
            showError("Failed to start server: " + e.getMessage());
            resetServerState();
        }
    }
    
    /**
     * Stop the server
     */
    private void stopServer() {
        try {
            isServerRunning = false;
            
            if (serverConnection != null) {
                serverConnection.close();
            }
            
            clearGridDisplay();
            resetServerState();
            
            LOGGER.info("Server stopped");
            
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error stopping server", e);
            showError("Error stopping server: " + e.getMessage());
        }
    }
    
    /**
     * Reset server state to initial conditions
     */
    private void resetServerState() {
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        isServerRunning = false;
    }
    
    /**
     * Show error message to user
     * @param message The error message to display
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    // Getters
    public int getServerPortNumber() {
        return serverPortNumber;
    }
    
    public boolean isServerRunning() {
        return isServerRunning;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        
        if (source == startButton || source == portField) {
            Thread serverThread = new Thread(this, "Server-Process");
            serverThread.start();
            
        } else if (source == stopButton) {
            stopServer();
        }
    }
}