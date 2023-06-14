package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.Booker;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;


import java.time.LocalDateTime;

public class BookingDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private long itemId;
    private Item item;
    private Booker booker;
    private BookingStatus status;


    public BookingDto(Long id, LocalDateTime start, LocalDateTime end, BookingStatus status, Booker id1, Item item) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.status = status;
        booker = id1;
        this.item = item;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public Booker getBooker() {
        return booker;
    }

    public void setBooker(Booker booker) {
        this.booker = booker;
    }

    public BookingStatus getStatus() {
        if (status == null) {
            return BookingStatus.WAITING;
        }
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}