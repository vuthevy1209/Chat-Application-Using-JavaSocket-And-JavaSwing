package utils;

public class StringUtils {
    public static String getInitials(String text) {
        if (text == null || text.isEmpty()) {
            return "?";
        }
        return text.substring(0, 1).toUpperCase();
    }
}
