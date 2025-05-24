package page;

import components.customs.ButtonCustom;
import components.customs.TextFieldCustom;
import services.FakeDataService;
import services.ChatService;
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
            
            // Try to get ChatService instance
            ChatService chatService = null;
            try {
                Class<?> appClass = Class.forName("App");
                java.lang.reflect.Method getChatServiceMethod = appClass.getMethod("getChatService");
                chatService = (ChatService) getChatServiceMethod.invoke(null);
            } catch (Exception ex) {
                System.err.println("Could not get ChatService from App: " + ex.getMessage());
            }
            
            if (chatService != null && chatService.isConnected()) {
                // Use socket service for login
                chatService.login(username, password, response -> {
                    if (response.isSuccess()) {
                        // Successfully logged in with socket service
                        SwingUtilities.invokeLater(() -> {
                            // Also login with fake data service for now
                            // (In a production app, we would remove this fake data dependency)
                            FakeDataService dataService = FakeDataService.getInstance();
                            dataService.login(username, password);
                            
                            // Open chat page
                            ChatPage chatPage = new ChatPage();
                            chatPage.setVisible(true);
                            this.dispose();
                        });
                    } else {
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(this, "Login failed: " + response.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        });
                    }
                });
            } else {
                // Fallback to fake data service
                FakeDataService dataService = FakeDataService.getInstance();
                boolean success = dataService.login(username, password);
                
                if (success) {
                    // Open chat page
                    ChatPage chatPage = new ChatPage();
                    chatPage.setVisible(true);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (e.getSource() == registerButton) {
            // Open register page
            RegisterPage registerPage = new RegisterPage();
            registerPage.setVisible(true);
            this.dispose();
        }
    }
}
