package ru.practicum.shareit.comment;

public class CommentDto {
    private Long id;
    private String text;

    private String authorName;

    private String created;

    public CommentDto(Long id, String text, String author, String now) {
        this.id = id;
        this.text = text;
        this.authorName = author;
        created = now;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
