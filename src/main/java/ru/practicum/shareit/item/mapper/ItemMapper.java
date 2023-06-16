package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.model.Item;

import java.util.List;


@Mapper
public interface ItemMapper {
    Item itemDtoInToItem(ItemDtoIn itemDto);

    ItemDto itemToItemDto(Item item);

    List<ItemDto> listToDtoList(List<Item> items);
}
