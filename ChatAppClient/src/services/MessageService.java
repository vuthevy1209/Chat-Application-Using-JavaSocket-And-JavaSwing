package services;

import config.Authentication;
import dto.request.ApiRequest;
import dto.request.MessageRequest;
import dto.response.ApiResponse;
import utils.ApiUtil;

public class MessageService {
    public static ApiResponse sendMessage(MessageRequest request) {
        ApiRequest apiRequest = ApiRequest.builder()
            .method("POST")
            .url("/messages")
            .headers(Authentication.getUser().getId())
            .payload(request)
            .build();

        return ApiUtil.handleRequest(apiRequest);
    }
}
