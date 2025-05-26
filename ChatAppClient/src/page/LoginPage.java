package page;

import components.customs.ButtonCustom;
import config.Authentication;
import dto.response.ApiResponse;
import dto.response.UserResponse;
import models.User;
import services.AuthService;
import utils.ThemeUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    
    public LoginPage() {

        // Set up the frame
        setTitle("Chat App - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main panel with BorderLayout for better control
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ThemeUtil.BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        // Content panel with BoxLayout
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(ThemeUtil.BACKGROUND_COLOR);
        
        // Title
        JLabel titleLabel = new JLabel("Login");
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 120, 20, 0));
        titleLabel.setFont(ThemeUtil.TITLE_FONT);
        titleLabel.setForeground(ThemeUtil.TEXT_COLOR);
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Username field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(ThemeUtil.HEADER_FONT);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(usernameLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        
        usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        usernameField.setPreferredSize(new Dimension(320, 40));
        usernameField.setBorder(ThemeUtil.getRoundedBorder(10));
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(usernameField);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(ThemeUtil.HEADER_FONT);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(passwordLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        
        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        passwordField.setPreferredSize(new Dimension(320, 40));
        passwordField.setBorder(ThemeUtil.getRoundedBorder(10));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(passwordField);
        contentPanel.add(Box.createVerticalStrut(40));

        // add divider
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separator.setForeground(ThemeUtil.TEXT_COLOR);
        separator.setBackground(ThemeUtil.TEXT_COLOR);
        contentPanel.add(separator);
        contentPanel.add(Box.createVerticalStrut(20));

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Button panel for consistent width
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(ThemeUtil.BACKGROUND_COLOR);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Login button - full width minus some padding
        loginButton = ButtonCustom.createButtonCustom("Log in", ThemeUtil.PRIMARY_COLOR, Color.WHITE, 320, 50);
        loginButton.addActionListener(this);
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createVerticalStrut(15));
        
        // Register button - full width minus some padding
        registerButton = ButtonCustom.createButtonCustom("Register", ThemeUtil.SECONDARY_COLOR, ThemeUtil.TEXT_COLOR, 320, 50);
        registerButton.addActionListener(this);
        registerButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        registerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        buttonPanel.add(registerButton);
        
        contentPanel.add(buttonPanel);
        
        // Add content panel to main panel
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter username and password", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ApiResponse response = AuthService.login(username, password);

            if (response.getCode().equals("200")) {
                UserResponse userResponse = (UserResponse) response.getData();
                Authentication.setUser(User.builder()
                        .id(userResponse.getId())
                        .username(userResponse.getUsername())
                        .email(userResponse.getEmail())
                        .avatarPath(userResponse.getAvatarPath())
                        .build());
                        
                // redirect to main chat page
                new ChatPage().setVisible(true);
                this.dispose();
            } else {
                // Login failed, show error message
                JOptionPane.showMessageDialog(this, response.getMessage(), "Login Failed", JOptionPane.ERROR_MESSAGE);
            }

        } else if (e.getSource() == registerButton) {
            // Open register page
            RegisterPage registerPage = new RegisterPage();
            registerPage.setVisible(true);
            this.dispose();
        }
    }
}