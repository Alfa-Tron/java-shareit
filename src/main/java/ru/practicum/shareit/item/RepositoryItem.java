package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface RepositoryItem {
    Item createItem(long userId, Item item);

    Item getItemById(long id);

    List<Item> getAllItems(long userId);

    List<Item> getSearchItems(String searchText);

    Item updateItem(Item updatedItem);

    void deleteItem(Long id);
}
