package ru.practicum.shareit.item;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.jpa.ItemRepository;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.jpa.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repositoryItem;
    private final UserRepository repositoryUser;
    private final BookingRepository bookingRepository;
    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;


    public ItemServiceImpl(ItemRepository repositoryItem, UserRepository repositoryUser, BookingRepository bookingRepository, ItemMapper itemMapper, BookingMapper bookingMapper, CommentRepository commentRepository, CommentMapper commentMapper) {
        this.repositoryItem = repositoryItem;
        this.repositoryUser = repositoryUser;
        this.bookingRepository = bookingRepository;
        this.itemMapper = itemMapper;
        this.bookingMapper = bookingMapper;
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }


    @Override
    @Transactional
    public Item createItem(long userId, Item item) {
        User user = repositoryUser.findById(userId).orElseThrow(() -> new NotFoundException("user not found"));
        item.setOwner(user);
        return repositoryItem.save(item);
    }

    @Override
    @Transactional
    public ItemDto getItemById(long id, long userId) {
        List<Booking> bookings = bookingRepository.findAllByItem_IdAndItem_Owner_IdOrderByStartDesc(id, userId);
        Item item = repositoryItem.findById(id).orElseThrow(() -> new NotFoundException("item not found"));
        ItemDto itemDto = itemMapper.ItemToItemDto(item);
        if (bookings.size() == 1 && bookings.get(0).getEnd().isAfter(LocalDateTime.now()) && bookings.get(0).getStart().isBefore(LocalDateTime.now())) {
            itemDto.setLastBooking(bookingMapper.BookingToBookingItemsDto(bookings.get(0)));
        } else {


            for (int i = bookings.size() - 1; i >= 0; i--) {
                Booking b = bookings.get(i);
                if (b.getStart().isAfter(LocalDateTime.now()) && b.getStatus().name().equals("APPROVED")) {
                    itemDto.setNextBooking(bookingMapper.BookingToBookingItemsDto(bookings.get(i)));
                    if (bookings.size() > 1) {
                        itemDto.setLastBooking(bookingMapper.BookingToBookingItemsDto(bookings.get(i + 1)));
                        if (itemDto.getLastBooking().getId().longValue() == itemDto.getNextBooking().getId().longValue())
                            itemDto.setNextBooking(null);
                    }
                    break;
                }
            }
        }

        itemDto.setComments(commentMapper.CommentListToDTos(commentRepository.findAllByItem_Id(id)));

        return itemDto;

    }

    @Override
    @Transactional
    public List<ItemDto> getAllItems(long userId) {
        List<Item> items = repositoryItem.findByOwnerId(userId);
        List<ItemDto> itemsDto = new ArrayList<>();
        for (Item i : items) {
            itemsDto.add(getItemById(i.getId(), userId));

        }


        return itemsDto;
    }

    @Override
    @Transactional
    public List<Item> getSearchItems(String searchText) {
        if (searchText.isBlank()) return Collections.emptyList();
        return repositoryItem.findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchText, searchText).stream()
                .filter(Item::isAvailable)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Item updateItem(long userId, Item updatedItem) {
        Optional<Item> item = repositoryItem.findById(updatedItem.getId());
        Optional<User> user = repositoryUser.findById(userId);
        if (item.isPresent() && user.isPresent()) {
            if (item.get().getOwner().equals(user.get())) {
                Item currentItem = item.get();

                if (updatedItem.getDescription() != null && !updatedItem.getDescription().isBlank())
                    currentItem.setDescription(updatedItem.getDescription());
                if (updatedItem.getName() != null && !updatedItem.getName().isBlank())
                    currentItem.setName(updatedItem.getName());
                if (updatedItem.isAvailable() != null) currentItem.setAvailable(updatedItem.isAvailable());

                return repositoryItem.save(currentItem);
            }
        }
        throw new NotFoundException("item not found");
    }

    @Override
    public void deleteItem(long id) {
        repositoryItem.deleteById(id);
    }

    @Override
    public Comment addComment(Long itemId, long userId, Comment text) {
        Item item = repositoryItem.findById(itemId).orElseThrow(() -> new NotFoundException("item not found"));
        User user = repositoryUser.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        List<Booking> userHasBooking = bookingRepository.findByBooker_IdAndItem_Id(userId, itemId);
        if (userHasBooking.get(0).getStart().isAfter(LocalDateTime.now()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start is after now");

        if (userHasBooking.isEmpty() || userHasBooking.get(0).getStatus().name().equals("REJECTED")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User did not rent the item");
        }
        text.setItem(item);
        text.setAuthorName(user);
        text.setCreated(LocalDateTime.now());
        commentRepository.save(text);
        return text;
    }

}
