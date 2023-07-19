package ru.practicum.shareit.user.mapper;

//import org.mapstruct.Mapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

//@Mapper
public interface UserMapper {
    User userDtoToUser(UserDto userDto);

    UserDto userToUserDto(User user);

    List<UserDto> listToDtoList(List<User> list);
}
