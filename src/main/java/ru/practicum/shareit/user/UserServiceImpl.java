package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> findAll(int from, int size) {
        return userRepository.findAll(PageRequest.of(from / size, size)).stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
    }

    @Override
    @Transactional
    public UserDto update(int userId, UserDto userDto) {
        User user = getValidUser(userId);
        userDto.setId(userId);
        userDto.setName(userDto.getName() != null ? userDto.getName() : user.getName());
        userDto.setEmail(userDto.getEmail() != null ? userDto.getEmail() : user.getEmail());
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
    }

    @Override
    public UserDto findUser(int userId) {
        User user = getValidUser(userId);
        return UserMapper.toUserDto(user);
    }

    @Transactional
    @Override
    public void deleteUser(int userId) {
        getValidUser(userId);
        userRepository.deleteById(userId);
    }

    @Override
    public User getValidUser(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("на сервере отстутствует пользователь c id = %s", userId)));
    }
}
