package ru.practicum.ewmservice.admin.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.admin.service.AdminUserService;
import ru.practicum.ewmservice.shared.dto.NewUserRequest;
import ru.practicum.ewmservice.shared.dto.UserDto;
import ru.practicum.ewmservice.shared.exceptions.NotFoundException;
import ru.practicum.ewmservice.shared.mapper.UserMapper;
import ru.practicum.ewmservice.shared.model.User;
import ru.practicum.ewmservice.shared.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto addUser(NewUserRequest userDto) {
        User user = userRepository.save(userMapper.toUser(userDto));
        return userMapper.toUserDto(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> getUsers(List<Long> ids, Pageable pageable) {
        if (!ids.isEmpty()) {
            return userRepository.findAllByIdIn(ids, pageable).stream()
                    .map(userMapper::toUserDto)
                    .collect(Collectors.toList());
        } else {
            return userRepository.findAll(pageable).stream()
                    .map(userMapper::toUserDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
        userRepository.deleteById(userId);
    }


}
