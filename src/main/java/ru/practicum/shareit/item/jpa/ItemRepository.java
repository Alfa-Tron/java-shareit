package ru.practicum.shareit.item.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwnerId(Long ownerId);

    List<Item> findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);
}