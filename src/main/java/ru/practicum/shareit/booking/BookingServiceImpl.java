package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.exeption.UnsupportedStatusException;
import ru.practicum.shareit.item.jpa.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.jpa.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;


    @Override
    @Transactional
    public Booking createBooking(long userId, long itemId, Booking booking) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("item not found"));
        ;
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user not found"));
        ;


        if (!item.isAvailable())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "item is not available");
        booking.setBooker(user);
        booking.setItem(item);
        LocalDateTime start = booking.getStart();
        LocalDateTime end = booking.getEnd();
        if (start.isAfter(end)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "startTime < EndTime");
        if (start.isEqual(end)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "startTime = EndTime");

        if (item.getOwner().getId() == user.getId()) {
            throw new NotFoundException("item not found");
        }
        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public Booking approvedBooking(Long bookingId, boolean approved, long userId) {
        Booking bookingDb = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("booking not found"));


        if (bookingDb.getItem().getOwner().getId() == userId) {
            if (bookingDb.getStatus().name().equals("APPROVED"))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Approved item");
            if (approved) {
                bookingDb.setStatus(BookingStatus.APPROVED);
            } else {
                bookingDb.setStatus(BookingStatus.REJECTED);
            }
            return bookingDb;
        } else throw new NotFoundException("not rights");

    }

    @Override
    public Booking getBooking(Long bookingId, long userId) {

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("booking not found"));
        if ((booking.getItem().getOwner().getId() == userId || booking.getBooker().getId() == userId)) {
            return booking;
        } else throw new NotFoundException("no rights");

    }


    private State checkStatus(String state) {
        State[] states = State.values();
        List<String> validStatuses = Arrays.stream(states)
                .map(Enum::name)
                .map(String::toUpperCase)
                .collect(Collectors.toList());

        boolean isValidStatus = validStatuses.stream()
                .anyMatch(s -> s.equals(state.toUpperCase()));

        if (!isValidStatus) {
            throw new UnsupportedStatusException();
        }
        return State.valueOf(state);
    }

    @Override
    public List<Booking> getAllBookings(long userId, String state) {
        State status = checkStatus(state);
        if (userRepository.findById(userId).isEmpty()) throw new NotFoundException("User not found");
        List<Booking> bookings = List.of();
        LocalDateTime now = LocalDateTime.now();

        switch (status) {
            case ALL:
                bookings = bookingRepository.findAllByBooker_IdOrderByStartDesc(userId);
                break;
            case FUTURE:
                bookings = bookingRepository.findByBookerIdAndStartIsAfterOrderByStartDesc(userId, now);
                break;
            case WAITING:
                bookings = bookingRepository.findAllByStatusAndBookerId(BookingStatus.WAITING, userId);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByStatusAndBookerId(BookingStatus.REJECTED, userId);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(userId, now, now);
                break;
            case PAST:
                bookings = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, now);
                break;
        }

        return bookings;
    }

    @Override
    public List<Booking> getBookingOwner(long userId, String state) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found");
        }
        State status = checkStatus(state);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = new ArrayList<>();
        switch (status) {
            case ALL:
                bookings = bookingRepository.findAllByItem_Owner_Id_OrderByStartDesc(userId);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByItem_Owner_IdAndStartAfterOrderByStartDesc(userId, now);
                break;
            case WAITING:
                bookings = bookingRepository.findAllByStatusAndItem_Owner_Id(BookingStatus.WAITING, userId);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByStatusAndItem_Owner_Id(BookingStatus.REJECTED, userId);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByItem_Owner_IdAndStartBeforeAndEndAfter(userId, now, now);
                break;
            case PAST:
                bookings = bookingRepository.findAllByItem_Owner_IdAndEndBeforeOrderByStartDesc(userId, now);
                break;
        }

        return bookings;

    }
}
