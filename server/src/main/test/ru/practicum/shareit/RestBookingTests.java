package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;

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
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.item.ItemService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

public class RestBookingTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ItemService itemService;
    @MockBean
    private BookingService bookingService;
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
    public void testCreateBooking() throws Exception {
        long userId = 1L;
        User user1 = new User();
        user1.setId(userId);
        user1.setName("John");
        user1.setEmail("john@example.com");

        BookingDtoIn bookingDtoIn = new BookingDtoIn(0L, LocalDateTime.now().plusSeconds(123), LocalDateTime.now().plusSeconds(144));

        BookingDto expectedBookingDto = new BookingDto(0L, LocalDateTime.now(), LocalDateTime.now().plusSeconds(1), BookingStatus.WAITING.toString(), 1L, 1L, "123");

        Booking booking = new Booking();
        booking.setStatus(BookingStatus.WAITING);
        booking.setBooker(user1);
        Item item = new Item();
        item.setId(1L);
        booking.setItem(item);

        when(bookingService.createBooking(anyLong(), anyLong(), any(Booking.class)))
                .thenReturn(booking);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bookingDtoIn)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedBookingDto.getId()))
                .andExpect(jsonPath("$.status").value(expectedBookingDto.getStatus().toString()));

    }

    @Test
    public void testUpdateBookingStatus() throws Exception {
        long bookingId = 1L;
        boolean approved = true;
        long userId = 1L;

        BookingDto expectedBookingDto = new BookingDto(0L, LocalDateTime.now(), LocalDateTime.now().plusSeconds(1), BookingStatus.WAITING.toString(), 1L, 1L, "123");
        User user1 = new User();
        user1.setId(userId);
        user1.setName("John");
        user1.setEmail("john@example.com");
        Booking booking = new Booking();
        booking.setStatus(BookingStatus.WAITING);
        booking.setBooker(user1);
        Item item = new Item();
        item.setId(1L);
        booking.setItem(item);


        when(bookingService.approvedBooking(anyLong(), anyBoolean(), anyLong()))
                .thenReturn(booking);

        mvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .param("approved", String.valueOf(approved))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedBookingDto.getId()));

        verify(bookingService).approvedBooking(eq(bookingId), eq(approved), eq(userId));
    }

    @Test
    public void testGetBooking() throws Exception {
        long bookingId = 1L;
        long userId = 1L;

        BookingDto expectedBookingDto = new BookingDto(0L, LocalDateTime.now(), LocalDateTime.now().plusSeconds(1), BookingStatus.WAITING.toString(), 1L, 1L, "123");
        User user1 = new User();
        user1.setId(userId);
        user1.setName("John");
        user1.setEmail("john@example.com");
        Booking booking = new Booking();
        booking.setStatus(BookingStatus.WAITING);
        booking.setBooker(user1);
        Item item = new Item();
        item.setId(1L);
        booking.setItem(item);

        when(bookingService.getBooking(anyLong(), anyLong()))
                .thenReturn(booking);

        mvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedBookingDto.getId()));

        verify(bookingService).getBooking(eq(bookingId), eq(userId));
    }

    @Test
    public void testGetAllBookings() throws Exception {
        long userId = 1L;
        String state = "ALL";
        Integer from = 0;
        Integer size = 10;

        List<BookingDto> expectedBookingDtoList = new ArrayList<>();
        BookingDto bookingDto1 = new BookingDto(0L, LocalDateTime.now(), LocalDateTime.now().plusSeconds(1), BookingStatus.WAITING.toString(), 1L, 1L, "123");
        User user1 = new User();
        user1.setId(userId);
        user1.setName("John");
        user1.setEmail("john@example.com");
        Booking booking1 = new Booking();
        booking1.setStatus(BookingStatus.WAITING);
        booking1.setBooker(user1);
        Item item = new Item();
        item.setId(1L);
        booking1.setItem(item);
        BookingDto bookingDto2 = new BookingDto(0L, LocalDateTime.now(), LocalDateTime.now().plusSeconds(1), BookingStatus.WAITING.toString(), 1L, 1L, "123");
        User user2 = new User();
        user1.setId(userId);
        user1.setName("John");
        user1.setEmail("john@example.com");
        Booking booking2 = new Booking();
        booking2.setStatus(BookingStatus.WAITING);
        booking2.setBooker(user2);
        Item item2 = new Item();
        item.setId(1L);
        booking2.setItem(item2);
        expectedBookingDtoList.add(bookingDto1);
        expectedBookingDtoList.add(bookingDto2);

        List<Booking> bookingList = new ArrayList<>();
        bookingList.add(booking1);
        bookingList.add(booking2);

        when(bookingService.getAllBookings(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(bookingList);

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", state)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(expectedBookingDtoList.get(0).getId()))
                .andExpect(jsonPath("$[1].id").value(expectedBookingDtoList.get(1).getId()));

        verify(bookingService).getAllBookings(eq(userId), eq(state), eq(from), eq(size));
    }

    @Test
    public void testGetBookingOwner() throws Exception {
        long userId = 1L;
        String state = "ALL";
        Integer from = 0;
        Integer size = 10;

        List<BookingDto> expectedBookingDtoList = new ArrayList<>();
        BookingDto bookingDto1 = new BookingDto(0L, LocalDateTime.now(), LocalDateTime.now().plusSeconds(1), BookingStatus.WAITING.toString(), 1L, 1L, "123");
        BookingDto expectedBookingDto = new BookingDto(2L, LocalDateTime.now(), LocalDateTime.now().plusSeconds(1), BookingStatus.WAITING.toString(), 1L, 1L, "123");
        expectedBookingDtoList.add(bookingDto1);
        expectedBookingDtoList.add(expectedBookingDto);

        List<Booking> bookingList = new ArrayList<>();
        Item item = new Item();
        item.setId(1L);
        Booking booking1 = new Booking();
        booking1.setId(0);
        booking1.setItem(item);
        Booking booking2 = new Booking();
        booking2.setItem(item);
        booking2.setId(2L);
        User user1 = new User();
        user1.setId(userId);
        user1.setName("John");
        user1.setEmail("john@example.com");
        booking1.setBooker(user1);
        booking2.setBooker(user1);
        bookingList.add(booking1);
        bookingList.add(booking2);

        when(bookingService.getBookingOwner(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(bookingList);

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", state)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(expectedBookingDtoList.get(0).getId()))
                .andExpect(jsonPath("$[1].id").value(expectedBookingDtoList.get(1).getId()));

        verify(bookingService).getBookingOwner(eq(userId), eq(state), eq(from), eq(size));
    }

}
