package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequest createRequest(long userId, ItemRequest requestDtoInToRequest);

    List<ItemRequestDto> getAllRequests(Integer from, Integer size, long userId);

    List<ItemRequestDto> getRequests(long userId);

    ItemRequestDto getRequestOne(Long id, long userId);
}
