package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.indicators.Create;
import ru.practicum.shareit.indicators.Update;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

/**
 *
 */
@RestController
@RequestMapping(path = "/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<UserDto> findAll() {
        log.info("Запрос всех пользователей");
        return userService.findAll();
    }

    @PostMapping()
    public UserDto create(@Validated(Create.class) @RequestBody UserDto userDto) {
        log.info("Запрос на создание пользователя {}", userDto.getEmail());
        return userService.create(userDto);
    }

    @PatchMapping("{id}")
    public UserDto update(@PathVariable("id") int userId,
                          @Validated(Update.class) @RequestBody UserDto userDto) {
        log.info("Запрос на обновление пользователя {}", userId);
        return userService.update(userId, userDto);
    }

    @GetMapping("{id}")
    public UserDto findUser(@PathVariable("id") int userId) {
        log.info("Запрос пользователя по id: {}", userId);
        return userService.findUser(userId);
    }

    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable("id") int userId) {
        log.info("Запрос на удаление пользователя по id: {}", userId);
        userService.deleteUser(userId);
    }
}
