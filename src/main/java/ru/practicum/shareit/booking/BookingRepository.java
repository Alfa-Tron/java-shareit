package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBooker_IdOrderByStartDesc(Long id);

    List<Booking> findByBookerIdAndStartIsAfterOrderByStartDesc(Long id, LocalDateTime localDateTime);

    List<Booking> findAllByStatusAndBookerId(BookingStatus waiting, Long id);

    List<Booking> findAllByStatusAndItem_Owner_Id(BookingStatus waiting, long userId);

    List<Booking> findAllByItem_Owner_IdAndStartAfterOrderByStartDesc(long userId, LocalDateTime now);

    List<Booking> findAllByItem_Owner_Id_OrderByStartDesc(long userId);

    List<Booking> findAllByItem_IdAndItem_Owner_IdOrderByStartDesc(long id, long userId);

    List<Booking> findByBooker_IdAndItem_Id(long userId, Long itemId);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(long userId, LocalDateTime now, LocalDateTime now1);

    List<Booking> findAllByItem_Owner_IdAndStartBeforeAndEndAfter(long userId, LocalDateTime now, LocalDateTime now1);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(long userId, LocalDateTime now);

    List<Booking> findAllByItem_Owner_IdAndEndBeforeOrderByStartDesc(long userId, LocalDateTime now);
}

