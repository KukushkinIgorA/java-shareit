package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.dictionary.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDto {
    private int id;
    private BookingStatus status;
    private LocalDateTime start;
    private LocalDateTime end;
    private int itemId;
    private Item item;
    private User booker;
}
