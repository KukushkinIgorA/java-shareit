package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserStorage userStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public List<UserDto> findAll() {
        return userStorage.findAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto create(UserDto userDto) {
        validate(userDto);
        return UserMapper.toUserDto(userStorage.create(UserMapper.toUser(userDto)));
    }

    @Override
    public UserDto update(int userId, UserDto userDto) {
        validate(userDto);
        if(userStorage.findUser(userId) == null){
            throw new NotFoundException(String.format("на сервере отстутствует пользователь c id = %s", userId));
        }
        userDto.setId(userId);
        return UserMapper.toUserDto(userStorage.update(UserMapper.toUser(userDto)));
    }

    @Override
    public UserDto findUser(int userId) {
        User user = userStorage.findUser(userId);
        if(user == null){
            throw new NotFoundException(String.format("на сервере отстутствует пользователь c id = %s", userId));
        }
        return UserMapper.toUserDto(user);
    }

    @Override
    public void deleteUser(int userId) {
        userStorage.deleteUser(userId);
    }

    private void validate(UserDto userDto) {
        if(userDto.getEmail() != null && userStorage.findAll().stream().filter(user -> userDto.getId() != user.getId()).anyMatch(user -> user.getEmail().equals(userDto.getEmail()))){
            throw new ConflictException(String.format("электронная почта не уникальна: %s", userDto.getEmail()));
        }
    }
}
