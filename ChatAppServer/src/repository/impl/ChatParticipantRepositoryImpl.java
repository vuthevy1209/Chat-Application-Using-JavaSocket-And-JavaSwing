package repository.impl;

import repository.ChatParticipantRepository;

import utils.ConnectionUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import models.ChatParticipant;
import java.util.ArrayList;
import java.util.List;

public class ChatParticipantRepositoryImpl implements ChatParticipantRepository {
    @Override
    public boolean save(ChatParticipant chatParticipant) {
        try (var connection = ConnectionUtil.getConnection();
             var statement = connection.createStatement()) {

            String sql = "INSERT INTO chat_participants (chat_id, user_id, joined_at) VALUES ('" + chatParticipant.getChatId() + "', '" + chatParticipant.getUserId() + "', NOW())";
            statement.executeUpdate(sql);
            
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<String> findAllByChatId(String chatId) {
        try (var connection = ConnectionUtil.getConnection();
             var statement = connection.createStatement()) {

            String sql = "SELECT user_id FROM chat_participants WHERE chat_id = '" + chatId + "'";
            var resultSet = statement.executeQuery(sql);
            List<String> userIds = new ArrayList<>();

            while (resultSet.next()) {
                userIds.add(resultSet.getString("user_id"));
            }
            return userIds;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<String> findAllByUserId(String userId) {
        try (Connection connection = ConnectionUtil.getConnection();
             Statement statement = connection.createStatement()) {

            String sql = "SELECT DISTINCT chat_id FROM chat_participants WHERE user_id = '" + userId + "'";
            ResultSet resultSet = statement.executeQuery(sql);
            List<String> chatIds = new ArrayList<>();

            while (resultSet.next()) {
                chatIds.add(resultSet.getString("chat_id"));
            }
            return chatIds;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean isParticipant(String userId, String chatId) {
        try (Connection connection = ConnectionUtil.getConnection();
             Statement statement = connection.createStatement()) {

            String sql = "SELECT * FROM chat_participants WHERE user_id = '" + userId + "' AND chat_id = '" + chatId + "'";
            ResultSet resultSet = statement.executeQuery(sql);

            return resultSet.next(); 
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
