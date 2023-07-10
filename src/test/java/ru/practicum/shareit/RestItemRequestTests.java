package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.comment.*;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.jpa.ItemRepository;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.jpa.UserRepository;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Import(UserController.class)
@Transactional
public class RestItemRequestTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ItemService itemService;
    @MockBean
    private BookingService bookingService;
    @MockBean
    private UserRepository repositoryUser;
    @MockBean
    private ItemRequestService itemRequestService;
    @Mock
    private ItemRepository itemRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ItemMapper itemMapper;

    @Test
    void testCreateRequest() throws Exception {
        long userId = 0;
        ItemRequestDtoIn requestDtoIn = new ItemRequestDtoIn();
        requestDtoIn.setDescription("Test Request");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription(requestDtoIn.getDescription());
        itemRequest.setRequestor(new User());

        when(itemRequestService.createRequest(eq(userId), any(ItemRequest.class))).thenReturn(itemRequest);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDtoIn)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequest.getId()))
                .andExpect(jsonPath("$.description").value(requestDtoIn.getDescription()))
                .andExpect(jsonPath("$.requestor.id").value(userId));

        verify(itemRequestService, times(1)).createRequest(eq(userId), any(ItemRequest.class));
    }
    @Test
    void testGetItemByIdList() throws Exception {
        long userId = 123;

        ItemRequestDto itemRequestDto1 = new ItemRequestDto();
        itemRequestDto1.setId(1L);
        itemRequestDto1.setDescription("Request 1");

        ItemRequestDto itemRequestDto2 = new ItemRequestDto();
        itemRequestDto2.setId(2L);
        itemRequestDto2.setDescription("Request 2");

        List<ItemRequestDto> itemRequestDtos = Arrays.asList(itemRequestDto1, itemRequestDto2);

        when(itemRequestService.getRequests(userId)).thenReturn(itemRequestDtos);

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemRequestDto1.getId()))
                .andExpect(jsonPath("$[0].description").value(itemRequestDto1.getDescription()))
                .andExpect(jsonPath("$[1].id").value(itemRequestDto2.getId()))
                .andExpect(jsonPath("$[1].description").value(itemRequestDto2.getDescription()));

        verify(itemRequestService, times(1)).getRequests(userId);
    }
    @Test
    void testGetAllRequests() throws Exception {
        long userId = 123;

        Integer from = 0;
        Integer size = 10;

        ItemRequestDto itemRequestDto1 = new ItemRequestDto();
        itemRequestDto1.setId(1L);
        itemRequestDto1.setDescription("Request 1");

        ItemRequestDto itemRequestDto2 = new ItemRequestDto();
        itemRequestDto2.setId(2L);
        itemRequestDto2.setDescription("Request 2");

        List<ItemRequestDto> itemRequestDtos = Arrays.asList(itemRequestDto1, itemRequestDto2);

        when(itemRequestService.getAllRequests(from, size, userId)).thenReturn(itemRequestDtos);

        mvc.perform(get("/requests/all")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemRequestDto1.getId()))
                .andExpect(jsonPath("$[0].description").value(itemRequestDto1.getDescription()))
                .andExpect(jsonPath("$[1].id").value(itemRequestDto2.getId()))
                .andExpect(jsonPath("$[1].description").value(itemRequestDto2.getDescription()));

        verify(itemRequestService, times(1)).getAllRequests(from, size, userId);
    }
    @Test
    void testGetItemById() throws Exception {
        long requestId = 123;
        long userId = 456;

        ItemRequestDto itemRequestDto1 = new ItemRequestDto();
        itemRequestDto1.setId(1L);
        itemRequestDto1.setDescription("Request 1");

        when(itemRequestService.getRequestOne(requestId, userId)).thenReturn(itemRequestDto1);

        mvc.perform(get("/requests/{id}", requestId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestDto1.getId()))
                .andExpect(jsonPath("$.description").value(itemRequestDto1.getDescription()));

        verify(itemRequestService, times(1)).getRequestOne(requestId, userId);
    }
}
