package dto.response;

import java.io.Serializable;
import java.util.Objects;

public class UserResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String username;
    private String email;
    private String avatarPath;

    private UserResponse(Builder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.email = builder.email;
        this.avatarPath = builder.avatarPath;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String username;
        private String email;
        private String avatarPath;

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

        public Builder avatarPath(String avatarPath) {
            this.avatarPath = avatarPath;
            return this;
        }

        public UserResponse build() {
            return new UserResponse(this);
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

    public String getAvatarPath() {
        return avatarPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserResponse that = (UserResponse) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", avatarPath='" + avatarPath + '\'' +
                '}';
    }
}
