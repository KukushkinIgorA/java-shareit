package ru.practicum.shareit.user.model;

import lombok.*;

import javax.persistence.*;

/**
 *
 */

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    @Column(name = "name", length = 32)
    private String name;

    @Column(name = "email")
    private String email;
}
