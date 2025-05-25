package services;

import java.io.*;
import java.net.Socket;
import config.Authentication;
import dto.response.MessageResponse;

public class WebSocketService {
    private static Socket webSocketConnection;
    private static ObjectInputStream webSocketInput;
    private static boolean isConnected = false;
    
    public static void connect() {
        try {
            webSocketConnection = new Socket("localhost", 8081);
            ObjectOutputStream out = new ObjectOutputStream(webSocketConnection.getOutputStream());
            webSocketInput = new ObjectInputStream(webSocketConnection.getInputStream());
            
            // Send user ID to register
            out.writeObject(Authentication.getUser().getId());
            out.flush();
            
            isConnected = true;
            
            // Start listening for messages
            new Thread(() -> listenForMessages()).start();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void listenForMessages() {
        while (isConnected && webSocketConnection != null && !webSocketConnection.isClosed()) {
            try {
                Object message = webSocketInput.readObject();
                if (message instanceof MessageResponse) {
                    // Handle received message
                    handleReceivedMessage((MessageResponse) message);
                }
            } catch (Exception e) {
                System.out.println("WebSocket connection lost");
                isConnected = false;
            }
        }
    }
    
    private static void handleReceivedMessage(MessageResponse message) {
        // Update UI with new message
        // You'll need to implement a callback mechanism to update ChatPage
        System.out.println("Received new message: " + message.getContent());
    }
    
    public static void disconnect() {
        isConnected = false;
        try {
            if (webSocketConnection != null) {
                webSocketConnection.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}