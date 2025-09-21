package com.konami.gaming.client;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Client main class that implements Runnable for multi-threaded execution.
 * This class handles the client-side GUI and application logic.
 */
public class ClientMain implements Runnable {
    
    private static final Logger LOGGER = Logger.getLogger(ClientMain.class.getName());
    
    @Override
    public void run() {
        try {
            LOGGER.info("Initializing client GUI...");
            GUIClient clientGui = new GUIClient("Konami Gaming Project - Client");
            clientGui.initialize();
            LOGGER.info("Client GUI initialized successfully");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error initializing client", e);
        }
    }
}