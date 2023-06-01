package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

@Mapper
public interface UserMapper {
    User userDtoToUser(UserDto userDto);

    UserDto userToUserDto(User user);
}
