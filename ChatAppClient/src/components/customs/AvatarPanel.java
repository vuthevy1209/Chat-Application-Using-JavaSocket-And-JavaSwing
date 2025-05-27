package components.customs;

import models.User;
import utils.ThemeUtil;
import utils.StringUtils;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class AvatarPanel extends JPanel {
    private int size;
    private String username;
    private ImageIcon avatar;
    
    public AvatarPanel(String username, String imagePath, int size) {
        this.username = username;
        this.size = size;
        this.setPreferredSize(new Dimension(size, size));
        this.setMaximumSize(new Dimension(size, size));
        this.setMinimumSize(new Dimension(size, size));
        this.setOpaque(false);

        if (imagePath != null) {
            try {
                BufferedImage img;
                if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
                    // Tải ảnh từ web
                    URL url = new URL(imagePath);
                    img = ImageIO.read(url);
                } else {
                    // Tải ảnh từ resources (local)
                    URL resourceUrl = getClass().getResource(imagePath);
                    if (resourceUrl != null) {
                        img = ImageIO.read(resourceUrl);
                    } else {
                        img = null;
                    }
                }
                
                if (img != null) {
                    Image scaledImg = img.getScaledInstance(size, size, Image.SCALE_SMOOTH);
                    avatar = new ImageIcon(scaledImg);
                } else {
                    avatar = null;
                }
            } catch (Exception e) {
                avatar = null;
                System.err.println("Failed to load image: " + e.getMessage());
            }
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw circle background
        g2d.setColor(ThemeUtil.generateAvatarColor(username));
        g2d.fillOval(0, 0, size, size);
        
        if (avatar != null) {
            // Create a circular clip
            Shape clip = new java.awt.geom.Ellipse2D.Float(0, 0, size, size);
            g2d.setClip(clip);
            
            // Draw the avatar image
            avatar.paintIcon(this, g2d, 0, 0);
        } else {
            // Draw initials
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, size / 2));
            FontMetrics fm = g2d.getFontMetrics();
            String initials = StringUtils.getInitials(username);
            int textWidth = fm.stringWidth(initials);
            int textHeight = fm.getHeight();
            g2d.drawString(initials, (size - textWidth) / 2, (size + textHeight / 2) / 2);
        }
        
        g2d.dispose();
    }
}
