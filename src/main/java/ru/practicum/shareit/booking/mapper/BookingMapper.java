package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoIn;

import java.util.List;


@Mapper
public interface BookingMapper {
    Booking BookingDtoInToBooking(BookingDtoIn bookingDto);

    BookingDto BookingToBookingDto(Booking booking);

    List<BookingDto> BookingListToBookingDtoList(List<Booking> bookingList);
}
