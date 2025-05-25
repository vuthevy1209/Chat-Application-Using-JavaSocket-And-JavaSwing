import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import config.Authentication;
import controllers.AuthController;
import controllers.ChatController;
import controllers.MessageController;
import dto.request.ApiRequest;
import dto.response.ApiResponse;

public class App {
    private static AuthController authController = new AuthController();
    private static ChatController chatController = new ChatController();
    private static MessageController messageController = new MessageController();

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

            
            String method = request.getMethod();
            String url = request.getUrl();

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
                        case "/chats/mychats":
                            objectOut.writeObject(chatController.getMyChats());
                            break;
                        case "/users/online":
                            objectOut.writeObject(authController.getAllUsersOnline());
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
