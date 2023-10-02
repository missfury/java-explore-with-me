package ru.practicum.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.admin.service.AdminUserService;
import ru.practicum.shared.dto.NewUserRequest;
import ru.practicum.shared.dto.UserDto;
import ru.practicum.shared.util.Pagination;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/users")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@Valid @RequestBody NewUserRequest newUserRequest) {
        UserDto user = adminUserService.addUser(newUserRequest);
        log.info("Создан пользователь с id= {}", newUserRequest);
        return user;
    }

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                  @RequestParam(name = "from", defaultValue = "0") Integer from,
                                  @RequestParam(name = "size", defaultValue = "10") Integer size) {
        List<UserDto> users = adminUserService.getUsers(ids, new Pagination(from, size, Sort.unsorted()));
        log.info("Получен список пользователей");
        return users;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUser(@PathVariable Long userId) {
        adminUserService.deleteUser(userId);
        log.info("Удален пользователь с id= {}", userId);
    }
}
