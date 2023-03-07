package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageRequest;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserService userService;

    @Mock
    private ItemService itemService;

    @InjectMocks
    private BookingServiceImpl bookingServiceImpl;

    Booking expectedBooking;

    BookingDto expectedBookingDto;

    User expectedUser;

    User expectedUser2;

    Item expectedItem;

    private final int userId = 0;

    private final int userId2 = 1;
    private final int itemId = 0;
    private final int bookingId = 0;

    private final int from = 0;

    private final int size = 1;

    private final boolean approved = true;

    private List<BookingDto> expectedBookingDtos;
    private List<Booking> expectedBookings;

    @BeforeEach
    void init() {
        expectedUser = User.builder()
                .name("name1")
                .email("email1")
                .build();

        expectedUser2 = User.builder()
                .id(userId2)
                .name("name2")
                .email("email2")
                .build();

        expectedItem = Item.builder()
                .name("name1")
                .description("description1")
                .available(true)
                .owner(expectedUser)
                .build();

        expectedBooking = Booking.builder()
                .status(BookingStatus.WAITING)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .item(expectedItem)
                .booker(expectedUser2)
                .build();
        expectedBookingDto = BookingMapper.toBookingDto(expectedBooking);

        expectedBookingDtos = new ArrayList<>(List.of(expectedBookingDto));
        expectedBookings = new ArrayList<>(List.of(expectedBooking));
    }

    @Test
    void create() {
        when(bookingRepository.save(expectedBooking)).thenReturn(expectedBooking);
        when(userService.getValidUser(userId2)).thenReturn(expectedUser2);
        when(itemService.getValidItem(itemId)).thenReturn(expectedItem);

        BookingDto bookingDto = bookingServiceImpl.create(userId2, expectedBookingDto);

        verify(userService, times(1)).getValidUser(userId2);
        verify(itemService, times(1)).getValidItem(itemId);
        verify(bookingRepository, times(1)).save(expectedBooking);
        assertEquals(expectedBookingDto, bookingDto);
    }

    @Test
    void createValidationException() {
        when(bookingRepository.save(expectedBooking)).thenReturn(expectedBooking);
        when(userService.getValidUser(userId2)).thenReturn(expectedUser2);
        when(itemService.getValidItem(itemId)).thenReturn(expectedItem);

        expectedItem.setAvailable(false);
        assertThrows(ValidationException.class, () -> bookingServiceImpl.create(userId2, expectedBookingDto));

        expectedItem.setAvailable(true);
        expectedBookingDto.setEnd(LocalDateTime.now().minusDays(1));
        assertThrows(ValidationException.class, () -> bookingServiceImpl.create(userId2, expectedBookingDto));

        expectedBookingDto.setEnd(LocalDateTime.now().plusDays(1));
        expectedBookingDto.setStart(LocalDateTime.now().plusDays(2));
        assertThrows(ValidationException.class, () -> bookingServiceImpl.create(userId2, expectedBookingDto));

        expectedBookingDto.setStart(LocalDateTime.now().minusDays(1));
        assertThrows(ValidationException.class, () -> bookingServiceImpl.create(userId2, expectedBookingDto));
    }

    @Test
    void createNotFoundException() {
        when(bookingRepository.save(expectedBooking)).thenReturn(expectedBooking);
        when(userService.getValidUser(userId)).thenReturn(expectedUser);
        when(itemService.getValidItem(itemId)).thenReturn(expectedItem);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(expectedBooking));

        assertThrows(NotFoundException.class, () -> bookingServiceImpl.create(userId, expectedBookingDto));
    }

    @Test
    void changeBookingStatus() {
        expectedBookingDto.setStatus(BookingStatus.APPROVED);
        when(bookingRepository.save(expectedBooking)).thenReturn(expectedBooking);
        when(userService.getValidUser(userId)).thenReturn(expectedUser);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(expectedBooking));

        BookingDto bookingDto = bookingServiceImpl.changeBookingStatus(userId, bookingId, approved);

        verify(userService, times(1)).getValidUser(userId);
        verify(bookingRepository, times(1)).save(expectedBooking);
        assertEquals(expectedBookingDto, bookingDto);
    }

    @Test
    void changeBookingStatusNotFoundException() {
        when(bookingRepository.save(expectedBooking)).thenReturn(expectedBooking);
        when(userService.getValidUser(userId2)).thenReturn(expectedUser2);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(expectedBooking));

        assertThrows(NotFoundException.class, () -> bookingServiceImpl.changeBookingStatus(userId2, bookingId, approved));
    }

    @Test
    void changeBookingStatusValidationException() {
        when(bookingRepository.save(expectedBooking)).thenReturn(expectedBooking);
        when(userService.getValidUser(userId)).thenReturn(expectedUser);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(expectedBooking));

        expectedBooking.setStatus(BookingStatus.APPROVED);
        assertThrows(ValidationException.class, () -> bookingServiceImpl.changeBookingStatus(userId, bookingId, approved));
    }

    @Test
    void findBooking() {
        when(userService.getValidUser(userId)).thenReturn(expectedUser);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(expectedBooking));

        BookingDto bookingDto = bookingServiceImpl.findBooking(userId, bookingId);

        verify(userService, times(1)).getValidUser(userId);
        verify(bookingRepository, times(1)).findById(bookingId);
        assertEquals(expectedBookingDto, bookingDto);
    }

    @Test
    void findBookingNotFoundException2() {
        when(userService.getValidUser(userId)).thenReturn(expectedUser);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(expectedBooking));

        expectedBooking.getItem().setOwner(expectedUser2);
        assertThrows(NotFoundException.class, () -> bookingServiceImpl.findBooking(userId, bookingId));
    }

    @Test
    void findBookingNotFoundException() {
        when(userService.getValidUser(userId)).thenReturn(expectedUser);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingServiceImpl.findBooking(userId, bookingId));
    }

    @ParameterizedTest()
    @EnumSource(value = BookingGetState.class, names = {"WAITING", "REJECTED", "PAST", "CURRENT", "FUTURE", "ALL"})
    void findUserBooking(BookingGetState bookingGetState) {
        when(userService.getValidUser(userId)).thenReturn(expectedUser);
        when(bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(userId,
                BookingStatus.WAITING, PageRequest.of(from, size)))
                .thenReturn(expectedBookings);
        when(bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(userId,
                BookingStatus.REJECTED, PageRequest.of(from, size)))
                .thenReturn(expectedBookings);
        when(bookingRepository.findByBooker_IdAndEndBeforeOrderByStartDesc(anyInt(),
                any(), any()))
                .thenReturn(expectedBookings);
        when(bookingRepository.findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(anyInt(),
                any(), any(), any()))
                .thenReturn(expectedBookings);
        when(bookingRepository.findByBooker_IdAndStartAfterOrderByStartDesc(anyInt(),
                any(), any()))
                .thenReturn(expectedBookings);
        when(bookingRepository.findByBooker_IdOrderByStartDesc(userId, PageRequest.of(from, size)))
                .thenReturn(expectedBookings);

        List<BookingDto> bookingDtos = bookingServiceImpl.findUserBooking(userId, bookingGetState, from, size);

        verify(userService, times(1)).getValidUser(userId);
        assertEquals(expectedBookingDtos, bookingDtos);
        switch (bookingGetState) {
            case WAITING:
                verify(bookingRepository, times(1))
                        .findByBooker_IdAndStatusOrderByStartDesc(userId,
                                BookingStatus.WAITING, PageRequest.of(from, size));
                break;
            case REJECTED:
                verify(bookingRepository, times(1))
                        .findByBooker_IdAndStatusOrderByStartDesc(userId,
                                BookingStatus.REJECTED, PageRequest.of(from, size));
                break;
            case PAST:
                verify(bookingRepository, times(1))
                        .findByBooker_IdAndEndBeforeOrderByStartDesc(anyInt(),
                                any(), any());
                break;
            case CURRENT:
                verify(bookingRepository, times(1))
                        .findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(anyInt(),
                                any(), any(), any());
                break;
            case FUTURE:
                verify(bookingRepository, times(1))
                        .findByBooker_IdAndStartAfterOrderByStartDesc(anyInt(),
                                any(), any());
                break;
            default:
                verify(bookingRepository, times(1))
                        .findByBooker_IdOrderByStartDesc(userId, PageRequest.of(from, size));
                break;
        }
    }

    @ParameterizedTest()
    @EnumSource(value = BookingGetState.class, names = {"WAITING", "REJECTED", "PAST", "CURRENT", "FUTURE", "ALL"})
    void findItemUserBooking(BookingGetState bookingGetState) {
        when(userService.getValidUser(userId)).thenReturn(expectedUser);
        when(bookingRepository.findByItem_Owner_IdAndStatusOrderByStartDesc(userId,
                BookingStatus.WAITING, PageRequest.of(from, size)))
                .thenReturn(expectedBookings);
        when(bookingRepository.findByItem_Owner_IdAndStatusOrderByStartDesc(userId,
                BookingStatus.REJECTED, PageRequest.of(from, size)))
                .thenReturn(expectedBookings);
        when(bookingRepository.findByItem_Owner_IdAndEndBeforeOrderByStartDesc(anyInt(),
                any(), any()))
                .thenReturn(expectedBookings);
        when(bookingRepository.findByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStartDesc(anyInt(),
                any(), any(), any()))
                .thenReturn(expectedBookings);
        when(bookingRepository.findByItem_Owner_IdAndStartAfterOrderByStartDesc(anyInt(),
                any(), any()))
                .thenReturn(expectedBookings);
        when(bookingRepository.findByItem_Owner_IdOrderByStartDesc(userId, PageRequest.of(from, size)))
                .thenReturn(expectedBookings);

        List<BookingDto> bookingDtos = bookingServiceImpl.findItemUserBooking(userId, bookingGetState, from, size);

        verify(userService, times(1)).getValidUser(userId);
        assertEquals(expectedBookingDtos, bookingDtos);
        switch (bookingGetState) {
            case WAITING:
                verify(bookingRepository, times(1))
                        .findByItem_Owner_IdAndStatusOrderByStartDesc(userId,
                                BookingStatus.WAITING, PageRequest.of(from, size));
                break;
            case REJECTED:
                verify(bookingRepository, times(1))
                        .findByItem_Owner_IdAndStatusOrderByStartDesc(userId,
                                BookingStatus.REJECTED, PageRequest.of(from, size));
                break;
            case PAST:
                verify(bookingRepository, times(1))
                        .findByItem_Owner_IdAndEndBeforeOrderByStartDesc(anyInt(),
                                any(), any());
                break;
            case CURRENT:
                verify(bookingRepository, times(1))
                        .findByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStartDesc(anyInt(),
                                any(), any(), any());
                break;
            case FUTURE:
                verify(bookingRepository, times(1))
                        .findByItem_Owner_IdAndStartAfterOrderByStartDesc(anyInt(),
                                any(), any());
                break;
            default:
                verify(bookingRepository, times(1))
                        .findByItem_Owner_IdOrderByStartDesc(userId, PageRequest.of(from, size));
                break;
        }
    }
}