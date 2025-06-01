package config;

import java.util.ArrayList;
import java.util.List;

import models.User;

public class UserOnlineList {
    private static List<User> userOnlines = new ArrayList<>();

    public static void setUserOnlines(List<User> users) {
        userOnlines = users;
    }
    public static List<User> getUserOnlines() {
        return userOnlines;
    }

    public static void addUserOnline(User user) {
        userOnlines.add(user);
    }

    public static void removeUserOnline(String userId) {
        userOnlines.removeIf(user -> user.getId().equals(userId));
    }
}
