package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item create(Item item);
    Item update(Item item);
    Item findItem(int itemId);
    List<Item> findUserItems(int userId);
    List<Item> findItems(int userId, String searchString);
}
