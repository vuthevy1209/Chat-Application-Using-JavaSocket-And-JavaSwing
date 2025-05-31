package utils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import config.CloudinaryConfig;

public class CloudinaryUtils {
    public static String uploadImage(File file) throws IOException {
        try {
            Map<String, Object> params = Map.of(
                "upload_preset", "vuthevy1209" // Tên upload preset trên Cloudinary
            );

            // Upload file trực tiếp lên Cloudinary
            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = (Map<String, Object>) CloudinaryConfig.getCloudinary()
                .uploader()
                .upload(file, params);

            // Trả về URL của file sau khi upload
            return (String) uploadResult.get("secure_url");
        } catch (Exception e) {
            throw new IOException("Error uploading image", e);
        }
    }
}
