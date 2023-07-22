package ru.practicum.shareit.user;

import java.util.List;


public interface UserService {
    User createUser(User user);

    List<User> getAllUsers();

    User getUserById(long id);

    User updateUser(User user);

    void deleteUser(long id);
}
