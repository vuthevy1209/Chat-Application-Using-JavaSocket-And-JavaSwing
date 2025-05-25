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

    public static UserResponse converterToUserResponse(User user) {
        if (user == null) {
            return null;
        }

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .avatarPath(user.getAvatarPath())
                .build();
    }
}
