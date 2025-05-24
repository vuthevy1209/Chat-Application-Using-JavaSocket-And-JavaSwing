package dto.request;

import java.io.Serializable;
import java.util.Objects;

public class MessageRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id; 
    private String senderId;
    private String chatId;
    private String content;
    private String imagePath;
    private String attachmentPath;
    private String messageType;
    private String requestType; // For action differentiation (send, edit, delete, etc.)

    private MessageRequest(Builder builder) {
        this.id = builder.id;
        this.senderId = builder.senderId;
        this.chatId = builder.chatId;
        this.content = builder.content;
        this.imagePath = builder.imagePath;
        this.attachmentPath = builder.attachmentPath;
        this.messageType = builder.messageType;
        this.requestType = builder.requestType;
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
        private String requestType;

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

        public Builder requestType(String requestType) {
            this.requestType = requestType;
            return this;
        }

        public MessageRequest build() {
            return new MessageRequest(this);
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

    public String getRequestType() {
        return requestType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageRequest that = (MessageRequest) o;
        return Objects.equals(senderId, that.senderId) &&
               Objects.equals(chatId, that.chatId) &&
               Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(senderId, chatId, content);
    }

    @Override
    public String toString() {
        return "MessageRequest{" +
                "senderId='" + senderId + '\'' +
                ", chatId='" + chatId + '\'' +
                ", content='" + content + '\'' +
                ", messageType='" + messageType + '\'' +
                ", requestType='" + requestType + '\'' +
                '}';
    }
}
