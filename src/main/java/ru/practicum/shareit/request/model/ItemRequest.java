package ru.practicum.shareit.request.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 */
@Entity
@Table(name = "requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private int id;

    @EqualsAndHashCode.Exclude
    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "description", nullable = false, length = 512)
    private String description;

    @ManyToOne
    @JoinColumn(name = "requestor_id")
    private User requestor;

    @OneToMany
    @JoinColumn(name = "request_id")
    private List<Item> items;
}
