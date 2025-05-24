package repository;

import models.ChatParticipant;
import java.util.List;

public interface ChatParticipantRepository {
    public boolean save(ChatParticipant chatParticipant);

    public List<String> findAllByChatId(String chatId);

    public boolean isParticipant(String userId, String chatId);
}
