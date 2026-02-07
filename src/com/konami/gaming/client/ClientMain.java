package com.konami.gaming.client;

import javafx.application.Platform;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Client launcher - must run on JavaFX Application thread.
 */
public class ClientMain implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(ClientMain.class.getName());

    @Override
    public void run() {
        Platform.runLater(() -> {
            try {
                GUIClient clientGui = new GUIClient("Konami Gaming Project - Client");
                clientGui.show();
                LOGGER.info("Client GUI initialized");
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error initializing client", e);
            }
        });
    }
}
