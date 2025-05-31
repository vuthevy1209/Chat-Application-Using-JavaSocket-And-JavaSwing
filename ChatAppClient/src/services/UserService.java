package services;

import config.Authentication;
import dto.request.ApiRequest;
import dto.response.ApiResponse;
import utils.ApiUtils;

public class UserService {
    public static ApiResponse getMyChats() {
        ApiRequest request = ApiRequest.builder()
            .method("GET")
            .headers(Authentication.getUser().getId())
            .url("/chats/mychats")
            .build();

        return ApiUtils.handleRequest(request);
    }

    public static ApiResponse getOnlineUsers() {
        ApiRequest request = ApiRequest.builder()
            .method("GET")
            .url("/users/online")
            .headers(Authentication.getUser().getId())
            .build();

        return ApiUtils.handleRequest(request);
    }

    public static ApiResponse getAllUsers() {
        ApiRequest request = ApiRequest.builder()
            .method("GET")
            .url("/users")
            .headers(Authentication.getUser().getId())
            .build();

        return ApiUtils.handleRequest(request);
    }
}
