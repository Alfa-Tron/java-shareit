package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findAllByBooker_IdOrderByStartDesc(Long id, PageRequest pageRequest);

    Page<Booking> findByBookerIdAndStartIsAfterOrderByStartDesc(Long id, LocalDateTime localDateTime, PageRequest pageRequest);

    Page<Booking> findAllByStatusAndBookerId(BookingStatus waiting, Long id, PageRequest pageRequest);

    Page<Booking> findAllByStatusAndItem_Owner_Id(BookingStatus waiting, long userId, PageRequest pageRequest);

    Page<Booking> findAllByItem_Owner_IdAndStartAfterOrderByStartDesc(long userId, LocalDateTime now, PageRequest pageRequest);

    Page<Booking> findAllByItem_Owner_Id_OrderByStartDesc(long userId, PageRequest pageRequest);

    Page<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(long userId, LocalDateTime now, LocalDateTime now1, PageRequest pageRequest);

    Page<Booking> findAllByItem_Owner_IdAndStartBeforeAndEndAfter(long userId, LocalDateTime now, LocalDateTime now1, PageRequest pageRequest);

    Page<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(long userId, LocalDateTime now, PageRequest pageRequest);

    Page<Booking> findAllByItem_Owner_IdAndEndBeforeOrderByStartDesc(long userId, LocalDateTime now, PageRequest pageRequest);

    Optional<Booking> findFirstByItemAndItem_Owner_IdAndStartAfterOrderByStartAsc(Item item, long userId, LocalDateTime now);

    Boolean existsByBooker_IdAndItem_IdAndStatusAndStartBefore(long userId, Long itemId, BookingStatus approved, LocalDateTime now);

    Optional<Booking> findFirstByItemAndItem_Owner_IdAndStartLessThanEqualOrderByStartDesc(Item item, long userId, LocalDateTime now);

    List<Booking> findByItemInAndStartAfterOrderByStartAsc(List<Item> items, LocalDateTime now);

    List<Booking> findByItemInAndStartLessThanEqualOrderByStartDesc(List<Item> items, LocalDateTime now);

    List<Booking> findAllByItem_Owner_Id(long userId);
}

