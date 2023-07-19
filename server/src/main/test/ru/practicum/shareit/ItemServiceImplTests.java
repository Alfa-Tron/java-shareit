package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.*;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.jpa.ItemRepository;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.jpa.UserRepository;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.when;
@Transactional

public class ItemServiceImplTests {

    @Mock
    private ItemRepository repositoryItem;

    @Mock
    private UserRepository repositoryUser;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ItemMapper itemMapper;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private ItemServiceImpl itemService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateItem() {
        long userId = 1L;
        Item item = createMockItem();
        User user = new User();
        user.setId(userId);
        Item savedItem = createMockItem();
        ItemDto expectedItemDto = createMockItemDto();

        when(repositoryUser.findById(userId)).thenReturn(Optional.of(user));
        when(repositoryItem.save(item)).thenReturn(savedItem);
        when(itemMapper.itemToItemDto(savedItem)).thenReturn(expectedItemDto);

        ItemDto result = itemService.createItem(userId, item);

        Assertions.assertEquals(expectedItemDto, result);
        verify(repositoryUser, times(1)).findById(userId);
        verify(repositoryItem, times(1)).save(item);
        verify(itemMapper, times(1)).itemToItemDto(savedItem);
    }

    @Test
    public void testGetItemById_ItemNotFound() {
        long itemId = 1L;
        long userId = 1L;

        when(repositoryItem.findById(itemId)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> itemService.getItemById(itemId, userId));

        verify(repositoryItem, times(1)).findById(itemId);
    }

    @Test
    public void testGetItemById_WithLastAndNextBookings() {
        long itemId = 1L;
        long userId = 1L;
        Item item = createMockItem();
        ItemDto expectedItemDto = createMockItemDto();
        List<Comment> comments = new ArrayList<>();
        Booking lastBooking = createMockBooking();
        Booking nextBooking = createMockBooking();
        LocalDateTime anyDateTime = Mockito.any(LocalDateTime.class);

        when(commentRepository.findByItemOrderByCreatedDesc(item)).thenReturn(comments);
        when(bookingRepository.findFirstByItemAndItem_Owner_IdAndStartLessThanEqualOrderByStartDesc(item, userId, anyDateTime)).thenReturn(Optional.of(lastBooking));
        when(bookingRepository.findFirstByItemAndItem_Owner_IdAndStartAfterOrderByStartAsc(item, userId, anyDateTime)).thenReturn(Optional.of(nextBooking));
        when(itemMapper.itemToItemDto(item)).thenReturn(expectedItemDto);
        when(repositoryItem.findById(itemId)).thenReturn(Optional.of(item));


        ItemDto result = itemService.getItemById(itemId, userId);

        Assertions.assertEquals(expectedItemDto, result);
        verify(repositoryItem, times(1)).findById(itemId);
        verify(commentRepository, times(1)).findByItemOrderByCreatedDesc(item);
    }

    @Test
    public void testGetItemById_WithoutLastBooking() {
        long itemId = 1L;
        long userId = 1L;
        Item item = createMockItem();
        ItemDto expectedItemDto = createMockItemDto();
        List<Comment> comments = new ArrayList<>();
        Booking nextBooking = createMockBooking();

        when(repositoryItem.findById(itemId)).thenReturn(Optional.of(item));
        when(commentRepository.findByItemOrderByCreatedDesc(item)).thenReturn(comments);
        when(bookingRepository.findFirstByItemAndItem_Owner_IdAndStartLessThanEqualOrderByStartDesc(item, userId, LocalDateTime.now())).thenReturn(Optional.empty());
        when(bookingRepository.findFirstByItemAndItem_Owner_IdAndStartAfterOrderByStartAsc(item, userId, LocalDateTime.now())).thenReturn(Optional.of(nextBooking));
        when(itemMapper.itemToItemDto(item)).thenReturn(expectedItemDto);

        ItemDto result = itemService.getItemById(itemId, userId);

        Assertions.assertEquals(expectedItemDto, result);
        verify(repositoryItem, times(1)).findById(itemId);
        verify(commentRepository, times(1)).findByItemOrderByCreatedDesc(item);
    }

