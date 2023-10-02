package ru.practicum.shared.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shared.dto.NewUserRequest;
import ru.practicum.shared.dto.UserDto;
import ru.practicum.shared.dto.UserShortDto;
import ru.practicum.shared.model.User;

@Component
public class UserMapper {

    public UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public UserShortDto toUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public User toUser(NewUserRequest newUserRequest) {
        return User.builder()
                .name(newUserRequest.getName())
                .email(newUserRequest.getEmail())
                .build();
    }

}
