package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.Marker;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.dto.RequestMapper;


import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private final RequestMapper requestMapper;

    @PostMapping
    public ItemRequestDto createRequest(@RequestHeader("X-Sharer-User-Id") long userId, @Validated({Marker.OnCreate.class}) @RequestBody ItemRequestDtoIn requestDtoIn) {
        log.info("CreateRequest userId: {}, RequestDtoIn: {}", userId, requestDtoIn);
        return requestMapper.requestToDto(itemRequestService.createRequest(userId, requestMapper.requestDtoInToRequest(requestDtoIn)));
    }

    @GetMapping
    public List<ItemRequestDto> getItemById(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("getItemById userId: {}", userId);
        return itemRequestService.getRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequests(@RequestParam(value = "from", required = false) Integer from, @RequestParam(value = "size", required = false) Integer size, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("getAllRequests userId: {}, from: {}, size: {}", userId, from, size);
        return itemRequestService.getAllRequests(from, size, userId);
    }

    @GetMapping("/{id}")
    public ItemRequestDto getItemById(@PathVariable Long id, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("getItemById userId: {}", userId);
        return itemRequestService.getRequestOne(id, userId);
    }
}
