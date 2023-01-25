package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> findAll();
    UserDto create(UserDto userDto);
    UserDto update(int userId, UserDto userDto);
    UserDto findUser(int userId);
    void deleteUser(int userId);
}
