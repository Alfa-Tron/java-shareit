package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exeption.NotFoundException;

import java.util.*;

@Repository
public class RepositoryUserMemoryImpl implements RepositoryUser {
    private final Map<Long, User> users;
    private long id = 1;

    public RepositoryUserMemoryImpl() {
        users = new HashMap<>();
    }

    @Override
    public User createUser(User user) {
        if (isEmailUnique(user.getEmail())) {
            long userId = id++;
            user.setId(userId);
            users.put(userId, user);
            return user;
        } else {
            throw new IllegalArgumentException("User with the same email already exists");
        }

    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(long id) {
        if (!users.containsKey(id)) throw new NotFoundException("Объект не найден");
        return users.get(id);
    }

    @Override
    public User updateUser(User user) {
        User userNew = users.get(user.getId());
        if (!isEmailUnique(user.getEmail()) && !userNew.getEmail().equals(user.getEmail()))
            throw new IllegalArgumentException();
        if (user.getEmail() != null && !user.getEmail().isEmpty()) userNew.setEmail(user.getEmail());
        if (user.getName() != null && !user.getName().isBlank()) userNew.setName(user.getName());

        return userNew;
    }

    @Override
    public Map<Long, User> getAllUsersMap() {
        return new HashMap<>(users);
    }

    @Override
    public void deleteUser(long id) {
        boolean delete = users.containsKey(id);
        if (!delete) throw new NotFoundException("user not found");
        users.remove(id);
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