    @Test
    public void testGetItemById_WithoutNextBooking() {
        long itemId = 1L;
        long userId = 1L;
        Item item = createMockItem();
        ItemDto expectedItemDto = createMockItemDto();
        List<Comment> comments = new ArrayList<>();
        Booking lastBooking = createMockBooking();

        when(repositoryItem.findById(itemId)).thenReturn(Optional.of(item));
        when(commentRepository.findByItemOrderByCreatedDesc(item)).thenReturn(comments);
        when(bookingRepository.findFirstByItemAndItem_Owner_IdAndStartLessThanEqualOrderByStartDesc(item, userId, LocalDateTime.now())).thenReturn(Optional.of(lastBooking));
        when(bookingRepository.findFirstByItemAndItem_Owner_IdAndStartAfterOrderByStartAsc(item, userId, LocalDateTime.now())).thenReturn(Optional.empty());
        when(itemMapper.itemToItemDto(item)).thenReturn(expectedItemDto);

        ItemDto result = itemService.getItemById(itemId, userId);

        Assertions.assertEquals(expectedItemDto, result);
        verify(repositoryItem, times(1)).findById(itemId);
        verify(commentRepository, times(1)).findByItemOrderByCreatedDesc(item);

    }

    @Test
    public void testGetItemById_WithoutBookings() {
        long itemId = 1L;
        long userId = 1L;
        Item item = createMockItem();
        ItemDto expectedItemDto = createMockItemDto();
        List<Comment> comments = new ArrayList<>();

        when(repositoryItem.findById(itemId)).thenReturn(Optional.of(item));
        when(commentRepository.findByItemOrderByCreatedDesc(item)).thenReturn(comments);
        when(bookingRepository.findFirstByItemAndItem_Owner_IdAndStartLessThanEqualOrderByStartDesc(item, userId, LocalDateTime.now())).thenReturn(Optional.empty());
        when(bookingRepository.findFirstByItemAndItem_Owner_IdAndStartAfterOrderByStartAsc(item, userId, LocalDateTime.now())).thenReturn(Optional.empty());
        when(itemMapper.itemToItemDto(item)).thenReturn(expectedItemDto);

        ItemDto result = itemService.getItemById(itemId, userId);

        Assertions.assertEquals(expectedItemDto, result);
        verify(repositoryItem, times(1)).findById(itemId);
        verify(commentRepository, times(1)).findByItemOrderByCreatedDesc(item);
    }

