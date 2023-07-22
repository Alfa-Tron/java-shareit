package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.comment.CommentDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class ItemDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;

    private BookingItemsDto lastBooking;
    private BookingItemsDto nextBooking;

    private List<CommentDto> comments = new ArrayList<>();
    private long requestId;

    public ItemDto(long id, String name, String description, Boolean available, long requestId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.requestId = requestId;
    }

    public ItemDto(long id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }

    public ItemDto(String s, String s1) {
        this.name = s;
        this.description = s1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemDto itemDto = (ItemDto) o;
        return id == itemDto.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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