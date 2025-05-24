package models;

import java.io.Serializable;

public class Response implements Serializable {
    private String type;
    private boolean success;
    private String message;
    private Object data;
    
    public Response(String type, boolean success, String message, Object data) {
        this.type = type;
        this.success = success;
        this.message = message;
        this.data = data;
    }
    
    public static Response success(String type, String message, Object data) {
        return new Response(type, true, message, data);
    }
    
    public static Response error(String type, String message) {
        return new Response(type, false, message, null);
    }
    
    public String getType() {
        return type;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public Object getData() {
        return data;
    }
}
