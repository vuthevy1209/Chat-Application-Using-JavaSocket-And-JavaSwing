package config;

public class Authentication {
    private static final ThreadLocal<String> userId = new ThreadLocal<>();     

    public static String getUserId() {
        return userId.get();
    }

    public static void setUserId(String userId) {
        Authentication.userId.set(userId);
    }
}
