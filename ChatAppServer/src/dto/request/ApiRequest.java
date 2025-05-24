package dto.request;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ApiRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String method; // e.g., "GET", "POST", etc.
    private String url; // e.g., "/api/resource"
    private String host; // e.g., "localhost"
    private String contentType; // e.g., "application/json"
    private Object payload; // JSON payload for POST requests
    private int contentLength; // Length of the payload
    private String headers; // Additional headers if needed
    private String queryParams; // Query parameters if any, e.g., "?key=value"
    private String protocol; // e.g., "HTTP/1.1"
}
