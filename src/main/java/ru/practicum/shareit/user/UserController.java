package ru.practicum.shareit.user;

import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.item.Marker;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    private final UserService userService;
    private final Validator validator;

    public UserController(UserService userService, Validator validator) {
        this.userService = userService;
        this.validator = validator;
    }

    @PostMapping
    @Validated({Marker.OnCreate.class})
    public ResponseEntity<?> createUser(@RequestBody UserDto userDTO) {
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDTO, Marker.OnCreate.class);
        if (!violations.isEmpty()) {
            throw new ValidationException();
        }
        try {
            UserDto createdUser = userService.createUser(userMapper.userDtoToUser(userDTO));
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") long id) {
        UserDto user = userService.getUserById(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            throw new NotFoundException("Объект не найден");
        }
    }

    @PatchMapping("/{id}")
    @Validated({Marker.OnUpdate.class})
    public ResponseEntity<?> updateUser(@PathVariable("id") long id, @RequestBody UserDto userDto) {
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto, Marker.OnUpdate.class);
        if (!violations.isEmpty()) {
            throw new ValidationException();
        }
        userDto.setId(id);
        try {
            UserDto createdUser = userService.updateUser(userMapper.userDtoToUser(userDto));
            return new ResponseEntity<>(createdUser, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);

        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new NotFoundException("user not found");
        }
    }
}
