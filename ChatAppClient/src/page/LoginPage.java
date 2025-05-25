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
        
        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(ThemeUtil.BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        // Title
        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(ThemeUtil.TITLE_FONT);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(30));
        
        // Username field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(ThemeUtil.NORMAL_FONT);
        mainPanel.add(usernameLabel);
        mainPanel.add(Box.createVerticalStrut(10));
        
        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(300, 40));
        usernameField.setBorder(ThemeUtil.getRoundedBorder(10));
        mainPanel.add(usernameField);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(ThemeUtil.NORMAL_FONT);
        mainPanel.add(passwordLabel);
        mainPanel.add(Box.createVerticalStrut(10));
        
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(300, 40));
        passwordField.setBorder(ThemeUtil.getRoundedBorder(10));
        mainPanel.add(passwordField);
        mainPanel.add(Box.createVerticalStrut(40));
        
        // Login button
        loginButton = ButtonCustom.createButtonCustom("Log in", ThemeUtil.PRIMARY_COLOR, Color.WHITE, 300, 50);
        loginButton.addActionListener(this);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(loginButton);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Register button
        registerButton = ButtonCustom.createButtonCustom("Register", ThemeUtil.SECONDARY_COLOR, ThemeUtil.TEXT_COLOR, 300, 50);
        registerButton.addActionListener(this);
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(registerButton);
        
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
                        
                JOptionPane.showMessageDialog(this, "Login successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
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
