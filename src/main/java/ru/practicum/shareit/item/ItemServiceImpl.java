package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    public final ItemRepository itemRepository;

    public final UserRepository userRepository;

    public final BookingRepository bookingRepository;

    public final CommentRepository commentRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository, BookingRepository bookingRepository, CommentRepository commentRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional
    public ItemDto create(int userId, ItemDto itemDto) {
        User user = getValidUser(userId);
        itemDto.setOwner(user);
        return ItemMapper.toItemDto(itemRepository.save(ItemMapper.toItem(itemDto)));
    }

    @Override
    @Transactional
    public ItemDto update(int userId, int itemId, ItemDto itemDto) {
        User user = getValidUser(userId);
        Item item = getValidItem(itemId);
        if (item.getOwner().getId() != userId) {
            throw new ForbiddenException(
                    String.format("Пользователю не хватает прав на редактирование вещи c id = %s", itemId));
        }
        itemDto.setId(itemId);
        itemDto.setOwner(user);
        itemDto.setAvailable(itemDto.getAvailable() != null && !itemDto.getAvailable().equals(item.getAvailable()) ?
                itemDto.getAvailable() : item.getAvailable());
        itemDto.setDescription(itemDto.getDescription() != null ? itemDto.getDescription() : item.getDescription());
        itemDto.setName(itemDto.getName() != null ? itemDto.getName() : item.getName());
        return ItemMapper.toItemDto(itemRepository.save(ItemMapper.toItem(itemDto)));
    }

    @Override
    public ItemDto findItem(int userId, int itemId) {
        getValidUser(userId);
        Item item = getValidItem(itemId);
        ItemDto itemDto = ItemMapper.toItemDto(item);
        findBookingDateTime(itemDto, userId);
        findComments(itemDto);
        return itemDto;
    }

    @Override
    public List<ItemDto> findUserItems(int userId) {
        User user = getValidUser(userId);
        return itemRepository.findByOwnerOrderById(user).stream().map(ItemMapper::toItemDto)
                .map(itemDto -> findBookingDateTime(itemDto, userId))
                .map(this::findComments)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> findItems(int userId, String searchString) {
        if (searchString.isEmpty()) {
            return Collections.emptyList();
        } else {
            return itemRepository.findItemBySearchString(searchString.toLowerCase())
                    .stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
        }
    }

    @Override
    @Transactional
    public CommentDto createComment(int userId, int itemId, CommentDto commentDto) {
        User user = getValidUser(userId);
        Item item = getValidItem(itemId);
        if (bookingRepository.findByBooker_IdAndItem_Id(userId, itemId)
                .stream()
                .noneMatch(booking -> booking.getEnd().isBefore(LocalDateTime.now()))) {
            throw new ValidationException(String.format("Пользователь c id = %s не может оставлять отзывы на вещь c id = %s", userId, itemId));
        }
        commentDto.setAuthor(user);
        commentDto.setItem(item);
        return CommentMapper.toCommentDto(commentRepository.save(CommentMapper.toComment(commentDto)));
    }

    private User getValidUser(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("на сервере отстутствует пользователь c id = %s", userId)));
    }

    private Item getValidItem(int itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("на сервере отстутствует вещь c id = %s", itemId)));
    }


    private ItemDto findBookingDateTime(ItemDto itemDto, int userId) {
        List<Booking> bookings = bookingRepository.findByItem_IdAndItem_Owner_IdOrderByStart(itemDto.getId(), userId);
        Booking lastBooking = null;
        Booking nextBooking = null;
        LocalDateTime now = LocalDateTime.now();
        for (Booking booking : bookings) {
            if (booking.getStart().isBefore(now)) {
                lastBooking = booking;
            }
            if (booking.getStart().isAfter(now) && nextBooking == null) {
                nextBooking = booking;
            }
        }

        itemDto.setLastBooking(lastBooking != null ? ItemMapper.toBookingItemDto(lastBooking) : null);
        itemDto.setNextBooking(nextBooking != null ? ItemMapper.toBookingItemDto(nextBooking) : null);

        return itemDto;
    }


    private ItemDto findComments(ItemDto itemDto) {
        List<Comment> comments = commentRepository.findByItem_Id(itemDto.getId());
        itemDto.setComments(comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList()));
        return itemDto;
    }
}