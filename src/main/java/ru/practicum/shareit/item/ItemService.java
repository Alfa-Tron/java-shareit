package ru.practicum.shareit.item;

import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;


public interface ItemService {
    ItemDto createItem(long userId, Item item);

    ItemDto getItemById(long id, long userId);

    List<ItemDto> getAllItems(long userId);

    List<ItemDto> getSearchItems(String searchText);

    ItemDto updateItem(long userId, long id, Item updatedItemDTO);

    void deleteItem(long id);

    CommentDto addComment(Long itemId, long userId, Comment text);
}
