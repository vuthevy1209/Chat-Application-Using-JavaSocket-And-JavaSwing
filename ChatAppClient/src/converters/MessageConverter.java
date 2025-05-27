package converters;

import dto.response.MessageResponse;
import models.Message;

public class MessageConverter {
    public static Message converterToMessage(MessageResponse messageResponse) {
        if (messageResponse == null) {
            return null;
        }

        return Message.builder()
                .id(messageResponse.getId())
                .senderId(messageResponse.getSenderId()) // Assuming senderId is not needed in Message model
                .senderUsername(messageResponse.getSenderUsername())
                .chatId(messageResponse.getChatId())
                .content(messageResponse.getContent())
                .imagePath(messageResponse.getImagePath())
                .attachmentPath(messageResponse.getAttachmentPath())
                .messageType(messageResponse.getMessageType())
                .isRead(messageResponse.isRead())
                .createdAt(messageResponse.getCreatedAt())
                .updatedAt(messageResponse.getUpdatedAt())
                .build();
    }
}
