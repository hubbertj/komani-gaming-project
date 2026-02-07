package com.konami.gaming.server;

import com.konami.gaming.common.NetworkConstants;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * JavaFX GUI Server - displays received key-value data in dynamic cards.
 */
public class GUIServer {

    private static final Logger LOGGER = Logger.getLogger(GUIServer.class.getName());

    private final Stage stage;
    private final Button startButton;
    private final Button stopButton;
    private final TextField ipField;
    private final TextField portField;
    private final VBox dataCardsBox;
    private int serverPortNumber;
    private volatile boolean isServerRunning = false;
    private SocketListener serverConnection;

    public GUIServer(String title) {
        stage = new Stage();
        stage.setTitle(title);
        stage.setWidth(760);
        stage.setHeight(520);
        stage.setX(80);
        stage.setY(80);

        startButton = new Button("▶ Start");
        startButton.setStyle(
            "-fx-background-color: #10b981; -fx-text-fill: white; -fx-cursor: hand; " +
            "-fx-background-radius: 6; -fx-padding: 8 16; -fx-font-size: 13; -fx-font-weight: bold;");
        startButton.setOnAction(e -> {
            if (validatePortInput()) startServer();
        });

        stopButton = new Button("■ Stop");
        stopButton.setStyle(
            "-fx-background-color: #ef4444; -fx-text-fill: white; -fx-cursor: hand; " +
            "-fx-background-radius: 6; -fx-padding: 8 16; -fx-font-size: 13; -fx-font-weight: bold;");
        stopButton.setDisable(true);
        stopButton.setOnAction(e -> stopServer());

        ipField = new TextField();
        ipField.setEditable(false);
        ipField.setPrefWidth(130);
        ipField.setStyle(
            "-fx-background-color: #334155; -fx-text-fill: #e2e8f0; -fx-background-radius: 6; " +
            "-fx-padding: 8 12; -fx-font-family: 'SF Mono', 'Monaco', monospace; -fx-font-size: 12;");
        portField = new TextField(String.valueOf(NetworkConstants.DEFAULT_PORT));
        portField.setPrefWidth(70);
        portField.setStyle(
            "-fx-background-color: #334155; -fx-text-fill: #f8fafc; -fx-background-radius: 6; " +
            "-fx-padding: 8 12; -fx-font-family: 'SF Mono', 'Monaco', monospace; -fx-font-size: 12;");
        portField.setOnAction(e -> {
            if (validatePortInput()) startServer();
        });

        Label ipLbl = new Label("IP");
        Label portLbl = new Label("Port");
        ipLbl.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 11; -fx-font-weight: bold;");
        portLbl.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 11; -fx-font-weight: bold;");

        HBox toolbar = new HBox(16);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setPadding(new Insets(12, 20, 12, 20));
        toolbar.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #1e293b, #0f172a); " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 2);");
        Region spacer = new Region();
        spacer.setMinWidth(1);
        spacer.setMaxWidth(1);
        spacer.setPrefHeight(28);
        spacer.setStyle("-fx-background-color: #475569;");

        toolbar.getChildren().addAll(
            startButton,
            stopButton,
            spacer,
            ipLbl, ipField,
            portLbl, portField
        );

        dataCardsBox = new VBox(12);
        dataCardsBox.setPadding(new Insets(20));
        dataCardsBox.setStyle("-fx-background-color: #f8fafc;");
        ScrollPane scroll = new ScrollPane(dataCardsBox);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: #f8fafc; -fx-background: #f8fafc; -fx-border-width: 0;");
        scroll.setPadding(new Insets(0));

        VBox root = new VBox(0);
        root.setStyle("-fx-background-color: #f8fafc;");
        root.getChildren().addAll(toolbar, scroll);
        VBox.setVgrow(scroll, Priority.ALWAYS);

        stage.setScene(new javafx.scene.Scene(root));
        stage.setOnCloseRequest(ev -> System.exit(0));
    }

    public void initialize() {
        detectLocalIP();
        stage.show();
        if (validatePortInput()) {
            startServer();
        }
        LOGGER.info("Server GUI ready");
    }

    private void detectLocalIP() {
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            ipField.setText(ip);
        } catch (UnknownHostException e) {
            LOGGER.log(Level.WARNING, "Could not detect IP", e);
            ipField.setText("Unable to detect");
        }
    }

    public void updateDisplay(String command, List<String[]> keyValuePairs) {
        Platform.runLater(() -> {
            dataCardsBox.getChildren().clear();

            if (command != null && !command.isEmpty()) {
                dataCardsBox.getChildren().add(createCard("Command", command));
            }
            if (keyValuePairs != null) {
                for (String[] pair : keyValuePairs) {
                    String key = pair.length > 0 ? pair[0] : "";
                    String val = pair.length > 1 ? pair[1] : "";
                    if (!key.isEmpty() || !val.isEmpty()) {
                        dataCardsBox.getChildren().add(createCard(key.isEmpty() ? "Key" : key, val));
                    }
                }
            }
        });
    }

    private VBox createCard(String label, String value) {
        VBox card = new VBox(6);
        card.setPadding(new Insets(16, 20, 16, 20));
        card.setStyle(
            "-fx-background-color: white; -fx-background-radius: 10; " +
            "-fx-border-color: #e2e8f0; -fx-border-radius: 10; -fx-border-width: 1; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.04), 6, 0, 0, 1);");
        Label labelLbl = new Label(label.toUpperCase());
        labelLbl.setFont(Font.font("System", FontWeight.BOLD, 11));
        labelLbl.setTextFill(Color.web("#64748b"));
        Label valueLbl = new Label(value);
        valueLbl.setFont(Font.font("System", FontWeight.NORMAL, 15));
        valueLbl.setTextFill(Color.web("#0f172a"));
        card.getChildren().addAll(labelLbl, valueLbl);
        return card;
    }

    public void clearGridDisplay() {
        Platform.runLater(() -> dataCardsBox.getChildren().clear());
    }

    private boolean validatePortInput() {
        String portText = portField.getText().trim();
        if (portText.isEmpty()) {
            showError("Please provide a valid port number");
            return false;
        }
        try {
            serverPortNumber = Integer.parseInt(portText);
            if (serverPortNumber < NetworkConstants.MIN_PORT || serverPortNumber > NetworkConstants.MAX_PORT) {
                showError("Port must be between " + NetworkConstants.MIN_PORT + " and " + NetworkConstants.MAX_PORT);
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            showError("Invalid port number");
            return false;
        }
    }

    private void startServer() {
        try {
            startButton.setDisable(true);
            stopButton.setDisable(false);
            isServerRunning = true;
            serverConnection = new SocketListener(this);
            new Thread(serverConnection, "Server-Socket-Thread").start();
            LOGGER.info("Server started on port " + serverPortNumber);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error starting server", e);
            showError("Failed to start: " + e.getMessage());
            resetServerState();
        }
    }

    private void stopServer() {
        try {
            isServerRunning = false;
            if (serverConnection != null) serverConnection.close();
            clearGridDisplay();
            resetServerState();
            LOGGER.info("Server stopped");
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error stopping", e);
        }
    }

    private void resetServerState() {
        Platform.runLater(() -> {
            startButton.setDisable(false);
            stopButton.setDisable(true);
        });
        isServerRunning = false;
    }

    private void showError(String message) {
        Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, message).showAndWait());
    }

    public int getServerPortNumber() { return serverPortNumber; }
    public boolean isServerRunning() { return isServerRunning; }
}
