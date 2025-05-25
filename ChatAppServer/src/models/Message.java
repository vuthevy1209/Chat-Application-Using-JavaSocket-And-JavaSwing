package models;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
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
}
