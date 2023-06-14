package ru.practicum.shareit.comment;

import ru.practicum.shareit.item.Marker;
import ru.practicum.shareit.item.model.Item;

import javax.validation.constraints.NotEmpty;

public class CommentDto {
    private Long id;
    @NotEmpty(groups = Marker.OnCreate.class)
    private String text;

    private Item item;

    private String authorName;

    private String created;

    public CommentDto(Long id, String text, Item item, String author, String now) {
        this.id = id;
        this.text = text;
        this.item = item;
        this.authorName = author;
        created = now;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
