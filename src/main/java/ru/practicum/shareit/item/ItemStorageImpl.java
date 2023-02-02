package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository("itemStorage")
public class ItemStorageImpl implements ItemStorage {
    int nextId = 1;
    Map<Integer, Item> itemMap = new HashMap<>();

    @Override
    public Item create(Item item) {
        item.setId(nextId++);
        itemMap.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Item item) {
        Item itemUpdate = itemMap.get(item.getId());
        itemUpdate.setAvailable(item.getAvailable() != null && !item.getAvailable().equals(itemUpdate.getAvailable()) ? item.getAvailable() : itemUpdate.getAvailable());
        itemUpdate.setDescription(item.getDescription() != null ? item.getDescription() : itemUpdate.getDescription());
        itemUpdate.setName(item.getName() != null ? item.getName() : itemUpdate.getName());
        return itemMap.put(itemUpdate.getId(), itemUpdate);
    }

    @Override
    public Item findItem(int itemId) {
        return itemMap.get(itemId);
    }

    @Override
    public List<Item> findUserItems(int userId) {
        return itemMap.values().stream()
                .filter(item -> item.getOwner().getId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> findItems(int userId, String searchString) {
        List<Item> items;
        if (searchString.isEmpty()) {
            items = Collections.emptyList();
        } else {
            items = itemMap.values().stream()
                    .filter(item -> item.getAvailable() &&
                            (item.getName().toLowerCase().contains(searchString.toLowerCase()) || item.getDescription().toLowerCase().contains(searchString.toLowerCase())))
                    .collect(Collectors.toList());
        }
        return items;
    }
}