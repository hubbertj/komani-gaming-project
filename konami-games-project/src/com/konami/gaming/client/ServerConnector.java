package com.konami.gaming.client;

import com.konami.gaming.common.NetworkConstants;

import java.io.*;
import java.net.*;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Modern server connector with proper resource management and error handling.
 * This class handles client-side communication with the server.
 */
public class ServerConnector implements Runnable {
    
    private static final Logger LOGGER = Logger.getLogger(ServerConnector.class.getName());
    
    private final String serverIp;
    private final int serverPort;
    private final GUIClient guiClient;
    private Socket connectionSocket;
    private PrintWriter outputWriter;
    private BufferedReader inputReader;
    
    /**
     * Constructor for ServerConnector
     * @param serverIp The server IP address
     * @param serverPort The server port
     * @param guiClient Reference to the GUI client
     */
    public ServerConnector(String serverIp, int serverPort, GUIClient guiClient) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.guiClient = guiClient;
    }
    
    /**
     * Send message to server
     * @param message The message to send
     * @throws IOException if connection fails
     */
    public void sendMessage(String message) throws IOException {
        try {
            // Create connection
            connectionSocket = new Socket();
            connectionSocket.connect(new InetSocketAddress(serverIp, serverPort), 
                                   NetworkConstants.CONNECTION_TIMEOUT);
            connectionSocket.setSoTimeout(NetworkConstants.SOCKET_TIMEOUT);
            
            // Create streams
            outputWriter = new PrintWriter(
                new OutputStreamWriter(connectionSocket.getOutputStream(), NetworkConstants.ENCODING), 
                true);
            inputReader = new BufferedReader(
                new InputStreamReader(connectionSocket.getInputStream(), NetworkConstants.ENCODING));
            
            // Send message
            String[] lines = message.split("\\n");
            for (String line : lines) {
                outputWriter.println(line);
            }
            outputWriter.println(NetworkConstants.END_MARKER);
            
            LOGGER.info("Message sent to server successfully");
            
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to send message to server", e);
            closeConnection();
            throw e;
        }
    }
    
    /**
     * Listen for server responses (run in separate thread)
     */
    @Override
    public void run() {
        try {
            String response;
            StringBuilder fullResponse = new StringBuilder();
            
            while ((response = inputReader.readLine()) != null) {
                if (NetworkConstants.END_MARKER.equals(response.trim())) {
                    break;
                }
                fullResponse.append(response).append("\\n");
            }
            
            if (fullResponse.length() > 0) {
                String responseText = fullResponse.toString().trim();
                guiClient.appendResponse(responseText);
                LOGGER.info("Response received from server: " + responseText);
            }
            
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error reading server response", e);
            guiClient.appendResponse("Error: Connection lost or server not responding");
        } finally {
            closeConnection();
        }
    }
    
    /**
     * Close connection and clean up resources
     */
    private void closeConnection() {
        try {
            if (outputWriter != null) {
                outputWriter.close();
            }
            if (inputReader != null) {
                inputReader.close();
            }
            if (connectionSocket != null && !connectionSocket.isClosed()) {
                connectionSocket.close();
            }
            LOGGER.info("Connection closed");
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error closing connection", e);
        }
    }
}