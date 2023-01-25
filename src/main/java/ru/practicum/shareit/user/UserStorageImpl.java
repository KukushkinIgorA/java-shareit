package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("userStorage")
public class UserStorageImpl implements UserStorage{

    int nextId = 1;
    Map<Integer, User> userMap = new HashMap<>();

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public User create(User user) {
        user.setId(nextId++);
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        User userUpdate = userMap.get(user.getId());
        userUpdate.setName(user.getName() != null ? user.getName() : userUpdate.getName());
        userUpdate.setEmail(user.getEmail() != null ? user.getEmail() : userUpdate.getEmail());
        return userMap.put(user.getId(), userUpdate);
    }

    @Override
    public User findUser(int userId) {
        return userMap.get(userId);
    }

    @Override
    public void deleteUser(int userId) {
        userMap.remove(userId);
    }
}
