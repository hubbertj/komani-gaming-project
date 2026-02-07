package com.konami.gaming.client;

import com.konami.gaming.common.NetworkConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Handles client-side socket communication with the server.
 * Runs send and receive in a single async flow.
 */
public class ServerConnector implements Runnable {
    
    private static final Logger LOGGER = Logger.getLogger(ServerConnector.class.getName());
    
    private final String serverIp;
    private final int serverPort;
    private final String message;
    private final MessageClient messageClient;
    private Socket connectionSocket;
    private PrintWriter outputWriter;
    private BufferedReader inputReader;
    
    public ServerConnector(String serverIp, int serverPort, String message, MessageClient messageClient) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.message = message;
        this.messageClient = messageClient;
    }
    
    /**
     * Connect, send message, wait for response, then close. All in one async run.
     * Guarantees onSendComplete is always called so the UI never stays locked.
     */
    @Override
    public void run() {
        try {
            connectionSocket = new Socket();
            connectionSocket.connect(new InetSocketAddress(serverIp, serverPort),
                    NetworkConstants.CONNECTION_TIMEOUT);
            connectionSocket.setSoTimeout(NetworkConstants.SOCKET_TIMEOUT);

            outputWriter = new PrintWriter(
                    new OutputStreamWriter(connectionSocket.getOutputStream(), NetworkConstants.ENCODING),
                    true);
            inputReader = new BufferedReader(
                    new InputStreamReader(connectionSocket.getInputStream(), NetworkConstants.ENCODING));

            String[] lines = message.split("\\n");
            for (String line : lines) {
                outputWriter.println(line);
            }
            outputWriter.println(NetworkConstants.END_MARKER);
            LOGGER.info("Message sent to server");

            StringBuilder fullResponse = new StringBuilder();
            String response;
            while ((response = inputReader.readLine()) != null) {
                if (NetworkConstants.END_MARKER.equals(response.trim())) {
                    break;
                }
                fullResponse.append(response).append("\n");
            }

            if (fullResponse.length() > 0) {
                messageClient.appendResponse(fullResponse.toString().trim());
            }

        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Connection error", e);
            String msg = e instanceof ConnectException ? "Connection refused. Is the server running?" : e.getMessage();
            messageClient.appendResponse("Error: " + msg);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error", e);
            messageClient.appendResponse("Error: " + e.getMessage());
        } finally {
            closeConnection();
            messageClient.onSendComplete();
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