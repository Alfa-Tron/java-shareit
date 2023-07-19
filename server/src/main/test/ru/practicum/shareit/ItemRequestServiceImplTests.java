package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.jpa.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepos;
import ru.practicum.shareit.request.ItemRequestServiceImpl;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.jpa.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Transactional
public class ItemRequestServiceImplTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRequestRepos requestRepository;

    @Mock
    private RequestMapper requestMapper;

    @Mock
    private ItemRepository repositoryItem;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testCreateRequest_ValidData_RequestCreatedSuccessfully() {
        long userId = 1L;
        ItemRequest request = new ItemRequest();
        User currentUser = new User();
        LocalDateTime now = LocalDateTime.now();

        when(userRepository.findById(userId)).thenReturn(Optional.of(currentUser));
        when(requestRepository.save(request)).thenReturn(request);

        ItemRequest result = itemRequestService.createRequest(userId, request);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(currentUser, request.getRequestor());
        Assertions.assertEquals(now.getYear(), request.getCreated().getYear());
        Assertions.assertEquals(now.getMonth(), request.getCreated().getMonth());
        Assertions.assertEquals(now.getDayOfMonth(), request.getCreated().getDayOfMonth());
        verify(requestRepository).save(request);
    }

    @Test
    public void testCreateRequest_InvalidUserId_ThrowsNotFoundException() {
        long userId = 1L;
        ItemRequest request = new ItemRequest();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> itemRequestService.createRequest(userId, request));

        Assertions.assertEquals(exception.getMessage(),"User not found");

    }

    @Test
    public void testGetRequests_ValidUserId_ReturnsListOfItemRequestDto() {
        long userId = 1L;
        User currentUser = new User();
        List<ItemRequest> requests = new ArrayList<>();
        ItemRequest request1 = new ItemRequest();
        ItemRequest request2 = new ItemRequest();

        requests.add(request1);
        requests.add(request2);

        Map<ItemRequest, List<Item>> items = new HashMap<>();
        items.put(request1, new ArrayList<>());
        items.put(request2, new ArrayList<>());

        List<ItemRequestDto> expectedDtos = new ArrayList<>();
        ItemRequestDto dto1 = new ItemRequestDto();
        ItemRequestDto dto2 = new ItemRequestDto();

        expectedDtos.add(dto1);
        expectedDtos.add(dto2);

        when(userRepository.findById(userId)).thenReturn(Optional.of(currentUser));
        when(requestRepository.findByRequestor_IdOrderByCreatedDesc(currentUser.getId())).thenReturn(requests);
        when(repositoryItem.findByRequestIn(requests)).thenReturn(new ArrayList<>());
        when(requestMapper.requestToDto(request1)).thenReturn(dto1);
        when(requestMapper.requestToDto(request2)).thenReturn(dto2);

        List<ItemRequestDto> result = itemRequestService.getRequests(userId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedDtos.size(), result.size());
        Assertions.assertTrue(result.contains(dto1));
        Assertions.assertTrue(result.contains(dto2));
    }

    @Test
    public void testGetRequests_InvalidUserId_ThrowsNotFoundException() {
        long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> itemRequestService.getRequests(userId));

        Assertions.assertEquals(exception.getMessage(),"User not found");
    }

    @Test
    public void testGetRequestOne_ValidData_ReturnsItemRequestDtoWithItems() {
        Long id = 1L;
        long userId = 1L;
        ItemRequest request = new ItemRequest();
        User user = new User();
        Item item = new Item();
        ItemRequestDto expectedDto = new ItemRequestDto();

        request.setId(id);

        when(requestRepository.findById(id)).thenReturn(Optional.of(request));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(repositoryItem.findByRequestId(request.getId())).thenReturn(item);
        when(requestMapper.requestToDto(request)).thenReturn(expectedDto);
        when(requestMapper.itemToRequestDtoItem(item)).thenReturn(new ItemRequestDto.Items());

        ItemRequestDto result = itemRequestService.getRequestOne(id, userId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedDto, result);
        Assertions.assertEquals(1, result.getItems().size());
        verify(requestMapper).itemToRequestDtoItem(item);
    }

    @Test
    public void testGetRequestOne_InvalidRequestId_ThrowsNotFoundException() {
        Long id = 1L;
        long userId = 1L;

        when(requestRepository.findById(id)).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> itemRequestService.getRequestOne(id, userId));

        Assertions.assertEquals(exception.getMessage(),"Request not found");


    }

    @Test
    public void testGetRequestOne_InvalidUserId_ThrowsNotFoundException() {
        Long id = 1L;
        long userId = 1L;
        ItemRequest request = new ItemRequest();

        when(requestRepository.findById(id)).thenReturn(Optional.of(request));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> itemRequestService.getRequestOne(id, userId));
        Assertions.assertEquals(exception.getMessage(),"User not found");

    }


    @Test
    public void testItemToDtos_WithItems_ReturnsListWithItemRequestDto() {
        Map<ItemRequest, List<Item>> items = new HashMap<>();
        ItemRequest request1 = new ItemRequest();
        ItemRequest request2 = new ItemRequest();
        List<Item> itemList1 = new ArrayList<>();
        List<Item> itemList2 = new ArrayList<>();

        items.put(request1, itemList1);
        items.put(request2, itemList2);

        ItemRequestDto dto1 = new ItemRequestDto();
        ItemRequestDto dto2 = new ItemRequestDto();

        when(requestMapper.requestToDto(request1)).thenReturn(dto1);
        when(requestMapper.requestToDto(request2)).thenReturn(dto2);

        List<ItemRequestDto> result = itemRequestService.itemToDtos(items);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(items.size(), result.size());
        Assertions.assertTrue(result.contains(dto1));
        Assertions.assertTrue(result.contains(dto2));
    }


    @Test
    public void testItemToDtos_WithoutItems_ReturnsListWithItemRequestDto() {
        Map<ItemRequest, List<Item>> items = new HashMap<>();
        ItemRequest request1 = new ItemRequest();
        ItemRequest request2 = new ItemRequest();

        items.put(request1, Collections.emptyList());
        items.put(request2, Collections.emptyList());

        ItemRequestDto dto1 = new ItemRequestDto();
        ItemRequestDto dto2 = new ItemRequestDto();

        when(requestMapper.requestToDto(request1)).thenReturn(dto1);
        when(requestMapper.requestToDto(request2)).thenReturn(dto2);

        List<ItemRequestDto> result = itemRequestService.itemToDtos(items);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(items.size(), result.size());
        Assertions.assertTrue(result.contains(dto1));
        Assertions.assertTrue(result.contains(dto2));
    }
}