package repository.impl;

import dto.request.MessageRequest;
import dto.response.MessageResponse;
import models.Message;
import repository.MessageRepository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import utils.ConnectionUtil;

public class MessageRepositoryImpl implements MessageRepository {

    @Override
    public Message save(MessageRequest messageRequest) {

        try (Connection connection = ConnectionUtil.getConnection();
            Statement statement = connection.createStatement()) {

            // Generate a unique ID for the message
            String generatedId = UUID.randomUUID().toString();
            
            String sql;

            if (messageRequest.getMessageType().equals("TEXT")) {
                sql = "INSERT INTO messages (id, sender_id, chat_id, content, message_type, is_read, created_at) " +
                        "VALUES ('" + generatedId + "', '" + messageRequest.getSenderId() + "', '" + messageRequest.getChatId() + "', '" +
                        messageRequest.getContent() + "', 'TEXT', false, NOW())";
            } else if (messageRequest.getMessageType().equals("IMAGE")) {
                sql = "INSERT INTO messages (id, sender_id, chat_id, content, image_path, message_type, is_read, created_at) " +
                        "VALUES ('" + generatedId + "', '" + messageRequest.getSenderId() + "', '" + messageRequest.getChatId() + "', '" +
                        messageRequest.getContent() + "', '" + messageRequest.getImagePath() + "', 'IMAGE', false, NOW())";
            } else if (messageRequest.getMessageType().equals("FILE")) {
                sql = "INSERT INTO messages (id, sender_id, chat_id, content, attachment_path, message_type, is_read, created_at) " +
                        "VALUES ('" + generatedId + "', '" + messageRequest.getSenderId() + "', '" + messageRequest.getChatId() + "', '" +
                        messageRequest.getContent() + "', '" + messageRequest.getAttachmentPath() + "', 'FILE', false, NOW())";
            } else {
                throw new IllegalArgumentException("Invalid request type: " + messageRequest.getMessageType());
            }

            statement.executeUpdate(sql);
            
            return Message.builder()
                    .id(generatedId)
                    .senderId(messageRequest.getSenderId())
                    .senderUsername(messageRequest.getSenderUsername())
                    .chatId(messageRequest.getChatId())
                    .content(messageRequest.getContent())
                    .imagePath(messageRequest.getImagePath())
                    .attachmentPath(messageRequest.getAttachmentPath())
                    .messageType(messageRequest.getMessageType())
                    .isRead(false)
                    .createdAt(java.time.LocalDateTime.now())
                    .build();
        
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean update(MessageRequest messageRequest) {
        try (Connection connection = ConnectionUtil.getConnection();
            Statement statement = connection.createStatement()) {

            String imagePath = messageRequest.getImagePath() == null ? "NULL" : "'" + messageRequest.getImagePath() + "'";
            String attachmentPath = messageRequest.getAttachmentPath() == null ? "NULL" : "'" + messageRequest.getAttachmentPath() + "'";
            String messageType = messageRequest.getMessageType() == null ? "NULL" : "'" + messageRequest.getMessageType() + "'";
            
            String sql = "UPDATE messages SET content = '" + messageRequest.getContent() + 
                    "', image_path = " + imagePath + 
                    ", attachment_path = " + attachmentPath +
                    ", message_type = " + messageType + 
                    " WHERE id = '" + messageRequest.getId() + "'";

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

            String sql = "DELETE FROM messages WHERE id = '" + id + "'";
            statement.executeUpdate(sql);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public MessageResponse findById(String id) {
        try (Connection connection = ConnectionUtil.getConnection();
            Statement statement = connection.createStatement()) {

            String sql = "SELECT * FROM messages WHERE id = '" + id + "'";
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                return MessageResponse.builder()
                        .id(resultSet.getString("id"))
                        .senderId(resultSet.getString("sender_id"))
                        .chatId(resultSet.getString("chat_id"))
                        .content(resultSet.getString("content"))
                        .imagePath(resultSet.getString("image_path"))
                        .attachmentPath(resultSet.getString("attachment_path"))
                        .messageType(resultSet.getString("message_type"))
                        .isRead(resultSet.getBoolean("is_read"))
                        .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    public List<MessageResponse> findAllByChatId(String chatId) {
        try (Connection connection = ConnectionUtil.getConnection();
            Statement statement = connection.createStatement()) {

            String sql = "SELECT * FROM messages WHERE chat_id = '" + chatId + "'";
            ResultSet resultSet = statement.executeQuery(sql);

            List<MessageResponse> messages = new ArrayList<>();
            while (resultSet.next()) {
                messages.add(MessageResponse.builder()
                        .id(resultSet.getString("id"))
                        .senderId(resultSet.getString("sender_id"))
                        .chatId(resultSet.getString("chat_id"))
                        .content(resultSet.getString("content"))
                        .imagePath(resultSet.getString("image_path"))
                        .attachmentPath(resultSet.getString("attachment_path"))
                        .messageType(resultSet.getString("message_type"))
                        .isRead(resultSet.getBoolean("is_read"))
                        .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                        .build());
            }

            // sort messages by created_at in descending order
            messages.sort((m1, m2) -> m2.getCreatedAt().compareTo(m1.getCreatedAt()));
            
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean existsById(String id) {
        try (Connection connection = ConnectionUtil.getConnection();
            Statement statement = connection.createStatement()) {

            String sql = "SELECT * FROM messages WHERE id = '" + id + "'";
            ResultSet resultSet = statement.executeQuery(sql);

            return resultSet.next(); 
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isMessageSentBySender(String messageId, String senderId) {
        try (Connection connection = ConnectionUtil.getConnection();
            Statement statement = connection.createStatement()) {

            String sql = "SELECT * FROM messages WHERE id = '" + messageId + "' AND sender_id = '" + senderId + "'";
            ResultSet resultSet = statement.executeQuery(sql);

            return resultSet.next(); 
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String findChatIdByMessageID(String messageId) {
        try (Connection connection = ConnectionUtil.getConnection();
            Statement statement = connection.createStatement()) {

            String sql = "SELECT chat_id FROM messages WHERE id = '" + messageId + "'";
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                return resultSet.getString("chat_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
