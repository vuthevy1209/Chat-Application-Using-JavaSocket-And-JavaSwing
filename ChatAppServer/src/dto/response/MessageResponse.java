package dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class MessageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String senderId;
    private String senderUsername; 
    private String chatId;
    private String content;
    private String imagePath;
    private String attachmentPath;
    private String messageType;
    private boolean isRead;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private MessageResponse(Builder builder) {
        this.id = builder.id;
        this.senderId = builder.senderId;
        this.senderUsername = builder.senderUsername;
        this.chatId = builder.chatId;
        this.content = builder.content;
        this.imagePath = builder.imagePath;
        this.attachmentPath = builder.attachmentPath;
        this.messageType = builder.messageType;
        this.isRead = builder.isRead;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String senderId;
        private String senderUsername;
        private String chatId;
        private String content;
        private String imagePath;
        private String attachmentPath;
        private String messageType;
        private boolean isRead;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder senderId(String senderId) {
            this.senderId = senderId;
            return this;
        }

        public Builder senderUsername(String senderUsername) {
            this.senderUsername = senderUsername;
            return this;
        }

        public Builder chatId(String chatId) {
            this.chatId = chatId;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder imagePath(String imagePath) {
            this.imagePath = imagePath;
            return this;
        }

        public Builder attachmentPath(String attachmentPath) {
            this.attachmentPath = attachmentPath;
            return this;
        }

        public Builder messageType(String messageType) {
            this.messageType = messageType;
            return this;
        }

        public Builder isRead(boolean isRead) {
            this.isRead = isRead;
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

        public MessageResponse build() {
            return new MessageResponse(this);
        }
    }


    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderId() {
        return this.senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderUsername() {
        return this.senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getChatId() {
        return this.chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getAttachmentPath() {
        return this.attachmentPath;
    }

    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    public String getMessageType() {
        return this.messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public boolean isRead() {
        return this.isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
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
        MessageResponse that = (MessageResponse) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "MessageResponse{" +
                "id='" + id + '\'' +
                ", senderId='" + senderId + '\'' +
                ", senderUsername='" + senderUsername + '\'' +
                ", chatId='" + chatId + '\'' +
                ", content='" + content + '\'' +
                ", messageType='" + messageType + '\'' +
                ", isRead=" + isRead +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
