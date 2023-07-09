package ru.practicum.shareit.request.dto;


import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;

import java.util.ArrayList;
import java.util.List;

@Component
public class RequestMapperImpl implements RequestMapper {
    @Override
    public ItemRequest requestDtoInToRequest(ItemRequestDtoIn in) {
        return new ItemRequest(in.getDescription());
    }

    @Override
    public ItemRequestDto requestToDto(ItemRequest itemRequest) {
        return new ItemRequestDto(itemRequest.getId(), itemRequest.getDescription(), itemRequest.getRequestor(), itemRequest.getCreated());
    }

    @Override
    public List<ItemRequestDto> requestsToDtos(List<ItemRequest> list) {
        List<ItemRequestDto> requestDtos = new ArrayList<>();
        for (ItemRequest i : list) {
            requestDtos.add(requestToDto(i));
        }
        return requestDtos;
    }

    @Override
    public ItemRequestDto.Items itemToRequestDtoItem(Item k) {
        ItemRequestDto.Items items1 = new ItemRequestDto.Items();
        items1.setId(k.getId());
        items1.setName(k.getName());
        items1.setAvailable(k.isAvailable());
        items1.setDescription(k.getDescription());
        items1.setRequestId(k.getRequest().getId());
        return items1;
    }
}