    @Test
    public void testGetSearchItems_WithBlankSearchText_ReturnsEmptyList() {
        String searchText = "";
        Integer from = 0;
        Integer size = 10;

        List<ItemDto> result = itemService.getSearchItems(searchText, from, size);

        Assertions.assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testUpdateItem_WithValidParameters_ReturnsUpdatedItemDto() {
        long userId = 1L;
        long id = 1L;
        Item updatedItem = new Item();
        updatedItem.setId(id);
        updatedItem.setName("Updated Item");
        updatedItem.setDescription("Updated Description");
        updatedItem.setAvailable(true);

        Item existingItem = new Item();
        existingItem.setId(id);
        existingItem.setName("Original Item");
        existingItem.setDescription("Original Description");
        existingItem.setAvailable(false);
        existingItem.setOwner(new User(userId));

        User existingUser = new User(userId);

        when(repositoryItem.findById(id)).thenReturn(java.util.Optional.of(existingItem));
        when(repositoryUser.findById(userId)).thenReturn(java.util.Optional.of(existingUser));

        Item updatedItemEntity = new Item();
        updatedItemEntity.setId(id);
        updatedItemEntity.setName("Updated Item");
        updatedItemEntity.setDescription("Updated Description");
        updatedItemEntity.setAvailable(true);
        updatedItemEntity.setOwner(existingUser);

        when(repositoryItem.save(updatedItemEntity)).thenReturn(updatedItemEntity);

        ItemDto expectedDto = new ItemDto();
        expectedDto.setId(id);
        expectedDto.setName("Updated Item");
        expectedDto.setDescription("Updated Description");
        expectedDto.setAvailable(true);

        when(itemMapper.itemToItemDto(updatedItemEntity)).thenReturn(expectedDto);
        when(repositoryItem.findById(updatedItemEntity.getId())).thenReturn(Optional.of(updatedItemEntity));
        ItemDto result = itemService.updateItem(userId, id, updatedItem);

        Assertions.assertEquals(expectedDto, result);
    }
    @Test
    public void testDeleteItem_WithValidId_CallsRepositoryDeleteById() {
        long id = 1L;

        itemService.deleteItem(id);

        verify(repositoryItem).deleteById(id);
    }
    @Test
    public void testAddComment_WithValidData_CommentAddedSuccessfully() {
        Long itemId = 1L;
        long userId = 1L;
        Comment text = new Comment();
        text.setText("123");
        Item item = new Item();
        User user = new User();
        CommentDto expectedDto = new CommentDto();

        when(repositoryItem.findById(itemId)).thenReturn(Optional.of(item));
        when(repositoryUser.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.existsByBooker_IdAndItem_IdAndStatusAndStartBefore(
                eq(userId), eq(itemId), eq(BookingStatus.APPROVED), any(LocalDateTime.class))).thenReturn(true);
        when(commentRepository.save(text)).thenReturn(text);
        when(commentMapper.commentToDto(text)).thenReturn(expectedDto);

        CommentDto resultDto = itemService.addComment(itemId, userId, text);

        Assertions.assertNotNull(resultDto);
        Assertions.assertEquals(expectedDto, resultDto);
        Assertions.assertEquals(item, text.getItem());
        Assertions.assertEquals(user, text.getAuthorName());
        Assertions.assertNotNull(text.getCreated());
        verify(commentRepository).save(text);
    }

    @Test
    public void testAddComment_WithInvalidUser_ThrowsNotFoundException() {
        Long itemId = 1L;
        long userId = 1L;
        Comment text = new Comment();

        when(repositoryUser.findById(userId)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> itemService.addComment(itemId, userId, text));
    }

    @Test
    public void testAddComment_WithInvalidItem_ThrowsNotFoundException() {
        Long itemId = 1L;
        long userId = 1L;
        Comment text = new Comment();
        User user = new User();

        when(repositoryUser.findById(userId)).thenReturn(Optional.of(user));
        when(repositoryItem.findById(itemId)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> itemService.addComment(itemId, userId, text));
    }

    @Test
    public void testAddComment_WithUserWithoutBooking_ThrowsResponseStatusException() {
        Long itemId = 1L;
        long userId = 1L;
        Comment text = new Comment();
        Item item = new Item();
        User user = new User();

        when(repositoryItem.findById(itemId)).thenReturn(Optional.of(item));
        when(repositoryUser.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.existsByBooker_IdAndItem_IdAndStatusAndStartBefore(userId, itemId, BookingStatus.APPROVED, LocalDateTime.now())).thenReturn(false);

        Assertions.assertThrows(ResponseStatusException.class, () -> itemService.addComment(itemId, userId, text));
        verify(commentRepository, never()).save(text);
    }

    private Item createMockItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Item 1");
        item.setDescription("Description 1");
        User user = new User();
        user.setId(2L);
        item.setOwner(user);
        return item;
    }

    private ItemDto createMockItemDto() {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Item 1");
        itemDto.setDescription("Description 1");
        User user = new User();
        user.setId(2L);
        itemDto.setRequestId(3L);
        return itemDto;
    }

    private Booking createMockBooking() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(LocalDateTime.now().withNano(0));
        User user = new User();
        user.setId(2L);
        booking.setBooker(user);
        return booking;
    }
}
