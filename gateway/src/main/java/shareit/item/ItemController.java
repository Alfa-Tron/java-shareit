package shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shareit.item.dto.CommentDtoIn;
import shareit.item.dto.ItemDtoIn;
import shareit.user.dto.Marker;


import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") long userId, @Validated({Marker.OnCreate.class}) @RequestBody ItemDtoIn item) {
        log.info("createItem userId: {}, item: {}", userId, item);
        return itemClient.createItem(userId, item);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemById(@PathVariable("id") long id, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("getItemById id: {}, userId: {}", id, userId);
        return itemClient.getItemById(id, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemByUser(@RequestHeader("X-Sharer-User-Id") long userId, @PositiveOrZero @RequestParam(value = "from", required = false) Integer from, @Positive @RequestParam(value = "size", required = false) Integer size) {
        log.info("getItemByUser userId: {}, from: {}, size: {}", userId, from, size);
        return itemClient.getItemByUser(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam("text") String searchText, @PositiveOrZero @RequestParam(value = "from", required = false) Integer from, @Positive @RequestParam(value = "size", required = false) Integer size) {
        log.info("searchItems searchText: {}, from: {}, size: {}", searchText, from, size);
        return itemClient.searchItems(searchText, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") long userId, @Validated({Marker.OnCreate.class}) @RequestBody CommentDtoIn comment) {
        return itemClient.addComment(itemId, userId, comment);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable("id") long id, @Validated({Marker.OnUpdate.class}) @RequestBody ItemDtoIn updatedItem) {
        return itemClient.updateItem(userId, id, updatedItem);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteItem(@PathVariable("id") long id) {
        return itemClient.deleteItem(id);
    }


}
