package ru.practicum.shareit.booking.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoIn;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookingMapperImpl implements BookingMapper {

    @Override
    public Booking bookingDtoInToBooking(BookingDtoIn bookingDto) {
        return new Booking(bookingDto.getStart(), bookingDto.getEnd());
    }

    @Override
    public BookingDto bookingToBookingDto(Booking booking) {
        return new BookingDto(booking.getId(), booking.getStart(), booking.getEnd(), booking.getStatus().toString(), booking.getBooker().getId(), booking.getItem().getId(), booking.getItem().getName());
    }

    @Override
    public List<BookingDto> bookingListToBookingDtoList(List<Booking> bookingList) {
        List<BookingDto> list = new ArrayList<>();
        for (Booking l : bookingList) {
            list.add(bookingToBookingDto(l));
        }
        return list;
    }
}
