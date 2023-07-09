package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.ValidationUtils;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.comment.CommentDtoIn;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.mapper.ItemMapper;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;


    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") long userId, @Validated({Marker.OnCreate.class}) @RequestBody ItemDtoIn item) {
        return itemService.createItem(userId, itemMapper.itemDtoInToItem(item));
    }

    @GetMapping("/{id}")
    public ItemDto getItemById(@PathVariable("id") long id, @RequestHeader("X-Sharer-User-Id") long userId) {

        return itemService.getItemById(id, userId);
    }

    @GetMapping
    public List<ItemDto> getItemByUser(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam(value = "from", required = false) Integer from, @RequestParam(value = "size", required = false) Integer size) {
        ValidationUtils.validatePaginationParameters(from, size);
        return itemService.getAllItems(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam("text") String searchText, @RequestParam(value = "from", required = false) Integer from, @RequestParam(value = "size", required = false) Integer size) {
        ValidationUtils.validatePaginationParameters(from, size);
        return itemService.getSearchItems(searchText, from, size);

    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") long userId, @Validated({Marker.OnCreate.class}) @RequestBody CommentDtoIn comment) {
        return itemService.addComment(itemId, userId, commentMapper.commentDtoInToComment(comment));
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("id") long id, @Validated({Marker.OnUpdate.class}) @RequestBody ItemDtoIn updatedItem) {
        return itemService.updateItem(userId, id, itemMapper.itemDtoInToItem(updatedItem));

    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable("id") long id) {
        itemService.deleteItem(id);
    }
}

