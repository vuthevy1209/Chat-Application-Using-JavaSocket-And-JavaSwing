package utils;

import java.io.BufferedReader;
import java.io.IOException;

public class RequestUtils {
    public static String extractPayload(String request) {
        String payloadKeyword = "Payload: ";
        int payloadIndex = request.indexOf(payloadKeyword);

        if (payloadIndex != -1) {
            // Lấy phần chuỗi JSON sau từ khóa "payload: "
            String payload = request.substring(payloadIndex + payloadKeyword.length()).trim();
            
            // Kiểm tra và loại bỏ ký tự xuống dòng hoặc khoảng trắng dư thừa nếu có
            return payload.replaceAll("\r|\n", "").trim();
        }
        return null; // Trả về null nếu không tìm thấy payload
    }

}