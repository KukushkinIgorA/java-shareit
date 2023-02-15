package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.dictionary.BookingGetState;

import java.util.List;

public interface BookingService {
    BookingDto create(int userId, BookingDto bookingDto);

    BookingDto changeBookingStatus(int userId, int bookingId, boolean approved);

    BookingDto findBooking(int userId, int bookingId);

    List<BookingDto> findUserBooking(int userId, BookingGetState bookingGetState);

    List<BookingDto> findItemUserBooking(int userId, BookingGetState bookingGetState);
}
