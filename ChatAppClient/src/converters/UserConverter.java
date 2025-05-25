package converters;

import dto.response.UserResponse;
import models.User;

public class UserConverter {
    public static User converterToUser(UserResponse userResponse) {
        if (userResponse == null) {
            return null;
        }

        return User.builder()
                .id(userResponse.getId())
                .username(userResponse.getUsername())
                .email(userResponse.getEmail())
                .avatarPath(userResponse.getAvatarPath())
                .build();
    }
}
