package ru.practicum.shareit.booking.dto;

import jdk.jshell.Snippet;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingDto {
    private final Long id;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final ItemDtoBooking item;
    private final BookerDto booker;
    private final String status;


    public BookingDto(Long id, LocalDateTime start, LocalDateTime end, String status, long bookerId, long itemId, String itemName) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.status = status;
        booker = new BookerDto(bookerId);
        item = new ItemDtoBooking(itemId, itemName);

    }

    @Getter
    @Setter
    public static class BookerDto {
        private long id;

        public BookerDto(long id) {
            this.id = id;
        }
    }

    @Getter
    @Setter
    public static class ItemDtoBooking {
        private final long id;
        private final String name;

        public ItemDtoBooking(long id, String name) {
            this.id = id;
            this.name = name;
        }

    }

    public String getStatus() {
        if (status == null) {
            return BookingStatus.WAITING.name();
        }
        return status;
    }

}