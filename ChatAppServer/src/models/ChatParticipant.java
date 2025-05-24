package models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class ChatParticipant implements Serializable {
    private static final long serialVersionUID = 1L;

    private String chatId;
    private String userId;
    private LocalDate joinedAt;

    private ChatParticipant(Builder builder) {
        this.chatId = builder.chatId;
        this.userId = builder.userId;
        this.joinedAt = builder.joinedAt == null ? LocalDate.now() : builder.joinedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String chatId;
        private String userId;
        private LocalDate joinedAt;

        public Builder chatId(String chatId) {
            this.chatId = chatId;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder joinedAt(LocalDate joinedAt) {
            this.joinedAt = joinedAt;
            return this;
        }

        public ChatParticipant build() {
            return new ChatParticipant(this);
        }
    }

    public String getChatId() {
        return chatId;
    }

    public String getUserId() {
        return userId;
    }

    public LocalDate getJoinedAt() {
        return joinedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatParticipant that = (ChatParticipant) o;
        return Objects.equals(chatId, that.chatId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, userId);
    }
}

