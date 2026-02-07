package com.konami.gaming.common;

import com.konami.gaming.server.ServerMain;
import com.konami.gaming.client.ClientMain;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.logging.Logger;

/**
 * JavaFX Application launcher. Starts server and client windows.
 */
public class SocketApp extends Application {

    private static final Logger LOGGER = Logger.getLogger(SocketApp.class.getName());

    @Override
    public void start(Stage primaryStage) {
        LOGGER.info("Starting Socket Communication Demo");
        primaryStage.close();

        Platform.runLater(() -> {
            new Thread(new ServerMain(), "Server-Thread").start();
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            Platform.runLater(() -> new Thread(new ClientMain(), "Client-Thread").start());
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
