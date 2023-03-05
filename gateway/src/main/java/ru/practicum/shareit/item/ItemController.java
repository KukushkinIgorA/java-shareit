package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.indicators.Create;
import ru.practicum.shareit.indicators.Update;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.params.PaginationParam.DEFAULT_PAGE_SIZE;
import static ru.practicum.shareit.params.PaginationParam.DEFAULT_START_INDEX;

/**
 *
 */
@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {
    public static final String X_SHARER_USER_ID = "X-Sharer-User-Id";
    private final ItemClient itemClient;

    @Autowired
    public ItemController(ItemClient itemClient) {
        this.itemClient = itemClient;
    }

    @PostMapping()
    public ResponseEntity<Object> create(@RequestHeader(X_SHARER_USER_ID) int userId,
                                         @Validated(Create.class) @RequestBody ItemDto itemDto) {
        log.info("Запрос на создание вещи {}", itemDto.getName());
        return itemClient.create(userId, itemDto);
    }

    @PatchMapping("{id}")
    public ResponseEntity<Object> update(@RequestHeader(X_SHARER_USER_ID) int userId,
                                         @PathVariable("id") int itemId,
                                         @Validated(Update.class) @RequestBody ItemDto itemDto) {
        log.info("Запрос на обновление вещи {}", itemId);
        return itemClient.update(userId, itemId, itemDto);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> findItem(@RequestHeader(X_SHARER_USER_ID) int userId,
                                           @PathVariable("id") int itemId) {
        log.info("Запрос вещи по id: {}", itemId);
        return itemClient.findItem(userId, itemId);
    }

    @GetMapping()
    public ResponseEntity<Object> findUserItems(@RequestHeader(X_SHARER_USER_ID) int userId,
                                                @PositiveOrZero @RequestParam(name = "from",
                                                        defaultValue = DEFAULT_START_INDEX) int from,
                                                @Positive @RequestParam(name = "size",
                                                        defaultValue = DEFAULT_PAGE_SIZE) int size) {
        log.info("Запрос всех вещей пользователя");
        return itemClient.findUserItems(userId, from, size);
    }

    @GetMapping("search")
    public ResponseEntity<Object> findItems(@RequestHeader(X_SHARER_USER_ID) int userId,
                                            @RequestParam(name = "text", required = false) String searchString,
                                            @PositiveOrZero @RequestParam(name = "from",
                                                    defaultValue = DEFAULT_START_INDEX) int from,
                                            @Positive @RequestParam(name = "size",
                                                    defaultValue = DEFAULT_PAGE_SIZE) int size) {
        log.info("Поиск вещей потенциальным арендатором. Строка запроса: {}", searchString);
        return itemClient.findItems(userId, searchString, from, size);
    }

    //POST /items/{itemId}/comment
    @PostMapping("{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(X_SHARER_USER_ID) int userId,
                                                @Valid @RequestBody CommentDto commentDto,
                                                @PathVariable("itemId") int itemId
    ) {
        log.info("Запрос на создание отзыва для вещи: {}", itemId);
        return itemClient.createComment(userId, itemId, commentDto);
    }
}