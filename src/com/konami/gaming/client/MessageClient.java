package com.konami.gaming.client;

/**
 * Callback interface for socket client - UI-agnostic so ServerConnector
 * works with any implementation (Swing, JavaFX, etc.).
 */
public interface MessageClient {
    void appendResponse(String response);
    void onSendComplete();
}
