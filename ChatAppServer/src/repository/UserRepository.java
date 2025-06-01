package repository;

import java.util.List;

import dto.request.RegisterRequest;
import dto.response.UserResponse;
import models.User;

public interface UserRepository {
    public boolean existsById(String id);

    public boolean existsByUsername(String username);

    public boolean existsByEmail(String email);

    public UserResponse findById(String id);

    public UserResponse findByUsername(String username);

    List<User> findAll();

    public boolean save(RegisterRequest user);

    public boolean update(RegisterRequest user);

    public boolean delete(String id);

    public UserResponse login(String username, String password);

}
