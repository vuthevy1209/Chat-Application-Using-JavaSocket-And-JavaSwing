package components;

import models.Message;
import utils.CloudinaryUtils;
import utils.IconUtils;
import utils.ThemeUtil;

import javax.imageio.ImageIO;
import javax.swing.*;

// import org.w3c.dom.events.MouseEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import components.customs.AvatarPanel;
import config.Authentication;
import dto.request.ApiRequest;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.time.format.DateTimeFormatter;

public class ChatBubble extends JPanel {
    private Message message;
    private boolean isSent;
    private ObjectOutputStream outObject;


    public Message getMessage() {
        return message;
    }
    
    public ChatBubble(Message message, ObjectOutputStream outObject) {
        this.outObject = outObject;
        this.message = message;
        this.isSent = message.getSenderId().equals(Authentication.getUser().getId());

        setOpaque(false);
        setLayout(new BorderLayout());
        
        // Create main container with padding
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setOpaque(false);
        mainContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        if (message.getMessageType().equals("TEXT")) {
            // Create message content panel with fixed width
            JPanel contentPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Set bubble color based on sent/received
                    if (isSent) {
                        g2.setColor(ThemeUtil.CHAT_BUBBLE_SENT);
                    } else {
                        g2.setColor(ThemeUtil.CHAT_BUBBLE_RECEIVED);
                    }
                    
                    // Draw rounded rectangle
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                    g2.dispose();
                }
                
                @Override
                public Dimension getPreferredSize() {
                    // Cố định chiều rộng, linh hoạt chiều cao
                    Dimension superSize = super.getPreferredSize();
                    return new Dimension(300, superSize.height);
                }
            };
            contentPanel.setOpaque(false);
            contentPanel.setLayout(new BorderLayout(5, 5));

            // Username label + Created at label
            JLabel headerLabel = new JLabel("@" + message.getSenderUsername() + " - " + message.getCreatedAt().format(DateTimeFormatter.ofPattern("HH:mm")));
            headerLabel.setFont(new Font("Arial", Font.BOLD, 15));
            headerLabel.setForeground(ThemeUtil.DARK_GRAY);
            headerLabel.setBorder(BorderFactory.createEmptyBorder(2, 12, 0, 12));

            // Create message text
            JTextArea textArea = new JTextArea(message.getContent());
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setOpaque(false);
            textArea.setFont(ThemeUtil.NORMAL_FONT);
            textArea.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

            contentPanel.add(headerLabel, BorderLayout.NORTH);
            contentPanel.add(textArea, BorderLayout.CENTER);

            // Create wrapper panel for alignment
            JPanel wrapperPanel = new JPanel(new BorderLayout());
            wrapperPanel.setOpaque(false);
            
            if (isSent) {
                // Tin nhắn của mình - nằm bên phải
                wrapperPanel.add(contentPanel, BorderLayout.EAST);
                // Thêm menu chuột phải nếu là tin nhắn của mình
                addRightClickMenu(contentPanel);
            } else {
                // Tin nhắn của người khác - nằm bên trái với avatar
                JPanel leftPanel = new JPanel(new BorderLayout(10, 0));
                leftPanel.setOpaque(false);
                
                AvatarPanel avatarPanel = new AvatarPanel(message.getSenderUsername(), null, 40);
                leftPanel.add(avatarPanel, BorderLayout.WEST); 
                leftPanel.add(contentPanel, BorderLayout.CENTER);
                
                wrapperPanel.add(leftPanel, BorderLayout.WEST);
            }
            
