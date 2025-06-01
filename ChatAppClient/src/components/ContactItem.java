package components;

import models.Chat;
import models.Message;
import models.User;
import utils.ThemeUtil;
import components.customs.AvatarPanel;
import config.Authentication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ContactItem extends JPanel {
    private String contactId;
    private Chat chat;
    private boolean isSelected;
    private Color defaultBackground;
    private Color hoverBackground;
    private Color selectedBackground;
    
    public ContactItem(Chat chat) {
        this.contactId = chat.getId();
        this.chat = chat;
        this.isSelected = false;
        this.defaultBackground = Color.WHITE;
        this.hoverBackground = new Color(245, 245, 245);
        this.selectedBackground = new Color(235, 235, 235);
        
        setLayout(new BorderLayout(10, 0));
        setBackground(defaultBackground);
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Avatar
        AvatarPanel avatarPanel = null;
        
        // Contact info
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setOpaque(false);

        JLabel nameLabel = null;
        if (chat.isGroup()) {
            nameLabel = new JLabel(chat.getName());
            avatarPanel = new AvatarPanel(chat.getName(), null, 40);
        } else {
            User participant = chat.getParticipants().get(0);
            if (participant.getId().equals(Authentication.getUser().getId())) {
                participant = chat.getParticipants().get(1); // Get the other participant in a one-on-one chat
            }
            nameLabel = new JLabel(participant.getUsername());
            avatarPanel = new AvatarPanel(participant.getUsername(), participant.getAvatarPath(), 40);
        }

        add(avatarPanel, BorderLayout.WEST);

        nameLabel.setFont(ThemeUtil.NORMAL_FONT);
        infoPanel.add(nameLabel, BorderLayout.CENTER);

        Message lastMessage = chat.getLastMessage();
    
        if (lastMessage != null) {
            JLabel previewLabel = null;
            if (lastMessage.getMessageType().equals("TEXT")) {
                String previewText = truncateText(lastMessage.getContent(), 30);
                previewLabel = new JLabel(previewText);
            } else if (lastMessage.getMessageType().equals("IMAGE")) {
                previewLabel = new JLabel("Sent an image");
            } else if (lastMessage.getMessageType().equals("FILE")) {
                previewLabel = new JLabel("Sent a file");
            }
            if (previewLabel != null) {
                previewLabel.setFont(ThemeUtil.SMALL_FONT);
                previewLabel.setForeground(Color.GRAY);
                infoPanel.add(previewLabel, BorderLayout.SOUTH);
            }
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

    public String getContactId() {
        return contactId;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
