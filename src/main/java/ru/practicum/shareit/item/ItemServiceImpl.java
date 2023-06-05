package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.List;
import java.util.Map;

@Service
public class ItemServiceImpl implements ItemService {
    private final RepositoryItemImpl repository;
    private final UserService userService;

    private ItemServiceImpl(RepositoryItemImpl repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @Override
    public Item createItem(long userId, Item item) {
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
    public Item updateItem(long userId, Item updatedItem) {
        boolean xShared = false;
        Map<Long, List<Item>> userItems = getAllUserItems();
        Map<Long, Item> items = getAllItemsMap();
        if (userItems.containsKey(userId)) {
            for (Item item : userItems.get(userId)) {
                if (item == items.get(updatedItem.getId())) xShared = true;
            }
        }

        if (userService.getAllUsersMap().containsKey(userId) && userService.getAllUsersMap().containsKey(userId) && xShared) {
            return repository.updateItem(updatedItem);
        }
        throw new NotFoundException("item not found");


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
