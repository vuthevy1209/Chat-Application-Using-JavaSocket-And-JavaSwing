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

import config.Authentication;
import config.UserOnlineList;

public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository = new ChatRepositoryImpl();
    private final MessageRepository messageRepository = new MessageRepositoryImpl();
    private final UserRepository userRepository = new UserRepositoryImpl();
    private final ChatParticipantRepository chatParticipantRepository = new ChatParticipantRepositoryImpl();


    @Override
    public boolean createChat(ChatRequest chatRequest) {
        try {
            String uniqueId = null;
            List<String> participantIds = new ArrayList<>(chatRequest.getParticipantIds());

            if (!chatRequest.isGroup()) {
                participantIds.sort(String::compareTo);
                uniqueId = String.join("-", participantIds);

                if (chatRepository.existsById(uniqueId)) {
                    return false; // Chat already exists
                }
            } else {
                uniqueId = UUID.randomUUID().toString();
            }

            chatRequest.setId(uniqueId);
            chatRepository.save(chatRequest);

            for (String participantId : participantIds) {
                ChatParticipant chatParticipant = ChatParticipant.builder()
                        .chatId(uniqueId)
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

        Chat chat = chatRepository.findById(id);
        return convertToChatResponse(chat);
    }

    @Override
    public List<ChatResponse> getAllMyChats() {
        String userId = Authentication.getUserId();
        List<String> chatIds = chatParticipantRepository.findAllByUserId(userId);

        List<ChatResponse> chats = new ArrayList<>();

        for (String chatId : chatIds) {
            Chat chat = chatRepository.findById(chatId);
            if (chat != null) {
                ChatResponse chatResponse = convertToChatResponse(chat);
                if (chatResponse != null) {
                    chats.add(chatResponse);
                }
            }
        }

        return chats;
    }

    private ChatResponse convertToChatResponse(Chat chat) {
        List<String> participantIds = chatParticipantRepository.findAllByChatId(chat.getId());
        List<UserResponse> participants = new ArrayList<>();
        for (String participantId : participantIds) {
            UserResponse user = userRepository.findById(participantId);
            if (user != null) {
                participants.add(user);
            }
        }

        List<MessageResponse> messages = messageRepository.findAllByChatId(chat.getId());
        for (MessageResponse message : messages) {
            UserResponse sender = userRepository.findById(message.getSenderId());
            message.setSenderUsername(sender.getUsername());
        }

        MessageResponse lastMessage = messages.isEmpty() ? null : messages.get(0);
        int unreadCount = 0;
        for (MessageResponse message : messages) {
            if (!message.isRead()) {
                unreadCount++;
            }
        }

        return ChatResponse.builder()
                .id(chat.getId())
                .name(chat.getName())
                .isGroup(chat.isGroup())
                .createdAt(chat.getCreatedAt())
                .updatedAt(chat.getUpdatedAt())
                .participants(participants)
                .messages(messages)
                .lastMessage(lastMessage)
                .unreadCount(unreadCount)
                .build();
    }

    @Override
    public List<String> getAllUserIdsByChatId(String chatId) {
        if (!chatRepository.existsById(chatId)) {
            return new ArrayList<>();
        }
        
        List<String> participantIds = chatParticipantRepository.findAllByChatId(chatId);

        return participantIds;
    }

    @Override
    public List<String> getAllParticipantsByChatId(String chatId) {
        if (!chatRepository.existsById(chatId)) {
            return new ArrayList<>();
        }

        return chatParticipantRepository.findAllByChatId(chatId);
    }

    @Override
    public String findChatIdByMessageId(String messageId) {
        if (!messageRepository.existsById(messageId)) {
            return null;
        }
        
        return messageRepository.findChatIdByMessageID(messageId);
    }
}
