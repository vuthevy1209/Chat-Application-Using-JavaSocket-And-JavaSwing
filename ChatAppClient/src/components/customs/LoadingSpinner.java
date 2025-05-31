package components.customs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;

public class LoadingSpinner extends JPanel implements ActionListener {
    private Timer timer;
    private int angle = 0;
    private final int delay = 50;
    
    public LoadingSpinner() {
        setPreferredSize(new Dimension(50, 50));
        setOpaque(false);
        timer = new Timer(delay, this);
    }
    
    public void start() {
        timer.start();
    }
    
    public void stop() {
        timer.stop();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(centerX, centerY) - 5;
        
        // Draw spinning arc
        g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(new Color(59, 130, 246)); // Blue color
        
        Arc2D.Double arc = new Arc2D.Double(
            centerX - radius, centerY - radius, 
            radius * 2, radius * 2, 
            angle, 120, Arc2D.OPEN
        );
        
        g2d.draw(arc);
        g2d.dispose();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        angle = (angle + 10) % 360;
        repaint();
    }
}