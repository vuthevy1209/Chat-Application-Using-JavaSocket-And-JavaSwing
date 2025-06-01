package page;

import components.ChatBubble;
import components.ContactItem;
import components.DateSeparator;
import components.OnlineUserItem;
import components.UserItem;
import components.customs.*;
import dto.request.ApiRequest;
import dto.request.ChatRequest;
import dto.request.MessageRequest;
import dto.response.ApiResponse;
import dto.response.ChatResponse;
import dto.response.MessageResponse;
import dto.response.UserResponse;
import models.Message;
import models.User;
import models.Chat;
import utils.CloudinaryUtils;
import utils.IconUtils;
import utils.ThemeUtil;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import config.Authentication;
import converters.ChatConverter;
import converters.UserConverter;
import services.AuthService;
import services.ChatService;
import services.MessageService;
import services.UserService;


public class ChatPage extends JFrame {
    private Chat currentChat = null;
    List<Chat> myChats = new ArrayList<>(); // List of chats for the user
    // Socket for communication with the server
    Socket socket;
    ObjectOutputStream outObject;
    ObjectInputStream inObject;
    
    // UI Components
    private JPanel leftPanel;
    private JPanel centerPanel;
    private JPanel rightPanel;
    private JPanel chatListPanel;
    private JPanel onlineUsersPanel;
    private JPanel messagesPanel;
    private JTextField messageField;
    private JButton sendButton;
    private JLabel currentChatNameLabel;
    
    // Timer to periodically refresh online users
    private Timer onlineUsersRefreshTimer;
    
    // Callback interface for refreshing chat list
    public interface ChatRefreshCallback {
        void refreshChatList();
    }


    public ChatPage() {
        // Connect to the realtime handler
        try {
            // Initialize socket connection
            socket = new Socket("localhost", 12345);
            outObject = new ObjectOutputStream(socket.getOutputStream());
            outObject.flush();
            inObject = new ObjectInputStream(socket.getInputStream());

            // Send authentication request to server
            String userId = Authentication.getUser().getId();
            outObject.writeObject(ApiRequest.builder()
                .method("POST")
                .url("/auth/realtime")
                .headers(userId)
                .payload(Authentication.getUser())
                .build());
                
            outObject.flush();

            // Thread nhận tin nhắn từ server
            Thread receiveThread = new Thread(() -> {
                try {
                    while(true) {
                        ApiResponse response = (ApiResponse) inObject.readObject();

                        if (response.getCode().equals("200") && response.getMessage().equals("MESSAGE_SENT")) {
                            MessageResponse messageResponse = (MessageResponse) response.getData();
                            Message message = Message.builder()
                                .id(messageResponse.getId())
                                .senderId(messageResponse.getSenderId())
                                .chatId(messageResponse.getChatId())
                                .senderUsername(messageResponse.getSenderUsername())
                                .content(messageResponse.getContent())
                                .attachmentPath(messageResponse.getAttachmentPath())
                                .imagePath(messageResponse.getImagePath())
                                .isRead(messageResponse.isRead())                                
                                .createdAt(messageResponse.getCreatedAt())
                                .messageType(messageResponse.getMessageType())
                                .build();

                            // Check if the message is for the current chat
                            if (currentChat != null && !currentChat.getId().equals(message.getChatId())) {
                                continue;
                            }

                            ChatBubble bubble = new ChatBubble(message, outObject); // Assuming received messages are from others
                            messagesPanel.add(bubble);

                            SwingUtilities.invokeLater(() -> {
                                messagesPanel.add(bubble);
                                messagesPanel.revalidate(); // Cập nhật layout
                                messagesPanel.repaint();    // Vẽ lại giao diện
                                
                                // Cuộn xuống cuối sau khi layout đã được cập nhật
                                SwingUtilities.invokeLater(() -> {
                                    JScrollPane scrollPane = (JScrollPane) messagesPanel.getParent().getParent();
                                    JScrollBar vertical = scrollPane.getVerticalScrollBar();
                                    vertical.setValue(vertical.getMaximum());
                                });
                            });

                            loadChatList(); // Refresh chat list to update last message time
                        } else if (response.getCode().equals("200") && response.getMessage().equals("MESSAGE_DELETED")) {
                            String deletedMessageId = (String) response.getData();
                            // Remove the deleted message from the UI
                            for (int i = messagesPanel.getComponentCount() - 1; i >= 0; i--) {
                                Component comp = messagesPanel.getComponent(i);
                                if (comp instanceof ChatBubble) {
                                    ChatBubble bubble = (ChatBubble) comp;
                                    if (bubble.getMessage().getId().equals(deletedMessageId)) {
                                        messagesPanel.remove(i);
                                    }
                                }
                            }
                            messagesPanel.revalidate();
                            messagesPanel.repaint();

                            loadChatList();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            receiveThread.start();
        } catch (IOException e) {
            System.err.println("Failed to connect to server 12345: " + e.getMessage());
            return;
        }

        setTitle("Chat App");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 500));

        // Add window listener to handle close operation
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Stop the timer to refresh online users
                stopOnlineUsersRefreshTimer();
                // Send logout request
                AuthService.logout();
                System.exit(0);
            }
        });
        
        // Create main layout
        setLayout(new BorderLayout());
        
        // Create panels
        createLeftPanel();
        createCenterPanel();
        createRightPanel();
        
        // Add panels to frame
        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);

