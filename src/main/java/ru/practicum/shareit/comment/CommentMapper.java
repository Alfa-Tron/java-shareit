package ru.practicum.shareit.comment;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    CommentDto CommentToDto(Comment comment);

    Comment CommentDtoToComment(CommentDto comment);

    List<CommentDto> CommentListToDTos(List<Comment> comments);
}
