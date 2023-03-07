package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.item.ItemController.X_SHARER_USER_ID;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ItemRequestService itemRequestService;

    ItemRequestDto expectedItemRequestDto;
    private final int userId = 0;

    @BeforeEach
    void init() {
        expectedItemRequestDto = ItemRequestDto.builder()
                .description("description1")
                .build();
    }

    @Test
    @SneakyThrows
    void create() {
        when(itemRequestService.create(userId, expectedItemRequestDto)).thenReturn(expectedItemRequestDto);

        String result = mockMvc.perform(post("/requests")
                        .header(X_SHARER_USER_ID, userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(expectedItemRequestDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        verify(itemRequestService, times(1)).create(userId, expectedItemRequestDto);
        assertEquals(objectMapper.writeValueAsString(expectedItemRequestDto), result);
    }

    @Test
    @SneakyThrows
    void findUserItemRequest() {
        when(itemRequestService.findUserItemRequest(userId))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/requests")
                        .header(X_SHARER_USER_ID, userId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(itemRequestService, times(1)).findUserItemRequest(userId);
    }

    @Test
    @SneakyThrows
    void findAllItemRequest() {
        int from = 0;
        int size = 1;
        when(itemRequestService.findAllItemRequest(userId, from, size))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/requests/all")
                        .header(X_SHARER_USER_ID, userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(itemRequestService, times(1)).findAllItemRequest(userId, from, size);
    }

    @Test
    @SneakyThrows
    void findItemRequest() {
        int requestId = 0;
        when(itemRequestService.findItemRequest(userId, requestId)).thenReturn(expectedItemRequestDto);

        String result = mockMvc.perform(get("/requests/{requestId}", requestId)
                        .header(X_SHARER_USER_ID, userId))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        verify(itemRequestService, times(1)).findItemRequest(userId, requestId);
        assertEquals(objectMapper.writeValueAsString(expectedItemRequestDto), result);
    }
}