        messagesPanel.setLayout(new GridBagLayout()); // Đảm bảo ảnh luôn ở giữa

        JPanel backgroundMessagePanel = new JPanel();
        backgroundMessagePanel.setOpaque(false); // Trong suốt
        Image backgroundImage = IconUtils.getImageIcon("/icon/Message2.png", 200, 200).getImage();
        backgroundMessagePanel.add(new JLabel(new ImageIcon(backgroundImage)));

        // Thêm ảnh vào giữa bằng GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        messagesPanel.add(backgroundMessagePanel, gbc);

        // Start the timer to refresh online users
        startOnlineUsersRefreshTimer();
    }
    
    private void createLeftPanel() {
        leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(250, getHeight()));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, ThemeUtil.LIGHT_GRAY));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, ThemeUtil.LIGHT_GRAY));
        headerPanel.setPreferredSize(new Dimension(250, 60));
        
        JLabel titleLabel = new JLabel("My Chat");
        titleLabel.setFont(ThemeUtil.HEADER_FONT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
  
        JLabel headerIcon = new JLabel(IconUtils.getImageIcon("/icon/Message.png", 50, 50));
        headerIcon.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        
        headerPanel.add(headerIcon, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Chat list
        chatListPanel = new JPanel();
        chatListPanel.setLayout(new BoxLayout(chatListPanel, BoxLayout.Y_AXIS));
        chatListPanel.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(chatListPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // điều chỉnh độ nhạy của thanh cuộn (scrollbar) khi người dùng sử dụng chuột để cuộn. 
        
        // Footer
        User currentUser = Authentication.getUser();

        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(Color.GRAY);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        

        AvatarPanel avatarPanel = new AvatarPanel(currentUser.getUsername(), currentUser.getAvatarPath(), 40);
        JLabel userLabel = new JLabel(currentUser.getUsername());
        userLabel.setFont(ThemeUtil.HEADER_FONT);
        userLabel.setForeground(Color.WHITE);
        userLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        buttonPanel.setBackground(Color.GRAY);
        JButton logoutButton = ButtonCustom.createButtonCustom("Log out", ThemeUtil.SECONDARY_COLOR, ThemeUtil.TEXT_COLOR);
        buttonPanel.add(logoutButton, BorderLayout.CENTER);

        JLabel settingIcon = new JLabel(IconUtils.getImageIcon("/icon/setting.png", 30, 30));
        settingIcon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        settingIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        settingIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Open settings dialog
                System.out.println( "Open settings dialog");
            }
        });

        footerPanel.add(avatarPanel, BorderLayout.WEST);
        footerPanel.add(userLabel, BorderLayout.CENTER);
        footerPanel.add(settingIcon, BorderLayout.EAST);
        footerPanel.add(buttonPanel, BorderLayout.SOUTH);


        logoutButton.addActionListener(e -> {
            ApiResponse response = AuthService.logout();
            if (response.getCode().equals("200")) {
                System.out.println("Logout successful");
                // redirect to login page
                new LoginPage().setVisible(true);
                this.dispose();
            } else {
                System.err.println("Logout failed: " + response.getMessage());
            }
        });
        
        // Add components to left panel
        leftPanel.add(headerPanel, BorderLayout.NORTH);
        leftPanel.add(scrollPane, BorderLayout.CENTER);
        leftPanel.add(footerPanel, BorderLayout.SOUTH);
        
        // Load chat list
        loadChatList();
    }
    
    private void createCenterPanel() {
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(240, 240, 240));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, ThemeUtil.LIGHT_GRAY));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));
        
        currentChatNameLabel = new JLabel("Select a chat");
        currentChatNameLabel.setFont(ThemeUtil.HEADER_FONT);
        currentChatNameLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        
        headerPanel.add(currentChatNameLabel, BorderLayout.WEST);
        headerPanel.add(Box.createHorizontalStrut(15), BorderLayout.EAST); // Mục đích là tạo một khoảng đệm (padding) 15 pixels ở bên phải của header
        
        // Messages area
        messagesPanel = new JPanel();
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        messagesPanel.setBackground(new Color(240, 240, 240));
        
        JScrollPane scrollPane = new JScrollPane(messagesPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // Input area
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        messageField = new JTextField();
        messageField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeUtil.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        messageField.setFont(ThemeUtil.NORMAL_FONT);
        
        JButton attachButton = new JButton();
        attachButton.setIcon(IconUtils.getImageIcon("/icon/FileAttach.png", 24, 24));
        attachButton.setBorderPainted(false);
        attachButton.setContentAreaFilled(false);
        attachButton.setFocusPainted(false);
        attachButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        attachButton.addActionListener(e -> {
            // Open file chooser to select a file to attach
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            fileChooser.setDialogTitle("Select a file to attach");
            int result = fileChooser.showOpenDialog(this);


            if (result == JFileChooser.APPROVE_OPTION) {
                // Get the selected file
                File selectedFile = fileChooser.getSelectedFile();
                // Handle file attachment logic here
                
                if (currentChat == null) {
                    JOptionPane.showMessageDialog(this, "Please select a chat to send the file", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Create loading dialog with custom spinner
                JDialog loadingDialog = new JDialog(this, "Uploading...", true);
                loadingDialog.setSize(250, 150);
                loadingDialog.setLocationRelativeTo(this);
                loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                loadingDialog.setResizable(false);
                
                // Create loading panel
                JPanel loadingPanel = new JPanel(new BorderLayout());
                loadingPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                
                // Add custom spinner
                LoadingSpinner spinner = new LoadingSpinner();
                JLabel loadingLabel = new JLabel("Uploading...", JLabel.CENTER);
                loadingLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                
                JPanel centerPanel = new JPanel(new BorderLayout());
                centerPanel.add(spinner, BorderLayout.CENTER);
                centerPanel.add(loadingLabel, BorderLayout.SOUTH);
                
                loadingPanel.add(centerPanel, BorderLayout.CENTER);
                loadingDialog.add(loadingPanel);
                
                // Disable the attach button during upload
                attachButton.setEnabled(false);
                
                // Start spinner
                spinner.start();
                
                // image file
                if (selectedFile.isFile() && selectedFile.getName().toLowerCase().endsWith(".png") || selectedFile.getName().toLowerCase().endsWith(".jpg") || selectedFile.getName().toLowerCase().endsWith(".jpeg")) {
                    // Perform upload in background thread
                    SwingWorker<String, Void> uploadWorker = new SwingWorker<String, Void>() {
                        @Override
                        protected String doInBackground() throws Exception {
                            return CloudinaryUtils.uploadImage(selectedFile);
                        }
                        
                        @Override
                        protected void done() {
                            try {
                                String imageUrl = get();
                                
                                // Create a message request for the image
                                MessageRequest messageRequest = MessageRequest.builder()
                                    .senderId(Authentication.getUser().getId())
                                    .senderUsername(Authentication.getUser().getUsername())
                                    .chatId(currentChat.getId())
                                    .imagePath(imageUrl)
                                    .messageType("IMAGE")
                                    .requestType("CREATE")
                                    .build();

                                // Send message request to server
                                outObject.writeObject(ApiRequest.builder()
                                    .method("POST")
                                    .url("/messages")
                                    .headers(Authentication.getUser().getId())
                                    .payload(messageRequest)
                                    .build());
                                outObject.flush();

                            } catch (Exception ex) {
                                SwingUtilities.invokeLater(() -> {
                                    JOptionPane.showMessageDialog(ChatPage.this, 
                                        "Failed to upload image: " + ex.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
                                });
                                ex.printStackTrace();
                            } finally {
                                // Close loading dialog and re-enable button
                                SwingUtilities.invokeLater(() -> {
                                    spinner.stop();
                                    loadingDialog.dispose();
                                    attachButton.setEnabled(true);
                                });
                            }
                        }
                    };
                    
                    // Show loading dialog and start upload
                    uploadWorker.execute();
                    loadingDialog.setVisible(true);
                    
                } else {
                    // Perform upload in background thread
                    SwingWorker<String, Void> uploadWorker = new SwingWorker<String, Void>() {
                        @Override
                        protected String doInBackground() throws Exception {
                            return CloudinaryUtils.uploadFile(selectedFile);
                        }
                        
                        @Override
                        protected void done() {
                            try {
                                String fileUrl = get();

                                // Create a message request for the file
                                MessageRequest messageRequest = MessageRequest.builder()
                                    .senderId(Authentication.getUser().getId())
                                    .senderUsername(Authentication.getUser().getUsername())
                                    .chatId(currentChat.getId())
                                    .attachmentPath(fileUrl)
                                    .content(selectedFile.getName()) // Set file name as content
                                    .messageType("FILE")
                                    .requestType("CREATE")
                                    .build();

                                // Send message request to server
                                outObject.writeObject(ApiRequest.builder()
                                    .method("POST")
                                    .url("/messages")
                                    .headers(Authentication.getUser().getId())
                                    .payload(messageRequest)
                                    .build());
                                outObject.flush();
                                
                            } catch (Exception ex) {
                                SwingUtilities.invokeLater(() -> {
                                    JOptionPane.showMessageDialog(ChatPage.this, 
                                        "Failed to upload image: " + ex.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
                                });
                                ex.printStackTrace();
                            } finally {
                                // Close loading dialog and re-enable button
                                SwingUtilities.invokeLater(() -> {
                                    spinner.stop();
                                    loadingDialog.dispose();
                                    attachButton.setEnabled(true);
                                });
                            }
                        }
                    };
                    
                    // Show loading dialog and start upload
                    uploadWorker.execute();
                    loadingDialog.setVisible(true);  
                }
            }
        });
        
        sendButton = new JButton();
        sendButton.setIcon(IconUtils.getImageIcon("/icon/Send_Icon.png", 24, 24));
        sendButton.setBorderPainted(false);
        sendButton.setContentAreaFilled(false);
        sendButton.setFocusPainted(false);
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sendButton.addActionListener(e -> sendMessage());
        
        // Add action listener to message field for Enter key
        messageField.addActionListener(e -> sendMessage());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(attachButton);
        buttonPanel.add(sendButton);
        
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Add components to center panel
        centerPanel.add(headerPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(inputPanel, BorderLayout.SOUTH);
    }
    
    private void createRightPanel() {
        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(250, getHeight()));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, ThemeUtil.LIGHT_GRAY));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.GRAY);
        headerPanel.setPreferredSize(new Dimension(250, 60));

        JLabel headerIcon = new JLabel(IconUtils.getImageIcon("/icon/Users.png", 40, 40));
        headerIcon.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        
        JLabel onlineLabel = new JLabel("Online");
        onlineLabel.setFont(ThemeUtil.HEADER_FONT);
        onlineLabel.setForeground(Color.WHITE);
        onlineLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        
        headerPanel.add(headerIcon, BorderLayout.WEST);
        headerPanel.add(onlineLabel, BorderLayout.CENTER);
        
        // Online users list
        onlineUsersPanel = new JPanel();
        onlineUsersPanel.setLayout(new BoxLayout(onlineUsersPanel, BoxLayout.Y_AXIS));
        onlineUsersPanel.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(onlineUsersPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Footer Panel
        JPanel footerPanel = new JPanel(new GridLayout(1, 1, 10, 0));
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton createGroupButton = ButtonCustom.createButtonCustom("Create Group Chat", ThemeUtil.PRIMARY_COLOR, Color.WHITE);
        createGroupButton.addActionListener(e -> {
            createGroupChat();
        });

        footerPanel.add(createGroupButton);
        
        // Add components to right panel
        rightPanel.add(headerPanel, BorderLayout.NORTH);
        rightPanel.add(scrollPane, BorderLayout.CENTER);
        rightPanel.add(footerPanel, BorderLayout.SOUTH);
        
        // Load online users and start refresh timer
        loadOnlineUsers();
        startOnlineUsersRefreshTimer();
    }

    private void createGroupChat() {
        // Show JcheckBox of all users to select for group chat
        ApiResponse response = UserService.getAllUsers();
        if (!response.getCode().equals("200")) {
            JOptionPane.showMessageDialog(this, "Failed to load users: " + response.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<UserResponse> userResponses = (List<UserResponse>) response.getData();

        List<User> users = new ArrayList<>();
        for (UserResponse userResponse : userResponses) {
            if (!userResponse.getId().equals(Authentication.getUser().getId())) { // Skip current user
                users.add(UserConverter.converterToUser(userResponse));
            }
        }

        if (users.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No users available to create a group chat", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Create a dialog to select users for group chat
        JDialog dialog = new JDialog(this, "Create Group Chat", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(1000, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        ((JComponent) dialog.getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        dialog.setResizable(false);
        dialog.setIconImage(IconUtils.getImageIcon("/icon/Chat.png", 24, 24).getImage());
        dialog.setModal(true);
        dialog.setLayout(new BorderLayout());
        dialog.setTitle("Create Group Chat");

        // Name field for group chat
        JTextField groupNameField = new JTextField();
        groupNameField.setFont(ThemeUtil.HEADER_FONT);
        groupNameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ThemeUtil.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        groupNameField.setPreferredSize(new Dimension(300, 40));
        JPanel namePanel = new JPanel(new BorderLayout());
        namePanel.setOpaque(false);
        namePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        JLabel groupNameLabel = new JLabel("Group Name:");
        groupNameLabel.setFont(ThemeUtil.HEADER_FONT);
        namePanel.add(groupNameLabel, BorderLayout.WEST);
        namePanel.add(groupNameField, BorderLayout.CENTER);

        dialog.add(namePanel, BorderLayout.NORTH);

        // Create a panel with checkboxes for each user
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        List<JCheckBox> checkBoxes = new ArrayList<>();
        for (User user : users) {
            UserItem userItem = new UserItem(user);
            JCheckBox checkBox = new JCheckBox();
            checkBox.setFont(ThemeUtil.NORMAL_FONT);

            JPanel checkBoxPanel = new JPanel(new BorderLayout());
            checkBoxPanel.setOpaque(false);
            checkBoxPanel.add(userItem, BorderLayout.CENTER);
            checkBoxPanel.add(checkBox, BorderLayout.EAST);

            userPanel.add(checkBoxPanel);
            checkBoxes.add(checkBox);
        }

        dialog.add(userPanel, BorderLayout.CENTER);

        JButton createButton = ButtonCustom.createButtonCustom("Create", ThemeUtil.PRIMARY_COLOR, Color.WHITE);
        createButton.addActionListener(ev -> {
            List<User> selectedUsers = new ArrayList<>();
            for (JCheckBox checkBox : checkBoxes) {
                if (checkBox.isSelected()) {
                    selectedUsers.add(users.get(checkBoxes.indexOf(checkBox)));
                }
            }

            String groupName = groupNameField.getText().trim();
            if (groupName.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Group name cannot be empty", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (selectedUsers.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please select at least one user to create a group chat", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            List<String> participantIds = new ArrayList<>();
            participantIds.add(Authentication.getUser().getId());
            for (User user : selectedUsers) {
                System.out.println("Selected user: " + user.getUsername());
                participantIds.add(user.getId());
            }

            ChatRequest chatRequest = ChatRequest.builder()
                .name(groupName)
                .participantIds(participantIds)
                .isGroup(true)
                .requestType("create")
                .build();

            ApiResponse res = (ApiResponse) ChatService.createChat(chatRequest);

            if (res.getCode().equals("200")) {
                loadChatList();
            } else if (res.getCode().equals("400")) {
                
            } else {
                JOptionPane.showMessageDialog(null, res.getMessage(), "ERROR", JOptionPane.INFORMATION_MESSAGE);

            }

            dialog.dispose();
        });
        dialog.add(createButton, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void loadChatList() {
        chatListPanel.removeAll();
        myChats.clear();
        
        ApiResponse response = (ApiResponse) UserService.getMyChats();

        if (response.getCode().equals("200")) {
            List<ChatResponse> chatResponses = (List<ChatResponse>) response.getData();
            for (ChatResponse chatResponse : chatResponses) {
                Chat chat = ChatConverter.converterToChat(chatResponse);
                if (chat != null) {
                    myChats.add(chat);
                }
            }
        } else {
            System.err.println("Failed to load chats: " + response.getMessage());
        }

        if (myChats.isEmpty()) {
            // Display a message when no chats are available
            JLabel noChatsLabel = new JLabel("No conversations yet");
            noChatsLabel.setFont(ThemeUtil.NORMAL_FONT);
            noChatsLabel.setForeground(Color.GRAY);
            noChatsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            chatListPanel.add(Box.createVerticalStrut(20));
            chatListPanel.add(noChatsLabel);
            chatListPanel.add(Box.createVerticalStrut(20));
        } else {
            for (Chat chat : myChats) {
                ContactItem contactItem = new ContactItem(chat);
                contactItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
                    
                // Set selected if this is the current chat
                if (currentChat != null && chat.getId().equals(currentChat.getId())) {
                    contactItem.setSelected(true);
                }
                    
                contactItem.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        // Deselect all contacts
                        for (Component comp : chatListPanel.getComponents()) {
                            if (comp instanceof ContactItem) {
                                ((ContactItem) comp).setSelected(false);
                            }
                        }
                        
                        // Select this contact
                        contactItem.setSelected(true);
                        
                        // Load chat
                        loadChat(contactItem.getChat());
                    }
                });
                
                chatListPanel.add(contactItem);
            }
        }
        
        chatListPanel.revalidate();
        chatListPanel.repaint();
    }
    
    private void loadOnlineUsers() {
        onlineUsersPanel.removeAll();

        List<User> onlineUsers = new ArrayList<>();
        
        ApiResponse response = (ApiResponse) UserService.getOnlineUsers();
        if (response.getCode().equals("200")) {
            List<UserResponse> userResponses = (List<UserResponse>) response.getData();
            for (UserResponse userResponse : userResponses) {
                onlineUsers.add(UserConverter.converterToUser(userResponse));
            }
        } else {
            System.err.println("Failed to load online users: " + response.getMessage());
        }

        if (onlineUsers.isEmpty()) {
            // Display a message when no chats are available
            JLabel noUserOnlineLabel = new JLabel("No users online");
            noUserOnlineLabel.setFont(ThemeUtil.NORMAL_FONT);
            noUserOnlineLabel.setForeground(Color.GRAY);
            noUserOnlineLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            onlineUsersPanel.add(Box.createVerticalStrut(20));
            onlineUsersPanel.add(noUserOnlineLabel);
            onlineUsersPanel.add(Box.createVerticalStrut(20));
        } else {
            for (User user : onlineUsers) {
                if (user.getId().equals(Authentication.getUser().getId())) {
                    // Skip current user
                    continue;
                }

                OnlineUserItem userItem = new OnlineUserItem(user, new ChatRefreshCallback() {
                    @Override
                    public void refreshChatList() {
                        loadChatList();
                    }
                });
                userItem.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
                
                onlineUsersPanel.add(userItem);
            }
        }
            
        onlineUsersPanel.revalidate();
        onlineUsersPanel.repaint();
    }
    
    private void loadChat(Chat chat) {
        currentChat = chat;

        // Update chat name in header
        currentChatNameLabel.setText(chat.getName());
        
        // Clear messages panel
        messagesPanel.removeAll();

        // setLayout 
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        
        // Group messages by date
        Map<String, List<Message>> messagesByDate = new HashMap<>();
        
        // Format for date grouping
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        LocalDateTime today = LocalDateTime.now();
        String todayStr = today.format(dateFormatter);
        String yesterdayStr = today.minusDays(1).format(dateFormatter);

        // Group messages by date
        for (Message message : chat.getMessages()) {
            String dateStr = message.getCreatedAt().format(dateFormatter);
            
            // Convert date to more readable format for display
            String displayDate;
            if (dateStr.equals(todayStr)) {
                displayDate = "Today";
            } else if (dateStr.equals(yesterdayStr)) {
                displayDate = "Yesterday"; 
            } else {
                displayDate = dateStr;
            }
            
            messagesByDate.computeIfAbsent(displayDate, key -> new ArrayList<>()).add(message);
        }

        // Sort messages within each date group by time (oldest first for display)
        for (List<Message> messages : messagesByDate.values()) {
            messages.sort((m1, m2) -> m1.getCreatedAt().compareTo(m2.getCreatedAt()));
        }

        List<String> sortedDates = new ArrayList<>(messagesByDate.keySet());
        sortedDates.sort((d1, d2) -> {
            // Handle special cases for "Today" and "Yesterday"
            if (d1.equals("Today") && d2.equals("Yesterday")) return 1;  // Today after Yesterday
            if (d1.equals("Yesterday") && d2.equals("Today")) return -1; // Yesterday before Today
            if (d1.equals("Today")) return 1;  // Today is latest (last in list)
            if (d2.equals("Today")) return -1;
            if (d1.equals("Yesterday")) return 1;  // Yesterday comes after other dates
            if (d2.equals("Yesterday")) return -1;

            // Convert date strings to LocalDate for comparison
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
                LocalDate date1 = LocalDate.parse(d1, formatter);
                LocalDate date2 = LocalDate.parse(d2, formatter);
                return date1.compareTo(date2);  // Sort by actual date (oldest first)
            } catch (Exception e) {
                // Fallback to string comparison if parsing fails
                return d1.compareTo(d2);
            }
        });

        // Add messages by date groups
        for (String date : sortedDates) {
            // Add date separator
            messagesPanel.add(new DateSeparator(date));
            
            // Add messages for this date
            List<Message> messages = messagesByDate.get(date);
            for (Message message : messages) {
                ChatBubble bubble = new ChatBubble(message, outObject);
                messagesPanel.add(bubble);
            }
        }

        // Add vertical glue to push messages to the top and prevent stretching
        messagesPanel.add(Box.createVerticalGlue());

        // Đảm bảo messagesPanel không kéo giãn các component con
        messagesPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        
        messagesPanel.revalidate();
        messagesPanel.repaint();

        SwingUtilities.invokeLater(() -> {
            JScrollPane scrollPane = (JScrollPane) messagesPanel.getParent().getParent();
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }
    
    private void sendMessage() {
        if (currentChat == null) {
            return;
        }
        
        String content = messageField.getText().trim();
        if (content.isEmpty()) {
            return;
        }

        MessageRequest messageRequest = MessageRequest.builder()
            .senderId(Authentication.getUser().getId())
            .senderUsername(Authentication.getUser().getUsername())
            .chatId(currentChat.getId())
            .content(content)
            .messageType("TEXT")
            .requestType("CREATE")
            .build();


        try {
            // Send message request to server
            outObject.writeObject(ApiRequest.builder()
                .method("POST")
                .url("/messages")
                .headers(Authentication.getUser().getId())
                .payload(messageRequest)
                .build());

            outObject.flush();

            // Clear input field
            messageField.setText("");

        } catch (IOException e) {
            System.err.println("Failed to send message: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Failed to send message: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }


    
    // Timer to periodically refresh online users
    private void startOnlineUsersRefreshTimer() {
        // Cancel existing timer if it exists
        if (onlineUsersRefreshTimer != null) {
            onlineUsersRefreshTimer.cancel();
        }
        
        // Schedule a timer to refresh online users every 5 seconds
        onlineUsersRefreshTimer = new Timer();
        onlineUsersRefreshTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Run on the EDT to avoid thread issues with Swing
                SwingUtilities.invokeLater(() -> {
                    loadOnlineUsers();
                    System.out.println("Refreshing online users list...");
                });
            }
        }, 5000, 5000); // Initial delay and period in milliseconds (5 seconds)
    }
    
    private void stopOnlineUsersRefreshTimer() {
        if (onlineUsersRefreshTimer != null) {
            onlineUsersRefreshTimer.cancel();
            onlineUsersRefreshTimer = null;
        }
    }
    
    @Override
    public void dispose() {
        // Stop the timer when the window is closed
        stopOnlineUsersRefreshTimer();
        super.dispose();
    }
    
    /**
     * Show a notification when a user's online status changes
     */
    private void showUserStatusNotification(User user, boolean isOnline) {
        // Create a notification panel
        JPanel notificationPanel = new JPanel(new BorderLayout());
        notificationPanel.setName("statusNotification");
        notificationPanel.setBackground(isOnline ? new Color(230, 255, 230) : new Color(255, 230, 230));
        notificationPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        JLabel messageLabel = new JLabel(
            user.getUsername() + (isOnline ? " is now online" : " has gone offline")
        );
        messageLabel.setFont(ThemeUtil.NORMAL_FONT);
        messageLabel.setForeground(isOnline ? new Color(0, 100, 0) : new Color(100, 0, 0));
        
        notificationPanel.add(messageLabel, BorderLayout.CENTER);
        
        // Remove any existing notification panels
        for (Component c : rightPanel.getComponents()) {
            if (c instanceof JPanel && "statusNotification".equals(c.getName())) {
                rightPanel.remove(c);
            }
        }
        
        // Add the new notification panel
        rightPanel.add(notificationPanel, BorderLayout.NORTH);
        rightPanel.revalidate();
        rightPanel.repaint();
        
        // Schedule removal after 3 seconds
        final JPanel finalPanel = notificationPanel;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    rightPanel.remove(finalPanel);
                    rightPanel.revalidate();
                    rightPanel.repaint();
                });
            }
        }, 3000);
    }
}
