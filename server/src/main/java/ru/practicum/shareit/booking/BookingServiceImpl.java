package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.dictionary.BookingGetState;
import ru.practicum.shareit.dictionary.BookingStatus;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    public final BookingRepository bookingRepository;

    public final UserService userService;
    public final ItemService itemService;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, UserService userService, ItemService itemService) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.itemService = itemService;
    }

    @Override
    @Transactional
    public BookingDto create(int userId, BookingDto bookingDto) {
        validateInputs(bookingDto);
        User user = userService.getValidUser(userId);
        Item item = getValidItem(bookingDto.getItemId());
        if (userId == item.getOwner().getId()) {
            throw new NotFoundException("Забронировать свою вещь невозможно");
        }
        bookingDto.setBooker(user);
        bookingDto.setStatus(BookingStatus.WAITING);
        bookingDto.setItem(item);
        return BookingMapper.toBookingDto(bookingRepository.save(BookingMapper.toBooking(bookingDto)));
    }

    @Override
    @Transactional
    public BookingDto changeBookingStatus(int userId, int bookingId, boolean approved) {
        User user = userService.getValidUser(userId);
        Booking booking = getValidBooking(bookingId);
        if (user.getId() != booking.getItem().getOwner().getId()) {
            throw new NotFoundException(
                    String.format("Пользователю не хватает прав на редактирование бронирования c id = %s", bookingId));
        }
        if (!booking.getStatus().equals(BookingStatus.WAITING)) {
            throw new ValidationException(
                    String.format("Статус бронирования c id = %s уже изменен", bookingId));
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto findBooking(int userId, int bookingId) {
        User user = userService.getValidUser(userId);
        Booking booking = getValidBooking(bookingId);
        if (user.getId() != booking.getBooker().getId() && user.getId() != booking.getItem().getOwner().getId()) {
            throw new NotFoundException(
                    String.format("Пользователю не хватает прав на поиск бронирования c id = %s", bookingId));
        }
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> findUserBooking(int userId, BookingGetState bookingGetState, int from, int size) {
        userService.getValidUser(userId);
        List<BookingDto> bookingDtos;
        switch (bookingGetState) {
            case WAITING:
                bookingDtos = bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING,
                                PageRequest.of(from / size, size)).stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
                break;
            case REJECTED:
                bookingDtos = bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED,
                                PageRequest.of(from / size, size)).stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
                break;
            case PAST:
                bookingDtos = bookingRepository.findByBooker_IdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now(),
                                PageRequest.of(from / size, size)).stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
                break;
            case CURRENT:
                bookingDtos = bookingRepository.findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                                LocalDateTime.now(), LocalDateTime.now(),
                                PageRequest.of(from / size, size)).stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
                break;
            case FUTURE:
                bookingDtos = bookingRepository.findByBooker_IdAndStartAfterOrderByStartDesc(userId,
                                LocalDateTime.now(),
                                PageRequest.of(from / size, size)).stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
                break;
            default:
                bookingDtos = bookingRepository.findByBooker_IdOrderByStartDesc(userId,
                                PageRequest.of(from / size, size)).stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
                break;
        }
        return bookingDtos;
    }

    @Override
    public List<BookingDto> findItemUserBooking(int userId, BookingGetState bookingGetState, int from, int size) {
        userService.getValidUser(userId);
        List<BookingDto> bookingDtos;
        switch (bookingGetState) {
            case WAITING:
                bookingDtos = bookingRepository.findByItem_Owner_IdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING, PageRequest.of(from / size, size)).stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
                break;
            case REJECTED:
                bookingDtos = bookingRepository.findByItem_Owner_IdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED, PageRequest.of(from / size, size)).stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
                break;
            case PAST:
                bookingDtos = bookingRepository.findByItem_Owner_IdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now(), PageRequest.of(from / size, size)).stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
                break;
            case CURRENT:
                bookingDtos = bookingRepository.findByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now(), PageRequest.of(from / size, size)).stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
                break;
            case FUTURE:
                bookingDtos = bookingRepository.findByItem_Owner_IdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now(), PageRequest.of(from / size, size)).stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
                break;
            default:
                bookingDtos = bookingRepository.findByItem_Owner_IdOrderByStartDesc(userId, PageRequest.of(from / size, size)).stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());
                break;
        }

        return bookingDtos;
    }

    private Booking getValidBooking(int bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("на сервере отстутствует бронирование c id = %s", bookingId)));
    }

    private Item getValidItem(int itemId) {
        Item item = itemService.getValidItem(itemId);
        if (item.getAvailable().equals(false)) {
            throw new ValidationException(
                    String.format("Вещь c id = %s не доступна для бронирования", itemId));
        }
        return item;
    }

    private void validateInputs(BookingDto bookingDto) {
        if (bookingDto.getEnd().isBefore(LocalDateTime.now())) {
            throw new ValidationException(
                    String.format("Дата окончания бронирования не может быть в прошлом: %s", bookingDto.getEnd()));
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new ValidationException(
                    String.format("Дата окончания бронирования: %s не может быть раньше даты начала бронирования: %s",
                            bookingDto.getEnd(), bookingDto.getStart()));
        }
        if (bookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new ValidationException(
                    String.format("Дата начала бронирования не может быть в прошлом: %s", bookingDto.getStart()));
        }
    }
}