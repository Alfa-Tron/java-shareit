package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;


public interface ItemService {
    Item createItem(long userId, Item item);

    Item getItemById(long id);

    List<Item> getAllItems(long userId);

    List<Item> getSearchItems(String searchText);

    Item updateItem(long userId, Item updatedItemDTO);

    boolean deleteItem(Long id);

    Map<Long, List<Item>> getAllUserItems();

    Map<Long, Item> getAllItemsMap();
}
