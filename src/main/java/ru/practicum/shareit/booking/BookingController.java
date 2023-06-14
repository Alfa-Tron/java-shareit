package ru.practicum.shareit.booking;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;

import java.util.List;


@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

    public BookingController(BookingService bookingService, BookingMapper bookingMapper) {
        this.bookingService = bookingService;
        this.bookingMapper = bookingMapper;
    }

    @PostMapping
    public BookingDto createBooking(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody BookingDto bookingDto) {
        Booking b = bookingMapper.BookingDtoToBooking(bookingDto);
        return bookingMapper.BookingToBookingDto(bookingService.createBooking(userId, bookingDto.getItemId(), b));
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBookingStatus(@PathVariable Long bookingId, @RequestParam boolean approved, @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingMapper.BookingToBookingDto(bookingService.approvedBooking(bookingId, approved, userId));
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@PathVariable Long bookingId, @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingMapper.BookingToBookingDto(bookingService.getBooking(bookingId, userId));
    }

    @GetMapping
    public List<BookingDto> getAllBookings(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam(required = false, defaultValue = "ALL") String state) {
        return bookingMapper.BookingListToBookingDtoList(bookingService.getAllBookings(userId, state));
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingOwner(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam(required = false, defaultValue = "ALL") String state) {
        return bookingMapper.BookingListToBookingDtoList(bookingService.getBookingOwner(userId, state));
    }

}
