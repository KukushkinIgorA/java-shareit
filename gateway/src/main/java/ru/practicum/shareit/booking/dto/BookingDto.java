package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.shareit.dictionary.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

/**
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class BookingDto {
    private int id;
    private BookingStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime start;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime end;
    private int itemId;
    private ItemDto item;
    private UserDto booker;
}
