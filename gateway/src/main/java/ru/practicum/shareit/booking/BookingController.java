package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.dictionary.BookingGetState;
import ru.practicum.shareit.indicators.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.params.PaginationParam.DEFAULT_PAGE_SIZE;
import static ru.practicum.shareit.params.PaginationParam.DEFAULT_START_INDEX;

/**
 *
 */
@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@Validated
public class BookingController {
    public static final String X_SHARER_USER_ID = "X-Sharer-User-Id";
    private final BookingClient bookingClient;

    @Autowired
    public BookingController(BookingClient bookingClient) {
        this.bookingClient = bookingClient;
    }

    @PostMapping()
    public ResponseEntity<Object> create(@RequestHeader(X_SHARER_USER_ID) int userId,
                                         @Validated(Create.class) @RequestBody BookingDto bookingDto) {
        log.info("Запрос на создание бронирования {}", bookingDto.getId());
        return bookingClient.create(userId, bookingDto);
    }

    @PatchMapping("{bookingId}")
    public ResponseEntity<Object> changeBookingStatus(@RequestHeader(X_SHARER_USER_ID) int userId,
                                                      @PathVariable int bookingId,
                                                      @RequestParam(name = "approved") boolean approved
    ) {
        log.info("Запрос на изменение статуса бронирования {}", bookingId);
        return bookingClient.changeBookingStatus(userId, bookingId, approved);
    }

    @GetMapping("{bookingId}")
    public ResponseEntity<Object> findBooking(@RequestHeader(X_SHARER_USER_ID) int userId,
                                              @PathVariable("bookingId") int bookingId) {
        log.info("Запрос бронирования по id: {}", bookingId);
        return bookingClient.findBooking(userId, bookingId);
    }

    @GetMapping()
    public ResponseEntity<Object> findUserBooking(@RequestHeader(X_SHARER_USER_ID) int userId,
                                                  @RequestParam(name = "state",
                                                          defaultValue = "ALL") String bookingGetStateString,
                                                  @PositiveOrZero @RequestParam(name = "from",
                                                          defaultValue = DEFAULT_START_INDEX) int from,
                                                  @Positive @RequestParam(name = "size",
                                                          defaultValue = DEFAULT_PAGE_SIZE) int size) {
        log.info("Запрос бронирований пользователя: {}", userId);
        BookingGetState bookingGetState = checkBookingGetState(bookingGetStateString);
        return bookingClient.findUserBooking(userId, bookingGetState, from, size);
    }

    @GetMapping("owner")
    public ResponseEntity<Object> findItemUserBooking(@RequestHeader(X_SHARER_USER_ID) int userId,
                                                      @RequestParam(name = "state",
                                                              defaultValue = "ALL") String bookingGetStateString,
                                                      @PositiveOrZero @RequestParam(name = "from",
                                                              defaultValue = DEFAULT_START_INDEX) int from,
                                                      @Positive @RequestParam(name = "size",
                                                              defaultValue = DEFAULT_PAGE_SIZE) int size) {
        log.info("Запрос бронирований для всех вещей пользователя: {}", userId);
        BookingGetState bookingGetState = checkBookingGetState(bookingGetStateString);
        return bookingClient.findItemUserBooking(userId, bookingGetState, from, size);
    }

    private static BookingGetState checkBookingGetState(String bookingGetStateString) {
        return BookingGetState.from(bookingGetStateString).orElseThrow(() ->
                new IllegalArgumentException("Unknown state: " + bookingGetStateString));
    }
}
