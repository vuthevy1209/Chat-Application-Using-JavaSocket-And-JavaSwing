package components;

import models.Chat;
import models.User;
import page.ChatPage.ChatRefreshCallback;
import services.FakeDataService;
import utils.IconUtil;
import utils.ThemeUtil;
import components.customs.AvatarPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.UUID;

public class OnlineUserItem extends JPanel {
    private User user;
    private ChatRefreshCallback refreshCallback;
    
    public OnlineUserItem(User user, ChatRefreshCallback refreshCallback) {
        this.user = user;
        this.refreshCallback = refreshCallback;
        
        setLayout(new BorderLayout(10, 0));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Avatar
        AvatarPanel avatarPanel = new AvatarPanel(user, 40);
        add(avatarPanel, BorderLayout.WEST);
        
        // User name
        JLabel nameLabel = new JLabel(user.getUsername());
        nameLabel.setFont(ThemeUtil.NORMAL_FONT);
        add(nameLabel, BorderLayout.CENTER);
        
        // Add button
        JButton addButton = new JButton(IconUtil.getImageIcon("/icon/Chat.png", 40, 40));
        addButton.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                createNewChat();
            }
        });
        add(addButton, BorderLayout.EAST);
    }
    
    private void createNewChat() {
        FakeDataService dataService = FakeDataService.getInstance();
        User currentUser = dataService.getCurrentUser();
        
        if (currentUser != null && user != null) {
            // Check if a chat between these users already exists
            boolean chatExists = false;
            for (Chat chat : dataService.getUserChats()) {
                if (!chat.isGroup() && chat.getParticipantIds().contains(user.getId()) && chat.getParticipantIds().size() == 2) {
                    chatExists = true;
                    break;
                }
            }
            
            // If chat doesn't exist, create a new one
            if (!chatExists) {
                // Try to get ChatService
                services.ChatService chatService = null;
                try {
                    Class<?> appClass = Class.forName("App");
                    java.lang.reflect.Method getChatServiceMethod = appClass.getMethod("getChatService");
                    chatService = (services.ChatService) getChatServiceMethod.invoke(null);
                } catch (Exception ex) {
                    System.err.println("Could not get ChatService from App: " + ex.getMessage());
                }
                
                // Create new chat using fake data service (in real implementation, this would use socket server)
                String chatId = UUID.randomUUID().toString();
                Chat newChat = new Chat(chatId, user.getUsername(), false);
                newChat.addParticipant(currentUser.getId());
                newChat.addParticipant(user.getId());
                
                // Add the new chat to the data service
                dataService.addChat(newChat);
                
                // If we have socket connection, we'd create the chat on the server too
                if (chatService != null && chatService.isConnected()) {
                    // This would be the place to create chat on server
                    // For now, we're just using the local fake data service
                }
                
                // Refresh the chat list in the UI
                if (refreshCallback != null) {
                    refreshCallback.refreshChatList();
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                    "A chat with " + user.getUsername() + " already exists!", 
                    "Chat Exists", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
