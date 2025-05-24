package utils;

import java.io.BufferedWriter;
import java.io.IOException;

public class ResponseUtils {
    public static void sendResponse(BufferedWriter out, String status, String body) throws IOException {
        out.write(status + "\r\n");
        out.write("Content-Type: text/plain\r\n");
        out.write("Content-Length: " + body.length() + "\r\n");
        out.write("\r\n");
        out.write(body);
        out.flush();
    }
}
