package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Map;


public interface UserService {
    User createUser(User user);

    List<User> getAllUsers();

    User getUserById(long id);

    User updateUser(User user);

    Map<Long, User> getAllUsersMap();

    void deleteUser(long id);
}
