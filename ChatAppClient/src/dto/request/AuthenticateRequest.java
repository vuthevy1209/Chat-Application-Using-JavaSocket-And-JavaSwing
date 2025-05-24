package dto.request;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AuthenticateRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
}
