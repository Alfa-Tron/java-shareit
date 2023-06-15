package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.user.jpa.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository repositoryUser;
    @Transactional
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
        User currentUser = repositoryUser.findById(user.getId()).orElseThrow(() -> new NotFoundException("User not found"));

        if (user.getName() != null && !user.getName().isBlank()) {
            currentUser.setName(user.getName());
        }

        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            currentUser.setEmail(user.getEmail());
        }
        return currentUser;

    }

    @Override
    @Transactional
    public void deleteUser(long id) {
        repositoryUser.deleteById(id);
    }
}
