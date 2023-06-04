package ru.practicum.shareit.item;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;


import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final UserService userService;
    private final Validator validator;

    public ItemController(ItemService itemService, UserService userService, Validator validator) {
        this.itemService = itemService;
        this.userService = userService;
        this.validator = validator;
    }

    @PostMapping
    public ResponseEntity<Item> createItem(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody Item item) {// не понимаю почему @Valid не работает, сделал так(все анотации ставил)
        Set<ConstraintViolation<Item>> violations = validator.validate(item, Marker.OnCreate.class);
        if (!violations.isEmpty()) {
            throw new ValidationException();
        }

        if (userService.getAllUsersMap().containsKey(userId)) {
            Item createdItem = itemService.createItem(userId, item);
            return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
        }
        throw new NotFoundException("user not found");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable("id") long id) {
        Item item = itemService.getItemById(id);
        if (item != null) {
            return new ResponseEntity<>(item, HttpStatus.OK);
        } else {
            throw new NotFoundException("item not found");
        }
    }

    @GetMapping
    public ResponseEntity<List<Item>> getItemByUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        List<Item> items = itemService.getAllItems(userId);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Item>> searchItems(@RequestParam("text") String searchText) {
        List<Item> items = itemService.getSearchItems(searchText);
        return new ResponseEntity<>(items, HttpStatus.OK);

    }

    @PatchMapping("/{id}")
    public ResponseEntity<Item> updateItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("id") long id, @RequestBody Item updatedItem) {
        Set<ConstraintViolation<Item>> violations = validator.validate(updatedItem, Marker.OnUpdate.class);
        if (!violations.isEmpty()) {
            throw new ValidationException();
        }
        boolean xShared = false;
        Map<Long, List<Item>> userItems = itemService.getAllUserItems();
        Map<Long, Item> items = itemService.getAllItemsMap();
        if (userItems.containsKey(userId)) {
            for (Item item : userItems.get(userId)) {
                if (item == items.get(id)) xShared = true;
            }
        }

        if (userService.getAllUsersMap().containsKey(userId) && userService.getAllUsersMap().containsKey(userId) && xShared) {
            updatedItem.setId(id);
            Item updatedItemT = itemService.updateItem(updatedItem);
            return new ResponseEntity<>(updatedItemT, HttpStatus.OK);

        }
        throw new NotFoundException("item not found");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable("id") long id) {
        boolean deleted = itemService.deleteItem(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new NotFoundException("item not found");
        }
    }
}

