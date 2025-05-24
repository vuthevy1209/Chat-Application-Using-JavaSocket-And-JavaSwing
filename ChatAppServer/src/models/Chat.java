package models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Chat implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private boolean isGroup;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Chat(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.isGroup = builder.isGroup;
        this.createdAt = builder.createdAt == null ? LocalDateTime.now() : builder.createdAt;
        this.updatedAt = builder.updatedAt == null ? LocalDateTime.now() : builder.updatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String name;
        private boolean isGroup;
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

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Chat build() {
            return new Chat(this);
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isGroup() {
        return isGroup;
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
        Chat chat = (Chat) o;
        return Objects.equals(id, chat.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
