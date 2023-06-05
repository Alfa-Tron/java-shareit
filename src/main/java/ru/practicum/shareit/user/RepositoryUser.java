package ru.practicum.shareit.user;

import java.util.List;
import java.util.Map;

public interface RepositoryUser {
    User createUser(User user);

    List<User> getAllUsers();

    User getUserById(long id);

    User updateUser(User user);

    Map<Long, User> getAllUsersMap();

    boolean deleteUser(long id);
}
