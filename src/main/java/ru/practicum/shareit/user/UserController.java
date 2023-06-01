package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.regex.Pattern;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDto userDTO) {
        String email = userDTO.getEmail();
        if (email == null || email.isEmpty()) {
            return new ResponseEntity<>("Email is required", HttpStatus.BAD_REQUEST);
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return new ResponseEntity<>("Invalid email format", HttpStatus.BAD_REQUEST);
        }
        try {
            UserDto createdUser = userService.createUser(userDTO);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("User with the same email already exists")) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
            } else {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") long id) {
        UserDto user = userService.getUserById(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") long id, @RequestBody UserDto userDto) {
        if (userDto.getId() != 0) return new ResponseEntity<>("id in body", HttpStatus.BAD_REQUEST);

        String email = userDto.getEmail();
        userDto.setId(id);
        if (email != null) {
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                return new ResponseEntity<>("Invalid email format", HttpStatus.BAD_REQUEST);
            }
        }
        try {
            UserDto createdUser = userService.updateUser(userDto);
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
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
