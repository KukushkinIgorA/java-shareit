package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@EqualsAndHashCode
@ToString
public class CommentDto {
    private int id;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime created;

    @NotBlank
    private String text;

    private Item item;

    private User author;

    private String authorName;
}
