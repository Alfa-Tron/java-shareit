package ru.practicum.shareit.comment;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CommentDtoIn {
    @NotBlank(groups = Marker.OnCreate.class)
    @Size(max = 50)
    private String text;
}
