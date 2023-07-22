package ru.practicum.shareit.comment;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommentMapperImpl implements CommentMapper {
    @Override
    public CommentDto commentToDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(), comment.getAuthorName().getName(), comment.getCreated());
    }

    @Override
    public Comment commentDtoInToComment(CommentDtoIn comment) {
        return new Comment(comment.getText());
    }

    @Override
    public List<CommentDto> commentListToDTos(List<Comment> comments) {
        List<CommentDto> commentDtos = new ArrayList<>();
        for (Comment c : comments) {
            commentDtos.add(commentToDto(c));
        }
        return commentDtos;
    }
}
