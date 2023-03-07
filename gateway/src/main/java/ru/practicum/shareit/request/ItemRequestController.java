package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.params.PaginationParam.DEFAULT_PAGE_SIZE;
import static ru.practicum.shareit.params.PaginationParam.DEFAULT_START_INDEX;

/**
 *
 */
@RestController
@RequestMapping(path = "/requests")
@Slf4j
@Validated
public class ItemRequestController {
    public static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    private final ItemRequestClient itemRequestClient;

    @Autowired
    public ItemRequestController(ItemRequestClient itemRequestClient) {
        this.itemRequestClient = itemRequestClient;
    }

    @PostMapping()
    public ResponseEntity<Object> create(@RequestHeader(X_SHARER_USER_ID) int userId,
                                         @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Запрос на добавление вещи {}", itemRequestDto.getId());
        return itemRequestClient.create(userId, itemRequestDto);
    }

    @GetMapping()
    public ResponseEntity<Object> findUserItemRequest(@RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("Запросы пользователя: {}", userId);
        return itemRequestClient.findUserItemRequest(userId);
    }

    @GetMapping("all")
    public ResponseEntity<Object> findAllItemRequest(@RequestHeader(X_SHARER_USER_ID) int userId,
                                                     @PositiveOrZero @RequestParam(name = "from",
                                                             defaultValue = DEFAULT_START_INDEX) int from,
                                                     @Positive @RequestParam(name = "size",
                                                             defaultValue = DEFAULT_PAGE_SIZE) int size) {
        log.info("Запросы всех пользователей: {}", userId);
        return itemRequestClient.findAllItemRequest(userId, from, size);
    }

    @GetMapping("{requestId}")
    public ResponseEntity<Object> findItemRequest(@RequestHeader(X_SHARER_USER_ID) int userId,
                                                  @PathVariable("requestId") int requestId) {
        log.info("Запрос: {}", requestId);
        return itemRequestClient.findItemRequest(userId, requestId);
    }
}