package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;

public interface RepositoryItem {
    Item createItem(long userId, Item item);

    Item getItemById(long id);

    List<Item> getAllItems(long userId);

    List<Item> getSearchItems(String searchText);

    Item updateItem(Item updatedItem);

    Map<Long, Item> getAllItemsMap();

    Map<Long, List<Item>> getAllUserItems();

    boolean deleteItem(Long id);
}
