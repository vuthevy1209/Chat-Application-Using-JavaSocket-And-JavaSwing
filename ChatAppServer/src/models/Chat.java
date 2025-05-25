package models;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Chat implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private boolean isGroup;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
