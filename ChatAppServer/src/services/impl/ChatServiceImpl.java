package services.impl;

import dto.request.ChatRequest;
import dto.response.ChatResponse;
import repository.ChatParticipantRepository;
import repository.ChatRepository;
import repository.MessageRepository;
import repository.UserRepository;
import repository.impl.ChatRepositoryImpl;
import repository.impl.MessageRepositoryImpl;
import repository.impl.UserRepositoryImpl;
import dto.response.UserResponse;
import models.Chat;
import models.ChatParticipant;
import dto.response.MessageResponse;
import repository.impl.ChatParticipantRepositoryImpl;
import services.ChatService;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository = new ChatRepositoryImpl();
    private final MessageRepository messageRepository = new MessageRepositoryImpl();
    private final UserRepository userRepository = new UserRepositoryImpl();
    private final ChatParticipantRepository chatParticipantRepository = new ChatParticipantRepositoryImpl();


    @Override
    public boolean createChat(ChatRequest chatRequest) {
        if (chatRepository.existsByName(chatRequest.getName())) {
            return false;
        }

        try {
            chatRepository.save(chatRequest);

            for (String participantId : chatRequest.getParticipantIds()) {
                ChatParticipant chatParticipant = ChatParticipant.builder()
                        .chatId(chatRequest.getId())
                        .userId(participantId)
                        .build();

                boolean isSaved = chatParticipantRepository.save(chatParticipant);
                if (!isSaved) {
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateChat(ChatRequest chatRequest) {
        if (!chatRepository.existsById(chatRequest.getId())) {
            return false;
        }

        return chatRepository.update(chatRequest);
    }

    @Override
    public boolean deleteChat(String id) {
        if (!chatRepository.existsById(id)) {
            return false;
        }
        return chatRepository.delete(id);
    }

    @Override
    public ChatResponse getChatById(String id) {
        if (!chatRepository.existsById(id)) {
            return null;
        }

        ChatResponse chat = chatRepository.findById(id);

        List<String> participantIds = chatParticipantRepository.findAllByChatId(id);
        List<UserResponse> participants = new ArrayList<>();
        for (String participantId : participantIds) {
            UserResponse user = userRepository.findById(participantId);
            if (user != null) {
                participants.add(user);
            }
        }

        List<MessageResponse> messages = messageRepository.findAllByChatId(id);
        for (MessageResponse message : messages) {
            UserResponse sender = userRepository.findById(message.getSenderId());
            message.setSenderUsername(sender.getUsername());
        }

        MessageResponse lastMessage = messages.isEmpty() ? null : messages.get(messages.size() - 1);
        int unreadCount = 0;
        for (MessageResponse message : messages) {
            if (!message.isRead()) {
                unreadCount++;
            }
        }

        chat.setParticipants(participants);
        chat.setMessages(messages);
        chat.setLastMessage(lastMessage);
        chat.setUnreadCount(unreadCount);

        return chat;
    }

}
