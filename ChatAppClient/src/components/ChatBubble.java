package components;

import models.Message;
import utils.ThemeUtil;

import javax.imageio.ImageIO;
import javax.swing.*;

import components.customs.AvatarPanel;
import config.Authentication;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.time.format.DateTimeFormatter;

public class ChatBubble extends JPanel {
    private Message message;
    private boolean isSent;
    
    public ChatBubble(Message message) {

        this.message = message;
        this.isSent = message.getSenderId().equals(Authentication.getUser().getId());

        setOpaque(false);
        setLayout(new BorderLayout());
        
        // Create main container with padding
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setOpaque(false);
        mainContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        if (message.getMessageType().equals("TEXT")) {
            // Create message content panel with fixed width
            JPanel contentPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Set bubble color based on sent/received
                    if (isSent) {
                        g2.setColor(ThemeUtil.CHAT_BUBBLE_SENT);
                    } else {
                        g2.setColor(ThemeUtil.CHAT_BUBBLE_RECEIVED);
                    }
                    
                    // Draw rounded rectangle
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                    g2.dispose();
                }
                
                @Override
                public Dimension getPreferredSize() {
                    // Cố định chiều rộng, linh hoạt chiều cao
                    Dimension superSize = super.getPreferredSize();
                    return new Dimension(300, superSize.height);
                }
            };
            contentPanel.setOpaque(false);
            contentPanel.setLayout(new BorderLayout(5, 5));

            // Username label + Created at label
            JLabel headerLabel = new JLabel("@" + message.getSenderUsername() + " - " + message.getCreatedAt().format(DateTimeFormatter.ofPattern("HH:mm")));
            headerLabel.setFont(new Font("Arial", Font.BOLD, 15));
            headerLabel.setForeground(ThemeUtil.DARK_GRAY);
            headerLabel.setBorder(BorderFactory.createEmptyBorder(2, 12, 0, 12));

            // Create message text
            JTextArea textArea = new JTextArea(message.getContent());
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setOpaque(false);
            textArea.setFont(ThemeUtil.NORMAL_FONT);
            textArea.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

            contentPanel.add(headerLabel, BorderLayout.NORTH);
            contentPanel.add(textArea, BorderLayout.CENTER);

            // Create wrapper panel for alignment
            JPanel wrapperPanel = new JPanel(new BorderLayout());
            wrapperPanel.setOpaque(false);
            
            if (isSent) {
                // Tin nhắn của mình - nằm bên phải
                wrapperPanel.add(contentPanel, BorderLayout.EAST);
            } else {
                // Tin nhắn của người khác - nằm bên trái với avatar
                JPanel leftPanel = new JPanel(new BorderLayout(10, 0));
                leftPanel.setOpaque(false);
                
                AvatarPanel avatarPanel = new AvatarPanel(message.getSenderUsername(), null, 40);
                leftPanel.add(avatarPanel, BorderLayout.WEST); 
                leftPanel.add(contentPanel, BorderLayout.CENTER);
                
                wrapperPanel.add(leftPanel, BorderLayout.WEST);
            }
            
            mainContainer.add(wrapperPanel, BorderLayout.CENTER);
            add(mainContainer, BorderLayout.CENTER);
        } else if (message.getMessageType().equals("IMAGE")) {
            try {
                URL url = new URL(message.getImagePath());
                BufferedImage img = ImageIO.read(url);
                
                // Tạo image panel với kích thước cố định
                JPanel imagePanel = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        
                        // Vẽ background với góc bo tròn
                        g2.setColor(Color.WHITE);
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                        
                        // Tính toán kích thước ảnh giữ tỷ lệ
                        int fixedWidth = 250; // Chiều ngang cố định
                        int originalWidth = img.getWidth();
                        int originalHeight = img.getHeight();
                        
                        // Tính chiều cao theo tỷ lệ
                        int scaledHeight = (originalHeight * fixedWidth) / originalWidth;
                        
                        // Vẽ ảnh với padding
                        int padding = 5;
                        g2.drawImage(img, padding, padding, 
                                   fixedWidth - 2*padding, scaledHeight - 2*padding, null);
                        g2.dispose();
                    }
                    
                    @Override
                    public Dimension getPreferredSize() {
                        int fixedWidth = 250;
                        int originalWidth = img.getWidth();
                        int originalHeight = img.getHeight();
                        int scaledHeight = (originalHeight * fixedWidth) / originalWidth;
                        return new Dimension(fixedWidth, scaledHeight + 10); // +10 cho padding
                    }
                };
                imagePanel.setOpaque(false);
                
                // Tạo wrapper panel cho alignment
                JPanel wrapperPanel = new JPanel(new BorderLayout());
                wrapperPanel.setOpaque(false);
                
                if (isSent) {
                    // Tin nhắn của mình - nằm bên phải
                    wrapperPanel.add(imagePanel, BorderLayout.EAST);
                } else {
                    // Tin nhắn của người khác - nằm bên trái với avatar
                    JPanel leftPanel = new JPanel(new BorderLayout(10, 0));
                    leftPanel.setOpaque(false);
                    
                    AvatarPanel avatarPanel = new AvatarPanel(message.getSenderUsername(), null, 40);
                    leftPanel.add(avatarPanel, BorderLayout.WEST);
                    leftPanel.add(imagePanel, BorderLayout.CENTER);
                    
                    wrapperPanel.add(leftPanel, BorderLayout.WEST);
                }
                
                mainContainer.add(wrapperPanel, BorderLayout.CENTER);
                add(mainContainer, BorderLayout.CENTER);

            } catch (Exception e) {
                e.printStackTrace();
                // Hiển thị error message nếu không load được ảnh
                JLabel errorLabel = new JLabel("Không thể tải hình ảnh");
                errorLabel.setForeground(Color.RED);
                mainContainer.add(errorLabel, BorderLayout.CENTER);
                add(mainContainer, BorderLayout.CENTER);
            }
        }
    }
}