package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {


    private final RepositoryUser repository;

    public UserServiceImpl(RepositoryUser repository) {
        this.repository = repository;
    }


    public UserDto createUser(User userDto) {
        return repository.createUser(userDto);
    }

    @Override
    public List<User> getAllUsers() {
        return repository.getAllUsers();
    }

    public Map<Long, User> getAllUsersMap() {
        return repository.getAllUsersMap();
    }

    @Override
    public UserDto getUserById(long id) {
        return repository.getUserById(id);
    }

    @Override
    public UserDto updateUser(User userDto) {

        return repository.updateUser(userDto);
    }

    @Override
    public boolean deleteUser(long id) {

        return repository.deleteUser(id);
    }


}
