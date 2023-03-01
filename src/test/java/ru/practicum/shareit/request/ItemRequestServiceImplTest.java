package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestServiceImpl;

    User expectedUser;
    ItemRequest expectedItemRequest;
    ItemRequestDto expectedItemRequestDto;
    Page<ItemRequest> expectedPageItemRequests;

    private final int userId = 0;

    private final int itemRequestId = 0;

    private List<ItemRequestDto> expectedItemRequestDtos;
    private List<ItemRequest> expectedItemRequests;

    @BeforeEach
    void init() {
        expectedUser = User.builder()
                .name("name1")
                .email("email1")
                .build();
        expectedItemRequest = ItemRequest.builder()
                .requestor(expectedUser)
                .build();
        expectedItemRequestDto = ItemRequestMapper.toItemRequestDto(expectedItemRequest);

        expectedItemRequestDtos = new ArrayList<>(List.of(expectedItemRequestDto));
        expectedItemRequests = new ArrayList<>(List.of(expectedItemRequest));
        expectedPageItemRequests = new PageImpl<>(List.of(expectedItemRequest));
    }

    @Test
    void create() {
        when(itemRequestRepository.save(expectedItemRequest)).thenReturn(expectedItemRequest);
        when(userService.getValidUser(userId)).thenReturn(expectedUser);

        ItemRequestDto itemRequestDto = itemRequestServiceImpl.create(userId, expectedItemRequestDto);

        verify(userService, times(1)).getValidUser(userId);
        verify(itemRequestRepository, times(1)).save(expectedItemRequest);
        assertEquals(expectedItemRequestDto, itemRequestDto);
    }

    @Test
    void findUserItemRequest() {
        when(userService.getValidUser(userId)).thenReturn(expectedUser);
        when(itemRequestRepository.findByRequestor_IdOrderByCreatedDesc(userId))
                .thenReturn(expectedItemRequests);

        List<ItemRequestDto> itemRequestDtos = itemRequestServiceImpl.findUserItemRequest(userId);

        verify(itemRequestRepository, times(1))
                .findByRequestor_IdOrderByCreatedDesc(userId);
        assertEquals(expectedItemRequestDtos, itemRequestDtos);
    }

    @Test
    void findAllItemRequest() {
        int from = 0;
        int size = 1;
        when(itemRequestRepository.findAll(PageRequest.of(from, size)))
                .thenReturn(expectedPageItemRequests);

        int userId2 = 1;
        List<ItemRequestDto> itemRequestDtos = itemRequestServiceImpl.findAllItemRequest(userId2, from, size);

        verify(itemRequestRepository, times(1))
                .findAll(PageRequest.of(from, size));
        assertEquals(expectedItemRequestDtos, itemRequestDtos);
    }

    @Test
    void findItemRequest() {
        when(userService.getValidUser(userId)).thenReturn(expectedUser);
        when(itemRequestRepository.findById(itemRequestId)).thenReturn(Optional.of(expectedItemRequest));

        ItemRequestDto itemRequestDto = itemRequestServiceImpl.findItemRequest(userId, itemRequestId);

        verify(userService, times(1)).getValidUser(userId);
        verify(itemRequestRepository, times(1)).findById(itemRequestId);
        assertEquals(expectedItemRequestDto, itemRequestDto);
    }

    @Test
    void findItemRequestNotFoundException() {
        when(userService.getValidUser(userId)).thenReturn(expectedUser);
        when(itemRequestRepository.findById(itemRequestId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemRequestServiceImpl.findItemRequest(userId, itemRequestId));
    }
}