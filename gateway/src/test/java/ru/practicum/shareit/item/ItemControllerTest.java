//package ru.practicum.shareit.item;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.SneakyThrows;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//import ru.practicum.shareit.item.dto.CommentDto;
//import ru.practicum.shareit.item.dto.ItemDto;
//
//import java.util.Collections;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static ru.practicum.shareit.item.ItemController.X_SHARER_USER_ID;
//
//@WebMvcTest(ItemController.class)
//class ItemControllerTest {
//
//    @Autowired
//    ObjectMapper objectMapper;
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @MockBean
//    private ItemService itemService;
//
//    ItemDto expectedItemDto;
//
//    CommentDto expectedCommentDto;
//
//    private final int itemId = 0;
//    private final int userId = 0;
//    private final int from = 0;
//    private final int size = 1;
//
//    @BeforeEach
//    void init() {
//        expectedItemDto = ItemDto.builder()
//                .name("name1")
//                .description("description1")
//                .available(true)
//                .build();
//        expectedCommentDto = CommentDto.builder()
//                .text("text1")
//                .build();
//    }
//
//    @Test
//    @SneakyThrows
//    void create() {
//        when(itemService.create(userId, expectedItemDto)).thenReturn(expectedItemDto);
//
//        String result = mockMvc.perform(post("/items")
//                        .header(X_SHARER_USER_ID, userId)
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(expectedItemDto)))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        verify(itemService, times(1)).create(userId, expectedItemDto);
//        assertEquals(objectMapper.writeValueAsString(expectedItemDto), result);
//    }
//
//    @Test
//    @SneakyThrows
//    void update() {
//        when(itemService.update(userId, itemId, expectedItemDto)).thenReturn(expectedItemDto);
//
//        String result = mockMvc.perform(patch("/items/{id}", itemId)
//                        .header(X_SHARER_USER_ID, userId)
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(expectedItemDto)))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        verify(itemService, times(1)).update(userId, itemId, expectedItemDto);
//        assertEquals(objectMapper.writeValueAsString(expectedItemDto), result);
//    }
//
//    @Test
//    @SneakyThrows
//    void findItem() {
//        when(itemService.findItem(userId, itemId)).thenReturn(expectedItemDto);
//
//        String result = mockMvc.perform(get("/items/{id}", itemId)
//                        .header(X_SHARER_USER_ID, userId))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        verify(itemService, times(1)).findItem(userId, itemId);
//        assertEquals(objectMapper.writeValueAsString(expectedItemDto), result);
//    }
//
//    @Test
//    @SneakyThrows
//    void findUserItems() {
//        when(itemService.findUserItems(userId, from, size))
//                .thenReturn(Collections.emptyList());
//
//        mockMvc.perform(get("/items")
//                        .header(X_SHARER_USER_ID, userId)
//                        .param("from", String.valueOf(from))
//                        .param("size", String.valueOf(size)))
//                .andExpect(status().isOk())
//                .andExpect(content().json("[]"));
//
//        verify(itemService, times(1)).findUserItems(userId, from, size);
//    }
//
//    @Test
//    @SneakyThrows
//    void findItems() {
//        String searchString = "searchString";
//        when(itemService.findItems(userId, searchString, from, size))
//                .thenReturn(Collections.emptyList());
//
//        mockMvc.perform(get("/items/search")
//                        .header(X_SHARER_USER_ID, userId)
//                        .param("text", searchString)
//                        .param("from", String.valueOf(from))
//                        .param("size", String.valueOf(size)))
//                .andExpect(status().isOk())
//                .andExpect(content().json("[]"));
//
//        verify(itemService, times(1)).findItems(userId, searchString, from, size);
//    }
//
//    @Test
//    @SneakyThrows
//    void createComment() {
//        when(itemService.createComment(userId, itemId, expectedCommentDto))
//                .thenReturn(expectedCommentDto);
//
//        String result = mockMvc.perform(post("/items/{itemId}/comment", itemId)
//                        .header(X_SHARER_USER_ID, userId)
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(expectedCommentDto)))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        verify(itemService, times(1)).createComment(userId, itemId, expectedCommentDto);
//        assertEquals(objectMapper.writeValueAsString(expectedCommentDto), result);
//    }
//}