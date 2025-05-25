package repository;

import dto.request.ChatRequest;
import models.Chat;

public interface ChatRepository {
    public boolean existsById(String id);

    public boolean existsByName(String name);

    public boolean save(ChatRequest chatRequest);

    public boolean update(ChatRequest chatRequest);

    public boolean delete(String id);

    public Chat findById(String id);
}
