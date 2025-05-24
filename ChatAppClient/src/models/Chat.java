// filepath: /Users/vyvuthe/WorkSpace/TESTAPP/ChatAppClient/src/models/Chat.java
package models;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    private String id;
    private String name;
    private boolean group;
    private List<String> participantIds;
    private List<Message> messages;
    
    public Chat(String id, String name, boolean group) {
        this.id = id;
        this.name = name;
        this.group = group;
        this.participantIds = new ArrayList<>();
        this.messages = new ArrayList<>();
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public boolean isGroup() {
        return group;
    }
    
    public List<String> getParticipantIds() {
        return participantIds;
    }
    
    public void addParticipant(String userId) {
        if (!participantIds.contains(userId)) {
            participantIds.add(userId);
        }
    }
    
    public void removeParticipant(String userId) {
        participantIds.remove(userId);
    }
    
    public List<Message> getMessages() {
        return messages;
    }
    
    public void addMessage(Message message) {
        messages.add(message);
    }
    
    public Message getLastMessage() {
        if (messages.isEmpty()) {
            return null;
        }
        return messages.get(messages.size() - 1);
    }
}
