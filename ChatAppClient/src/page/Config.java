package page;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import com.cloudinary.Api;

import components.customs.ButtonCustom;
import config.SocketConfig;
import dto.request.ApiRequest;
import dto.response.ApiResponse;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.net.Socket;

import utils.ApiUtils;
import utils.ThemeUtil;

public class Config extends JFrame {
    private JTextField ip;
    private JTextField port;

    public Config() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setResizable(false);

        // main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        mainPanel.setBackground(ThemeUtil.BACKGROUND_COLOR);

        // Title
        JLabel titleLabel = new JLabel("Configuration");
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 80, 20, 0));
        titleLabel.setFont(ThemeUtil.TITLE_FONT);
        titleLabel.setForeground(ThemeUtil.TEXT_COLOR);
        mainPanel.add(titleLabel, BorderLayout.NORTH);
    

        // IP field
        JLabel ipLabel = new JLabel("IP Address");
        ipLabel.setFont(ThemeUtil.HEADER_FONT);
        ipLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(ipLabel);
        mainPanel.add(Box.createVerticalStrut(10));

        ip = new JTextField();
        ip.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        ip.setPreferredSize(new Dimension(320, 40));
        ip.setBorder(ThemeUtil.getRoundedBorder(10));
        ip.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(ip);
        mainPanel.add(Box.createVerticalStrut(20));

        // Port field
        JLabel portLabel = new JLabel("Port");
        portLabel.setFont(ThemeUtil.HEADER_FONT);
        portLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(portLabel);
        mainPanel.add(Box.createVerticalStrut(10));

        port = new JTextField();
        port.setText("8080"); // Default port
        port.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        port.setPreferredSize(new Dimension(320, 40));
        port.setBorder(ThemeUtil.getRoundedBorder(10));
        port.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(port);
        mainPanel.add(Box.createVerticalStrut(20));

        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separator.setForeground(ThemeUtil.TEXT_COLOR);
        separator.setBackground(ThemeUtil.TEXT_COLOR);
        mainPanel.add(separator);
        mainPanel.add(Box.createVerticalStrut(20));

        JButton connectButton = ButtonCustom.createButtonCustom("Connect", ThemeUtil.PRIMARY_COLOR, Color.WHITE, 320, 50);

        connectButton.addActionListener(e -> {
            String ipAddress = ip.getText().trim();
            String portNumber = port.getText().trim();

            if (ipAddress.isEmpty() || portNumber.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, "Please enter IP address and port", "Input Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                ApiResponse response = ApiUtils.handleRequest(ApiRequest.builder()
                        .method("GET")
                        .url("/test_connection")
                        .build());

                if (response.getCode().equals("200")) {
                    SocketConfig.setIpAddress(ipAddress);
                    SocketConfig.setPort(Integer.parseInt(portNumber));

                    new LoginPage().setVisible(true); 
                    dispose(); // Close the config window
                } else {
                    javax.swing.JOptionPane.showMessageDialog(this, "Connection failed: " + response.getMessage(), "Connection Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (Exception ex) {
                javax.swing.JOptionPane.showMessageDialog(this, "Connection failed: " + ex.getMessage(), "Connection Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
        });

        connectButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        connectButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        mainPanel.add(connectButton);

        add(mainPanel);    
    }
}