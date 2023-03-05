//package ru.practicum.shareit.booking;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.SneakyThrows;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ValueSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//import ru.practicum.shareit.booking.dto.BookingDto;
//import ru.practicum.shareit.dictionary.BookingGetState;
//
//import java.util.Collections;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static ru.practicum.shareit.item.ItemController.X_SHARER_USER_ID;
//
//@WebMvcTest(BookingController.class)
//class BookingControllerTest {
//
//    @Autowired
//    ObjectMapper objectMapper;
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @MockBean
//    private BookingService bookingService;
//
//    BookingDto expectedBookingDto;
//
//    private final int userId = 0;
//
//    private final int bookingId = 0;
//
//    private final int from = 0;
//
//    private final int size = 1;
//
//
//    @BeforeEach
//    void init() {
//        expectedBookingDto = BookingDto.builder()
//                .build();
//    }
//
//    @Test
//    @SneakyThrows
//    void create() {
//        when(bookingService.create(userId, expectedBookingDto)).thenReturn(expectedBookingDto);
//
//        String result = mockMvc.perform(post("/bookings")
//                        .header(X_SHARER_USER_ID, userId)
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(expectedBookingDto)))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        verify(bookingService, times(1)).create(userId, expectedBookingDto);
//        assertEquals(objectMapper.writeValueAsString(expectedBookingDto), result);
//    }
//
//    @Test
//    @SneakyThrows
//    void changeBookingStatus() {
//        boolean approved = true;
//        when(bookingService.changeBookingStatus(userId, bookingId, approved)).thenReturn(expectedBookingDto);
//
//        String result = mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
//                        .header(X_SHARER_USER_ID, userId)
//                        .param("approved", String.valueOf(approved)))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        verify(bookingService, times(1)).changeBookingStatus(userId, bookingId, approved);
//        assertEquals(objectMapper.writeValueAsString(expectedBookingDto), result);
//    }
//
//    @Test
//    @SneakyThrows
//    void findBooking() {
//        when(bookingService.findBooking(userId, bookingId)).thenReturn(expectedBookingDto);
//
//        String result = mockMvc.perform(get("/bookings/{bookingId}", bookingId)
//                        .header(X_SHARER_USER_ID, userId))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        verify(bookingService, times(1)).findBooking(userId, bookingId);
//        assertEquals(objectMapper.writeValueAsString(expectedBookingDto), result);
//    }
//
//    @ParameterizedTest
//    @SneakyThrows
//    @ValueSource(strings = {"ALL", "CURRENT", "PAST", "FUTURE", "WAITING", "REJECTED", ""})
//    void findUserBooking(String bookingGetStateString) {
//        when(bookingService.findUserBooking(userId, BookingGetState.from(bookingGetStateString)
//                .orElse(BookingGetState.ALL), from, size))
//                .thenReturn(Collections.emptyList());
//
//        mockMvc.perform(get("/bookings")
//                        .header(X_SHARER_USER_ID, userId)
//                        .param("state", bookingGetStateString)
//                        .param("from", String.valueOf(from))
//                        .param("size", String.valueOf(size)))
//                .andExpect(status().isOk())
//                .andExpect(content().json("[]"));
//
//        verify(bookingService, times(1))
//                .findUserBooking(userId, BookingGetState.from(bookingGetStateString)
//                        .orElse(BookingGetState.ALL), from, size);
//    }
//
//    @ParameterizedTest
//    @SneakyThrows
//    @ValueSource(strings = {"FFF", "AAA"})
//    void findUserBookingIllegalArgumentExceptio(String bookingGetStateString) {
//        mockMvc.perform(get("/bookings")
//                        .header(X_SHARER_USER_ID, userId)
//                        .param("state", bookingGetStateString)
//                        .param("from", String.valueOf(from))
//                        .param("size", String.valueOf(size)))
//                .andExpect(status().isBadRequest())
//                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException));
//    }
//
//    @ParameterizedTest
//    @SneakyThrows
//    @ValueSource(strings = {"ALL", "CURRENT", "PAST", "FUTURE", "WAITING", "REJECTED", ""})
//    void findItemUserBooking(String bookingGetStateString) {
//        when(bookingService.findItemUserBooking(userId, BookingGetState.from(bookingGetStateString)
//                .orElse(BookingGetState.ALL), from, size))
//                .thenReturn(Collections.emptyList());
//
//        mockMvc.perform(get("/bookings/owner")
//                        .header(X_SHARER_USER_ID, userId)
//                        .param("state", bookingGetStateString)
//                        .param("from", String.valueOf(from))
//                        .param("size", String.valueOf(size)))
//                .andExpect(status().isOk())
//                .andExpect(content().json("[]"));
//
//        verify(bookingService, times(1))
//                .findItemUserBooking(userId, BookingGetState.from(bookingGetStateString).orElse(BookingGetState.ALL), from, size);
//    }
//
//    @ParameterizedTest
//    @SneakyThrows
//    @ValueSource(strings = {"FFF", "AAA"})
//    void findItemUserBookingIllegalArgumentExceptio(String bookingGetStateString) {
//        mockMvc.perform(get("/bookings/owner")
//                        .header(X_SHARER_USER_ID, userId)
//                        .param("state", bookingGetStateString)
//                        .param("from", String.valueOf(from))
//                        .param("size", String.valueOf(size)))
//                .andExpect(status().isBadRequest())
//                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException));
//    }
//}