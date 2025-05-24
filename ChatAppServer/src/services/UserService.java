package services;

import dto.request.UserRequest;
import dto.response.UserResponse;

public interface UserService {
    public boolean createUser(UserRequest userRequest);

    public boolean updateUser(UserRequest userRequest);

    public boolean deleteUser(String id);

    public UserResponse getUserById(String id);

    public UserResponse getUserByUsername(String username);

    public boolean login(String username, String password);
}
