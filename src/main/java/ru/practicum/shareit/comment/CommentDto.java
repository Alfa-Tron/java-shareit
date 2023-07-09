package ru.practicum.shareit.comment;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDto {
    private Long id;
    private String text;

    private String authorName;

    private LocalDateTime created;

    public CommentDto(Long id, String text, String author, LocalDateTime now) {
        this.id = id;
        this.text = text;
        this.authorName = author;
        created = now;
    }

    public CommentDto() {

    }
}
