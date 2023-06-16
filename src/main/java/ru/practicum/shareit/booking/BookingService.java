package ru.practicum.shareit.booking;

import java.util.List;

public interface BookingService {
    Booking createBooking(long userId, long itemId, Booking booking);

    Booking approvedBooking(Long bookingId, boolean approved, long userId);

    Booking getBooking(Long bookingId, long userId);

    List<Booking> getAllBookings(long userId, String state);

    List<Booking> getBookingOwner(long userId, String state);
}
