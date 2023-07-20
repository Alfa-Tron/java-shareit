package shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import shareit.booking.dto.BookingDtoIn;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);

        return bookingClient.getBookings(userId, stateParam, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> bookItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestBody @Valid BookingDtoIn requestDto) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        LocalDateTime start = requestDto.getStart();
        LocalDateTime end = requestDto.getEnd();
        if (start.isAfter(end)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "startTime < EndTime");
        if (start.isEqual(end)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "startTime = EndTime");

        return bookingClient.bookItem(userId, requestDto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> update(@PathVariable Long bookingId, @RequestParam boolean approved, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("updateBookingStatus bookingId: {},approved: {}, userId: {}", bookingId, approved, userId);
        return bookingClient.updateBooking(bookingId, approved, userId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingOwner(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam(defaultValue = "ALL") String state, @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                  @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("getBookingOwner userId: {}, from: {}, size: {}", userId, from, size);
        return bookingClient.getBookingOwner(userId, state, from, size);
    }

}
