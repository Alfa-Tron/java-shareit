package ru.practicum.shareit.comment;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    CommentDto commentToDto(Comment comment);

    Comment commentDtoInToComment(CommentDtoIn comment);

    List<CommentDto> commentListToDTos(List<Comment> comments);
}
