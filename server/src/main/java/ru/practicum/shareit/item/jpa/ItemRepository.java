package ru.practicum.shareit.item.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findByOwnerId(Long ownerId, PageRequest pageRequest);

    @Query("SELECT i FROM Item i WHERE (LOWER(i.name) LIKE %:searchString% OR LOWER(i.description) LIKE %:searchString%) AND i.available = true ")
    Page<Item> findAllByNameOrDescription(String searchString, Pageable pageable);

    List<Item> findByRequestIn(List<ItemRequest> request);

    Item findByRequestId(Long r);
}