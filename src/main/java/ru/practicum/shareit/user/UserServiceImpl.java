package ru.practicum.shareit.user;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    private Map<Long, User> users = new HashMap<>();
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    private long id = 1;


    public UserDto createUser(UserDto userDto) {
        if (userDto.getEmail() == null || userDto.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        User user = userMapper.userDtoToUser(userDto);
        if (isEmailUnique(user.getEmail())) {
            long userId = id++;
            user.setId(userId);
            users.put(userId, user);
            return userMapper.userToUserDto(user);
        } else {
            throw new IllegalArgumentException("User with the same email already exists");
        }

    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> usersDto = new ArrayList<>();
        for (User user : users.values()) {
            usersDto.add(userMapper.userToUserDto(user));
        }
        return usersDto;
    }

    public Map<Long, User> getAllUsersMap() {
        return new HashMap<>(users);
    }

    @Override
    public UserDto getUserById(long id) {
        return userMapper.userToUserDto(users.get(id));
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        if (!isEmailUnique(userDto.getEmail()) && !users.get(userDto.getId()).getEmail().equals(userDto.getEmail()))
            throw new IllegalArgumentException();
        User user = users.get(userDto.getId());
        if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());
        if (userDto.getName() != null) user.setName(userDto.getName());
        users.put(user.getId(), user);
        return userMapper.userToUserDto(user);
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
