package ru.practicum.shareit.user;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exeption.ConflictException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RepositoryUserImpl implements RepositoryUser {
    private final Map<Long, User> users = new HashMap<>();
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    private long id = 1;

    @Override
    public UserDto createUser(User user) {
        if (isEmailUnique(user.getEmail())) {
            ;
            long userId = id++;
            user.setId(userId);
            users.put(userId, user);
            return userMapper.userToUserDto(user);
        } else {
            throw new ConflictException("User with the same email already exists");
        }

    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public UserDto getUserById(long id) {
        return userMapper.userToUserDto(users.get(id));
    }

    @Override
    public UserDto updateUser(User user) {
        if (!isEmailUnique(user.getEmail()) && !users.get(user.getId()).getEmail().equals(user.getEmail()))
            throw new IllegalArgumentException();
        User userNew = users.get(user.getId());
        if (user.getEmail() != null) userNew.setEmail(user.getEmail());
        if (user.getName() != null && !user.getName().isEmpty()) userNew.setName(user.getName());

        return userMapper.userToUserDto(userNew);
    }

    @Override
    public Map<Long, User> getAllUsersMap() {
        return new HashMap<>(users);
    }

    @Override
    public boolean deleteUser(long id) {
        boolean delete = users.containsKey(id);
        users.remove(id);
        return delete;
    }

    private boolean isEmailUnique(String email) {
        for (User user : users.values()) {
            if (user.getEmail().equals(email)) {
                return false;
            }
        }
        return true;
    }
}
