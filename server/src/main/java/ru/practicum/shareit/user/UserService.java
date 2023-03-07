package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    List<UserDto> findAll(int from, int size);

    UserDto create(UserDto userDto);

    UserDto update(int userId, UserDto userDto);

    UserDto findUser(int userId);

    void deleteUser(int userId);

    User getValidUser(int userId);
}
