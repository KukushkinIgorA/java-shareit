package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
    private int id;

    private LocalDateTime created;

    @NotBlank
    private String text;

    private Item item;

    private User author;

    private String authorName;
}
