package ru.practicum.shareit.request.dto;


import org.mapstruct.Mapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

@Mapper
public interface RequestMapper {
    ItemRequest requestDtoInToRequest(ItemRequestDtoIn in);

    ItemRequestDto requestToDto(ItemRequest itemRequest);

    List<ItemRequestDto> requestsToDtos(List<ItemRequest> list);

    ItemRequestDto.Items itemToRequestDtoItem(Item item);
}
