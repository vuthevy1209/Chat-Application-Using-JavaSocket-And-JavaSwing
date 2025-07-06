package config;

import java.util.HashMap;
import java.util.Map;
import com.cloudinary.Cloudinary;

public class CloudinaryConfig {
    private static Cloudinary cloudinary;

    static {
        try {
            Map<String, String> config = new HashMap<>();
            config.put("cloud_name", "dfgdfgghh9999d9999999999999udods");
            config.put("api_key", "345dfsg57dffgsgdfgdf2684dfg185999999dfsg99999999999999937");
            config.put("api_secret", "a5b0-MK9ifdgf9fugjnfghsfg3ucAdfsgdfsswoQzu2Y2Gf65o");
            
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
