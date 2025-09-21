/**
 * Java Sample Program
 * Jerum Lee Hubbert
 * 1700 Toltec Cir
 * Henderson, NV 89014
 * Cell: 210-995-4393
 * 
 * Requirements:
 * 
 * Java Coding assignment for Konami Games.
 * You should be able to complete this task if you understand 
 * 1) the Java language, 2) the Java API's swing GUI / Tables 
 * / Socket Communication and 3) Parsing an xml file. 
 * Please make the sample apps are compatible with JDK 1.6+ 
 * and be able to run on Windows or Linux. For the extra 
 * credit portion, you will need to understand threads and 
 * how they interact with the GUI.
 */

package com.konami.gaming.common;

import com.konami.gaming.server.ServerMain;
import com.konami.gaming.client.ClientMain;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Main thread runner class that coordinates the client and server applications.
 * This class demonstrates multi-threaded programming by launching both
 * server and client components simultaneously.
 */
public class ThreadRunner {
    
    private static final Logger LOGGER = Logger.getLogger(ThreadRunner.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Starting Konami Gaming Project - Thread Runner");
        
        try {
            // Objects to be passed into Thread Objects
            ServerMain serverMain = new ServerMain();
            ClientMain clientMain = new ClientMain();
            
            // Create threads for running the programs
            Thread serverThread = new Thread(serverMain, "Server-Thread");
            Thread clientThread = new Thread(clientMain, "Client-Thread");
            
            // Set threads as daemon threads for proper shutdown
            serverThread.setDaemon(false);
            clientThread.setDaemon(false);
            
            // Starting threads
            LOGGER.info("Starting server thread...");
            serverThread.start();
            
            // Small delay to ensure server starts first
            Thread.sleep(500);
            
            LOGGER.info("Starting client thread...");
            clientThread.start();
            
            LOGGER.info("Both threads started successfully");
            
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Thread execution interrupted", e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error starting application", e);
        }
    }
}