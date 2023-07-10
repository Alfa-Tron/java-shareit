package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ItemRequestDtoIn {
    @NotBlank(groups = Marker.OnCreate.class)
    @Size(min = 5, max = 50, groups = Marker.OnCreate.class)
    private String description;
}
