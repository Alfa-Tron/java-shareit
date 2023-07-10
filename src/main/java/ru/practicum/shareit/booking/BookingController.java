package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.mapper.BookingMapper;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
@Slf4j
public class BookingController {
    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

    @PostMapping
    public BookingDto createBooking(@RequestHeader("X-Sharer-User-Id") long userId, @Valid @RequestBody BookingDtoIn bookingDto) {
        Booking b = bookingMapper.bookingDtoInToBooking(bookingDto);
        b.setStatus(BookingStatus.WAITING);
        log.info("createBooking userId: {},bookingDto: {}",userId,bookingDto);
        return bookingMapper.bookingToBookingDto(bookingService.createBooking(userId, bookingDto.getItemId(), b));
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBookingStatus(@PathVariable Long bookingId, @RequestParam boolean approved, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("updateBookingStatus bookingId: {},approved: {}, userId: {}",bookingId,approved,userId);
        return bookingMapper.bookingToBookingDto(bookingService.approvedBooking(bookingId, approved, userId));
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@PathVariable Long bookingId, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("getBooking bookingId: {}, userId: {}",bookingId,userId);
        return bookingMapper.bookingToBookingDto(bookingService.getBooking(bookingId, userId));
    }

    @GetMapping
    public List<BookingDto> getAllBookings(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam(defaultValue = "ALL") String state, @PositiveOrZero @RequestParam(value = "from", required = false) Integer from, @Positive @RequestParam(value = "size", required = false) Integer size) {
        log.info("getAllBookings userId: {}, state: {}, from: {}, size: {}",userId,state,from,size);
        return bookingMapper.bookingListToBookingDtoList(bookingService.getAllBookings(userId, state, from, size));
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingOwner(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam(defaultValue = "ALL") String state,@PositiveOrZero @RequestParam(value = "from", required = false) Integer from,@Positive @RequestParam(value = "size", required = false) Integer size) {
        log.info("getBookingOwner userId: {}, from: {}, size: {}",userId,from,size);
        return bookingMapper.bookingListToBookingDtoList(bookingService.getBookingOwner(userId, state, from, size));
    }

}
