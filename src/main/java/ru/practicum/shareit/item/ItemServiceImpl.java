package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.jpa.ItemRepository;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.jpa.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repositoryItem;
    private final UserRepository repositoryUser;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final ItemMapper itemMapper;


    @Override
    @Transactional
    public ItemDto createItem(long userId, Item item) {
        User user = repositoryUser.findById(userId).orElseThrow(() -> new NotFoundException("user not found"));
        item.setOwner(user);
        return itemMapper.itemToItemDto(repositoryItem.save(item));
    }

    @Override
    public ItemDto getItemById(long id, long userId) {
        Item item = repositoryItem.findById(id).orElseThrow(() -> new NotFoundException("item not found"));
        List<Comment> comments = commentRepository.findByItemOrderByCreatedDesc(item);
        LocalDateTime now = LocalDateTime.now().withNano(0);
        Optional<Booking> lastBookings = bookingRepository.findFirstByItemAndItem_Owner_IdAndStartLessThanEqualOrderByStartDesc(item, userId, now);
        Optional<Booking> nextBookings = bookingRepository.findFirstByItemAndItem_Owner_IdAndStartAfterOrderByStartAsc(item, userId, now);
        ItemDto i = new ItemDto(item.getId(), item.getName(), item.getDescription(), item.isAvailable());

        if (lastBookings.isPresent()) {
            Booking last = lastBookings.get();
            i.setLastBooking(new ItemDto.BookingItemsDto(last.getId(), last.getBooker().getId()));
        }

        if (nextBookings.isPresent()) {
            if (lastBookings.isPresent() && lastBookings.get().getId().longValue() != nextBookings.get().getId().longValue()) {
                Booking next = nextBookings.get();

                i.setNextBooking(new ItemDto.BookingItemsDto(next.getId(), next.getBooker().getId()));
            }
        }
        if (comments != null) i.setComments(commentMapper.commentListToDTos(comments));

        return i;
    }

    @Override
    public List<ItemDto> getAllItems(long userId, Integer from, Integer size) {
        if (from == null && size == null) {
            from = 0;
            size = 99;
        }
        PageRequest pageRequest = PageRequest.of((from) / size, size);
        List<Item> items = repositoryItem.findByOwnerId(userId, pageRequest).getContent();
        List<ItemDto> itemsDto = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now().withNano(0);
        Map<Item, List<Comment>> comments = commentRepository.findByItemIn(items, Sort.by(DESC, "created"))
                .stream()
                .collect(groupingBy(Comment::getItem, toList()));
        Map<Item, List<Booking>> lastBookings = bookingRepository.findByItemInAndStartLessThanEqualOrderByStartDesc(items, now)
                .stream()
                .collect(groupingBy(Booking::getItem, toList()));
        Map<Item, List<Booking>> nextBookings = bookingRepository.findByItemInAndStartAfterOrderByStartAsc(items, now)
                .stream()
                .collect(groupingBy(Booking::getItem, toList()));

        for (Item entry : items) {
            List<Comment> value = comments.get(entry);

            ItemDto i = new ItemDto(entry.getId(), entry.getName(), entry.getDescription(), entry.isAvailable());
            if (!lastBookings.isEmpty() && !nextBookings.isEmpty()) {
                if (lastBookings.containsKey(entry) && nextBookings.containsKey(entry)) {
                    Booking last = lastBookings.get(entry).get(0);
                    Booking next = nextBookings.get(entry).get(0);
                    if (last.getId().longValue() == next.getId().longValue()) {
                        i.setLastBooking(new ItemDto.BookingItemsDto(last.getId(), last.getBooker().getId()));
                    } else {
                        i.setLastBooking(new ItemDto.BookingItemsDto(last.getId(), last.getBooker().getId()));
                        i.setNextBooking(new ItemDto.BookingItemsDto(next.getId(), next.getBooker().getId()));
                    }
                }
            }
            if (value != null) i.setComments(commentMapper.commentListToDTos(value));

            itemsDto.add(i);
        }


        return itemsDto;
    }

    @Override
    public List<ItemDto> getSearchItems(String searchText, Integer from, Integer size) {
        if (searchText.isBlank()) return Collections.emptyList();
        if (from == null && size == null) {
            from = 0;
            size = 99;
        }
        PageRequest pageRequest = PageRequest.of((from) / size, size);

        return itemMapper.listToDtoList(repositoryItem.findAllByNameOrDescription(searchText.toLowerCase(), pageRequest).getContent());

    }

    @Override
    @Transactional
    public ItemDto updateItem(long userId, long id, Item updatedItem) {
        updatedItem.setId(id);
        Item item = repositoryItem.findById(updatedItem.getId()).orElseThrow(() -> new NotFoundException("item not found"));
        User user = repositoryUser.findById(userId).orElseThrow(() -> new NotFoundException("user not found"));

        if (!item.getOwner().equals(user)) {
            throw new IllegalArgumentException("not rights");
        }

        if (updatedItem.getDescription() != null && !updatedItem.getDescription().isBlank())
            item.setDescription(updatedItem.getDescription());
        if (updatedItem.getName() != null && !updatedItem.getName().isBlank())
            item.setName(updatedItem.getName());
        if (updatedItem.isAvailable() != null) item.setAvailable(updatedItem.isAvailable());
        return itemMapper.itemToItemDto(item);
    }

    @Override
    public void deleteItem(long id) {
        repositoryItem.deleteById(id);
    }

    @Override
    public CommentDto addComment(Long itemId, long userId, Comment text) {
        Item item = repositoryItem.findById(itemId).orElseThrow(() -> new NotFoundException("item not found"));
        User user = repositoryUser.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Boolean userHasBooking = bookingRepository.existsByBooker_IdAndItem_IdAndStatusAndStartBefore(userId, itemId, BookingStatus.APPROVED, LocalDateTime.now());

        if (!userHasBooking) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User did not rent the item");
        } else {
            text.setItem(item);
            text.setAuthorName(user);
            text.setCreated(LocalDateTime.now());
            commentRepository.save(text);
        }

        return commentMapper.commentToDto(text);
    }

}
