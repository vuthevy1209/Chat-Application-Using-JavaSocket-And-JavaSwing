package components;

import utils.ThemeUtil;

import javax.swing.*;
import java.awt.*;

public class DateSeparator extends JPanel {
    private String dateText;
    
    public DateSeparator(String dateText) {
        this.dateText = dateText;
        setOpaque(false);
        setLayout(new BorderLayout());
        
        JLabel dateLabel = new JLabel("- " + dateText + " -", SwingConstants.CENTER);
        dateLabel.setForeground(Color.GRAY);
        dateLabel.setFont(ThemeUtil.HEADER_FONT);
        dateLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        add(dateLabel, BorderLayout.CENTER);
    }
    
    @Override
    public Dimension getPreferredSize() {
        JLabel tempLabel = new JLabel(dateText);
        tempLabel.setFont(ThemeUtil.SMALL_FONT);
        tempLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        Dimension labelSize = tempLabel.getPreferredSize();
        return new Dimension(labelSize.width, labelSize.height);
    }
    
    @Override
    public Dimension getMaximumSize() {
        Dimension preferred = getPreferredSize();
        // Quan trọng: Giới hạn chiều cao tối đa để tránh bị kéo giãn
        return new Dimension(Integer.MAX_VALUE, preferred.height);
    }
    
    @Override
    public Dimension getMinimumSize() {
        return new Dimension(50, 20);
    }
}