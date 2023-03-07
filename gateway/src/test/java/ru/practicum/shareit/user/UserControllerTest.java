//package ru.practicum.shareit.user;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.SneakyThrows;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//import ru.practicum.shareit.user.dto.UserDto;
//
//import java.util.Collections;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(UserController.class)
//class UserControllerTest {
//    @Autowired
//    ObjectMapper objectMapper;
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @MockBean
//    private UserService userService;
//
//    UserDto expectedUserDto;
//
//    private final int userId = 0;
//
//    @BeforeEach
//    void init() {
//        expectedUserDto = UserDto.builder()
//                .name("name1")
//                .email("email1@test.ru")
//                .build();
//    }
//
//    @Test
//    @SneakyThrows
//    void findAll() {
//        when(userService.findAll(anyInt(), anyInt()))
//                .thenReturn(Collections.emptyList());
//
//        mockMvc.perform(get("/users"))
//                .andExpect(status().isOk())
//                .andExpect(content().json("[]"));
//
//        verify(userService, times(1)).findAll(anyInt(), anyInt());
//
//    }
//
//    @Test
//    @SneakyThrows
//    void create() {
//        when(userService.create(expectedUserDto)).thenReturn(expectedUserDto);
//
//        String result = mockMvc.perform(post("/users")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(expectedUserDto)))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        verify(userService, times(1)).create(expectedUserDto);
//        assertEquals(objectMapper.writeValueAsString(expectedUserDto), result);
//    }
//
//    @Test
//    @SneakyThrows
//    void update() {
//        when(userService.update(userId, expectedUserDto)).thenReturn(expectedUserDto);
//
//        String result = mockMvc.perform(patch("/users/{id}", userId)
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(expectedUserDto)))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        verify(userService, times(1)).update(userId, expectedUserDto);
//        assertEquals(objectMapper.writeValueAsString(expectedUserDto), result);
//    }
//
//    @Test
//    @SneakyThrows
//    void findUser() {
//        when(userService.findUser(userId)).thenReturn(expectedUserDto);
//
//        String result = mockMvc.perform(get("/users/{id}", userId))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        verify(userService, times(1)).findUser(userId);
//        assertEquals(objectMapper.writeValueAsString(expectedUserDto), result);
//    }
//
//    @Test
//    @SneakyThrows
//    void deleteUser() {
//        mockMvc.perform(delete("/users/{id}", userId))
//                .andExpect(status().isOk());
//
//        verify(userService, times(1)).deleteUser(userId);
//    }
//}