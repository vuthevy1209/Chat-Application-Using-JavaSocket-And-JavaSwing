package controllers;

import dto.request.ChatRequest;
import dto.response.ApiResponse;
import services.ChatService;
import services.impl.ChatServiceImpl;

public class ChatController {
    private ChatService chatService = new ChatServiceImpl();

    public ApiResponse getMyChats() {
        try {
            return ApiResponse.builder()
                    .code("200")
                    .message("Chats retrieved successfully")
                    .data(chatService.getAllMyChats())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.builder()
                    .code("500")
                    .message("Internal server error")
                    .build();
        }
    }

    public ApiResponse createChat(Object payload) {
        try {
            ChatRequest chatRequest = (ChatRequest) payload;

            boolean isCreated = chatService.createChat(chatRequest);

            if (isCreated) {
                return ApiResponse.builder()
                        .code("200")
                        .message("Chat created successfully")
                        .data(chatService.getChatById(chatRequest.getId()))
                        .build();
            } else {
                return ApiResponse.builder()
                        .code("400")
                        .message("Chat already exists")
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.builder()
                    .code("500")
                    .message("Internal server error")
                    .build();
        }
    }

    public ApiResponse sendMessage(Object payload) {
        try {
            ChatRequest chatRequest = (ChatRequest) payload;

            boolean isSent = chatService.createChat(chatRequest);

            if (isSent) {
                return ApiResponse.builder()
                        .code("200")
                        .message("Message sent successfully")
                        .build();
            } else {
                return ApiResponse.builder()
                        .code("400")
                        .message("Failed to send message")
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.builder()
                    .code("500")
                    .message("Internal server error")
                    .build();
        }
    }

    public ApiResponse getChatById(Object payload) {
        String chatId = (String) payload;
        try {
            return ApiResponse.builder()
                    .code("200")
                    .message("Chat retrieved successfully")
                    .data(chatService.getChatById(chatId))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.builder()
                    .code("500")
                    .message("Internal server error")
                    .build();
        }
    }
}
