package server;

import controllers.AuthController;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerHandler {
    private final int port;
    private ServerSocket serverSocket;
    private final ExecutorService executorService;
    private boolean running = false;
    
    public ServerHandler(int port) {
        this.port = port;
        this.executorService = Executors.newCachedThreadPool();
    }
    
    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            running = true;
            System.out.println("Server started on port " + port);
            
            // Start accepting client connections
            while (running) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());
                
                // Handle client connection in a separate thread
                executorService.submit(() -> handleClient(clientSocket));
            }
        } catch (Exception e) {
            System.out.println("Server error: " + e.getMessage());
            stop();
        }
    }
    
    private void handleClient(Socket clientSocket) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            
            // Initialize controllers
            AuthController authController = new AuthController();
            
            // Process client requests
            while (running && !clientSocket.isClosed()) {
                try {
                    // Read command from client
                    String command = (String) inputStream.readObject();
                    
                    // Process command
                    switch (command) {
                        case "LOGIN":
                            authController.handleLogin(clientSocket, inputStream, outputStream);
                            break;
                        case "REGISTER":
                            authController.handleRegister(clientSocket, inputStream, outputStream);
                            break;
                        case "LOGOUT":
                            System.out.println("Client logged out: " + clientSocket.getInetAddress());
                            break;
                        default:
                            System.out.println("Unknown command: " + command);
                            break;
                    }
                } catch (Exception e) {
                    System.out.println("Error processing client request: " + e.getMessage());
                    break;
                }
            }
            
            // Close client socket
            clientSocket.close();
        } catch (Exception e) {
            System.out.println("Error handling client: " + e.getMessage());
        }
    }
    
    public void stop() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            executorService.shutdown();
        } catch (Exception e) {
            System.out.println("Error stopping server: " + e.getMessage());
        }
    }
}
