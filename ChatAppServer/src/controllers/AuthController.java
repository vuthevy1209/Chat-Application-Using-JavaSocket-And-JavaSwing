package controllers;

import services.UserService;
import services.impl.UserServiceImpl;
import dto.response.ApiResponse;
import dto.response.UserResponse;
import models.User;

import java.util.ArrayList;
import java.util.List;

import config.Authentication;
import config.UserOnlineList;
import dto.request.AuthenticateRequest;
import dto.request.RegisterRequest;

import converters.UserConverter;

public class AuthController {
    private final UserService userService;
    
    public AuthController() {
        this.userService = new UserServiceImpl();
    }

    public ApiResponse handleLogin(Object payload) {
        try {
            AuthenticateRequest authRequest = (AuthenticateRequest) payload;
            UserResponse userResponse = userService.login(authRequest.getUsername(), authRequest.getPassword());
            if (userResponse != null) {
                return ApiResponse.builder()
                    .code("200")
                    .message("Login successfully")
                    .data(userResponse)
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

    public ApiResponse handleLogout() {
        try {
            userService.logout();
            return ApiResponse.builder()
                .code("200")
                .message("Logout successfully")
                .build();
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.builder()
                .code("500")
                .message("Internal server error")
                .build();
        }
    }

    public ApiResponse handleRegister(Object payload) {
        try {
            RegisterRequest authRequest = (RegisterRequest) payload;

            boolean success = userService.registerUser(authRequest);
            if (success) {
                return ApiResponse.builder()
                    .code("200")
                    .message("User registered successfully")
                    .build();
            } else {
                return ApiResponse.builder()
                    .code("400")
                    .message("User registration failed")
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

    public ApiResponse getAllUsersOnline() {
        try {
            List<UserResponse> userResponses = new ArrayList<>();
            for (User user : UserOnlineList.getUserOnlines()) {
                UserResponse userResponse = UserConverter.converterToUserResponse(user);
                if (userResponse != null && !user.getId().equals(Authentication.getUserId())) {
                    userResponses.add(userResponse);
                }
            }

            return ApiResponse.builder()
                .code("200")
                .message("Users retrieved successfully")
                .data(userResponses)
                .build();
                
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.builder()
                .code("500")
                .message("Internal server error")
                .build();
        }
    }

    public ApiResponse getAllUsers() {
        try {
            List<UserResponse> userResponses = userService.getAllUsers();
            return ApiResponse.builder()
                .code("200")
                .message("Users retrieved successfully")
                .data(userResponses)
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
