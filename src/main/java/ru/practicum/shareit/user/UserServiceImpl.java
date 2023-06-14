package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.user.jpa.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repositoryUser;

    @Autowired
    public UserServiceImpl(UserRepository repositoryUser) {
        this.repositoryUser = repositoryUser;
    }

    public User createUser(User user) {
        return repositoryUser.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return repositoryUser.findAll();
    }

    @Override
    public User getUserById(long id) {
        return repositoryUser.findById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        Optional<User> existingUser = repositoryUser.findById(user.getId());
        User currentUser;
        if (existingUser.isPresent()) {
            currentUser = existingUser.get();
        } else {
            throw new NotFoundException("User not found");
        }
        if (user.getName() != null && !user.getName().isBlank()) {
            currentUser.setName(user.getName());
        }

        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            currentUser.setEmail(user.getEmail());
        }
        return repositoryUser.save(currentUser);

    }

    @Override
    public void deleteUser(long id) {
        repositoryUser.deleteById(id);
    }
}
