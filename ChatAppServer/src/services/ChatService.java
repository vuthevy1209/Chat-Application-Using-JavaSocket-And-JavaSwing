package services;

import dto.request.ChatRequest;
import dto.response.ChatResponse;

public interface ChatService {
    public boolean createChat(ChatRequest chatRequest);

    public boolean updateChat(ChatRequest chatRequest);

    public boolean deleteChat(String id);

    public ChatResponse getChatById(String id);
}
