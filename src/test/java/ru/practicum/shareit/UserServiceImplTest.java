package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.jpa.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
@Transactional

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testCreateUser() {

        User user = new User();

        when(userRepository.save(user)).thenReturn(user);

        User createdUser = userService.createUser(user);

        Assertions.assertNotNull(createdUser);
    }

    @Test
    public void testGetAllUsers() {
        List<User> expectedUsers = List.of(new User(), new User());

        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<User> users = userService.getAllUsers();

        Assertions.assertEquals(expectedUsers.size(), users.size());
    }

    @Test
    public void testGetUserById_UserExists() {
        long userId = 1L;
        User expectedUser = new User();
        expectedUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        User user = userService.getUserById(userId);

        assertNotNull(user);
        assertEquals(expectedUser.getId(), user.getId());
    }

    @Test
    public void testGetUserById_UserNotFound() {
        long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> userService.getUserById(userId));

        Assertions.assertEquals(exception.getMessage(),"Пользователь не найден");
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");

        User existingUser = new User();
        existingUser.setId(1L);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        User updatedUser = userService.updateUser(user);

        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals(user.getId(), updatedUser.getId());
        Assertions.assertEquals(user.getName(), updatedUser.getName());
        Assertions.assertEquals(user.getEmail(), updatedUser.getEmail());
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> userService.updateUser(user));

        Assertions.assertEquals(exception.getMessage(),"User not found");

    }

    @Test
    public void testDeleteUser() {
        long userId = 1L;

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }
}
