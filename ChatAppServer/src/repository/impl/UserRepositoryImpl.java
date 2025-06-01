package repository.impl;

import repository.UserRepository;
import utils.ConnectionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import config.Authentication;
import config.UserOnlineList;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import dto.request.RegisterRequest;
import dto.response.UserResponse;
import models.User;

import java.sql.ResultSet;


public class UserRepositoryImpl implements UserRepository {
    @Override
    public boolean existsById(String id) {
        try (Connection connection = ConnectionUtil.getConnection();
             Statement statement = connection.createStatement()) {

            String sql = "SELECT * FROM users WHERE id = '" + id + "'";
            ResultSet resultSet = statement.executeQuery(sql);

            return resultSet.next(); // If a record is found, the ID exists
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        try (Connection connection = ConnectionUtil.getConnection();
             Statement statement = connection.createStatement()) {

            String sql = "SELECT * FROM users WHERE username = '" + username + "'";
            ResultSet resultSet = statement.executeQuery(sql);

            return resultSet.next(); // If a record is found, the username exists
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        try (Connection connection = ConnectionUtil.getConnection();
             Statement statement = connection.createStatement()) {

            String sql = "SELECT * FROM users WHERE email = '" + email + "'";
            ResultSet resultSet = statement.executeQuery(sql);

            return resultSet.next(); // If a record is found, the email exists
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override 
    public UserResponse findById(String id) {
        try (Connection connection = ConnectionUtil.getConnection();
             Statement statement = connection.createStatement()) {

            String sql = "SELECT * FROM users WHERE id = '" + id + "'";
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                return UserResponse.builder()
                        .id(resultSet.getString("id"))
                        .username(resultSet.getString("username"))
                        .email(resultSet.getString("email"))
                        .avatarPath(resultSet.getString("avatar_path"))
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    @Override
    public UserResponse findByUsername(String username) {
        try (Connection connection = ConnectionUtil.getConnection();
             Statement statement = connection.createStatement()) {

            String sql = "SELECT * FROM users WHERE username = '" + username + "'";
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                return UserResponse.builder()
                        .id(resultSet.getString("id"))
                        .username(resultSet.getString("username"))
                        .email(resultSet.getString("email"))
                        .avatarPath(resultSet.getString("avatar_path"))
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
             Statement statement = connection.createStatement()) {

            String sql = "SELECT * FROM users";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                User user = User.builder()
                        .id(resultSet.getString("id"))
                        .username(resultSet.getString("username"))
                        .email(resultSet.getString("email"))
                        .avatarPath(resultSet.getString("avatar_path"))
                        .build();
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public boolean save(RegisterRequest user) {
        String generatedId = UUID.randomUUID().toString();
        String hashedPassword = String.valueOf(user.getPassword().hashCode());

        try (Connection connection = ConnectionUtil.getConnection();
             Statement statement = connection.createStatement()) {

            String sql = "INSERT INTO users (id, username, email, password, avatar_path, created_at) VALUES ('"
                    + generatedId + "', '"
                    + user.getUsername() + "', '"
                    + user.getEmail() + "', '"
                    + hashedPassword + "', '"
                    + user.getAvatarPath() + "', NOW())";

            int rowsAffected = statement.executeUpdate(sql);
            return rowsAffected > 0; // Return true if the user was successfully created
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(RegisterRequest user) {
        try (Connection connection = ConnectionUtil.getConnection();
             Statement statement = connection.createStatement()) {

            String sql = "UPDATE users SET username = '" + user.getUsername() + "', email = '" + user.getEmail() + "', avatar_path = '" + user.getAvatarPath() + "', updated_at = NOW() WHERE id = '" + user.getId() + "'";
            int rowsAffected = statement.executeUpdate(sql);
            return rowsAffected > 0; // Return true if the user was successfully updated
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        try (Connection connection = ConnectionUtil.getConnection();
             Statement statement = connection.createStatement()) {

            String sql = "DELETE FROM users WHERE id = '" + id + "'";

            int rowsAffected = statement.executeUpdate(sql);
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false; 
        }
    }

    @Override
    public UserResponse login(String username, String password) {

        try (Connection connection = ConnectionUtil.getConnection();
             Statement statement = connection.createStatement()) {

            String hashedPassword = String.valueOf(password.hashCode());
            String sql = "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + hashedPassword + "'";
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                User user = User.builder()
                        .id(resultSet.getString("id"))
                        .username(resultSet.getString("username"))
                        .email(resultSet.getString("email"))
                        .avatarPath(resultSet.getString("avatar_path"))
                        .build();

                // Set the authenticated user in the Authentication context
                Authentication.setUserId(user.getId());
                UserOnlineList.addUserOnline(user);

                return UserResponse.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .avatarPath(user.getAvatarPath())
                        .build();
            } else {
                return null;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
