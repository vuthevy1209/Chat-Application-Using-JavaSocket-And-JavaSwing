package utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import config.CloudinaryConfig;

public class CloudinaryUtils {
    public static String uploadImage(File file) throws IOException {
        try {
            Map<String, Object> params = Map.of(
                "upload_preset", "vuthevy1209" 
            );

            System.out.println("Uploading file:fsdoiifjsadofj");

            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = (Map<String, Object>) CloudinaryConfig.getCloudinary()
                .uploader()
                .upload(file, params);

            return (String) uploadResult.get("secure_url");
        } catch (Exception e) {
            throw new IOException("Error uploading image", e);
        }
    }

    public static String uploadFile(File file) throws IOException {
        // Lấy tên file không bao gồm extension
        String fileName = file.getName();
        int lastDotIndex = fileName.lastIndexOf('.');
        String fileNameWithoutExtension = lastDotIndex > 0 ? fileName.substring(0, lastDotIndex) : fileName;

        String uniquePublicId = fileNameWithoutExtension + "_" + System.currentTimeMillis();

        try {
            Map<String, Object> params = Map.of(
                "upload_preset", "vuthevy1209",
                "resource_type", "raw",
                "public_id", uniquePublicId,
                "unique_filename", true,
                "type", "upload"
                
            );

            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = (Map<String, Object>) CloudinaryConfig.getCloudinary()
                .uploader()
                .upload(file, params);

            return (String) uploadResult.get("secure_url");
        } catch (Exception e) {
            throw new IOException("Error uploading image", e);
        }
    }

    public static boolean downloadFile(String url, String destinationPath, String fileName) {
        try (InputStream in = new java.net.URL(url).openStream();
            OutputStream out = new FileOutputStream(destinationPath + File.separator + fileName)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
