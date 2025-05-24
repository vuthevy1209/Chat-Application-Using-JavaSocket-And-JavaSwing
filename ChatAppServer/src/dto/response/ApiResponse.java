package dto.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@Builder
public class ApiResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    
    String code;
    String message;
    Object data;
}

