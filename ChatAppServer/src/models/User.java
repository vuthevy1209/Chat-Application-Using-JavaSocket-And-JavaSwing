package models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String username;
    private String email;
    private String password;
    private String avatarPath;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private User(Builder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.email = builder.email;
        this.password = builder.password;
        this.avatarPath = builder.avatarPath;
        this.createdAt = builder.createdAt == null ? LocalDateTime.now() : builder.createdAt;
        this.updatedAt = builder.updatedAt == null ? LocalDateTime.now() : builder.updatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String username;
        private String email;
        private String password;
        private String avatarPath;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder avatarPath(String avatarPath) {
            this.avatarPath = avatarPath;
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

        public User build() {
            return new User(this);
        }
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getAvatarPath() {
        return avatarPath;
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
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
