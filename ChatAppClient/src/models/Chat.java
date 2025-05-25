package models;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Setter
@Getter
@Builder
public class Chat implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private boolean isGroup;
    private List<User> participants;
    private List<Message> messages;
    private Message lastMessage; 
    private int unreadCount; 
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
