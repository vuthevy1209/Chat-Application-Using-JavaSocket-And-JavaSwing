package utils;

import java.util.HashMap;
import java.util.Map;

public class JsonParser {
    
    /**
     * Converts a simple JSON string into a Map
     * 
     * @param jsonPayload The JSON string to parse
     * @return A Map containing the key-value pairs from the JSON
     */
    public static Map<String, String> parseJson(String jsonPayload) {
        Map<String, String> result = new HashMap<>();
        
        if (jsonPayload == null || jsonPayload.trim().isEmpty()) {
            return result;
        }
        
        try {
            // Clean the JSON string
            String json = jsonPayload.trim();
            if (json.startsWith("{") && json.endsWith("}")) {
                json = json.substring(1, json.length() - 1);
            }
            
            // Split into key-value pairs
            String[] pairs = json.split(",");
            for (String pair : pairs) {
                String[] kv = pair.split(":", 2);
                if (kv.length == 2) {
                    String key = kv[0].trim().replaceAll("\"", "");
                    String value = kv[1].trim().replaceAll("\"", "");
                    result.put(key, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
}