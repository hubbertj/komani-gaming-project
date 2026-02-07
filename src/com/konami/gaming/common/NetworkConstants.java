package com.konami.gaming.common;

/**
 * Constants for network configuration
 */
public final class NetworkConstants {
    
    // Prevent instantiation
    private NetworkConstants() {}
    
    // Port range constants
    public static final int MIN_PORT = 1024;
    public static final int MAX_PORT = 65535;
    public static final int DEFAULT_PORT = 8080;
    
    // Connection constants
    public static final int CONNECTION_TIMEOUT = 3000; // 3 seconds
    public static final int SOCKET_TIMEOUT = 10000; // 10 seconds
    
    // Protocol constants
    public static final String END_MARKER = "END";
    public static final String ENCODING = "UTF-8";
    
    // Server response messages
    public static final String CONFIRMATION_MESSAGE = "Confirmed: Message has been received, come again!";
    public static final String ERROR_MESSAGE = "Error processing request";
}