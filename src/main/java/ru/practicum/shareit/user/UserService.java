package ru.practicum.shareit.user;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Service
public interface UserService {
    UserDto createUser(UserDto userDto);

    List<UserDto> getAllUsers();

    UserDto getUserById(long id);

    UserDto updateUser(UserDto userDto);

    boolean deleteUser(long id);
}
