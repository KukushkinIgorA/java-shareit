package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.indicators.Create;
import ru.practicum.shareit.indicators.Update;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

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
public class ItemDto {
    @NotNull(groups = {Update.class})
    private int id;

    @NotBlank(groups = {Create.class})
    private String name;

    @NotBlank(groups = {Create.class})
    private String description;

    @NotNull(groups = {Create.class})
    private Boolean available;

    private User owner;

    private int requestId;

    private BookingItemDto lastBooking;

    private BookingItemDto nextBooking;

    private List<CommentDto> comments;
}
