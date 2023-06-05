package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {


    private final RepositoryUser repository;

    public UserServiceImpl(RepositoryUser repository) {
        this.repository = repository;
    }


    public User createUser(User user) {
        return repository.createUser(user);
    }

    @Override
    public List<User> getAllUsers() {
        return repository.getAllUsers();
    }

    public Map<Long, User> getAllUsersMap() {
        return repository.getAllUsersMap();
    }

    @Override
    public User getUserById(long id) {
        return repository.getUserById(id);
    }

    @Override
    public User updateUser(User user) {

        return repository.updateUser(user);
    }

    @Override
    public boolean deleteUser(long id) {

        return repository.deleteUser(id);
    }


}
