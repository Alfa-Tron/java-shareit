package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {


    private final RepositoryUser repositoryUserMemory;

    public UserServiceImpl(RepositoryUser repositoryUserMemory) {
        this.repositoryUserMemory = repositoryUserMemory;
    }


    public User createUser(User user) {
        return repositoryUserMemory.createUser(user);
    }

    @Override
    public List<User> getAllUsers() {
        return repositoryUserMemory.getAllUsers();
    }

    public Map<Long, User> getAllUsersMap() {
        return repositoryUserMemory.getAllUsersMap();
    }

    @Override
    public User getUserById(long id) {
        return repositoryUserMemory.getUserById(id);
    }

    @Override
    public User updateUser(User user) {

        return repositoryUserMemory.updateUser(user);
    }

    @Override
    public void deleteUser(long id) {
         repositoryUserMemory.deleteUser(id);
    }


}
