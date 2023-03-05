package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ItemServiceImpl itemServiceImpl;

    Item expectedItem;
    User expectedUser;
    ItemDto expectedItemDto;
    Comment expectedComment;
    CommentDto expectedCommentDto;

    Booking expectedBooking;
    private List<ItemDto> expectedItemDtos;
    private final int userId = 0;
    private final int userId2 = 1;
    private final int itemId = 0;

    private final int from = 0;

    private final int size = 1;
    private List<Item> expectedItems;
    private List<Booking> expectedBookings;


    @BeforeEach
    void init() {
        expectedUser = User.builder()
                .name("name1")
                .email("email1")
                .build();

        expectedItem = Item.builder()
                .name("name1")
                .description("description1")
                .available(true)
                .owner(expectedUser)
                .build();

        expectedComment = Comment.builder()
                .text("text1")
                .author(expectedUser)
                .item(expectedItem)
                .build();

        expectedCommentDto = CommentMapper.toCommentDto(expectedComment);

        expectedBooking = Booking.builder()
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .booker(expectedUser)
                .build();

        expectedBookings = new ArrayList<>(List.of(expectedBooking));

        expectedItemDto = ItemMapper.toItemDto(expectedItem);
        expectedItemDto.setLastBooking(ItemMapper.toBookingItemDto(expectedBooking));
        expectedItemDto.setComments(Collections.emptyList());

        expectedItemDtos = new ArrayList<>(List.of(expectedItemDto));
        expectedItems = new ArrayList<>(List.of(expectedItem));
    }

    @Test
    void create() {
        expectedItemDto.setLastBooking(null);
        expectedItemDto.setComments(null);
        when(itemRepository.save(expectedItem)).thenReturn(expectedItem);
        when(userService.getValidUser(userId)).thenReturn(expectedUser);

        ItemDto itemDto = itemServiceImpl.create(userId, expectedItemDto);

        verify(userService, times(1)).getValidUser(userId);
        verify(itemRepository, times(1)).save(expectedItem);
        assertEquals(expectedItemDto, itemDto);
    }

    @Test
    void update() {
        expectedItemDto.setComments(null);
        expectedItemDto.setLastBooking(null);
        when(itemRepository.save(expectedItem)).thenReturn(expectedItem);
        when(userService.getValidUser(userId)).thenReturn(expectedUser);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(expectedItem));

        ItemDto itemDto = itemServiceImpl.update(userId, itemId, expectedItemDto);

        verify(userService, times(1)).getValidUser(userId);
        verify(itemRepository, times(1)).findById(itemId);
        verify(itemRepository, times(1)).save(expectedItem);
        assertEquals(expectedItemDto, itemDto);
    }

    @Test
    void updateForbiddenException() {
        when(userService.getValidUser(userId2)).thenReturn(expectedUser);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(expectedItem));

        assertThrows(ForbiddenException.class, () -> itemServiceImpl.update(userId2, itemId, expectedItemDto));

    }

    @Test
    void findItem() {
        when(userService.getValidUser(userId)).thenReturn(expectedUser);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(expectedItem));
        when(bookingRepository.findByItem_IdAndItem_Owner_IdOrderByStart(itemId, userId)).thenReturn(expectedBookings);

        ItemDto itemDto = itemServiceImpl.findItem(userId, itemId);

        verify(userService, times(1)).getValidUser(userId);
        verify(itemRepository, times(1)).findById(itemId);
        assertEquals(expectedItemDto, itemDto);
    }

    @Test
    void findItemNotFoundException() {
        when(userService.getValidUser(userId)).thenReturn(expectedUser);
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemServiceImpl.findItem(userId, itemId));
    }

    @Test
    void findUserItems() {
        expectedItemDto.setLastBooking(null);
        when(userService.getValidUser(userId)).thenReturn(expectedUser);
        when(itemRepository.findByOwnerOrderById(expectedUser, PageRequest.of(from, size)))
                .thenReturn(expectedItems);

        List<ItemDto> itemDtos = itemServiceImpl.findUserItems(userId, from, size);

        verify(itemRepository, times(1))
                .findByOwnerOrderById(expectedUser, PageRequest.of(from, size));
        assertEquals(expectedItemDtos, itemDtos);
    }

    @Test
    void findItems() {
        expectedItemDtos.get(0).setComments(null);
        expectedItemDto.setLastBooking(null);
        String searchString = "searchString";
        when(itemRepository.findItemBySearchString(searchString.toLowerCase(), PageRequest.of(from, size)))
                .thenReturn(expectedItems);

        List<ItemDto> itemDtos = itemServiceImpl.findItems(userId, searchString, from, size);

        verify(itemRepository, times(1))
                .findItemBySearchString(searchString.toLowerCase(), PageRequest.of(from, size));
        assertEquals(expectedItemDtos, itemDtos);

        itemDtos = itemServiceImpl.findItems(userId, "", from, size);
        verify(itemRepository, times(1))
                .findItemBySearchString(searchString.toLowerCase(), PageRequest.of(from, size));
        assertEquals(Collections.emptyList(), itemDtos);
    }

    @Test
    void createComment() {
        when(userService.getValidUser(userId2)).thenReturn(expectedUser);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(expectedItem));
        when(commentRepository.save(expectedComment)).thenReturn(expectedComment);
        when(bookingRepository.findByBooker_IdAndItem_Id(userId2, itemId))
                .thenReturn(expectedBookings);

        CommentDto commentDto = itemServiceImpl.createComment(userId2, itemId, expectedCommentDto);

        verify(commentRepository, times(1)).save(expectedComment);
        assertEquals(expectedCommentDto, commentDto);
    }

    @Test
    void createCommentValidationException() {
        when(userService.getValidUser(userId2)).thenReturn(expectedUser);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(expectedItem));
        when(bookingRepository.findByBooker_IdAndItem_Id(userId2, itemId))
                .thenReturn(expectedBookings);

        expectedBooking.setEnd(LocalDateTime.now().plusDays(1));
        assertThrows(ValidationException.class, () -> itemServiceImpl.createComment(userId2, itemId, expectedCommentDto));

    }
}