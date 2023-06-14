package ru.practicum.shareit.item;

import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;


public interface ItemService {
    Item createItem(long userId, Item item);

    ItemDto getItemById(long id, long userId);

    List<ItemDto> getAllItems(long userId);

    List<Item> getSearchItems(String searchText);

    Item updateItem(long userId, Item updatedItemDTO);

    void deleteItem(long id);

    Comment addComment(Long itemId, long userId, Comment text);
}
