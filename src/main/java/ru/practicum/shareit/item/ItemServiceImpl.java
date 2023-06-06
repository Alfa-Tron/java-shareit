package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.RepositoryUser;

import java.util.Collections;
import java.util.List;

import java.util.Objects;

@Service
public class ItemServiceImpl implements ItemService {
    private final RepositoryItem repositoryItemMemory;
    private final RepositoryUser repositoryUserMemory;

    private ItemServiceImpl(RepositoryItem repositoryItemMemory, RepositoryUser repositoryUserMemory) {
        this.repositoryItemMemory = repositoryItemMemory;
        this.repositoryUserMemory = repositoryUserMemory;
    }

    @Override
    public Item createItem(long userId, Item item) {
        if (repositoryUserMemory.getAllUsersMap().containsKey(userId)) {
            return repositoryItemMemory.createItem(userId, item);
        }
        throw new NotFoundException("user not found");
    }

    @Override
    public Item getItemById(long id) {
        return repositoryItemMemory.getItemById(id);
    }

    @Override
    public List<Item> getAllItems(long userId) {
        return repositoryItemMemory.getAllItems(userId);
    }

    @Override
    public List<Item> getSearchItems(String searchText) {
        if (searchText.isBlank()) return Collections.emptyList();

        return repositoryItemMemory.getSearchItems(searchText);
    }

    @Override
    public Item updateItem(long userId, Item updatedItem) {
        Item item = repositoryItemMemory.getItemById(updatedItem.getId());
        if (Objects.equals(item.getOwner(), userId)) {
            return repositoryItemMemory.updateItem(updatedItem);
        }
        throw new NotFoundException("item not found");
    }

    @Override
    public void deleteItem(long id) {
        repositoryItemMemory.deleteItem(id);
    }

}
