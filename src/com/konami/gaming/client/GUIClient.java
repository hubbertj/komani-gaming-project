package com.konami.gaming.client;

import com.konami.gaming.common.NetworkConstants;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * JavaFX GUI Client - same look and feel as server window.
 */
public class GUIClient implements MessageClient {

    private final Stage stage;
    private final Button sendButton;
    private final TextField ipTextField;
    private final TextField portTextField;
    private final KeyValuePanel keyValuePanel;
    private final TextArea responseTextArea;
    private volatile boolean isSending = false;

    private static final String TOOLBAR_STYLE =
        "-fx-background-color: linear-gradient(to bottom, #1e293b, #0f172a); " +
        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 2);";
    private static final String TOOLBAR_FIELD_STYLE =
        "-fx-background-color: #334155; -fx-text-fill: #f8fafc; -fx-background-radius: 6; " +
        "-fx-padding: 8 12; -fx-font-family: 'SF Mono', 'Monaco', monospace; -fx-font-size: 12;";
    private static final String TOOLBAR_LABEL_STYLE = "-fx-text-fill: #94a3b8; -fx-font-size: 11; -fx-font-weight: bold;";
    private static final String SEND_BTN_STYLE =
        "-fx-background-color: #10b981; -fx-text-fill: white; -fx-cursor: hand; " +
        "-fx-background-radius: 6; -fx-padding: 8 16; -fx-font-size: 13; -fx-font-weight: bold;";

    public GUIClient(String title) {
        stage = new Stage();
        stage.setTitle(title);
        stage.setWidth(820);
        stage.setHeight(540);
        stage.setX(120);
        stage.setY(120);

        sendButton = new Button("📤 Send");
        sendButton.setStyle(SEND_BTN_STYLE);
        sendButton.setOnAction(e -> handleSend());

        ipTextField = new TextField("localhost");
        ipTextField.setPrefWidth(130);
        ipTextField.setStyle(TOOLBAR_FIELD_STYLE);

        portTextField = new TextField(String.valueOf(NetworkConstants.DEFAULT_PORT));
        portTextField.setPrefWidth(70);
        portTextField.setStyle(TOOLBAR_FIELD_STYLE);

        Label ipLbl = new Label("IP");
        Label portLbl = new Label("Port");
        ipLbl.setStyle(TOOLBAR_LABEL_STYLE);
        portLbl.setStyle(TOOLBAR_LABEL_STYLE);

        Region spacer = new Region();
        spacer.setMinWidth(1);
        spacer.setMaxWidth(1);
        spacer.setPrefHeight(28);
        spacer.setStyle("-fx-background-color: #475569;");

        HBox toolbar = new HBox(16);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setPadding(new Insets(12, 20, 12, 20));
        toolbar.setStyle(TOOLBAR_STYLE);
        toolbar.getChildren().addAll(
            sendButton,
            spacer,
            ipLbl, ipTextField,
            portLbl, portTextField
        );

        keyValuePanel = new KeyValuePanel();

        responseTextArea = new TextArea();
        responseTextArea.setEditable(false);
        responseTextArea.setWrapText(true);
        responseTextArea.setPromptText("Server responses will appear here...");
        responseTextArea.setStyle(
            "-fx-control-inner-background: white; -fx-font-family: 'SF Mono', 'Monaco', monospace; -fx-font-size: 12; " +
            "-fx-background-radius: 10; -fx-border-color: #e2e8f0; -fx-border-radius: 10; -fx-padding: 12;");

        HBox contentBox = new HBox(20);
        contentBox.setPadding(new Insets(20));
        contentBox.setStyle("-fx-background-color: #f8fafc;");
        contentBox.getChildren().addAll(keyValuePanel, responseTextArea);
        HBox.setHgrow(keyValuePanel, Priority.ALWAYS);
        HBox.setHgrow(responseTextArea, Priority.ALWAYS);

        VBox root = new VBox(0);
        root.setStyle("-fx-background-color: #f8fafc;");
        root.getChildren().addAll(toolbar, contentBox);
        VBox.setVgrow(contentBox, Priority.ALWAYS);

        stage.setScene(new javafx.scene.Scene(root));
        stage.setOnCloseRequest(ev -> System.exit(0));
    }

    public void show() {
        stage.show();
    }

    private void handleSend() {
        if (isSending) return;
        if (!validateInput()) return;

        String ipAddress = ipTextField.getText().trim();
        int portNumber = Integer.parseInt(portTextField.getText().trim());
        String xmlMessage = keyValuePanel.toXml();

        setSendingState(true);
        responseTextArea.appendText("\n[" + java.time.LocalTime.now() + "] Connecting...\n");

        ServerConnector connector = new ServerConnector(ipAddress, portNumber, xmlMessage, this);
        new Thread(connector, "Client-Send-Thread").start();
    }

    private boolean validateInput() {
        if (keyValuePanel.getKeyValues().isEmpty() && keyValuePanel.getCommand().isEmpty()) {
            showError("Please add at least one key-value pair or a command");
            return false;
        }
        if (portTextField.getText().trim().isEmpty()) {
            showError("Please enter a port number");
            return false;
        }
        if (ipTextField.getText().trim().isEmpty()) {
            showError("Please enter an IP address");
            return false;
        }
        try {
            int port = Integer.parseInt(portTextField.getText().trim());
            if (port < NetworkConstants.MIN_PORT || port > NetworkConstants.MAX_PORT) {
                showError("Port must be between " + NetworkConstants.MIN_PORT + " and " + NetworkConstants.MAX_PORT);
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Invalid port number");
            return false;
        }
        return true;
    }

    @Override
    public void onSendComplete() {
        Platform.runLater(() -> setSendingState(false));
    }

    @Override
    public void appendResponse(String response) {
        Platform.runLater(() -> {
            responseTextArea.appendText("\n[" + java.time.LocalTime.now() + "] Server: " + response);
        });
    }

    private void setSendingState(boolean sending) {
        isSending = sending;
        sendButton.setDisable(sending);
        sendButton.setText(sending ? "Sending..." : "📤 Send");
    }

    private void showError(String message) {
        Platform.runLater(() ->
            new Alert(Alert.AlertType.ERROR, message).showAndWait());
    }
}
