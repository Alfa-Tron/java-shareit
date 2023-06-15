package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ItemDtoIn {
    @NotBlank(groups = Marker.OnCreate.class)
    @Size(max = 50)
    private String name;
    @NotEmpty(groups = Marker.OnCreate.class)
    @Size(max = 50)
    private String description;
    @NotNull(groups = Marker.OnCreate.class)
    private Boolean available;


    public ItemDtoIn(String name, String description, Boolean available) {
        this.name = name;
        this.description = description;
        this.available = available;
    }
}