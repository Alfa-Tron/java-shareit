package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.Marker;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.List;


@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserMapper userMapper;

    private final UserService userService;

    @PostMapping
    public UserDto createUser(@Validated({Marker.OnCreate.class}) @RequestBody UserDto userDTO) {
        log.info("createUser userDto: {}", userDTO);
        return userMapper.userToUserDto(userService.createUser(userMapper.userDtoToUser(userDTO)));
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("getIAllUsers");
        return userMapper.listToDtoList(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable("id") long id) {
        log.info("getUserById Id: {}", id);
        return userMapper.userToUserDto(userService.getUserById(id));
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable("id") long id, @Validated({Marker.OnUpdate.class}) @RequestBody UserDto userDto) {
        userDto.setId(id);
        User createdUser = userService.updateUser(userMapper.userDtoToUser(userDto));
        log.info("updateUser Id: {}, userDto: {}", id, userDto);
        return userMapper.userToUserDto(createdUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") long id) {
        log.info("deleteUser id: {},userId,from,size", id);
        userService.deleteUser(id);
    }
}
