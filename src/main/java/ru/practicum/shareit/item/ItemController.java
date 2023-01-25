package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.indicators.Create;
import ru.practicum.shareit.indicators.Update;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping()
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") int userId,
                          @Validated(Create.class) @RequestBody ItemDto itemDto) {
        log.info("Запрос на создание вещи {}", itemDto.getName());
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("{id}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") int userId,
                          @PathVariable("id") int itemId,
                          @Validated(Update.class) @RequestBody ItemDto itemDto) {
        log.info("Запрос на обновление вещи {}", itemId);
        return itemService.update(userId, itemId, itemDto);
    }

    @GetMapping("{id}")
    public ItemDto findItem(@RequestHeader("X-Sharer-User-Id") int userId,
                            @PathVariable("id") int itemId) {
        log.info("Запрос вещи по id: {}", itemId);
        return itemService.findItem(userId, itemId);
    }

    @GetMapping()
    public List<ItemDto> findUserItems(@RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("Запрос всех вещей пользователя");
        return itemService.findUserItems(userId);
    }

    @GetMapping("search")
    public List<ItemDto> findItems(@RequestHeader("X-Sharer-User-Id") int userId,
                                   @RequestParam(name = "text", required = false) String searchString) {
        log.info("Поиск вещей потенциальным арендатором. Строка запроса: {}", searchString);
        return itemService.findItems(userId, searchString);
    }
}