package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.shareit.user.dto.UserDto;

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

    private ItemDto item;

    private UserDto author;

    private String authorName;
}
