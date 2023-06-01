package ru.practicum.shareit.item;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserServiceImpl;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final UserServiceImpl userService;

    public ItemController(ItemService itemService, UserServiceImpl userService) {
        this.itemService = itemService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ItemDto> createItem(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody ItemDto itemDTO) {
        if (userService.getAllUsersMap().containsKey(userId)) {
            try {
                itemDTO.setUserId(userId);
                ItemDto createdItem = itemService.createItem(itemDTO);
                return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getItemById(@PathVariable("id") long id) {
        ItemDto item = itemService.getItemById(id);
        if (item != null) {
            return new ResponseEntity<>(item, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> getItemByUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        List<ItemDto> items = itemService.getAllItems(userId);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/search")
    public  ResponseEntity<List<ItemDto>> searchItems(@RequestParam("text") String searchText){
        int k =1;
        List<ItemDto> items = itemService.getSearchItems(searchText);
        return new ResponseEntity<>(items,HttpStatus.OK);

    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemDto> updateItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("id") long id, @RequestBody ItemDto updatedItemDto) {
        boolean xShared =false;
        for(Item item : itemService.getAllItemsMap().values()){
            if(item.getUserId() == userId) xShared =true;
        }
        if (userService.getAllUsersMap().containsKey(userId) && userService.getAllUsersMap().containsKey(userId)  && xShared) {
           updatedItemDto.setId(id);
            ItemDto updatedItem = itemService.updateItem(updatedItemDto);
            return new ResponseEntity<>(updatedItem, HttpStatus.OK);

        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable("id") long id) {
        boolean deleted = itemService.deleteItem(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

