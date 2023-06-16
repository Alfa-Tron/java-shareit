package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBooker_IdOrderByStartDesc(Long id);

    List<Booking> findByBookerIdAndStartIsAfterOrderByStartDesc(Long id, LocalDateTime localDateTime);

    List<Booking> findAllByStatusAndBookerId(BookingStatus waiting, Long id);

    List<Booking> findAllByStatusAndItem_Owner_Id(BookingStatus waiting, long userId);

    List<Booking> findAllByItem_Owner_IdAndStartAfterOrderByStartDesc(long userId, LocalDateTime now);

    List<Booking> findAllByItem_Owner_Id_OrderByStartDesc(long userId);
    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(long userId, LocalDateTime now, LocalDateTime now1);

    List<Booking> findAllByItem_Owner_IdAndStartBeforeAndEndAfter(long userId, LocalDateTime now, LocalDateTime now1);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(long userId, LocalDateTime now);

    List<Booking> findAllByItem_Owner_IdAndEndBeforeOrderByStartDesc(long userId, LocalDateTime now);

    Optional<Booking> findFirstByItemAndItem_Owner_IdAndStartAfterOrderByStartAsc(Item item, long userId, LocalDateTime now);

    Boolean existsByBooker_IdAndItem_IdAndStatusAndStartBefore(long userId, Long itemId, BookingStatus approved, LocalDateTime now);

    Optional<Booking> findFirstByItemAndItem_Owner_IdAndStartLessThanEqualOrderByStartDesc(Item item, long userId, LocalDateTime now);

    List<Booking> findByItemInAndStartAfterOrderByStartAsc(List<Item> items, LocalDateTime now);

    List<Booking> findByItemInAndStartLessThanEqualOrderByStartDesc(List<Item> items, LocalDateTime now);
}

