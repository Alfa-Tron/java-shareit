package ru.practicum.shareit.booking.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booker;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemsDto;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookingMapperImpl implements BookingMapper {

    @Override
    public Booking BookingDtoToBooking(BookingDto bookingDto) {
        return new Booking(bookingDto.getStart(),bookingDto.getEnd(),bookingDto.getStatus());
    }

    @Override
    public BookingDto BookingToBookingDto(Booking booking) {
        return new BookingDto(booking.getId(), booking.getStart(),booking.getEnd(),booking.getStatus(),new Booker(booking.getBooker().getId()),booking.getItem());
    }

    @Override
    public List<BookingDto> BookingListToBookingDtoList(List<Booking> bookingList){
        List<BookingDto> list = new ArrayList<>();
        for(Booking l : bookingList){
            list.add(BookingToBookingDto(l));
        }
        return list;
    }

    @Override
    public BookingItemsDto BookingToBookingItemsDto(Booking booking) {
        return new BookingItemsDto(booking.getId(), booking.getStart(),booking.getEnd(),booking.getStatus(), booking.getBooker().getId(), booking.getItem());
    }
}
