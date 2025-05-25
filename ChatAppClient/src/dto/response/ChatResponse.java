package dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
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
}
