package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;


@Mapper
public interface BookingMapper {
    Booking BookingDtoToBooking(BookingDto bookingDto);

    BookingDto BookingToBookingDto(Booking booking);
}
