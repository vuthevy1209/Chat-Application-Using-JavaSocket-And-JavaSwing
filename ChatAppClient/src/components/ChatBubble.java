package components;

import models.Message;
import utils.IconUtil;
import utils.ThemeUtil;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class ChatBubble extends JPanel {
    private Message message;
    private boolean isSent;
    private JLabel timeLabel;
    private JLabel readStatusLabel;
    
    public ChatBubble(Message message, boolean isSent) {
        this.message = message;
        this.isSent = isSent;
        
        setOpaque(false);
        setLayout(new BorderLayout(5, 5));
        
        // Create message content panel
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
        };
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BorderLayout(5, 5));
        
        // Create message text
        JTextArea textArea = new JTextArea(message.getContent()); 
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setOpaque(false);
        textArea.setFont(ThemeUtil.NORMAL_FONT);
        textArea.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        
        contentPanel.add(textArea, BorderLayout.CENTER);
        
        // Create status panel (time and read status)
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 0));
        statusPanel.setOpaque(false);
        

        timeLabel = new JLabel(message.getCreatedAt().format(DateTimeFormatter.ofPattern("HH:mm")));
        timeLabel.setFont(ThemeUtil.SMALL_FONT);
        timeLabel.setForeground(Color.DARK_GRAY);
        
        statusPanel.add(timeLabel);
        
        if (isSent) {
            readStatusLabel = new JLabel();
            if (message.isRead()) {
                readStatusLabel.setIcon(IconUtil.getImageIcon("/icon/check_Icon.png", 12, 12));
            }
            statusPanel.add(readStatusLabel);
        }
        
        contentPanel.add(statusPanel, BorderLayout.SOUTH);
        
        // Add content panel to main panel with margins for alignment
        if (isSent) {
            // For sent messages, add left margin to push bubble to the right
            setBorder(BorderFactory.createEmptyBorder(0, 300, 0, 0));
        } else {
            // For received messages, add right margin to keep bubble on the left
            setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 300));
        }

        add(contentPanel, BorderLayout.CENTER);
        
        // Add padding
        add(Box.createVerticalStrut(2), BorderLayout.NORTH);
        add(Box.createVerticalStrut(2), BorderLayout.SOUTH);
        add(Box.createHorizontalStrut(2), BorderLayout.EAST);
        add(Box.createHorizontalStrut(2), BorderLayout.WEST);
    }
    
    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        
        // Tính toán kích thước dựa trên nội dung text
        JTextArea tempTextArea = new JTextArea(message.getContent());
        tempTextArea.setFont(ThemeUtil.NORMAL_FONT);
        tempTextArea.setLineWrap(true);
        tempTextArea.setWrapStyleWord(true);
        
        // Giới hạn chiều rộng tối đa
        Container parent = getParent();
        int maxWidth = 300; // Default max width
        if (parent != null && parent.getWidth() > 0) {
            maxWidth = Math.min(400, (int) (parent.getWidth() * 0.6));
        }
        
        // Đặt chiều rộng cho textArea để tính chiều cao
        tempTextArea.setSize(maxWidth - 50, Integer.MAX_VALUE); // -50 for padding and margins
        Dimension textSize = tempTextArea.getPreferredSize();
        
        // Tính toán kích thước bubble
        int bubbleWidth = Math.max(100, Math.min(maxWidth, textSize.width + 30)); // +30 for padding
        int bubbleHeight = textSize.height + 50; // +50 for padding, status panel
        
        return new Dimension(bubbleWidth, bubbleHeight);
    }
    
    @Override
    public Dimension getMaximumSize() {
        Dimension preferred = getPreferredSize();
        // Quan trọng: Giới hạn chiều cao tối đa để tránh bị kéo giãn
        return new Dimension(Integer.MAX_VALUE, preferred.height);
    }
    
    @Override
    public Dimension getMinimumSize() {
        return new Dimension(100, 40);
    }
}
