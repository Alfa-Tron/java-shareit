package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;

public class BookingMapperImpl implements BookingMapper {
    @Override
    public Booking BookingDtoToBooking(BookingDto bookingDto) {
        return new Booking(bookingDto.getId(), bookingDto.getStart(), bookingDto.getEnd(), bookingDto.getItem(), bookingDto.getBooker(), bookingDto.getStatus());
    }

    @Override
    public BookingDto BookingToBookingDto(Booking booking) {
        return new BookingDto(booking.getId(), booking.getStart(), booking.getEnd(), booking.getItem(), booking.getBooker(), booking.getStatus());
    }
}
