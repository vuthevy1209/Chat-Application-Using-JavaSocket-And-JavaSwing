package dto.request;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class ChatRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id; // Optional, used for update and delete operations
    private String name;
    private boolean isGroup;
    private List<String> participantIds;
    private String requestType; // For action differentiation (create, update, delete, join, leave, etc.)

    private ChatRequest(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.isGroup = builder.isGroup;
        this.participantIds = builder.participantIds;
        this.requestType = builder.requestType;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id; // Optional, used for update and delete operations
        private String name;
        private boolean isGroup;
        private List<String> participantIds;
        private String requestType;

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

        public Builder participantIds(List<String> participantIds) {
            this.participantIds = participantIds;
            return this;
        }

        public Builder requestType(String requestType) {
            this.requestType = requestType;
            return this;
        }

        public ChatRequest build() {
            return new ChatRequest(this);
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

    public List<String> getParticipantIds() {
        return participantIds;
    }

    public String getRequestType() {
        return requestType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatRequest that = (ChatRequest) o;
        return isGroup == that.isGroup &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, isGroup);
    }

    @Override
    public String toString() {
        return "ChatRequest{" +
                "name='" + name + '\'' +
                ", isGroup=" + isGroup +
                ", participantIds=" + participantIds +
                ", requestType='" + requestType + '\'' +
                '}';
    }
}
