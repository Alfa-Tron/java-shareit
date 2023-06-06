package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;


@Mapper
public interface ItemMapper {
    Item ItemDtoToItem(ItemDto itemDto);

    ItemDto ItemToItemDto(Item item);

    List<ItemDto> ListToDtoList(List<Item> items);
}
