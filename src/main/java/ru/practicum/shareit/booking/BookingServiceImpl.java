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
        Optional<Item> item = itemRepository.findById(itemId);
        Optional<User> user = userRepository.findById(userId);


        if (item.isEmpty()) throw new NotFoundException("item not found");
        if (!item.get().isAvailable())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "item is not available");
        if (user.isEmpty()) throw new NotFoundException("user not found");
        booking.setBooker(user.get());
        booking.setItem(item.get());
        LocalDateTime start = booking.getStart();
        LocalDateTime end = booking.getEnd();
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(start)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "start time is after");
        if (now.isAfter(end)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "end time is after");
        if (start.isAfter(end)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "startTime < EndTime");
        if (start.isEqual(end)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "startTime = EndTime");

        if (item.get().getOwner().getId() == user.get().getId()) {
            throw new NotFoundException("item not found");
        }
        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public Booking approvedBooking(Long bookingId, boolean approved, long userId) {
        Booking bookingDb;
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isPresent()) {
            bookingDb = booking.get();
            if (bookingDb.getItem().getOwner().getId() == userId) {
                if (booking.get().getStatus().name().equals("APPROVED"))
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Approved item");
                if (approved) {
                    bookingDb.setStatus(BookingStatus.APPROVED);
                    bookingRepository.save(bookingDb);
                } else {
                    bookingDb.setStatus(BookingStatus.REJECTED);
                    bookingRepository.save(bookingDb);
                }
                return bookingDb;
            }
        }
        throw new NotFoundException("booking not found");
    }

    @Override
    public Booking getBooking(Long bookingId, long userId) {

        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isEmpty()) throw new NotFoundException("not found booking");
        if ((booking.get().getItem().getOwner().getId() == userId || booking.get().getBooker().getId() == userId)) {
            return booking.get();
        } else throw new NotFoundException("no rights");

    }


    private void checkStatus(String state) {
        State[] States = State.values();
        List<String> validStatuses = Arrays.stream(States)
                .map(State::name)
                .collect(Collectors.toList());

        if (!validStatuses.contains(state)) {
            throw new UnsupportedStatusException();
        }
    }

    @Override
    public List<Booking> getAllBookings(long userId, String state) {
        checkStatus(state);
        if (userRepository.findById(userId).isEmpty()) throw new NotFoundException("User not found");
        List<Booking> bookings = null;
        State status = State.valueOf(state);

        switch (status) {
            case ALL:
                bookings = bookingRepository.findAllByBooker_IdOrderByStartDesc(userId);
                break;
            case FUTURE:
                bookings = bookingRepository.findByBookerIdAndStartIsAfterOrderByStartDesc(userId, LocalDateTime.now());
                break;
            case WAITING:
                bookings = bookingRepository.findAllByStatusAndBookerId(BookingStatus.WAITING, userId);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByStatusAndBookerId(BookingStatus.REJECTED, userId);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(userId, LocalDateTime.now(), LocalDateTime.now());
                break;
            case PAST:
                bookings = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now());
                break;
        }

        return bookings;
    }

    @Override
    public List<Booking> getBookingOwner(long userId, String state) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        checkStatus(state);
        State status = State.valueOf(state);
        List<Booking> bookings = new ArrayList<>();
        switch (status) {
            case ALL:
                bookings.addAll(bookingRepository.findAllByItem_Owner_Id_OrderByStartDesc(userId));
                break;
            case FUTURE:
                bookings.addAll(bookingRepository.findAllByItem_Owner_IdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now()));
                break;
            case WAITING:
                bookings = bookingRepository.findAllByStatusAndItem_Owner_Id(BookingStatus.WAITING, userId);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByStatusAndItem_Owner_Id(BookingStatus.REJECTED, userId);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByItem_Owner_IdAndStartBeforeAndEndAfter(userId, LocalDateTime.now(), LocalDateTime.now());
                break;
            case PAST:
                bookings = bookingRepository.findAllByItem_Owner_IdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now());
                break;
        }

        return bookings;

    }
}
