package ru.practicum.admin.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shared.dto.NewUserRequest;
import ru.practicum.shared.dto.UserDto;

import java.util.List;

public interface AdminUserService {
    UserDto addUser(NewUserRequest newUserRequest);

    List<UserDto> getUsers(List<Long> ids, Pageable pageable);

    void deleteUser(Long userId);
}
