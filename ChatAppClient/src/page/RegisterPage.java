package page;

import components.customs.ButtonCustom;
import dto.request.RegisterRequest;
import dto.response.ApiResponse;
import services.AuthService;
import utils.ThemeUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterPage extends JFrame implements ActionListener {
    private JTextField emailField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JButton loginButton;

    public RegisterPage() {
        setTitle("Chat App - Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 550);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(ThemeUtil.BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Title
        JLabel titleLabel = new JLabel("Register");
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 120, 20, 0));
        titleLabel.setFont(ThemeUtil.TITLE_FONT);
        titleLabel.setForeground(ThemeUtil.TEXT_COLOR);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Content panel with BoxLayout
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(ThemeUtil.BACKGROUND_COLOR);

        // Email field
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(ThemeUtil.HEADER_FONT);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(emailLabel);
        contentPanel.add(Box.createVerticalStrut(10));

        emailField = new JTextField();
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        emailField.setPreferredSize(new Dimension(320, 40));
        emailField.setBorder(ThemeUtil.getRoundedBorder(10));
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(emailField);
        contentPanel.add(Box.createVerticalStrut(20));

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
        contentPanel.add(Box.createVerticalStrut(20));

        // Divider
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separator.setForeground(ThemeUtil.TEXT_COLOR);
        separator.setBackground(ThemeUtil.TEXT_COLOR);
        contentPanel.add(separator);
        contentPanel.add(Box.createVerticalStrut(10));

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(ThemeUtil.BACKGROUND_COLOR);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Register button
        registerButton = ButtonCustom.createButtonCustom("Register", ThemeUtil.PRIMARY_COLOR, Color.WHITE, 320, 50);
        registerButton.addActionListener(this);
        registerButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        registerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        buttonPanel.add(registerButton);
        buttonPanel.add(Box.createVerticalStrut(15));

        // Login button
        loginButton = ButtonCustom.createButtonCustom("Login", ThemeUtil.SECONDARY_COLOR, ThemeUtil.TEXT_COLOR, 320, 50);
        loginButton.addActionListener(this);
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        buttonPanel.add(loginButton);

        contentPanel.add(buttonPanel);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            String email = emailField.getText();
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate email format
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                JOptionPane.showMessageDialog(this, "Please enter a valid email address", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            RegisterRequest registerRequest = RegisterRequest.builder()
                    .email(email)
                    .username(username)
                    .password(password)
                    .avatarPath("default_avatar.png")
                    .requestType("CREATE")
                    .build();

            ApiResponse response = AuthService.register(registerRequest);

            if (response.getCode().equals("200")) {
                JOptionPane.showMessageDialog(this, "Registration successful! Please login.", "Success", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
                new LoginPage().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, response.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == loginButton) {
            LoginPage loginPage = new LoginPage();
            loginPage.setVisible(true);
            this.dispose();
        }
    }
}
