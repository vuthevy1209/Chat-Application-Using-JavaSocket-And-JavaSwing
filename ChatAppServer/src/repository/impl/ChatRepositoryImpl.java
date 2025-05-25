package repository.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import models.Chat;
import dto.request.ChatRequest;
import dto.response.ChatResponse;
import repository.ChatRepository;
import java.util.UUID;

import utils.ConnectionUtil;

public class ChatRepositoryImpl implements ChatRepository {
    @Override
    public boolean existsById(String id) {
        try (Connection connection = ConnectionUtil.getConnection();
             Statement statement = connection.createStatement()) {

            String sql = "SELECT * FROM chats WHERE id = '" + id + "'";
            ResultSet resultSet = statement.executeQuery(sql);

            return resultSet.next(); 
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean existsByName(String name) {
        try (Connection connection = ConnectionUtil.getConnection();
             Statement statement = connection.createStatement()) {

            String sql = "SELECT * FROM chats WHERE name = '" + name + "'";
            ResultSet resultSet = statement.executeQuery(sql);

            return resultSet.next(); 
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean save(ChatRequest chatRequest) {

        try (Connection connection = ConnectionUtil.getConnection();
            Statement statement = connection.createStatement()) {

            String sql = "INSERT INTO chats (id, name, is_group, created_at) " +
                    "VALUES ('" + chatRequest.getId() + "', '" + chatRequest.getName() + "', " + chatRequest.isGroup() + ", NOW())";
            statement.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(ChatRequest chatRequest) {
        try (Connection connection = ConnectionUtil.getConnection();
            Statement statement = connection.createStatement()) {

            String sql = "UPDATE chats SET name = '" + chatRequest.getName() + "', is_group = " + chatRequest.isGroup() +
                    " WHERE id = '" + chatRequest.getId() + "'";
            statement.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        try (Connection connection = ConnectionUtil.getConnection();
            Statement statement = connection.createStatement()) {

            String sql = "DELETE FROM chats WHERE id = '" + id + "'";
            statement.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Chat findById(String id) {
        try (Connection connection = ConnectionUtil.getConnection();
            Statement statement = connection.createStatement()) {

            String sql = "SELECT * FROM chats WHERE id = '" + id + "'";
            var resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                return Chat.builder()
                        .id(resultSet.getString("id"))
                        .name(resultSet.getString("name"))
                        .isGroup(resultSet.getBoolean("is_group"))
                        .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                        .updatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime())
                        .build();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }
}
