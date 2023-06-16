package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemMapperImpl implements ItemMapper {
    @Override
    public Item itemDtoInToItem(ItemDtoIn itemDto) {
        return new Item( itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable());
    }

    @Override
    public ItemDto itemToItemDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.isAvailable());
    }

    @Override
    public List<ItemDto> listToDtoList(List<Item> items) {
        return items.stream()
                .map(this::itemToItemDto)
                .collect(Collectors.toList());
    }
}
