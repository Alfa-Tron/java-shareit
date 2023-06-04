package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;

@Service
public class ItemServiceImpl implements ItemService {
    private final RepositoryItemImpl repository;

    private ItemServiceImpl(RepositoryItemImpl repository) {
        this.repository = repository;
    }

    private long id = 1;

    @Override
    public Item createItem(long userId, Item item) {
        item.setId(id++);
        return repository.createItem(userId, item);
    }

    @Override
    public Item getItemById(long id) {
        return repository.getItemById(id);
    }

    @Override
    public List<Item> getAllItems(long userId) {
        return repository.getAllItems(userId);
    }

    @Override
    public List<Item> getSearchItems(String searchText) {

        return repository.getSearchItems(searchText);
    }

    @Override
    public Item updateItem(Item updatedItem) {

        return repository.updateItem(updatedItem);
    }

    @Override
    public boolean deleteItem(Long id) {
        return repository.deleteItem(id);
    }

    public Map<Long, Item> getAllItemsMap() {
        return repository.getAllItemsMap();
    }

    public Map<Long, List<Item>> getAllUserItems() {
        return repository.getAllUserItems();
    }

    public boolean deleteItem(long id) {
        return repository.deleteItem(id);
    }

}
