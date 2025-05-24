import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

import config.Authentication;
import controllers.AuthController;
import dto.request.ApiRequest;
import dto.request.AuthenticateRequest;
import models.User;

public class App {
    private static AuthController authController = new AuthController();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Server is listening on port 8080");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                new Thread(() -> handleClient(socket)).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void handleClient(Socket socket) {
        try {
            ObjectOutputStream objectOut = new ObjectOutputStream(socket.getOutputStream());
            objectOut.flush(); 
            ObjectInputStream objectIn = new ObjectInputStream(socket.getInputStream());

            // Read the request
            ApiRequest request = (ApiRequest) objectIn.readObject();
            System.out.println("Request: " + request.getMethod() + " " + request.getUrl());

            if (request.getMethod().equals("POST") && request.getUrl().equals("/login")) {
                objectOut.writeObject(authController.handleLogin(request.getPayload()));
            } 
            else if (request.getMethod().equals("POST") && request.getUrl().equals("/register")) {
                // Handle registration logic here
                // For now, just send a placeholder response
                objectOut.writeObject("Registration endpoint not implemented yet");
            } 
            else if (request.getMethod().equals("GET") && request.getUrl().equals("/users")) {
                // Handle fetching users logic here
                // For now, just send a placeholder response
                objectOut.writeObject("Fetch users endpoint not implemented yet");
            }            
            else {
                objectOut.writeObject("Unsupported operation");
                objectOut.flush();
            }

            // Close streams and socket properly
            objectOut.close();
            objectIn.close();
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
