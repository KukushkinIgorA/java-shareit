package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRepositoryIT {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    User expectedUser;
    Item expectedItem;

    @BeforeEach
    void init() {
        expectedUser = User.builder()
                .name("name1")
                .email("email1")
                .build();

        userRepository.save(expectedUser);

        expectedItem = Item.builder()
                .name("name1")
                .description("description1")
                .available(true)
                .owner(expectedUser)
                .build();

        itemRepository.save(expectedItem);
    }

    @Test
    void findByOwnerOrderById() {
        List<Item> items = itemRepository.findByOwnerOrderById(expectedUser, PageRequest.of(0, 1));
        assertEquals(expectedItem, items.get(0));
    }

    @Test
    void findItemBySearchString() {
        List<Item> items = itemRepository.findItemBySearchString("name1", PageRequest.of(0, 1));
        assertEquals(expectedItem, items.get(0));
    }

    @AfterEach
    void end() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }
}