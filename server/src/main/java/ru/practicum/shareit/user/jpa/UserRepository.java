package ru.practicum.shareit.user.jpa;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.User;


public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsById(long userId);

}
