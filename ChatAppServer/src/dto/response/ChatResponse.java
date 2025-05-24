package dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class ChatResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private boolean isGroup;
    private List<UserResponse> participants;
    private List<MessageResponse> messages;
    private MessageResponse lastMessage; 
    private int unreadCount; 
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private ChatResponse(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.isGroup = builder.isGroup;
        this.messages = builder.messages;
        this.participants = builder.participants;
        this.lastMessage = builder.lastMessage;
        this.unreadCount = builder.unreadCount;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String name;
        private boolean isGroup;
        private List<MessageResponse> messages; 
        private List<UserResponse> participants;
        private MessageResponse lastMessage;
        private int unreadCount;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder isGroup(boolean isGroup) {
            this.isGroup = isGroup;
            return this;
        }

        public Builder messages(List<MessageResponse> messages) {
            this.messages = messages;
            return this;
        }

        public Builder participants(List<UserResponse> participants) {
            this.participants = participants;
            return this;
        }

        public Builder lastMessage(MessageResponse lastMessage) {
            this.lastMessage = lastMessage;
            return this;
        }

        public Builder unreadCount(int unreadCount) {
            this.unreadCount = unreadCount;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public ChatResponse build() {
            return new ChatResponse(this);
        }
    }


    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIsGroup() {
        return this.isGroup;
    }

    public boolean getIsGroup() {
        return this.isGroup;
    }

    public void setIsGroup(boolean isGroup) {
        this.isGroup = isGroup;
    }

    public List<UserResponse> getParticipants() {
        return this.participants;
    }

    public void setParticipants(List<UserResponse> participants) {
        this.participants = participants;
    }

    public List<MessageResponse> getMessages() {
        return this.messages;
    }

    public void setMessages(List<MessageResponse> messages) {
        this.messages = messages;
    }

    public MessageResponse getLastMessage() {
        return this.lastMessage;
    }

    public void setLastMessage(MessageResponse lastMessage) {
        this.lastMessage = lastMessage;
    }

    public int getUnreadCount() {
        return this.unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatResponse that = (ChatResponse) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ChatResponse{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", isGroup=" + isGroup +
                ", messages=" + (messages != null ? messages.size() : "null") +
                ", participants=" + participants +
                ", lastMessage=" + (lastMessage != null ? lastMessage.getId() : "null") +
                ", unreadCount=" + unreadCount +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
