package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

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

    private final ItemRequestService itemRequestService;

    @Autowired
    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping()
    public ItemRequestDto create(@RequestHeader(X_SHARER_USER_ID) int userId,
                                 @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Запрос на добавление вещи {}", itemRequestDto.getId());
        return itemRequestService.create(userId, itemRequestDto);
    }

    @GetMapping()
    public List<ItemRequestDto> findUserItemRequest(@RequestHeader(X_SHARER_USER_ID) int userId) {
        log.info("Запросы пользователя: {}", userId);
        return itemRequestService.findUserItemRequest(userId);
    }

    @GetMapping("all")
    public List<ItemRequestDto> findAllItemRequest(@RequestHeader(X_SHARER_USER_ID) int userId,
                                                   @PositiveOrZero @RequestParam(name = "from",
                                                           defaultValue = DEFAULT_START_INDEX) int from,
                                                   @Positive @RequestParam(name = "size",
                                                           defaultValue = DEFAULT_PAGE_SIZE) int size) {
        log.info("Запросы всех пользователей: {}", userId);
        return itemRequestService.findAllItemRequest(userId, from, size);
    }

    @GetMapping("{requestId}")
    public ItemRequestDto findItemRequest(@RequestHeader(X_SHARER_USER_ID) int userId,
                                          @PathVariable("requestId") int requestId) {
        log.info("Запрос: {}", requestId);
        return itemRequestService.findItemRequest(userId, requestId);
    }


}