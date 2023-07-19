package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class ItemDtoIn {

    private String name;

    private String description;

    private Boolean available;
    private long requestId;

}