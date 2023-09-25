package ru.practicum.ewmservice.shared.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewmservice.shared.dto.NewUserRequest;
import ru.practicum.ewmservice.shared.dto.UserDto;
import ru.practicum.ewmservice.shared.dto.UserShortDto;
import ru.practicum.ewmservice.shared.model.User;

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
