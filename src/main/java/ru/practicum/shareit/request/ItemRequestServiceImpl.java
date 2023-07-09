package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.jpa.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.jpa.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final UserRepository userRepository;
    private final ItemRequestRepos requestRepository;
    private final RequestMapper requestMapper;
    private final ItemRepository repositoryItem;


    @Override
    @Transactional
    public ItemRequest createRequest(long userId, ItemRequest request) {
        User currentUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        request.setRequestor(currentUser);
        request.setCreated(LocalDateTime.now());
        return requestRepository.save(request);
    }

    @Override
    public List<ItemRequestDto> getRequests(long userId) {
        User currentUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        List<ItemRequest> requests = requestRepository.findByRequestor_IdOrderByCreatedDesc(currentUser.getId());
        Map<ItemRequest, List<Item>> items = repositoryItem.findByRequestIn(requests)
                .stream()
                .collect(groupingBy(Item::getRequest, toList()));
        for (ItemRequest i : requests) {
            if (!items.containsKey(i)) {
                items.put(i, Collections.emptyList());
            }
        }

        return itemToDtos(items);
    }

    @Override
    public ItemRequestDto getRequestOne(Long id, long userId) {
        ItemRequest request = requestRepository.findById(id).orElseThrow(() -> new NotFoundException("Request not found"));
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Item item = repositoryItem.findByRequestId(request.getId());
        ItemRequestDto dto = requestMapper.requestToDto(request);
        dto.setItems(Collections.singletonList(requestMapper.itemToRequestDtoItem(item)));

        return dto;
    }

    @Override
    public List<ItemRequestDto> getAllRequests(Integer from, Integer size, long userId) {
        if (from == null && size == null) {
            List<ItemRequest> list = requestRepository.findAllByOrderByCreatedAsc();
            List<ItemRequestDto> dtos = new ArrayList<>(list.size());
            for (ItemRequest s : list) {
                dtos.add(requestMapper.requestToDto(s));
            }
            return dtos;
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "created");
        PageRequest pageRequest = PageRequest.of(from / size, size, sort);
        User currentUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Page<ItemRequest> itemRequestPage = requestRepository.findAllByRequestorNot(currentUser, pageRequest);

        Map<ItemRequest, List<Item>> items = repositoryItem.findByRequestIn(itemRequestPage.getContent())
                .stream()
                .collect(groupingBy(Item::getRequest, toList()));


        return itemToDtos(items);
    }

    public List<ItemRequestDto> itemToDtos(Map<ItemRequest, List<Item>> items) {
        List<ItemRequestDto> dtos = new ArrayList<>(items.size());
        for (Map.Entry<ItemRequest, List<Item>> entry : items.entrySet()) {
            ItemRequest key = entry.getKey();
            List<Item> value = entry.getValue();
            ItemRequestDto r = requestMapper.requestToDto(key);
            List<ItemRequestDto.Items> irI = r.getItems();
            for (Item k : value) {
                irI.add(requestMapper.itemToRequestDtoItem(k));
            }
            dtos.add(r);
        }

        return dtos;
    }
}
