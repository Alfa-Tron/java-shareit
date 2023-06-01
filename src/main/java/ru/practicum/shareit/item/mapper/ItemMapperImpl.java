package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapperImpl implements ItemMapper {
    @Override
    public Item itemDtoToItem(ItemDto itemDto) {
        return new Item(itemDto.getId(), itemDto.getName(), itemDto.getDescription(), itemDto.isAvailable(), itemDto.getUserId(), itemDto.getRequest());
    }

    @Override
    public ItemDto itemToItemDto(Item item) {
        return new ItemDto(item.getId(),item.getName(),item.getDescription(),item.isAvailable(),item.getUserId(),item.getRequest());
    }
}
