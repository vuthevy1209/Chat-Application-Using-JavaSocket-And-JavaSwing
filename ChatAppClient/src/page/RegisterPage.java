package page;

import components.customs.ButtonCustom;
import components.customs.TextFieldCustom;
import dto.request.ApiRequest;
import dto.request.AuthenticateRequest;
import dto.request.RegisterRequest;
import dto.response.ApiResponse;
import utils.ThemeUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

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
        
        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(ThemeUtil.BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        // Title
        JLabel titleLabel = new JLabel("Register");
        titleLabel.setFont(ThemeUtil.TITLE_FONT);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(30));
        
        // Email field
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(ThemeUtil.NORMAL_FONT);
        mainPanel.add(emailLabel);
        mainPanel.add(Box.createVerticalStrut(10));
        
        emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(300, 40));
        emailField.setBorder(ThemeUtil.getRoundedBorder(10));
        mainPanel.add(emailField);
        mainPanel.add(Box.createVerticalStrut(20));
        
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
        
        // Register button
        registerButton = ButtonCustom.createButtonCustom("Register", ThemeUtil.PRIMARY_COLOR, Color.WHITE, 300, 50);
        registerButton.addActionListener(this);
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(registerButton);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Login button
        loginButton = ButtonCustom.createButtonCustom("Login", ThemeUtil.SECONDARY_COLOR, ThemeUtil.TEXT_COLOR, 300, 50);
        loginButton.addActionListener(this);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(loginButton);
        
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
            
            try (Socket socket = new Socket("localhost", 8080)) {
                ObjectOutputStream outObject = new ObjectOutputStream(socket.getOutputStream());
                outObject.flush();
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                ApiRequest request = ApiRequest.builder()
                        .method("POST")
                        .url("/register")
                        .payload(RegisterRequest.builder()
                                .email(email)
                                .username(username)
                                .password(password)
                                .avatarPath("default_avatar.png") // Default avatar path
                                .requestType("CREATE")
                                .build())
                        .build();

                // Send the request
                outObject.writeObject(request);
                outObject.flush();

                // Read the response
                ApiResponse response = (ApiResponse) in.readObject();

                System.out.println("Response Code: " + response.getCode());
                System.out.println("Response Message: " + response.getMessage());

                if (response.getCode().equals("200")) {
                    // redirect to login page
                    JOptionPane.showMessageDialog(this, "Registration successful! Please login.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();
                    new LoginPage().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, response.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

                // Close streams
                outObject.close();
                in.close();
                socket.close();
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
            

            
        } else if (e.getSource() == loginButton) {
            // Open login page
            LoginPage loginPage = new LoginPage();
            loginPage.setVisible(true);
            this.dispose();
        }
    }
}
