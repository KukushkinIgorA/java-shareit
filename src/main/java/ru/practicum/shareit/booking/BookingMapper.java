package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .status(booking.getStatus())
                .start(booking.getStart())
                .end(booking.getEnd())
                .itemId(booking.getItem().getId())
                .item(booking.getItem())
                .booker(booking.getBooker())
                .build();
    }

    public static Booking toBooking(BookingDto bookingDto) {
        return Booking.builder()
                .id(bookingDto.getId())
                .status(bookingDto.getStatus())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .booker(bookingDto.getBooker())
                .build();
    }
}
