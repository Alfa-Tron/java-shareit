package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.comment.CommentDto;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemDto {
    private long id;
    private String name;
    private String description;
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

    @Setter
    @Getter
    public static class BookingItemsDto {
        private Long id;
        private Long bookerId;

        public BookingItemsDto(Long id, Long bookerId) {
            this.id = id;
            this.bookerId = bookerId;
        }
    }
}