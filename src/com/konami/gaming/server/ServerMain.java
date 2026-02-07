package com.konami.gaming.server;

import javafx.application.Platform;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Server launcher - must run on JavaFX Application thread.
 */
public class ServerMain implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(ServerMain.class.getName());

    @Override
    public void run() {
        Platform.runLater(() -> {
            try {
                GUIServer serverGui = new GUIServer("Konami Gaming Project - Server");
                serverGui.initialize();
                LOGGER.info("Server GUI initialized");
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error initializing server", e);
            }
        });
    }
}
