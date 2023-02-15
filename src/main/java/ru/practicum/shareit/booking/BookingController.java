package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.dictionary.BookingGetState;
import ru.practicum.shareit.indicators.Create;

import java.util.List;

/**
 *
 */
@RestController
@RequestMapping(path = "/bookings")
@Slf4j
public class BookingController {
    public static final String X_SHARER_USER_ID = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping()
    public BookingDto create(@RequestHeader(X_SHARER_USER_ID) int userId,
                             @Validated(Create.class) @RequestBody BookingDto bookingDto) {
        log.info("Запрос на создание бронирования {}", bookingDto.getId());
        return bookingService.create(userId, bookingDto);
    }

    @PatchMapping("{bookingId}")
    public BookingDto changeBookingStatus(@RequestHeader(X_SHARER_USER_ID) int userId,
                                          @PathVariable int bookingId,
                                          @RequestParam(name = "approved") boolean approved
    ) {
        log.info("Запрос на изменение статуса бронирования {}", bookingId);
        return bookingService.changeBookingStatus(userId, bookingId, approved);
    }

    @GetMapping("{bookingId}")
    public BookingDto findBooking(@RequestHeader(X_SHARER_USER_ID) int userId,
                                  @PathVariable("bookingId") int bookingId) {
        log.info("Запрос бронирования по id: {}", bookingId);
        return bookingService.findBooking(userId, bookingId);
    }

    @GetMapping()
    public List<BookingDto> findUserBooking(@RequestHeader(X_SHARER_USER_ID) int userId,
                                            @RequestParam(name = "state", defaultValue = "ALL") String bookingGetStateString) {
        log.info("Запрос бронирований пользователя: {}", userId);
        BookingGetState bookingGetState = BookingGetState.from(bookingGetStateString).orElseThrow(() ->
                new IllegalArgumentException("Unknown state: " + bookingGetStateString));
        return bookingService.findUserBooking(userId, bookingGetState);
    }

    @GetMapping("owner")
    public List<BookingDto> findItemUserBooking(@RequestHeader(X_SHARER_USER_ID) int userId,
                                                @RequestParam(name = "state", defaultValue = "ALL") String bookingGetStateString) {
        log.info("Запрос бронирований для всех вещей пользователя: {}", userId);
        BookingGetState bookingGetState = BookingGetState.from(bookingGetStateString).orElseThrow(() ->
                new IllegalArgumentException("Unknown state: " + bookingGetStateString));
        return bookingService.findItemUserBooking(userId, bookingGetState);
    }
}
