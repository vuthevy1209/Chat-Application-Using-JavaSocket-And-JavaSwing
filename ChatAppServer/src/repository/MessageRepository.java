package repository;

import dto.response.MessageResponse;
import models.Message;
import dto.request.MessageRequest;
import java.util.List;

public interface MessageRepository {
    public Message save(MessageRequest messageRequest);

    public boolean update(MessageRequest messageRequest);

    public boolean delete(String id);

    public MessageResponse findById(String id);

    public List<MessageResponse> findAllByChatId(String chatId);

    public boolean existsById(String id);

    public boolean isMessageSentBySender(String messageId, String senderId);

    public String findChatIdByMessageID(String messageId);

}
