package test.Konami;

import java.io.*;
import java.net.*;

public class ServerAccess {
    private final String serverIp;
    private final int serverPort;

    public ServerAccess(String serverIp, int serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    public void connect(GUIClient gui) {
        try (
            Socket socket = new Socket(serverIp, serverPort);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            String clientInput;
            while ((clientInput = stdIn.readLine()) != null) {
                out.println(clientInput);
                String response = in.readLine();
                if (response != null) {
                    System.out.println("Writing: " + response);
                } else {
                    break;
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Cannot connect to server: " + serverIp);
        } catch (IOException e) {
            System.err.println("I/O error with connection to: " + serverIp);
        }
    }
}
