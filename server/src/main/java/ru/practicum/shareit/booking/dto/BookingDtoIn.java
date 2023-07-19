package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Getter
@Setter
public class BookingDtoIn {

    private final Long itemId;

    private final LocalDateTime start;

    private final LocalDateTime end;

    public BookingDtoIn(Long itemId, LocalDateTime start, LocalDateTime end) {
        this.itemId = itemId;
        this.start = start;
        this.end = end;
    }
}
