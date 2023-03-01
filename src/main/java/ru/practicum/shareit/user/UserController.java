package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.indicators.Create;
import ru.practicum.shareit.indicators.Update;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.shareit.params.PaginationParam.DEFAULT_PAGE_SIZE;
import static ru.practicum.shareit.params.PaginationParam.DEFAULT_START_INDEX;

/**
 *
 */
@RestController
@RequestMapping(path = "/users")
@Slf4j
@Validated
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<UserDto> findAll(@PositiveOrZero @RequestParam(name = "from",
                                            defaultValue = DEFAULT_START_INDEX) int from,
                                 @Positive @RequestParam(name = "size",
                                            defaultValue = DEFAULT_PAGE_SIZE) int size

    ) {
        log.info("Запрос всех пользователей");
        return userService.findAll(from, size);
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
