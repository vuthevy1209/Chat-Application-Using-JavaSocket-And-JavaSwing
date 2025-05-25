package controllers;

import dto.request.MessageRequest;
import dto.response.ApiResponse;
import services.MessageService;
import services.impl.MessageServiceImpl;

public class MessageController {
    MessageService messageService;

    public MessageController() {
        this.messageService = new MessageServiceImpl();
    }

    public ApiResponse handleSendMessage(Object payload) {
        try {
            MessageRequest messageRequest = (MessageRequest) payload;
            boolean isSent = messageService.sendMessage(messageRequest);
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
}
