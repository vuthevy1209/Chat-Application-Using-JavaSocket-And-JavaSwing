package components;

import models.Chat;
import models.User;
import page.ChatPage.ChatRefreshCallback;
import services.ChatService;
import utils.IconUtil;
import utils.ThemeUtil;
import components.customs.AvatarPanel;
import config.Authentication;
import dto.request.ApiRequest;
import dto.request.ChatRequest;
import dto.response.ApiResponse;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.Socket;
import java.util.List;

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
        AvatarPanel avatarPanel = new AvatarPanel(user.getUsername(), user.getAvatarPath(), 40);
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
        List<String> participantIds = List.of(Authentication.getUser().getId(), user.getId());

        ChatRequest chatRequest = ChatRequest.builder()
            .name(user.getUsername())
            .isGroup(false)
            .participantIds(participantIds)
            .requestType("create")
            .build();

        ApiResponse response = (ApiResponse) ChatService.createChat(chatRequest);

        if (response.getCode().equals("200")) {
            if (refreshCallback != null) {
                refreshCallback.refreshChatList();
            }
        } else {
            JOptionPane.showMessageDialog(null, response.getMessage(), "ERROR", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
