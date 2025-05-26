package page;

import components.ChatBubble;
import components.ContactItem;
import components.DateSeparator;
import components.OnlineUserItem;
import components.customs.*;
import dto.request.ApiRequest;
import dto.request.MessageRequest;
import dto.response.ApiResponse;
import dto.response.ChatResponse;
import dto.response.MessageResponse;
import dto.response.UserResponse;
import models.Message;
import models.User;
import models.Chat;
import utils.IconUtil;
import utils.ThemeUtil;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
        
        // Load initial chat if available
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

        if (!myChats.isEmpty()) {
            loadChat(myChats.get(0));
        }
        
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
  
        JLabel headerIcon = new JLabel(IconUtil.getImageIcon("/icon/Message.png", 50, 50));
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

        footerPanel.add(avatarPanel, BorderLayout.WEST);
        footerPanel.add(userLabel, BorderLayout.CENTER);
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
        attachButton.setIcon(IconUtil.getImageIcon("/icon/FileAttach.png", 24, 24));
        attachButton.setBorderPainted(false);
        attachButton.setContentAreaFilled(false);
        attachButton.setFocusPainted(false);
        attachButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        sendButton = new JButton();
        sendButton.setIcon(IconUtil.getImageIcon("/icon/Send_Icon.png", 24, 24));
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

        JLabel headerIcon = new JLabel(IconUtil.getImageIcon("/icon/Users.png", 40, 40));
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
            JOptionPane.showMessageDialog(this, "Create Group Chat functionality not implemented yet", "Info", JOptionPane.INFORMATION_MESSAGE);
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
                    
                /*
                Đoạn code này xử lý sự kiện khi người dùng nhấp chuột vào một liên hệ (contact) trong danh sách chat. Cụ thể:
                @Override public void mouseClicked(MouseEvent e) - Ghi đè phương thức mouseClicked từ MouseAdapter để xử lý sự kiện khi người dùng nhấp chuột vào contactItem
                // Deselect all contacts - Bỏ chọn tất cả các liên hệ trước đó:
                Duyệt qua tất cả các component trong chatListPanel
                Kiểm tra nếu component là một ContactItem
                Gọi setSelected(false) để bỏ chọn nó (loại bỏ hiệu ứng được chọn)
                // Select this contact - Chọn liên hệ vừa được nhấp:

                Gọi setSelected(true) trên contactItem hiện tại
                Điều này tạo hiệu ứng trực quan để người dùng biết đang chọn cuộc trò chuyện nào
                // Load chat - Tải cuộc trò chuyện đã chọn:

                Gọi phương thức loadChat(chat) để hiển thị tin nhắn của cuộc trò chuyện đó trong cửa sổ chính
                Cập nhật tiêu đề, nội dung tin nhắn và các thành phần liên quan
                    */
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
                        loadChat(chat);
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
                boolean isSent = message.getSenderId().equals(Authentication.getUser().getId());
                ChatBubble bubble = new ChatBubble(message, isSent);
                messagesPanel.add(bubble);
            }
        }
        
        // Add glue at the bottom to push messages up
        /*
            messagesPanel.add(Box.createVerticalGlue());
            Thêm một "vertical glue" vào cuối panel chứa tin nhắn
            "Glue" là một component đặc biệt có thể co giãn để chiếm tất cả không gian còn lại
            Mục đích là "đẩy" tất cả tin nhắn lên phía trên của panel khi không đủ tin nhắn để lấp đầy không gian hiển thị
            Giúp tin nhắn luôn bắt đầu từ phía trên thay vì "trôi nổi" ở giữa panel
            messagesPanel.revalidate(); messagesPanel.repaint();
            revalidate() bắt Swing tính toán lại layout sau khi thêm/xóa component
            repaint() vẽ lại giao diện với các thay đổi mới
            Đảm bảo người dùng nhìn thấy tất cả tin nhắn mới được thêm vào
         */
        // Add vertical glue to push messages to the top and prevent stretching
        messagesPanel.add(Box.createVerticalGlue());

        // Đảm bảo messagesPanel không kéo giãn các component con
        messagesPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        
        messagesPanel.revalidate();
        messagesPanel.repaint();
        
        // Scroll to bottom
        /*
            SwingUtilities.invokeLater(() -> {...});
            Đưa đoạn code vào Event Dispatch Thread của Swing
            Đảm bảo code chạy sau khi giao diện đã được cập nhật hoàn toàn

            JScrollPane scrollPane = (JScrollPane) messagesPanel.getParent().getParent();
            Lấy ra đối tượng JScrollPane chứa messagesPanel
            messagesPanel nằm trong một viewport, viewport nằm trong scrollPane

            vertical.setValue(vertical.getMaximum());
            Thiết lập thanh cuộn xuống vị trí thấp nhất (cuối cùng)
            Đảm bảo người dùng luôn nhìn thấy tin nhắn mới nhất ở dưới cùng
            Mô phỏng hành vi của các ứng dụng chat thông thường khi tin nhắn mới xuất hiện
         */
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
            .chatId(currentChat.getId())
            .content(content)
            .messageType("TEXT")
            .requestType("CREATE")
            .build();

        ApiResponse response = MessageService.sendMessage(messageRequest);
        if (response.getCode().equals("200")) {
            // Clear input field
            messageField.setText("");

            // Reload chat
            ApiResponse res = (ApiResponse) ChatService.getChatById(currentChat.getId());
            Chat chat = ChatConverter.converterToChat((ChatResponse) res.getData());
            loadChat(chat);

            // Reload chat list to update  last message preview
            loadChatList();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to send message: " + response.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Failed to send message: " + response.getMessage());
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
