package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
public class RepositoryItemImpl implements RepositoryItem {
    private final Map<Long, List<Item>> userItemIndex = new LinkedHashMap<>();
    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public Item createItem(long userId, Item item) {
        final List<Item> items = userItemIndex.computeIfAbsent(userId, k -> new ArrayList<>());
        items.add(item);
        this.items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item getItemById(long id) {
        return items.get(id);
    }

    @Override
    public List<Item> getAllItems(long userId) {

        return new ArrayList<>(userItemIndex.get(userId));
    }

    @Override
    public List<Item> getSearchItems(String searchText) {
        List<Item> searchItem = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getDescription().toLowerCase().contains(searchText.toLowerCase()) && item.isAvailable() && !searchText.isBlank())
                searchItem.add(item);
        }
        return searchItem;
    }

    @Override
    public Item updateItem(Item updatedItem) {
        if (items.containsKey(updatedItem.getId())) {
            Item item = items.get(updatedItem.getId());
            if (updatedItem.getName() != null && !updatedItem.getName().isBlank()) item.setName(updatedItem.getName());
            if (updatedItem.getDescription() != null)
                item.setDescription(updatedItem.getDescription());
            if (updatedItem.isAvailable() != null)
                item.setAvailable(updatedItem.isAvailable());
        }
        return items.get(updatedItem.getId());
    }

    @Override
    public Map<Long, Item> getAllItemsMap() {
        return new HashMap<>(items);
    }

    @Override
    public Map<Long, List<Item>> getAllUserItems() {
        return new HashMap<>(userItemIndex);
    }

    @Override
    public boolean deleteItem(Long id) {

        items.remove(id);

        return true;
    }
}