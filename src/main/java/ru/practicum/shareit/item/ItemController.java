package ru.practicum.shareit.item;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final ItemMapper itemMapper;

    public ItemController(ItemService itemService, ItemMapper itemMapper) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") long userId, @Validated({Marker.OnCreate.class}) @RequestBody ItemDto item) {
        return itemMapper.ItemToItemDto(itemService.createItem(userId, itemMapper.ItemDtoToItem(item)));
    }

    @GetMapping("/{id}")
    public ItemDto getItemById(@PathVariable("id") long id) {

        return itemMapper.ItemToItemDto(itemService.getItemById(id));
    }

    @GetMapping
    public List<ItemDto> getItemByUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemMapper.ListToDtoList(itemService.getAllItems(userId));
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam("text") String searchText) {
        return itemMapper.ListToDtoList(itemService.getSearchItems(searchText));

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

