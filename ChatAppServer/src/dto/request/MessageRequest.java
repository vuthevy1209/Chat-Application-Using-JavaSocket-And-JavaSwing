package dto.request;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MessageRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id; 
    private String senderId;
    private String senderUsername;
    private String chatId;
    private String content;
    private String imagePath;
    private String attachmentPath;
    private String messageType;
    private String requestType; // For action differentiation (send, edit, delete, etc.)
}
