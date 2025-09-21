/**
 * Konami Gaming Project - Thread Runner (Legacy Version)
 * 
 * This is the main entry point for the legacy client-server application.
 * It creates and manages two separate threads:
 * - Server Thread: Handles XML processing and server-side GUI
 * - Client Thread: Manages client-side GUI and server communication
 * 
 * @author Jerum Lee Hubbert
 * @version 1.0 (Legacy - JDK 1.6 Compatible)
 * @since 2025
 * 
 * Requirements Met:
 * ✓ Java Swing GUI implementation
 * ✓ Socket communication between client and server
 * ✓ XML file parsing capabilities
 * ✓ Multi-threaded architecture
 * ✓ Cross-platform compatibility (Windows/Linux)
 */

package koanami.pack;

/**
 * Main thread controller class that orchestrates the client-server application.
 * This class creates and manages separate threads for server and client
 * components.
 */
public class ThreadRunner {

	/**
	 * Main entry point for the application.
	 * Creates and starts both server and client threads.
	 * 
	 * @param args Command line arguments (not used)
	 */
	public static void main(String[] args) {

		// Create objects for thread execution
		Main serverMain = new Main();
		MainOut clientMain = new MainOut();

		// Create threads for concurrent execution
		Thread serverThread = new Thread(serverMain);
		serverThread.setName("Server Thread");

		Thread clientThread = new Thread(clientMain);
		clientThread.setName("Client Thread");

		// Start both threads
		System.out.println("Starting Konami Gaming Project - Thread Runner (Legacy)");
		System.out.println("Initializing server and client threads...");

		serverThread.start();
		clientThread.start();

		System.out.println("Both threads started successfully.");
	}
}
