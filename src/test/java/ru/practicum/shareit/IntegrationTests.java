package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.jpa.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.jpa.UserRepository;

import java.time.LocalDateTime;
import java.util.List;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class IntegrationTests {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemService itemService;

    @Test
    void testGetAllBookings() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setName("1234");
        userRepository.save(user);

        Booking booking1 = new Booking();
        booking1.setStart(LocalDateTime.now().plusDays(1));
        booking1.setEnd(LocalDateTime.now().plusDays(2));
        booking1.setStatus(BookingStatus.WAITING);
        booking1.setBooker(user);
        Item item = new Item();
        item.setName("123");
        item.setDescription("333");
        item.setOwner(user);
        item.setAvailable(true);
        itemRepository.save(item);
        booking1.setItem(item);
        bookingRepository.save(booking1);

        Booking booking2 = new Booking();
        booking2.setBooker(user);
        booking2.setStart(LocalDateTime.now().plusDays(3));
        booking2.setEnd(LocalDateTime.now().plusDays(4));
        booking2.setStatus(BookingStatus.REJECTED);
        bookingRepository.save(booking2);

        List<Booking> result = bookingService.getAllBookings(user.getId(), "ALL", 0, 10);

        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.contains(booking2));
        Assertions.assertTrue(result.contains(booking1));
    }
    @Test
    void testGetAllItems() {
        User user = new User();
        user.setName("4444");
        user.setEmail("jsdf.fds@example.com");
        userRepository.save(user);

        Item item1 = new Item();
        item1.setOwner(user);
        item1.setName("12");
        item1.setAvailable(true);
        item1.setId(1);
        item1.setDescription("232232");
        itemRepository.save(item1);
        User user2 = new User();
        user2.setName("44344");
        user2.setEmail("jsd3f.fds@example.com");
        userRepository.save(user2);
        Item item2 = new Item();
        item2.setOwner(user);
        item2.setName("12412");
        item2.setAvailable(true);
        item2.setId(2);
        item2.setDescription("232232");
        itemRepository.save(item2);

        List<ItemDto> result = itemService.getAllItems(user.getId(), null, null);

        Assertions.assertEquals(2, result.size());
    }

}