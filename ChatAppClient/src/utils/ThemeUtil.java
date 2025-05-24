package utils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class ThemeUtil {
    // Colors
    public static final Color PRIMARY_COLOR = new Color(51, 51, 51);
    public static final Color SECONDARY_COLOR = new Color(200, 200, 200);
    public static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    public static final Color TEXT_COLOR = new Color(51, 51, 51);
    public static final Color LIGHT_GRAY = new Color(230, 230, 230);
    public static final Color CHAT_BUBBLE_SENT = new Color(144, 238, 144);
    public static final Color CHAT_BUBBLE_RECEIVED = new Color(255, 255, 255);
    public static final Color ONLINE_COLOR = new Color(0, 0, 0);
    
    // Fonts
    public static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
    public static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 18);
    public static final Font NORMAL_FONT = new Font("Arial", Font.PLAIN, 14);
    public static final Font SMALL_FONT = new Font("Arial", Font.PLAIN, 12);
    
    // Borders
    public static Border getRoundedBorder(int radius) {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        );
    }
    
    // Apply theme settings
    public static void setupTheme() {
        // Set default font
        UIManager.put("Label.font", NORMAL_FONT);
        UIManager.put("Button.font", NORMAL_FONT);
        UIManager.put("TextField.font", NORMAL_FONT);
        UIManager.put("PasswordField.font", NORMAL_FONT);
        UIManager.put("TextArea.font", NORMAL_FONT);
    }
    
    // Create rounded panel
    public static JPanel createRoundedPanel(Color backgroundColor) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(backgroundColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        return panel;
    }
}
