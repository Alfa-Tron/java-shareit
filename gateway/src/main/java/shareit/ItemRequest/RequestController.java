package shareit.ItemRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shareit.ItemRequest.dto.ItemRequestDtoIn;
import shareit.user.dto.Marker;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader("X-Sharer-User-Id") long userId, @Validated({Marker.OnCreate.class}) @RequestBody ItemRequestDtoIn requestDtoIn) {
        log.info("CreateRequest userId: {}, RequestDtoIn: {}", userId, requestDtoIn);
        return requestClient.createRequest(userId, requestDtoIn);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsById(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("getItemById userId: {}", userId);
        return requestClient.getItemsById(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@PositiveOrZero @RequestParam(value = "from", required = false) Integer from, @Positive @RequestParam(value = "size", required = false) Integer size, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("getAllRequests userId: {}, from: {}, size: {}", userId, from, size);
        return requestClient.getAllrequests(userId, from, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemById(@PathVariable Long id, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("getItemById userId: {}", userId);
        return requestClient.getItemById(id, userId);

    }
}
