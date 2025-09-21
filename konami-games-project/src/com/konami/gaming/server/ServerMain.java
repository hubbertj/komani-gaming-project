package com.konami.gaming.server;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Server main class that extends Thread for multi-threaded execution.
 * This class handles the server-side GUI and application logic.
 */
public class ServerMain implements Runnable {
    
    private static final Logger LOGGER = Logger.getLogger(ServerMain.class.getName());
    
    @Override
    public void run() {
        try {
            LOGGER.info("Initializing server GUI...");
            GUIServer serverGui = new GUIServer("Konami Gaming Project - Server");
            serverGui.initialize();
            LOGGER.info("Server GUI initialized successfully");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error initializing server", e);
        }
    }
}