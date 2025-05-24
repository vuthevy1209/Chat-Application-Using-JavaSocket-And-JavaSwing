package repository;

import dto.request.ChatRequest;
import dto.response.ChatResponse;

public interface ChatRepository {
    public boolean existsById(String id);

    public boolean existsByName(String name);

    public boolean save(ChatRequest chatRequest);

    public boolean update(ChatRequest chatRequest);

    public boolean delete(String id);

    public ChatResponse findById(String id);
}
