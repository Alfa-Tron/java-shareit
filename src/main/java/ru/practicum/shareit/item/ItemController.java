package ru.practicum.shareit.item;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public Item createItem(@RequestHeader("X-Sharer-User-Id") long userId, @Validated({Marker.OnCreate.class}) @RequestBody Item item) {
        return itemService.createItem(userId, item);
    }

    @GetMapping("/{id}")
    public Item getItemById(@PathVariable("id") long id) {

        return itemService.getItemById(id);
    }

    @GetMapping
    public List<Item> getItemByUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getAllItems(userId);
    }

    @GetMapping("/search")
    public List<Item> searchItems(@RequestParam("text") String searchText) {
        return itemService.getSearchItems(searchText);

    }

    @PatchMapping("/{id}")
    public Item updateItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("id") long id, @Validated({Marker.OnUpdate.class}) @RequestBody Item updatedItem) {
        updatedItem.setId(id);
        return itemService.updateItem(userId, updatedItem);

    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable("id") long id) {
        itemService.deleteItem(id);
    }
}

