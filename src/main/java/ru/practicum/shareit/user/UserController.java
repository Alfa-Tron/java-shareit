package ru.practicum.shareit.user;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.Marker;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.List;


@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserMapper userMapper;

    private final UserService userService;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public UserDto createUser(@Validated({Marker.OnCreate.class}) @RequestBody UserDto userDTO) {
        return userMapper.userToUserDto(userService.createUser(userMapper.userDtoToUser(userDTO)));
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userMapper.listToDtoList(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable("id") long id) {
        return userMapper.userToUserDto(userService.getUserById(id));
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable("id") long id, @Validated({Marker.OnUpdate.class}) @RequestBody UserDto userDto) {
        userDto.setId(id);
        User createdUser = userService.updateUser(userMapper.userDtoToUser(userDto));
        return userMapper.userToUserDto(createdUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
    }
}
