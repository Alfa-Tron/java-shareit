package ru.practicum.shareit.item;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    public ItemController(ItemService itemService, ItemMapper itemMapper, CommentMapper commentMapper) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
        this.commentMapper = commentMapper;
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") long userId, @Validated({Marker.OnCreate.class}) @RequestBody ItemDto item) {
        return itemMapper.ItemToItemDto(itemService.createItem(userId, itemMapper.ItemDtoToItem(item)));
    }

    @GetMapping("/{id}")
    public ItemDto getItemById(@PathVariable("id") long id, @RequestHeader("X-Sharer-User-Id") long userId) {

        return itemService.getItemById(id, userId);
    }

    @GetMapping
    public List<ItemDto> getItemByUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getAllItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam("text") String searchText) {
        return itemMapper.ListToDtoList(itemService.getSearchItems(searchText));

    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") long userId, @Validated({Marker.OnCreate.class}) @RequestBody CommentDto comment) {
        return commentMapper.CommentToDto(itemService.addComment(itemId, userId, commentMapper.CommentDtoToComment(comment)));
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("id") long id, @Validated({Marker.OnUpdate.class}) @RequestBody ItemDto updatedItem) {
        updatedItem.setId(id);
        return itemMapper.ItemToItemDto(itemService.updateItem(userId, itemMapper.ItemDtoToItem(updatedItem)));

    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable("id") long id) {
        itemService.deleteItem(id);
    }
}

