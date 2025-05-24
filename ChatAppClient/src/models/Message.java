package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
    private String id;
    private String senderId;
    private String content;
    private LocalDateTime timestamp;
    private boolean read;
    private String attachmentPath;
    
    public Message(String id, String senderId, String content) {
        this.id = id;
        this.senderId = senderId;
        this.content = content;
        this.timestamp = LocalDateTime.now();
        this.read = false;
        this.attachmentPath = null;
    }
    
    // Getters and setters
    public String getId() {
        return id;
    }
    
    public String getSenderId() {
        return senderId;
    }
    
    
    public String getContent() {
        return content;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public String getFormattedTime() {
        return timestamp.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
    
    public boolean isRead() {
        return read;
    }
    
    public void setRead(boolean read) {
        this.read = read;
    }
    
    public String getAttachmentPath() {
        return attachmentPath;
    }
    
    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }
}
