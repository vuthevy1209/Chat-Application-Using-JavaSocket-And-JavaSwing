package services;

import config.Authentication;
import dto.request.ApiRequest;
import dto.request.AuthenticateRequest;
import dto.request.RegisterRequest;
import dto.response.ApiResponse;
import utils.ApiUtils;

public class AuthService {

    public static ApiResponse login(String username, String password) {
        ApiRequest request = ApiRequest.builder()
                .method("POST")
                .url("/auth/login")
                .payload(AuthenticateRequest.builder()
                            .username(username)
                            .password(password)
                            .build())
                .build();

        return ApiUtils.handleRequest(request);
    }

    public static ApiResponse register(RegisterRequest request) {
        ApiRequest apiRequest = ApiRequest.builder()
                .method("POST")
                .url("/auth/register")
                .payload(request)
                .build();

        return ApiUtils.handleRequest(apiRequest);
    }

    public static ApiResponse logout() {
        ApiRequest request = ApiRequest.builder()
            .method("POST")
            .url("/auth/logout")
            .headers(Authentication.getUser().getId())
            .build();

        return ApiUtils.handleRequest(request);
    }
}