            mainContainer.add(wrapperPanel, BorderLayout.CENTER);
            add(mainContainer, BorderLayout.CENTER);
        } else if (message.getMessageType().equals("IMAGE")) {
            try {
                URL url = new URL(message.getImagePath());
                BufferedImage img = ImageIO.read(url);
                
                // Tạo image panel với kích thước cố định
                JPanel imagePanel = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        
                        // Vẽ background với góc bo tròn
                        g2.setColor(Color.WHITE);
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                        
                        // Tính toán kích thước ảnh giữ tỷ lệ
                        int fixedWidth = 250; // Chiều ngang cố định
                        int originalWidth = img.getWidth();
                        int originalHeight = img.getHeight();
                        
                        // Tính chiều cao theo tỷ lệ
                        int scaledHeight = (originalHeight * fixedWidth) / originalWidth;
                        
                        // Vẽ ảnh với padding
                        int padding = 5;
                        g2.drawImage(img, padding, padding, 
                                   fixedWidth - 2*padding, scaledHeight - 2*padding, null);
                        g2.dispose();
                    }
                    
                    @Override
                    public Dimension getPreferredSize() {
                        int fixedWidth = 250;
                        int originalWidth = img.getWidth();
                        int originalHeight = img.getHeight();
                        int scaledHeight = (originalHeight * fixedWidth) / originalWidth;
                        return new Dimension(fixedWidth, scaledHeight + 10); // +10 cho padding
                    }
                };
                imagePanel.setOpaque(false);
                
                // Tạo wrapper panel cho alignment
                JPanel wrapperPanel = new JPanel(new BorderLayout());
                wrapperPanel.setOpaque(false);
                
                if (isSent) {
                    // Tin nhắn của mình - nằm bên phải
                    wrapperPanel.add(imagePanel, BorderLayout.EAST);
                    // Thêm menu chuột phải nếu là tin nhắn của mình
                    addRightClickMenu(imagePanel);

                } else {
                    // Tin nhắn của người khác - nằm bên trái với avatar
                    JPanel leftPanel = new JPanel(new BorderLayout(10, 0));
                    leftPanel.setOpaque(false);
                    
                    AvatarPanel avatarPanel = new AvatarPanel(message.getSenderUsername(), null, 40);
                    leftPanel.add(avatarPanel, BorderLayout.WEST);
                    leftPanel.add(imagePanel, BorderLayout.CENTER);
                    
                    wrapperPanel.add(leftPanel, BorderLayout.WEST);
                }
                
                mainContainer.add(wrapperPanel, BorderLayout.CENTER);
                add(mainContainer, BorderLayout.CENTER);

            } catch (Exception e) {
                e.printStackTrace();
                // Hiển thị error message nếu không load được ảnh
                JLabel errorLabel = new JLabel("Không thể tải hình ảnh");
                errorLabel.setForeground(Color.RED);
                mainContainer.add(errorLabel, BorderLayout.CENTER);
                add(mainContainer, BorderLayout.CENTER);
            }
        } else if (message.getMessageType().equals("FILE")) {
            String fileName = message.getContent();

            // Icon
            ImageIcon attachIcon = IconUtils.getImageIcon("/icon/attach.png", 20, 20);
            ImageIcon downloadIcon = IconUtils.getImageIcon("/icon/Download.png", 20, 20);
            
            // Tạo file panel với nền trắng bo góc
            JPanel filePanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Vẽ nền trắng với góc bo tròn
                    g2.setColor(Color.WHITE);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                    
                    // Vẽ viền nhẹ
                    g2.setColor(new Color(220, 220, 220));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                    
                    g2.dispose();
                }
                
                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(300, 50);
                }
            };
            filePanel.setOpaque(false);
            filePanel.setLayout(new BorderLayout(10, 0));
            filePanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            
            // Icon attach
            JLabel attachLabel = new JLabel(attachIcon);
            
            // File name
            JLabel fileNameLabel = new JLabel(fileName);
            fileNameLabel.setFont(ThemeUtil.NORMAL_FONT);
            fileNameLabel.setForeground(ThemeUtil.DARK_GRAY);
            
            // Download button
            JButton downloadButton = new JButton(downloadIcon);
            downloadButton.setBorderPainted(false);
            downloadButton.setContentAreaFilled(false);
            downloadButton.setFocusPainted(false);
            downloadButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            downloadButton.setToolTipText("Tải xuống");
            
            // Thêm action listener cho download button (có thể implement sau)
            downloadButton.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Select location to save the file");
                fileChooser.setSelectedFile(new File(fileName));
                int userSelection = fileChooser.showSaveDialog(null);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String saveDirectory = selectedFile.getParent();
                    String saveFileName = selectedFile.getName();
                    
                    // Tạo thread riêng để tải file
                    SwingWorker<Boolean, Void> downloadWorker = new SwingWorker<Boolean, Void>() {
                        @Override
                        protected Boolean doInBackground() throws Exception {
                            return CloudinaryUtils.downloadFile(message.getAttachmentPath(), saveDirectory, saveFileName);
                        }
                        
                        @Override
                        protected void done() {
                            try {
                                boolean success = get();
                                if (!success) {
                                    System.err.println("Download failed");
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    };
                    
                    downloadWorker.execute();
                }
            });
            
            // Thêm các components vào file panel
            filePanel.add(attachLabel, BorderLayout.WEST);
            filePanel.add(fileNameLabel, BorderLayout.CENTER);
            filePanel.add(downloadButton, BorderLayout.EAST);
            
            // Tạo wrapper panel cho alignment
            JPanel wrapperPanel = new JPanel(new BorderLayout());
            wrapperPanel.setOpaque(false);
            
            if (isSent) {
                // Tin nhắn của mình - nằm bên phải
                wrapperPanel.add(filePanel, BorderLayout.EAST);
                // Thêm menu chuột phải nếu là tin nhắn của mình
                addRightClickMenu(filePanel);

            } else {
                // Tin nhắn của người khác - nằm bên trái với avatar
                JPanel leftPanel = new JPanel(new BorderLayout(10, 0));
                leftPanel.setOpaque(false);
                
                AvatarPanel avatarPanel = new AvatarPanel(message.getSenderUsername(), null, 40);
                leftPanel.add(avatarPanel, BorderLayout.WEST);
                leftPanel.add(filePanel, BorderLayout.CENTER);
                
                wrapperPanel.add(leftPanel, BorderLayout.WEST);
            }
            
            mainContainer.add(wrapperPanel, BorderLayout.CENTER);
            add(mainContainer, BorderLayout.CENTER);
        }
    }

    private void addRightClickMenu(JComponent component) {
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showDeleteConfirmation(e);
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showDeleteConfirmation(e);
                }
            }
        });
    }

    private void showDeleteConfirmation(MouseEvent e) {
        JPopupMenu popupMenu = new JPopupMenu();
        
        JMenuItem deleteItem = new JMenuItem("Xóa tin nhắn");
        deleteItem.setIcon(IconUtils.getImageIcon("/icon/x.png", 16, 16)); 
        deleteItem.addActionListener(event -> {
            int result = JOptionPane.showConfirmDialog(
                null,
                "Bạn có chắc chắn muốn xóa tin nhắn này không?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (result == JOptionPane.YES_OPTION) {
                deleteMessage();
            }
        });
        
        popupMenu.add(deleteItem);
        popupMenu.show(e.getComponent(), e.getX(), e.getY());
    }

    private void deleteMessage() {
        SwingWorker<Boolean, Void> deleteWorker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                if (outObject == null) {
                    return false; // Không có connection
                }
                
                ApiRequest request = ApiRequest.builder()
                    .method("DELETE")
                    .url("/messages")
                    .headers(Authentication.getUser().getId())
                    .payload(ChatBubble.this.getMessage().getId())
                    .build();

                try {
                    outObject.writeObject(request);
                    outObject.flush();
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                } 
            }

            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        JOptionPane.showMessageDialog(
                            null,
                            "Tin nhắn đã được xóa thành công!",
                            "Thông báo",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    } else {
                        JOptionPane.showMessageDialog(
                            null,
                            "Không thể xóa tin nhắn. Vui lòng thử lại!",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                        null,
                        "Đã xảy ra lỗi khi xóa tin nhắn!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };
        
        deleteWorker.execute();
    }
}