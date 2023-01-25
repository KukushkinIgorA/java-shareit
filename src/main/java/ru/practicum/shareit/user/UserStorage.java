package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {
    List<User> findAll();
    User create(User user);
    User update(User user);
    User findUser(int userId);
    void deleteUser(int userId);
}
