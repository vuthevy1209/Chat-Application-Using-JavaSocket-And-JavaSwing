package config;

import java.util.ArrayList;
import java.util.List;

import models.User;

public class Authentication {
    private static User currentUser = null;
    private static List<User> userOnlines = new ArrayList<>();

    public static void setUser(User user) {
        currentUser = user;
    }

    public static User getUser() {
        return currentUser;
    }

    public static void setUserOnlines(List<User> users) {
        userOnlines = users;
    }
    public static List<User> getUserOnlines() {
        return userOnlines;
    }
}
