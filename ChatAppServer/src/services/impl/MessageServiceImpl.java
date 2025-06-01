package services.impl;

import dto.request.MessageRequest;
import dto.response.ApiResponse;
import dto.response.MessageResponse;
import models.Message;
import services.MessageService;
import repository.MessageRepository;
import repository.UserRepository;
import repository.ChatRepository;
import repository.ChatParticipantRepository;
import repository.impl.UserRepositoryImpl;
import repository.impl.ChatRepositoryImpl;
import repository.impl.MessageRepositoryImpl;
import repository.impl.ChatParticipantRepositoryImpl;

public class MessageServiceImpl implements MessageService {


    private final MessageRepository messageRepository  = new MessageRepositoryImpl();
    private final UserRepository userRepository = new UserRepositoryImpl();
    private final ChatRepository chatRepository = new ChatRepositoryImpl();
    private final ChatParticipantRepository chatParticipantRepository = new ChatParticipantRepositoryImpl();


    @Override
    public MessageResponse sendMessage(MessageRequest messageRequest) {
        String sendId = messageRequest.getSenderId();
        String chatId = messageRequest.getChatId();

        // Check if the sender exists
        if (!userRepository.existsById(sendId)) {
            return null;
        }

        // Check if the chat exists
        if (!chatRepository.existsById(chatId)) {
            return null;
        }

        // Check if the sender is a participant of the chat
        if (!chatParticipantRepository.isParticipant(sendId, chatId)) {
            return null;
        }

        Message message = messageRepository.save(messageRequest);

        return message != null ? 
            MessageResponse.builder()
                .id(message.getId())
                .senderId(message.getSenderId())
                .senderUsername(message.getSenderUsername())
                .chatId(message.getChatId())
                .content(message.getContent())
                .imagePath(message.getImagePath())
                .attachmentPath(message.getAttachmentPath())
                .messageType(message.getMessageType())
                .isRead(message.isRead())
                .createdAt(message.getCreatedAt())
                .build() 
            : null;


    }

    @Override
    public boolean editMessage(MessageRequest messageRequest) {
        // Check if the message exists
        if (!messageRepository.existsById(messageRequest.getId())) {
            return false;
        }

        // Check if the sender exists
        if (!userRepository.existsById(messageRequest.getSenderId())) {
            return false;
        }

        // Check if the chat exists
        if (!chatRepository.existsById(messageRequest.getChatId())) {
            return false;
        }

        // Check if the sender is a participant of the chat
        if (!chatParticipantRepository.isParticipant(messageRequest.getSenderId(), messageRequest.getChatId())) {
            return false;
        }

        // Check if the message is sent by the sender
        if (!messageRepository.isMessageSentBySender(messageRequest.getId(), messageRequest.getSenderId())) {
            return false;
        }
        
        return messageRepository.update(messageRequest);
    }

    @Override
    public boolean deleteMessage(String messageId) {
        return messageRepository.delete(messageId);
    }

    @Override
    public MessageResponse getMessageById(String messageId) {
        return messageRepository.findById(messageId);
    }

}
