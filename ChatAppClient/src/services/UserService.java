package services;

import config.Authentication;
import dto.request.ApiRequest;
import dto.response.ApiResponse;
import utils.ApiUtil;

public class UserService {
    public static ApiResponse getMyChats() {
        ApiRequest request = ApiRequest.builder()
            .method("GET")
            .headers(Authentication.getUser().getId())
            .url("/chats/mychats")
            .build();

        return ApiUtil.handleRequest(request);
    }

    public static ApiResponse getOnlineUsers() {
        ApiRequest request = ApiRequest.builder()
            .method("GET")
            .url("/users/online")
            .headers(Authentication.getUser().getId())
            .build();

        return ApiUtil.handleRequest(request);
    }
}
