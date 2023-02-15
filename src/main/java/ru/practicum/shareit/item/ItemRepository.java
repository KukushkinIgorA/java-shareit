package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findByOwnerOrderById(User owner);

    @Query("select item from Item item " +
            "where item.available = true " +
            "and (lower(item.name) like %:searchString% " +
            "or lower(item.description) like %:searchString%)")
    List<Item> findItemBySearchString(String searchString);
}
