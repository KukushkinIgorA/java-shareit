package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(int userId, ItemRequestDto itemRequestDto);

    List<ItemRequestDto> findUserItemRequest(int userId);

    List<ItemRequestDto> findAllItemRequest(int userId, int from, int size);

    ItemRequestDto findItemRequest(int userId, int requestId);

    ItemRequest getValidItemRequest(int itemRequestId);
}
