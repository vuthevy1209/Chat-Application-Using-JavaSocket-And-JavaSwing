package server;

import java.io.*;
import java.net.*;
import java.util.*;

import controllers.MessageController;
import dto.request.ApiRequest;
import dto.request.MessageRequest;
import dto.response.ApiResponse;
import dto.response.MessageResponse;
import services.ChatService;
import services.MessageService;
import services.impl.ChatServiceImpl;
import services.impl.MessageServiceImpl;

public class RealtimeHandler implements Runnable {
    private static Map<String, ClientHandler> clients = new HashMap<>();


    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server soket đã khởi động và đang lắng nghe trên cổng 12345...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Server soket đã chấp nhận kết nối từ: " + socket);

                // Xử lý client trong một thread mới
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized boolean addClient(String userId, ClientHandler clientHandler) {
        if (clients.containsKey(userId)) {
            return false; 
        }
        clients.put(userId, clientHandler);
        return true;
    }

    public static synchronized void removeClient(String userId) {
        clients.remove(userId);
    }

    public static synchronized Map<String, ClientHandler> getClients() {
        return new HashMap<>(clients);
    }
}

class ClientHandler implements Runnable {
    private Socket socket;
    private String userId;
    ObjectOutputStream outObject;
    ObjectInputStream inObject;

    private MessageController messageController = new MessageController();
    private ChatService chatService = new ChatServiceImpl();
    private MessageService messageService = new MessageServiceImpl();

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            outObject = new ObjectOutputStream(socket.getOutputStream());
            outObject.flush();
            inObject = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            while (true) {
                ApiRequest apiRequest = (ApiRequest) inObject.readObject();

                if (apiRequest.getMethod().equals("POST") && apiRequest.getUrl().equals("/auth/realtime")) {
                    RealtimeHandler.addClient(apiRequest.getHeaders(), this);
                    continue; // Skip if the request is malformed
                }

                String method = apiRequest.getMethod();
                String url = apiRequest.getUrl();

                System.out.println("Real-time request: " + method + " " + url);

                switch (method) {
                    case "POST":
                        switch (url) {
                            case "/messages":
                                MessageRequest messageRequest = null;
                                if (apiRequest.getPayload() instanceof MessageRequest) {
                                    messageRequest = (MessageRequest) apiRequest.getPayload();
                                }
                                MessageResponse messageResponse = messageService.sendMessage(messageRequest);

                                List<String> participantIds = chatService.getAllUserIdsByChatId(messageRequest.getChatId());
                                Map<String, ClientHandler> clients = RealtimeHandler.getClients();

                                for (String participantId : participantIds) {
                                    // if (participantId.equals(apiRequest.getHeaders())) {
                                    //     continue;
                                    // }

                                    if (clients != null && clients.containsKey(participantId)) {
                                        ClientHandler clientHandler = clients.get(participantId);
                                        if (clientHandler != null) {
                                            clientHandler.sendObject(
                                                ApiResponse.builder()
                                                    .code("200")
                                                    .message("MESSAGE_SENT")
                                                    .data(messageResponse)
                                                    .build()
                                            );
                                        }
                                    }
                                }
                                break;
                            default:
                                break;
                            }
                        break;
                            
                    
                    case "DELETE":
                        switch (url) {
                            case "/messages":
                                String messageId = null;
                                if (apiRequest.getPayload() instanceof String) {
                                    messageId = (String) apiRequest.getPayload();
                                }

                                String chatId = chatService.findChatIdByMessageId(messageId);
                                boolean isDeleted = messageService.deleteMessage(messageId);

                                if (isDeleted) {
                                    List<String> participantIds = chatService.getAllParticipantsByChatId(chatId);
                                    Map<String, ClientHandler> clients = RealtimeHandler.getClients();
                                    for (String participantId : participantIds) {
                                        // if (participantId.equals(apiRequest.getHeaders())) {
                                        //     continue;
                                        // }

                                        if (clients != null && clients.containsKey(participantId)) {
                                            ClientHandler clientHandler = clients.get(participantId);
                                            if (clientHandler != null) {
                                                clientHandler.sendObject(
                                                    ApiResponse.builder()
                                                        .code("200")
                                                        .message("MESSAGE_DELETED")
                                                        .data(messageId)
                                                        .build()
                                                );
                                            }
                                        }
                                    }
                                } else {
                                    System.out.println("Failed to delete message with ID: " + messageId);
                                }
                                break;
                            default:
                                break;
                        }
                        break;

                    default:
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendObject(Object object) {
        try {
            outObject.writeObject(object);
            outObject.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
