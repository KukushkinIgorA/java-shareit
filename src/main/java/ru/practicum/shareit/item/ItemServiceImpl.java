package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {
    public final ItemStorage itemStorage;

    public final UserStorage userStorage;

    @Autowired
    public ItemServiceImpl(ItemStorage itemStorage, UserStorage userStorage) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }

    @Override
    public ItemDto create(int userId, ItemDto itemDto) {
        User user = getValidUser(userId);
        itemDto.setOwner(user);
        return ItemMapper.toItemDto(itemStorage.create(ItemMapper.toItem(itemDto)));
    }

    @Override
    public ItemDto update(int userId, int itemId, ItemDto itemDto) {
        getValidUser(userId);
        Item item = getValidItem(itemId);
        if (item.getOwner().getId() != userId) {
            throw new ForbiddenException(
                    String.format("Пользователю не хватает прав на редактирование вещи c id = %s", itemId));
        }
        itemDto.setId(itemId);
        return ItemMapper.toItemDto(itemStorage.update(ItemMapper.toItem(itemDto)));
    }

    @Override
    public ItemDto findItem(int userId, int itemId) {
        Item item = getValidItem(itemId);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> findUserItems(int userId) {
        getValidUser(userId);
        return itemStorage.findUserItems(userId).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> findItems(int userId, String searchString) {
        return itemStorage.findItems(userId, searchString).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    private User getValidUser(int userId) {
        User user = userStorage.findUser(userId);
        if (user == null) {
            throw new NotFoundException(String.format("на сервере отстутствует пользователь c id = %s", userId));
        }
        return user;
    }

    private Item getValidItem(int itemId) {
        Item item = itemStorage.findItem(itemId);
        if (itemStorage.findItem(itemId) == null) {
            throw new NotFoundException(String.format("на сервере отстутствует вещь c id = %s", itemId));
        }
        return item;
    }
}