package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    public final ItemRequestRepository itemRequestRepository;

    public final UserService userService;

    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository, UserService userService) {
        this.itemRequestRepository = itemRequestRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public ItemRequestDto create(int userId, ItemRequestDto itemRequestDto) {
        User user = userService.getValidUser(userId);
        itemRequestDto.setCreated(LocalDateTime.now());
        itemRequestDto.setRequestor(user);
        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(
                ItemRequestMapper.toItemRequest(itemRequestDto)));
    }

    @Override
    public List<ItemRequestDto> findUserItemRequest(int userId) {
        userService.getValidUser(userId);
        return itemRequestRepository.findByRequestor_IdOrderByCreatedDesc(userId)
                .stream().map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> findAllItemRequest(int userId, int from, int size) {
        return itemRequestRepository.findAll(PageRequest.of(from / size, size)).get()
                .filter(itemRequest -> itemRequest.getRequestor().getId() != userId)
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto findItemRequest(int userId, int requestId) {
        userService.getValidUser(userId);
        ItemRequest itemRequest = getValidItemRequest(requestId);
        return ItemRequestMapper.toItemRequestDto(itemRequest);
    }

    @Override
    public ItemRequest getValidItemRequest(int itemRequestId) {
        return itemRequestRepository.findById(itemRequestId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("на сервере отстутствует запрос c id = %s", itemRequestId)));
    }
}