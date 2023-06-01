package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;

@Service
public interface ItemService {
    ItemDto createItem(ItemDto itemDTO);

    ItemDto getItemById(long id);

    List<ItemDto> getAllItems(long userId);

    List<ItemDto> getSearchItems(String searchText);

    ItemDto updateItem(ItemDto updatedItemDTO);

    boolean deleteItem(long id);

    Map<Long, Item> getAllItemsMap();
}
