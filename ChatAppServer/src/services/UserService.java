package services;

import dto.request.RegisterRequest;
import dto.response.UserResponse;

public interface UserService {
    public boolean registerUser(RegisterRequest userRequest);

    public boolean updateUser(RegisterRequest userRequest);

    public boolean deleteUser(String id);

    public UserResponse getUserById(String id);

    public UserResponse getUserByUsername(String username);

    public UserResponse login(String username, String password);

    public boolean logout();
}
