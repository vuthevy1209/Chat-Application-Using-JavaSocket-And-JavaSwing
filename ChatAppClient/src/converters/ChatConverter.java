package converters;

import dto.response.ChatResponse;
import models.Chat;
import models.User;
import java.util.List;
import java.util.ArrayList;
import dto.response.UserResponse;
import dto.response.MessageResponse;
import models.Message;

public class ChatConverter {
    public static Chat converterToChat(ChatResponse chatResponse) {
        if (chatResponse == null) {
            return null;
        }

        List<User> participants = new ArrayList<>();
        for (UserResponse userResponse : chatResponse.getParticipants()) {
            participants.add(UserConverter.converterToUser(userResponse));
        }

        List<Message> messages = new ArrayList<>();
        for (MessageResponse messageResponse : chatResponse.getMessages()) {
            messages.add(MessageConverter.converterToMessage(messageResponse));
        }

        Message lastMessage = chatResponse.getLastMessage() != null 
            ? MessageConverter.converterToMessage(chatResponse.getLastMessage()) 
            : null;

        return Chat.builder()
                .id(chatResponse.getId())
                .name(chatResponse.getName())
                .isGroup(chatResponse.isGroup())
                .participants(participants)
                .messages(messages)
                .lastMessage(lastMessage)
                .createdAt(chatResponse.getCreatedAt())
                .updatedAt(chatResponse.getUpdatedAt())
                .build();
    }
}
