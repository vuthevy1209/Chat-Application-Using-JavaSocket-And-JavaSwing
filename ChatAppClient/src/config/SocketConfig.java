package config;

public class SocketConfig {
    private static String IP_ADDRESS = "localhost"; // Default IP address
    private static int PORT = 8080;

    public static String getIpAddress() {
        return IP_ADDRESS;
    }

    public static int getPort() {
        return PORT;
    }

    public static void setIpAddress(String ipAddress) {
        IP_ADDRESS = ipAddress;
    }

    public static void setPort(int port) {
        PORT = port;
    }
}
