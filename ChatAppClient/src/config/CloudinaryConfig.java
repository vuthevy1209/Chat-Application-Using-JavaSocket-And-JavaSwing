package config;

import java.util.HashMap;
import java.util.Map;
import com.cloudinary.Cloudinary;

public class CloudinaryConfig {
    private static Cloudinary cloudinary;

    static {
        try {
            Map<String, String> config = new HashMap<>();
            config.put("cloud_name", "dghhudods");
            config.put("api_key", "345575268418537");
            config.put("api_secret", "a5b0-MK9if3ucAwoQzu2Y2Gf65o");
            
            cloudinary = new Cloudinary(config);
            System.out.println("Cloudinary initialized successfully");
        } catch (Exception e) {
            System.err.println("Failed to initialize Cloudinary: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Cloudinary getCloudinary() {
        return cloudinary;
    }
}