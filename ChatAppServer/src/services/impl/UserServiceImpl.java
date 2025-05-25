package services.impl;

import services.UserService;
import config.Authentication;
import dto.request.RegisterRequest;
import dto.response.UserResponse;
import models.User;
import repository.UserRepository;
import repository.impl.UserRepositoryImpl;


public class UserServiceImpl implements UserService {

    private final UserRepository userRepository = new UserRepositoryImpl();

    @Override
    public boolean registerUser(RegisterRequest userRequest) {
        // Check if the user already exists
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            return false;
        }
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            return false;
        }

        return userRepository.save(userRequest);
    }

    @Override
    public boolean updateUser(RegisterRequest userRequest) {
        if (!userRepository.existsById(userRequest.getId())) {
            return false;
        }

        return userRepository.update(userRequest);
    }

    @Override
    public boolean deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            return false;
        }

        return userRepository.delete(id);
    }

    @Override
    public UserResponse getUserById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserResponse login(String username, String password) {
        return userRepository.login(username, password);
    }

    @Override 
    public boolean logout() {
        try {
            Authentication.getUserOnlines().remove(Authentication.getUser());
            Authentication.setUser(null);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }
    }

}
