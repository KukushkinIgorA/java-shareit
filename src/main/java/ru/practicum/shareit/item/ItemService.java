package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto create(int userId, ItemDto itemDto);

    ItemDto update(int userId, int itemId, ItemDto itemDto);

    ItemDto findItem(int userId, int itemId);

    List<ItemDto> findUserItems(int userId, int from, int size);

    List<ItemDto> findItems(int userId, String searchString, int from, int size);

    CommentDto createComment(int userId, int itemId, CommentDto commentDto);

    Item getValidItem(int itemId);
}