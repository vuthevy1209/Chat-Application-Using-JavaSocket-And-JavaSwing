package services;

import dto.request.ChatRequest;
import dto.response.ChatResponse;

import java.util.List;


public interface ChatService {
    public boolean createChat(ChatRequest chatRequest);

    public boolean updateChat(ChatRequest chatRequest);

    public boolean deleteChat(String id);

    public ChatResponse getChatById(String id);

    public List<ChatResponse> getAllMyChats();

    public List<String> getAllUserIdsByChatId(String chatId);

    public List<String> getAllParticipantsByChatId(String chatId);

    public String findChatIdByMessageId(String messageId);
}
