package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto create(int userId, ItemDto itemDto);

    ItemDto update(int userId, int itemId, ItemDto itemDto);

    ItemDto findItem(int userId, int itemId);

    List<ItemDto> findUserItems(int userId);

    List<ItemDto> findItems(int userId, String searchString);


}