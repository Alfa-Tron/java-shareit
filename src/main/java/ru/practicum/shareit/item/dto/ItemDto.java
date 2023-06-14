package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.dto.BookingItemsDto;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.item.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class ItemDto {
    private long id;
    @NotBlank(groups = Marker.OnCreate.class)
    private String name;
    @NotEmpty(groups = Marker.OnCreate.class)
    private String description;
    @NotNull(groups = Marker.OnCreate.class)
    private Boolean available;

    private BookingItemsDto lastBooking;
    private BookingItemsDto nextBooking;

    private List<CommentDto> comments = new ArrayList<>();

    public ItemDto(long id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }

    public ItemDto() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public BookingItemsDto getLastBooking() {
        return lastBooking;
    }

    public void setLastBooking(BookingItemsDto lastBooking) {
        this.lastBooking = lastBooking;
    }

    public BookingItemsDto getNextBooking() {
        return nextBooking;
    }

    public void setNextBooking(BookingItemsDto nextBooking) {
        this.nextBooking = nextBooking;
    }

    public List<CommentDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }
}