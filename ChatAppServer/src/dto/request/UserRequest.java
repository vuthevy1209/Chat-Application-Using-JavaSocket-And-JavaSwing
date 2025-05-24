package dto.request;

import java.io.Serializable;
import java.util.Objects;

public class UserRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String username;
    private String email;
    private String password;
    private String avatarPath;
    private String requestType;

    private UserRequest(Builder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.email = builder.email;
        this.password = builder.password;
        this.avatarPath = builder.avatarPath;
        this.requestType = builder.requestType;
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
        private String requestType;

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

        public Builder requestType(String requestType) {
            this.requestType = requestType;
            return this;
        }

        public UserRequest build() {
            return new UserRequest(this);
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

    public String getRequestType() {
        return requestType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRequest that = (UserRequest) o;
        return Objects.equals(email, that.email) &&
               Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, username);
    }

    @Override
    public String toString() {
        return "UserRequest{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", requestType='" + requestType + '\'' +
                ", avatarPath='" + avatarPath + '\'' +
                // Not including password in toString for security reasons
                '}';
    }
}
