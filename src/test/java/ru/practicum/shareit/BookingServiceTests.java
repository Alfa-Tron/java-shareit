package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.*;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.exeption.UnsupportedStatusException;
import ru.practicum.shareit.item.jpa.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.jpa.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@Transactional

public class BookingServiceTests {
    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateBooking() {
        long userId = 3L;
        long itemId = 1L;
        User user = new User();
        user.setId(userId);
        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);
        User user1 = new User();
        user1.setId(2L);
        item.setOwner(user1);
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusHours(1));


        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(bookingRepository.save(booking)).thenReturn(booking);

        Booking createdBooking = bookingService.createBooking(userId, itemId, booking);

        assertNotNull(createdBooking);
        assertEquals(user, createdBooking.getBooker());
        assertEquals(item, createdBooking.getItem());
        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    public void testCreateBooking_ItemNotAvailable() {
        long userId = 1L;
        long itemId = 1L;
        User user = new User();
        user.setId(userId);
        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(false);
        Booking booking = new Booking();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> bookingService.createBooking(userId, itemId, booking));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("item is not available", exception.getReason());
        verify(bookingRepository, never()).save(booking);
    }

    @Test
    public void testCreateBooking_ItemOwnerIsBooker() {
        long userId = 1L;
        long itemId = 1L;
        User user = new User();
        user.setId(userId);
        Item item = new Item();
        item.setId(itemId);
        item.setOwner(user);
        item.setAvailable(true);
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusSeconds(1));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.createBooking(userId, itemId, booking));

        assertEquals("item not found", exception.getMessage());
        verify(bookingRepository, never()).save(booking);
    }

    @Test
    public void testApprovedBooking_OwnerApprovesBooking() {
        long bookingId = 1L;
        long userId = 1L;
        Booking booking = new Booking();
        booking.setId(bookingId);
        User owner = new User();
        owner.setId(userId);
        Item item = new Item();
        item.setOwner(owner);
        booking.setItem(item);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);
        Booking updatedBooking = bookingService.approvedBooking(bookingId, true, userId);

        assertNotNull(updatedBooking);
        assertEquals(BookingStatus.APPROVED, updatedBooking.getStatus());
    }

    @Test
    public void testApprovedBooking_OwnerRejectsBooking() {
        long bookingId = 1L;
        long userId = 1L;
        Booking booking = new Booking();
        booking.setId(bookingId);
        User owner = new User();
        owner.setId(userId);
        Item item = new Item();
        item.setOwner(owner);
        booking.setItem(item);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        Booking updatedBooking = bookingService.approvedBooking(bookingId, false, userId);

        assertNotNull(updatedBooking);
        assertEquals(BookingStatus.REJECTED, updatedBooking.getStatus());
    }

    @Test
    public void testApprovedBooking_BookingNotFound() {
        long bookingId = 1L;
        long userId = 1L;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.approvedBooking(bookingId, true, userId));

        assertEquals("booking not found", exception.getMessage());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    public void testApprovedBooking_NotOwner() {
        long bookingId = 1L;
        long userId = 1L;
        long ownerUserId = 2L;
        Booking booking = new Booking();
        booking.setId(bookingId);
        User owner = new User();
        owner.setId(ownerUserId);
        Item item = new Item();
        item.setOwner(owner);
        booking.setItem(item);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.approvedBooking(bookingId, true, userId));

        assertEquals("not rights", exception.getMessage());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    public void testGetBooking_ValidBookingIdAndOwner() {
        long bookingId = 1L;
        long userId = 1L;
        Booking booking = new Booking();
        booking.setId(bookingId);
        User owner = new User();
        owner.setId(userId);
        Item item = new Item();
        item.setOwner(owner);
        booking.setItem(item);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        Booking result = bookingService.getBooking(bookingId, userId);

        assertNotNull(result);
        assertEquals(booking, result);
    }

    @Test
    public void testGetBooking_ValidBookingIdAndBooker() {
        long bookingId = 1L;
        long userId = 1L;
        Booking booking = new Booking();
        booking.setId(bookingId);
        User booker = new User();
        booker.setId(userId);
        booking.setBooker(booker);
        Item item = new Item();
        item.setOwner(new User());
        booking.setItem(item);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        Booking result = bookingService.getBooking(bookingId, userId);

        assertNotNull(result);
        assertEquals(booking, result);
    }

    @Test
    public void testGetBooking_BookingNotFound() {
        long bookingId = 1L;
        long userId = 1L;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.getBooking(bookingId, userId));

        assertEquals("booking not found", exception.getMessage());
    }

    @Test
    public void testGetBooking_NotOwnerOrBooker() {
        long bookingId = 1L;
        long userId = 1L;
        long otherUserId = 2L;
        Booking booking = new Booking();
        booking.setId(bookingId);
        User booker = new User();
        booker.setId(otherUserId);
        booking.setBooker(booker);
        Item item = new Item();
        item.setOwner(new User());
        booking.setItem(item);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.getBooking(bookingId, userId));

        assertEquals("no rights", exception.getMessage());
    }

    @Test
    public void testGetAllBookings_AllStatus() {
        long userId = 1L;
        String state = "ALL";
        Integer from = null;
        Integer size = null;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        List<Booking> expectedBookings = List.of(new Booking(), new Booking(), new Booking());
        when(bookingRepository.findAllByBooker_IdOrderByStartDesc(userId, PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(expectedBookings));

        List<Booking> result = bookingService.getAllBookings(userId, state, from, size);

        assertNotNull(result);
        assertEquals(3, result.size());
        verify(bookingRepository, times(1)).findAllByBooker_IdOrderByStartDesc(userId, PageRequest.of(0, 10));
    }

    @Test
    public void testGetAllBookings_FutureStatus() {
        long userId = 1L;
        String state = "FUTURE";
        Integer from = null;
        Integer size = null;
        User user = new User();
        user.setId(userId);
        LocalDateTime now = LocalDateTime.now().withNano(0);
        List<Booking> expectedBookings = List.of(new Booking(), new Booking(), new Booking());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findByBookerIdAndStartIsAfterOrderByStartDesc(eq(userId), any(LocalDateTime.class), eq(PageRequest.of(0, 10))))
                .thenReturn(new PageImpl<>(expectedBookings));

        List<Booking> result = bookingService.getAllBookings(userId, state, from, size);

        assertNotNull(result);
        assertEquals(3, result.size());
        verify(bookingRepository, times(1)).findByBookerIdAndStartIsAfterOrderByStartDesc(userId, now, PageRequest.of(0, 10));
    }

    @Test
    public void testGetAllBookings_WaitingStatus() {
        long userId = 1L;
        String state = "WAITING";
        Integer from = null;
        Integer size = null;
        User user = new User();
        user.setId(userId);
        List<Booking> expectedBookings = List.of(new Booking(), new Booking());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByStatusAndBookerId(BookingStatus.WAITING, userId, PageRequest.of(0, 10)))
                .thenReturn(new PageImpl<>(expectedBookings));

        List<Booking> result = bookingService.getAllBookings(userId, state, from, size);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(bookingRepository, times(1)).findAllByStatusAndBookerId(BookingStatus.WAITING, userId, PageRequest.of(0, 10));
    }

    @Test
    public void testGetAllBookings_InvalidState() {
        long userId = 1L;
        String state = "INVALID";
        Integer from = null;
        Integer size = null;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UnsupportedStatusException exception = assertThrows(UnsupportedStatusException.class,
                () -> bookingService.getAllBookings(userId, state, from, size));

        assertEquals("Unknown state: UNSUPPORTED_STATUS", exception.getMessage());
        verify(bookingRepository, never()).findAllByBooker_IdOrderByStartDesc(anyLong(), any(PageRequest.class));
    }

    @Test
    public void testGetBookingOwner_FutureStatus() {
        long userId = 1L;
        String state = "FUTURE";
        Integer from = null;
        Integer size = null;
        User user = new User();
        user.setId(userId);
        List<Booking> expectedBookings = List.of(new Booking(), new Booking(), new Booking());

        when(userRepository.existsById(userId)).thenReturn(true);
        when(bookingRepository.findAllByItem_Owner_IdAndStartAfterOrderByStartDesc(eq(userId), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(expectedBookings));

        List<Booking> result = bookingService.getBookingOwner(userId, state, from, size);

        Assertions.assertEquals(3, result.size());

    }



    @Test
    public void testGetBookingOwner_UserNotFound() {
        long userId = 1L;
        String state = "ALL";
        Integer from = null;
        Integer size = null;

        when(userRepository.existsById(userId)).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class, () -> {
            bookingService.getBookingOwner(userId, state, from, size);
        });
    }
    @Test
    public void testGetBookingOwner_WaitingStatus() {
        long userId = 1L;
        String state = "WAITING";
        Integer from = null;
        Integer size = null;
        User user = new User();
        user.setId(userId);
        List<Booking> expectedBookings = List.of(new Booking(), new Booking());

        when(userRepository.existsById(userId)).thenReturn(true);
        when(bookingRepository.findAllByStatusAndItem_Owner_Id(eq(BookingStatus.WAITING), eq(userId), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(expectedBookings));

        List<Booking> result = bookingService.getBookingOwner(userId, state, from, size);

        Assertions.assertEquals(2, result.size());
        // Add more assertions as needed
    }

    @Test
    public void testGetBookingOwner_RejectedStatus() {
        long userId = 1L;
        String state = "REJECTED";
        Integer from = null;
        Integer size = null;
        User user = new User();
        user.setId(userId);
        List<Booking> expectedBookings = List.of(new Booking(), new Booking(), new Booking());

        when(userRepository.existsById(userId)).thenReturn(true);
        when(bookingRepository.findAllByStatusAndItem_Owner_Id(eq(BookingStatus.REJECTED), eq(userId), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(expectedBookings));

        List<Booking> result = bookingService.getBookingOwner(userId, state, from, size);

        Assertions.assertEquals(3, result.size());
    }

    @Test
    public void testGetBookingOwner_CurrentStatus() {
        long userId = 1L;
        String state = "CURRENT";
        Integer from = null;
        Integer size = null;
        User user = new User();
        user.setId(userId);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> expectedBookings = List.of(new Booking(), new Booking());

        when(userRepository.existsById(userId)).thenReturn(true);
        when(bookingRepository.findAllByItem_Owner_IdAndStartBeforeAndEndAfter(eq(userId), any(LocalDateTime.class), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(expectedBookings));

        List<Booking> result = bookingService.getBookingOwner(userId, state, from, size);

        Assertions.assertEquals(2, result.size());
    }

    @Test
    public void testGetBookingOwner_PastStatus() {
        long userId = 1L;
        String state = "PAST";
        Integer from = null;
        Integer size = null;
        User user = new User();
        user.setId(userId);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> expectedBookings = List.of(new Booking());

        when(userRepository.existsById(userId)).thenReturn(true);
        when(bookingRepository.findAllByItem_Owner_IdAndEndBeforeOrderByStartDesc(eq(userId), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(expectedBookings));

        List<Booking> result = bookingService.getBookingOwner(userId, state, from, size);

        Assertions.assertEquals(1, result.size());
    }
    @Test
    public void testGetBookingOwner_AllStatus() {
        long userId = 1L;
        String state = "ALL";
        Integer from = null;
        Integer size = null;
        User user = new User();
        user.setId(userId);
        List<Booking> expectedBookings = List.of(new Booking(), new Booking(), new Booking());

        when(userRepository.existsById(userId)).thenReturn(true);
        when(bookingRepository.findAllByItem_Owner_Id_OrderByStartDesc(eq(userId), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(expectedBookings));

        List<Booking> result = bookingService.getBookingOwner(userId, state, from, size);

        Assertions.assertEquals(3, result.size());
    }
    @Test
    public void testGetAllBookings_RejectedStatus() {
        long userId = 1L;
        String state = "REJECTED";
        Integer from = null;
        Integer size = null;
        User user = new User();
        user.setId(userId);
        List<Booking> expectedBookings = List.of(new Booking(), new Booking(), new Booking());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByStatusAndBookerId(eq(BookingStatus.REJECTED), eq(userId), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(expectedBookings));

        List<Booking> result = bookingService.getAllBookings(userId, state, from, size);

        Assertions.assertEquals(3, result.size());
    }

    @Test
    public void testGetAllBookings_PastStatus() {
        long userId = 1L;
        String state = "PAST";
        Integer from = null;
        Integer size = null;
        User user = new User();
        user.setId(userId);
        List<Booking> expectedBookings = List.of(new Booking(), new Booking(), new Booking());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(eq(userId), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(expectedBookings));

        List<Booking> result = bookingService.getAllBookings(userId, state, from, size);

        Assertions.assertEquals(3, result.size());
    }
    @Test
    public void testGetAllBookings_CurrentStatus() {
        long userId = 1L;
        String state = "CURRENT";
        Integer from = null;
        Integer size = null;
        User user = new User();
        user.setId(userId);
        List<Booking> expectedBookings = List.of(new Booking(), new Booking(), new Booking());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(eq(userId), any(LocalDateTime.class), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(expectedBookings));

        List<Booking> result = bookingService.getAllBookings(userId, state, from, size);

        Assertions.assertEquals(3, result.size());

    }}

