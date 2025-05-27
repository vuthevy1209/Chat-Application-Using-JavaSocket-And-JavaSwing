package components;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import components.customs.AvatarPanel;
import models.User;
import utils.ThemeUtil;

public class UserItem extends JPanel{
    User user;

    public UserItem(User user) {
        this.user = user;

        setLayout(null);
        setBounds(0, 0, 200, 50);
        setBorder(BorderFactory.createEmptyBorder(5, 20, 15, 20));
        setLayout(new BorderLayout());

        AvatarPanel avatarPanel = new AvatarPanel(user.getUsername(), user.getAvatarPath(), 40);
        JLabel usernameLabel = new JLabel(user.getUsername());
        usernameLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        usernameLabel.setFont(ThemeUtil.HEADER_FONT);
        usernameLabel.setBounds(60, 10, 130, 30);
        
        add(avatarPanel, BorderLayout.WEST);
        add(usernameLabel, BorderLayout.CENTER);
    }

    public User getUser() {
        return user;
    }

    public String getUserId() {
        return user.getId();
    }
}
