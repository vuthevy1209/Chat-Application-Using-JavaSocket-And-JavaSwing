package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
    public static String dbNameDefault = "chat_app";
    public static String usernameDefault = "root";
    public static String passwordDefault = "123456";
    public static String ipDefault = "localhost";

    public static final Connection getConnection() {
        try {
            String dbUrl = "jdbc:mysql://" + ipDefault + ":3306/" + dbNameDefault;
            return DriverManager.getConnection(dbUrl, usernameDefault, passwordDefault);
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public static boolean checkConnection() {
        return getConnection() != null;
    }
}