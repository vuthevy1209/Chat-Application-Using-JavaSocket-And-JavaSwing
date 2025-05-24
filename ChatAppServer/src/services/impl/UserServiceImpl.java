package services.impl;

import services.UserService;

import dto.request.UserRequest;
import dto.response.UserResponse;
import repository.UserRepository;
import repository.impl.UserRepositoryImpl;


public class UserServiceImpl implements UserService {

    private final UserRepository userRepository = new UserRepositoryImpl();

    @Override
    public boolean createUser(UserRequest userRequest) {
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
    public boolean updateUser(UserRequest userRequest) {
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
    public boolean login(String username, String password) {
        return userRepository.login(username, password);
    }

}
