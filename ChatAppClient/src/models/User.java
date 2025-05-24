package models;

import java.awt.Color;
import java.util.Objects;

public class User {
    private String id;
    private String username;
    private String email;
    private String password;
    private boolean online;
    private String avatarPath;
    private Color avatarColor;
    
    public User(String id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.online = false;
        this.avatarPath = null;
        this.avatarColor = generateAvatarColor();
    }
    
    private Color generateAvatarColor() {
        // Generate a random pastel color based on username
        int hash = Objects.hash(username);
        int r = (hash & 0xFF0000) >> 16;
        int g = (hash & 0x00FF00) >> 8;
        int b = hash & 0x0000FF;
        
        // Make it pastel by mixing with white
        r = (r + 255) / 2;
        g = (g + 255) / 2;
        b = (b + 255) / 2;
        
        return new Color(r, g, b);
    }
    
    // Getters and setters
    public String getId() {
        return id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean isOnline() {
        return online;
    }
    
    public void setOnline(boolean online) {
        this.online = online;
    }
    
    public String getAvatarPath() {
        return avatarPath;
    }
    
    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }
    
    public Color getAvatarColor() {
        return avatarColor;
    }
    
    public String getInitials() {
        if (username == null || username.isEmpty()) {
            return "?";
        }
        return username.substring(0, 1).toUpperCase();
    }
}
