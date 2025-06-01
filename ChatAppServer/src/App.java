import java.awt.Label;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.http.HttpResponse.PushPromiseHandler;

import config.Authentication;
import config.UserOnlineList;
import controllers.AuthController;
import controllers.ChatController;
import controllers.MessageController;
import dto.request.ApiRequest;
import dto.response.ApiResponse;
import server.RealtimeHandler;

public class App {
    private static AuthController authController = new AuthController();
    private static ChatController chatController = new ChatController();
    private static MessageController messageController = new MessageController();

    public static void main(String[] args) {
        // Handle Realtime connections
        Thread realtimeThread = new Thread(new RealtimeHandler());
        realtimeThread.start();  // Start the RealtimeHandler thread

        // Start the RealtimeHandler to handle WebSocket connections

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

            
            String method = request.getMethod();
            String url = request.getUrl();

            if (!url.equals("/auth/login") && !url.equals("/auth/register") && !url.equals("/test_connection")) {
                String header = (String) request.getHeaders();
                if (header == null || header.isEmpty()) {
                    objectOut.writeObject(ApiResponse.builder()
                            .code("401")
                            .message("Unauthorized")
                            .build());
                } else {
                    Authentication.setUserId(header);
                }
            } 
            
            switch (method) {
                case "POST":
                    switch (url) {
                        case "/auth/login":
                            objectOut.writeObject(authController.handleLogin(request.getPayload()));
                            break;
                        case "/auth/logout":
                            objectOut.writeObject(authController.handleLogout());
                            break;
                        case "/auth/register":
                            objectOut.writeObject(authController.handleRegister(request.getPayload()));
                            break;
                        case "/chats":
                            objectOut.writeObject(chatController.createChat(request.getPayload()));
                            break;
                        case "/messages":
                            objectOut.writeObject(messageController.handleSendMessage(request.getPayload()));
                            break;
                        default:
                            objectOut.writeObject("Unsupported POST URL");
                            break;
                    }
                    break;
                case "GET":
                    switch (url) {
                        case "/test_connection":
                            objectOut.writeObject(ApiResponse.builder()
                                    .code("200")
                                    .message("Connection successful")
                                    .build());
                            break;
                        case "/chats/mychats":
                            objectOut.writeObject(chatController.getMyChats());
                            break;
                        case "/chats/id":
                            objectOut.writeObject(chatController.getChatById(request.getPayload()));
                            break;
                        case "/users/online":
                            objectOut.writeObject(authController.getAllUsersOnline());
                            break;
                        case "/users":
                            objectOut.writeObject(authController.getAllUsers());
                            break;
                        default:
                            objectOut.writeObject("Unsupported GET URL");
                            break;
                    }
                    break;
                default:
                    objectOut.writeObject("Unsupported method");
                    break;
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
