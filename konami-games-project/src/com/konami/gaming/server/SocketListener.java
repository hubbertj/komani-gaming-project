package com.konami.gaming.server;

import com.konami.gaming.xml.XmlProcessor;
import com.konami.gaming.common.NetworkConstants;

import java.net.*;
import java.io.*;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Modern socket listener implementation using proper resource management
 * and error handling. This class handles incoming client connections
 * and processes XML messages.
 */
public class SocketListener implements Runnable {
    
    private static final Logger LOGGER = Logger.getLogger(SocketListener.class.getName());
    
    private final GUIServer guiServer;
    private ServerSocket serverSocket;
    private volatile boolean isRunning = false;
    
    /**
     * Constructor for SocketListener
     * @param guiServer Reference to the GUI server
     */
    public SocketListener(GUIServer guiServer) {
        this.guiServer = guiServer;
    }
    
    @Override
    public void run() {
        try {
            startListening();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error in socket listener", e);
        }
    }
    
    /**
     * Start listening for client connections
     * @throws IOException if socket creation fails
     */
    private void startListening() throws IOException {
        isRunning = true;
        
        try (ServerSocket server = new ServerSocket(guiServer.getServerPortNumber())) {
            this.serverSocket = server;
            server.setSoTimeout(1000); // 1 second timeout for accept()
            
            LOGGER.info("Server listening on port: " + guiServer.getServerPortNumber());
            
            while (isRunning && guiServer.isServerRunning()) {
                try {
                    Socket clientSocket = server.accept();
                    LOGGER.info("Client connected: " + clientSocket.getRemoteSocketAddress());
                    
                    // Handle client in separate method
                    handleClient(clientSocket);
                    
                } catch (SocketTimeoutException e) {
                    // Timeout is expected, continue checking if we should still run
                    continue;
                } catch (IOException e) {
                    if (isRunning) {
                        LOGGER.log(Level.WARNING, "Error accepting client connection", e);
                    }
                }
            }
            
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to create server socket", e);
            throw e;
        }
    }
    
    /**
     * Handle individual client connection
     * @param clientSocket The connected client socket
     */
    private void handleClient(Socket clientSocket) {
        try (Socket socket = clientSocket;
             BufferedReader reader = new BufferedReader(
                 new InputStreamReader(socket.getInputStream(), NetworkConstants.ENCODING));
             PrintWriter writer = new PrintWriter(
                 socket.getOutputStream(), true)) {
            
            StringBuilder xmlData = new StringBuilder();
            String line;
            boolean messageComplete = false;
            
            // Read XML message from client
            while ((line = reader.readLine()) != null && !messageComplete) {
                if (NetworkConstants.END_MARKER.equals(line.trim())) {
                    messageComplete = true;
                    break;
                }
                xmlData.append(line).append("\\n");
            }
            
            if (messageComplete && xmlData.length() > 0) {
                // Process XML data
                processXmlMessage(xmlData.toString());
                
                // Send confirmation to client
                writer.println(NetworkConstants.CONFIRMATION_MESSAGE);
                writer.println(NetworkConstants.END_MARKER);
                
                LOGGER.info("XML message processed and response sent");
            } else {
                LOGGER.warning("Incomplete or empty message received");
                writer.println(NetworkConstants.ERROR_MESSAGE);
                writer.println(NetworkConstants.END_MARKER);
            }
            
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error handling client", e);
        }
    }
    
    /**
     * Process received XML message
     * @param xmlData The XML data to process
     */
    private void processXmlMessage(String xmlData) {
        try {
            XmlProcessor xmlProcessor = new XmlProcessor(xmlData, guiServer);
            xmlProcessor.process();
            
            // Update GUI with processed data
            String[] names = xmlProcessor.getNames();
            String[] addresses = xmlProcessor.getAddresses();
            String command = xmlProcessor.getCommand();
            
            guiServer.updateGridDisplay(names, addresses, command);
            
            LOGGER.info("XML message processed successfully");
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing XML message", e);
            guiServer.clearGridDisplay();
        }
    }
    
    /**
     * Close the socket listener
     * @throws IOException if there's an error closing resources
     */
    public void close() throws IOException {
        isRunning = false;
        
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
            LOGGER.info("Server socket closed");
        }
    }
}