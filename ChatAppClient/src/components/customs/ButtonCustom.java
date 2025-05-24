package components.customs;

import javax.swing.*;
import java.awt.*;

public class ButtonCustom {
    public static JButton createButtonCustom(String text, Color backgroundColor, Color textColor){
        return createButtonCustom(text, backgroundColor, textColor, 120, 40);
    }

    public static JButton createButtonCustom(String text, Color backgroundColor, Color textColor, int width, int height) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                if (getModel().isArmed()) {
                    g.setColor(backgroundColor.darker()); // Màu khi nhấn
                } else if (getModel().isRollover()) {
                    g.setColor(backgroundColor.brighter()); // Màu khi hover
                } else {
                    g.setColor(backgroundColor); // Màu nền mặc định
                }
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 13, 13); // Bo góc
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                g.setColor(Color.BLACK);
                g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 13, 13); // Vẽ viền
            }
        };
        button.setFocusPainted(false); // Bỏ hiệu ứng khi focus
        button.setContentAreaFilled(false); // Bỏ nền mặc định
        button.setOpaque(false); // Đảm bảo màu nền tùy chỉnh hiển thị
        button.setBorderPainted(false); // Bỏ viền
        button.setForeground(textColor); // Màu chữ
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Con trỏ chuột dạng tay
        button.setPreferredSize(new Dimension(width, height));
        return button;
    }

    public static JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBackground(color);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.darker(), 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });

        return button;
    }
}
