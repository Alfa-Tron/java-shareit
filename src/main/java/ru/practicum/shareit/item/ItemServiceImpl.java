package ru.practicum.shareit.item;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemServiceImpl implements ItemService {
    private final Map<Long, Item> items = new HashMap<>();
    private final ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);

    private long id = 1;

    @Override
    public ItemDto createItem(ItemDto itemDTO) {
        if (itemDTO.getName().isBlank()) {
            throw new IllegalArgumentException("name clear");
        }
        if (itemDTO.isAvailable() == null) {
            throw new IllegalArgumentException("available clear");
        }
        if (itemDTO.getDescription() == null) {
            throw new IllegalArgumentException("description clear");
        }
        itemDTO.setId(id);
        items.put(id++, itemMapper.itemDtoToItem(itemDTO));
        return itemDTO;
    }

    @Override
    public ItemDto getItemById(long id) {
        return itemMapper.itemToItemDto(items.get(id));
    }

    @Override
    public List<ItemDto> getAllItems(long userId) {
        List<ItemDto> itemsDto = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getUserId() == userId) itemsDto.add(itemMapper.itemToItemDto(item));
        }
        return itemsDto;
    }

    @Override
    public List<ItemDto> getSearchItems(String searchText) {
        List<ItemDto> itemsDto = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getDescription().toLowerCase().contains(searchText.toLowerCase()) && item.isAvailable() && !searchText.isBlank())
                itemsDto.add(itemMapper.itemToItemDto(item));
        }
        return itemsDto;
    }

    @Override
    public ItemDto updateItem(ItemDto updatedItemDTO) {
        if (items.containsKey(updatedItemDTO.getId())) {
            System.out.println(items.get(updatedItemDTO.getId()));
            if (updatedItemDTO.getName() != null) items.get(updatedItemDTO.getId()).setName(updatedItemDTO.getName());
            if (updatedItemDTO.getDescription() != null)
                items.get(updatedItemDTO.getId()).setDescription(updatedItemDTO.getDescription());
            if (updatedItemDTO.isAvailable() != null)
                items.get(updatedItemDTO.getId()).setAvailable(updatedItemDTO.isAvailable());
        }
        return itemMapper.itemToItemDto(items.get(updatedItemDTO.getId()));
    }

    public Map<Long, Item> getAllItemsMap() {
        return new HashMap<>(items);
    }

    @Override
    public boolean deleteItem(long id) {
        return false;
    }
}
