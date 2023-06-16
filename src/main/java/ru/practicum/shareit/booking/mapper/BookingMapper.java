package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoIn;

import java.util.List;


@Mapper
public interface BookingMapper {
    Booking bookingDtoInToBooking(BookingDtoIn bookingDto);

    BookingDto bookingToBookingDto(Booking booking);

    List<BookingDto> bookingListToBookingDtoList(List<Booking> bookingList);
}
