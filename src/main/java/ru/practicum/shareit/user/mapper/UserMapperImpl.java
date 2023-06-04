package ru.practicum.shareit.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User userDtoToUser(UserDto userDto) {
        return new User(userDto.getId(), userDto.getName(), userDto.getEmail());
    }

    @Override
    public UserDto userToUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }
}
