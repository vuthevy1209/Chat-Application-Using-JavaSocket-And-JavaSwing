package components;

import models.Chat;
import models.Message;
import models.User;
import utils.ThemeUtil;
import components.customs.AvatarPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ContactItem extends JPanel {
    private Chat chat;
    private User user;
    private boolean isSelected;
    private Color defaultBackground;
    private Color hoverBackground;
    private Color selectedBackground;
    
    public ContactItem(Chat chat, User user) {
        this.chat = chat;
        this.user = user;
        this.isSelected = false;
        this.defaultBackground = Color.WHITE;
        this.hoverBackground = new Color(245, 245, 245);
        this.selectedBackground = new Color(235, 235, 235);
        
        setLayout(new BorderLayout(10, 0));
        setBackground(defaultBackground);
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Avatar
        AvatarPanel avatarPanel = new AvatarPanel(user, 40);
        add(avatarPanel, BorderLayout.WEST);
        
        // Contact info
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel(user.getUsername());
        nameLabel.setFont(ThemeUtil.NORMAL_FONT);
        infoPanel.add(nameLabel, BorderLayout.CENTER);
        
        // Last message preview (if any)
        Message lastMessage = chat.getLastMessage();
        if (lastMessage != null) {
            JLabel previewLabel = new JLabel(truncateText(lastMessage.getContent(), 30));
            previewLabel.setFont(ThemeUtil.SMALL_FONT);
            previewLabel.setForeground(Color.GRAY);
            infoPanel.add(previewLabel, BorderLayout.SOUTH);
        }
        
        add(infoPanel, BorderLayout.CENTER);
        
        // Mouse events for hover effect
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isSelected) {
                    setBackground(hoverBackground);
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (!isSelected) {
                    setBackground(defaultBackground);
                }
            }
        });
    }
    
    private String truncateText(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }
    
    public void setSelected(boolean selected) {
        this.isSelected = selected;
        setBackground(selected ? selectedBackground : defaultBackground);
    }
    
    public Chat getChat() {
        return chat;
    }
}
