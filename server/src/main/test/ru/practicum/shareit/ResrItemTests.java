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
import ru.practicum.shareit.comment.*;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.jpa.ItemRepository;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
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
public class ResrItemTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ItemService itemService;
    @MockBean
    private UserRepository repositoryUser;
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
    @Transactional
    void createItem() throws Exception {
        ItemDtoIn itemDtoIn = new ItemDtoIn();
        itemDtoIn.setName("Новый предмет");
        itemDtoIn.setDescription("123");
        itemDtoIn.setAvailable(true);

        ItemDto createdItem = new ItemDto();
        createdItem.setId(1L);
        createdItem.setName("Новый предмет");

        User user1 = new User();
        user1.setId(1L);
        user1.setName("John");
        user1.setEmail("john@example.com");

        when(itemService.createItem(anyLong(), any(Item.class))).thenReturn(createdItem);
        when(repositoryUser.findById(anyLong())).thenReturn(Optional.of(user1));

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(createdItem.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(createdItem.getName())));

        verify(itemService, times(1)).createItem(anyLong(), any(Item.class));
    }

    @Test
    void testGetItemById() throws Exception {
        long itemId = 1L;
        long userId = 123L;

        ItemDto itemDto = new ItemDto();
        itemDto.setId(itemId);
        itemDto.setName("Test Item");

        when(itemService.getItemById(eq(itemId), eq(userId))).thenReturn(itemDto);

        mvc.perform(get("/items/{id}", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()));

        verify(itemService, times(1)).getItemById(eq(itemId), eq(userId));

    }

    @Test
    public void testGetItemByUser() throws Exception {
        long userId = 1L;
        Integer from = 0;
        Integer size = 10;

        ItemDto item1 = new ItemDto(1L, "Item 1", "Description 1", true);
        ItemDto item2 = new ItemDto(2L, "Item 2", "Description 2", false);
        List<ItemDto> expectedItems = Arrays.asList(item1, item2);

        when(itemService.getAllItems(userId, from, size)).thenReturn(expectedItems);

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", String.valueOf(userId))
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(item1.getId()))
                .andExpect(jsonPath("$[0].name").value(item1.getName()))
                .andExpect(jsonPath("$[0].description").value(item1.getDescription()))
                .andExpect(jsonPath("$[0].available").value(item1.getAvailable()))
                .andExpect(jsonPath("$[1].id").value(item2.getId()))
                .andExpect(jsonPath("$[1].name").value(item2.getName()))
                .andExpect(jsonPath("$[1].description").value(item2.getDescription()))
                .andExpect(jsonPath("$[1].available").value(item2.getAvailable()));
    }

    @Test
    public void testSearchItems() throws Exception {
        String searchText = "desc";
        Integer from = 0;
        Integer size = 10;

        ItemDto item1 = new ItemDto(1L, "Item 1", "Description 1", true);
        ItemDto item2 = new ItemDto(2L, "Item 2", "Description 2", false);
        List<ItemDto> expectedItems = Arrays.asList(item1, item2);

        when(itemService.getSearchItems(searchText, from, size)).thenReturn(expectedItems);
        mvc.perform(get("/items/search")
                        .param("text", searchText)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(item1.getId()))
                .andExpect(jsonPath("$[0].name").value(item1.getName()))
                .andExpect(jsonPath("$[0].description").value(item1.getDescription()))
                .andExpect(jsonPath("$[0].available").value(item1.getAvailable()))
                .andExpect(jsonPath("$[1].id").value(item2.getId()))
                .andExpect(jsonPath("$[1].name").value(item2.getName()))
                .andExpect(jsonPath("$[1].description").value(item2.getDescription()))
                .andExpect(jsonPath("$[1].available").value(item2.getAvailable()));
    }

    @Test
    public void testAddComment() throws Exception {
        long itemId = 1L;
        long userId = 2L;

        CommentDtoIn commentDtoIn = new CommentDtoIn();
        commentDtoIn.setText("This is a comment");

        CommentDto expectedCommentDto = new CommentDto();
        expectedCommentDto.setId(1L);
        expectedCommentDto.setText(commentDtoIn.getText());
        Comment comment = new Comment();
        comment.setText("This is a comment");
        comment.setCreated(LocalDateTime.now());


        when(itemService.addComment(anyLong(), anyLong(), any()))
                .thenReturn(expectedCommentDto);

        mvc.perform(post("/items/" + itemId + "/comment")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(commentDtoIn)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedCommentDto.getId()))
                .andExpect(jsonPath("$.text").value(expectedCommentDto.getText()));
    }

    @Test
    public void testUpdateItem() throws Exception {
        long userId = 1L;
        long itemId = 1L;

        ItemDtoIn updatedItemDtoIn = new ItemDtoIn();
        updatedItemDtoIn.setName("Updated Item");
        updatedItemDtoIn.setDescription("Updated Description");

        ItemDto updatedItemDto = new ItemDto();
        updatedItemDto.setId(itemId);
        updatedItemDto.setName(updatedItemDtoIn.getName());
        updatedItemDto.setDescription(updatedItemDtoIn.getDescription());
        Item item = new Item();
        item.setId(itemId);
        item.setName("Updated Item");
        item.setDescription("Updated Description");

        when(itemService.updateItem(anyLong(), anyLong(), any(Item.class)))
                .thenReturn(updatedItemDto);

        mvc.perform(patch("/items/{id}", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedItemDtoIn)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedItemDto.getId()))
                .andExpect(jsonPath("$.name").value(updatedItemDto.getName()))
                .andExpect(jsonPath("$.description").value(updatedItemDto.getDescription()));
    }

    @Test
    public void testDeleteItem() throws Exception {
        long itemId = 1L;

        mvc.perform(delete("/items/{id}", itemId))
                .andExpect(status().isOk());

        verify(itemService).deleteItem(itemId);
    }

}