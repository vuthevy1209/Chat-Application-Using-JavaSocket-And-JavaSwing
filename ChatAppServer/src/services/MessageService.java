package services;

import dto.request.MessageRequest;
import dto.response.MessageResponse;

public interface MessageService {
    public boolean sendMessage(MessageRequest messageRequest);

    public boolean editMessage(MessageRequest messageRequest);

    public boolean deleteMessage(String messageId);

    public MessageResponse getMessageById(String messageId);

}
