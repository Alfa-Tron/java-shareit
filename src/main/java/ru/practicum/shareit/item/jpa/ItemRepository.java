package ru.practicum.shareit.item.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwnerId(Long ownerId);

    @Query("SELECT i FROM Item i WHERE (LOWER(i.name) LIKE %:searchString% OR LOWER(i.description) LIKE %:searchString%) AND i.available = true ")
    List<Item> findAllByNameOrDescription(String searchString);
}