package ru.practicum.shareit.user.model;

import lombok.*;

/**
 *
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    int id;
    String name;
    String email;
}
