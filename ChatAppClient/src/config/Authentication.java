package config;

import models.User;

public class Authentication {
    public static User currentUser = null;

    public static void setUser(User user) {
        currentUser = user;
    }

    public static User getUser() {
        return currentUser;
    }
}
