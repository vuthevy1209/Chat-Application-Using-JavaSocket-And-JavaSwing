package dto.request;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ChatRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id; // Optional, used for update and delete operations
    private String name;
    private boolean isGroup;
    private List<String> participantIds;
    private String requestType; // For action differentiation (create, update, delete, join, leave, etc.)
}
