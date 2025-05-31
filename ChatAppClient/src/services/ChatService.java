package services;

import config.Authentication;
import dto.request.ApiRequest;
import dto.request.ChatRequest;
import dto.response.ApiResponse;
import utils.ApiUtils;

public class ChatService {
    public static ApiResponse createChat(ChatRequest request) {

        ApiRequest apiRequest = ApiRequest.builder()
                .method("POST")
                .url("/chats")
                .headers(Authentication.getUser().getId())
                .payload(request)
                .build();

        return ApiUtils.handleRequest(apiRequest);
    }

    public static ApiResponse getChatById(String chatId) {

        ApiRequest apiRequest = ApiRequest.builder()
                .method("GET")
                .url("/chats/id")
                .headers(Authentication.getUser().getId())
                .payload(chatId)
                .build();

        return ApiUtils.handleRequest(apiRequest);
    }
}
