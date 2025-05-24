package models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String senderId;
    private String chatId;
    private String content;
    private String imagePath;
    private String attachmentPath;
    private String messageType;
    private boolean isRead;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Message(Builder builder) {
        this.id = builder.id;
        this.senderId = builder.senderId;
        this.chatId = builder.chatId;
        this.content = builder.content;
        this.imagePath = builder.imagePath;
        this.attachmentPath = builder.attachmentPath;
        this.messageType = builder.messageType;
        this.isRead = builder.isRead;
        this.createdAt = builder.createdAt == null ? LocalDateTime.now() : builder.createdAt;
        this.updatedAt = builder.updatedAt == null ? LocalDateTime.now() : builder.updatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String senderId;
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

        public Message build() {
            return new Message(this);
        }
    }

    public String getId() {
        return id;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getChatId() {
        return chatId;
    }

    public String getContent() {
        return content;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getAttachmentPath() {
        return attachmentPath;
    }

    public String getMessageType() {
        return messageType;
    }

    public boolean isRead() {
        return isRead;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(id, message.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
