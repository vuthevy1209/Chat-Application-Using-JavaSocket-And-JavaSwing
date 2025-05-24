package components.customs;

import models.User;

import javax.swing.*;
import java.awt.*;

public class AvatarPanel extends JPanel {
    private User user;
    private int size;
    private ImageIcon avatar;
    
    public AvatarPanel(User user, int size) {
        this.user = user;
        this.size = size;
        this.setPreferredSize(new Dimension(size, size));
        this.setMaximumSize(new Dimension(size, size));
        this.setMinimumSize(new Dimension(size, size));
        this.setOpaque(false);
        
        if (user.getAvatarPath() != null) {
            try {
                avatar = new ImageIcon(getClass().getResource(user.getAvatarPath()));
                Image img = avatar.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
                avatar = new ImageIcon(img);
            } catch (Exception e) {
                avatar = null;
            }
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw circle background
        g2d.setColor(user.getAvatarColor());
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
            String initials = user.getInitials();
            int textWidth = fm.stringWidth(initials);
            int textHeight = fm.getHeight();
            g2d.drawString(initials, (size - textWidth) / 2, (size + textHeight / 2) / 2);
        }
        
        g2d.dispose();
    }
}
