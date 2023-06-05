package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class RepositoryItemMemoryImpl implements RepositoryItem {
    private final Map<Long, List<Item>> userItemIndex = new LinkedHashMap<>();

    private final Map<Long, Item> items = new HashMap<>();
    private long id = 1;


    @Override
    public Item createItem(long userId, Item item) {
        item.setId(id++);
        item.setOwner(userId);
        final List<Item> items = userItemIndex.computeIfAbsent(userId, k -> new ArrayList<>());
        items.add(item);
        this.items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item getItemById(long id) {
        Item item = items.get(id);
        if (item == null) {
            throw new NotFoundException("item not found");
        }
        return item;
    }

    @Override
    public List<Item> getAllItems(long userId) {
        return new ArrayList<>(userItemIndex.getOrDefault(userId, Collections.emptyList()));
    }

    @Override
    public List<Item> getSearchItems(String searchText) {
        return items.values()
                .stream()
                .filter(item -> ((item.getDescription().toLowerCase().contains(searchText.toLowerCase())
                        || item.getName().toLowerCase().contains(searchText.toLowerCase()))
                        && item.isAvailable()))
                .collect(Collectors.toList());
    }

    @Override
    public Item updateItem(Item updatedItem) {
        Item item = null;
        if (items.containsKey(updatedItem.getId())) {
            item = items.get(updatedItem.getId());
            if (updatedItem.getName() != null && !updatedItem.getName().isBlank()) item.setName(updatedItem.getName());
            if (updatedItem.getDescription() != null && !updatedItem.getDescription().isEmpty())
                item.setDescription(updatedItem.getDescription());
            if (updatedItem.isAvailable() != null)
                item.setAvailable(updatedItem.isAvailable());
        }
        return item;
    }

    @Override
    public void deleteItem(Long id) {
        if (items.remove(id) == null) throw new NotFoundException("item not found");
    }
}
