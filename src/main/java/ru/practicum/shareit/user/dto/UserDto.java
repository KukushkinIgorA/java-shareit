package ru.practicum.shareit.user.dto;

import lombok.*;
import ru.practicum.shareit.indicators.Create;
import ru.practicum.shareit.indicators.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    @NotNull(groups = {Update.class})
    int id;

    @NotBlank(groups = {Create.class})
    String name;

    @NotBlank(groups = {Create.class})
    @Email(groups = {Create.class, Update.class})
    String email;
}
