package services;

import models.Chat;
import models.Message;
import models.User;
import models.Response;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.function.Consumer;

public class ChatService {
    private static ChatService instance;
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private User currentUser;
    private boolean connected = false;
    
    // Private constructor for singleton pattern
    private ChatService() {
        connectToServer();
    }
    
    // Get singleton instance
    public static ChatService getInstance() {
        if (instance == null) {
            instance = new ChatService();
        }
        return instance;
    }
    
    // Connect to the server
    private void connectToServer() {
        try {
            socket = new Socket("localhost", 8888);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            connected = true;
            System.out.println("Connected to server");
        } catch (Exception e) {
            System.out.println("Could not connect to server: " + e.getMessage());
            connected = false;
        }
    }
    
    // Check if connected to server
    public boolean isConnected() {
        return connected;
    }
    
    // Login
    public void login(String username, String password, Consumer<Response> callback) {
        try {
            if (!connected) {
                connectToServer();
                if (!connected) {
                    callback.accept(new Response(false, "Could not connect to server"));
                    return;
                }
            }
            
            // Send login command
            outputStream.writeObject("LOGIN");
            outputStream.writeObject(username);
            outputStream.writeObject(password);
            
            // Get response
            boolean success = (boolean) inputStream.readObject();
            if (success) {
                currentUser = (User) inputStream.readObject();
                callback.accept(new Response(true, "Login successful"));
            } else {
                callback.accept(new Response(false, "Invalid username or password"));
            }
        } catch (Exception e) {
            System.out.println("Error during login: " + e.getMessage());
            callback.accept(new Response(false, "Error: " + e.getMessage()));
        }
    }
    
    // Register
    public void register(String email, String username, String password, Consumer<Response> callback) {
        try {
            if (!connected) {
                connectToServer();
                if (!connected) {
                    callback.accept(new Response(false, "Could not connect to server"));
                    return;
                }
            }
            
            // Send register command
            outputStream.writeObject("REGISTER");
            outputStream.writeObject(email);
            outputStream.writeObject(username);
            outputStream.writeObject(password);
            
            // Get response
            boolean success = (boolean) inputStream.readObject();
            if (success) {
                currentUser = (User) inputStream.readObject();
                callback.accept(new Response(true, "Registration successful"));
            } else {
                callback.accept(new Response(false, "Username or email already exists"));
            }
        } catch (Exception e) {
            System.out.println("Error during registration: " + e.getMessage());
            callback.accept(new Response(false, "Error: " + e.getMessage()));
        }
    }
    
    // Logout
    public void logout() {
        currentUser = null;
        try {
            if (connected) {
                outputStream.writeObject("LOGOUT");
            }
        } catch (Exception e) {
            System.out.println("Error during logout: " + e.getMessage());
        }
    }
    
    // Get current user
    public User getCurrentUser() {
        return currentUser;
    }
    
    // Close connection
    public void closeConnection() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            connected = false;
        } catch (Exception e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}
