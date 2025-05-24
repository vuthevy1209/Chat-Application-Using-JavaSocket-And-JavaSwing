package repository;

import dto.request.UserRequest;
import dto.response.UserResponse;

public interface UserRepository {
    public boolean existsById(String id);

    public boolean existsByUsername(String username);

    public boolean existsByEmail(String email);

    public UserResponse findById(String id);

    public UserResponse findByUsername(String username);

    public boolean save(UserRequest user);

    public boolean update(UserRequest user);

    public boolean delete(String id);

    public boolean login(String username, String password);

    public boolean logout(String username);

}
