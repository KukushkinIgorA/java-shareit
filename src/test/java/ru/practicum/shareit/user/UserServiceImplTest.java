package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    User expectedUser;

    UserDto expectedUserDto;

    List<UserDto> expectedUserDtos;
    Page<User> expectedPageUsers;

    private final int userId = 0;

    @BeforeEach
    void init() {
        expectedUser = User.builder()
                .name("name1")
                .email("email1")
                .build();
        expectedUserDto = UserMapper.toUserDto(expectedUser);
        expectedUserDtos = List.of(expectedUserDto);
        expectedPageUsers = new PageImpl<>(List.of(expectedUser));
    }

    @Test
    void findAll() {

        when(userRepository.findAll(PageRequest.of(0, 1))).thenReturn(expectedPageUsers);

        List<UserDto> userDtos = userServiceImpl.findAll(0, 1);

        verify(userRepository, times(1)).findAll(PageRequest.of(0, 1));
        assertEquals(expectedUserDtos, userDtos);
    }

    @Test
    void create() {
        when(userRepository.save(expectedUser)).thenReturn(expectedUser);

        UserDto userDto = userServiceImpl.create(expectedUserDto);

        verify(userRepository, times(1)).save(expectedUser);
        assertEquals(expectedUserDto, userDto);
    }

    @Test
    void update() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));
        when(userRepository.save(expectedUser)).thenReturn(expectedUser);

        UserDto userDto = userServiceImpl.update(userId, expectedUserDto);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(expectedUser);
        assertEquals(expectedUserDto, userDto);
    }

    @Test
    void updateNameEmailNull() {
        expectedUserDto.setName(null);
        expectedUserDto.setEmail(null);
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));
        when(userRepository.save(expectedUser)).thenReturn(expectedUser);

        UserDto userDto = userServiceImpl.update(userId, expectedUserDto);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(expectedUser);
        assertEquals(expectedUserDto, userDto);
    }

    @Test
    void findUser() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        UserDto userDto = userServiceImpl.findUser(userId);

        verify(userRepository, times(1)).findById(userId);
        assertEquals(expectedUserDto, userDto);
    }

    @Test
    void findUserNotFoundException() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userServiceImpl.findUser(userId));
    }

    @Test
    void deleteUser() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        userServiceImpl.deleteUser(userId);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }
}