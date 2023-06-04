package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Map;

public interface RepositoryUser {
    UserDto createUser(User user);

    List<User> getAllUsers();

    UserDto getUserById(long id);

    UserDto updateUser(User user);

    Map<Long, User> getAllUsersMap();

    boolean deleteUser(long id);
}
