package ru.practicum.shareit.comment;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommentMapperImpl implements CommentMapper {
    @Override
    public CommentDto CommentToDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(), comment.getItem(), comment.getAuthorName().getName(), comment.getCreated().toString());
    }

    @Override
    public Comment CommentDtoToComment(CommentDto comment) {
        return new Comment(comment.getText(), comment.getItem());
    }

    @Override
    public List<CommentDto> CommentListToDTos(List<Comment> comments) {
        List<CommentDto> commentDtos = new ArrayList<>();
        for (Comment c : comments) {
            commentDtos.add(CommentToDto(c));
        }
        return commentDtos;
    }
}
