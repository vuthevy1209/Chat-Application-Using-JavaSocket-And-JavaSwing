package controllers;

import services.UserService;
import services.impl.UserServiceImpl;
import dto.response.ApiResponse;

import dto.request.AuthenticateRequest;

public class AuthController {
    private final UserService userService;
    
    public AuthController() {
        this.userService = new UserServiceImpl();
    }

    public ApiResponse handleLogin(Object payload) {
        try {
            AuthenticateRequest authRequest = (AuthenticateRequest) payload;
            boolean success = userService.login(authRequest.getUsername(), authRequest.getPassword());
            if (success) {
                return ApiResponse.builder()
                    .code("200")
                    .message("Login successfully")
                    .build();
            } else {
                return ApiResponse.builder()
                    .code("401")
                    .message("Invalid username or password")
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
