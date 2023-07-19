package shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shareit.user.dto.Marker;
import shareit.user.dto.UserDto;


@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("getIAllUsers");
        return  userClient.getAllUsers();
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@Validated({Marker.OnCreate.class}) @RequestBody UserDto userDTO) {
        log.info("createUser userDto: {}", userDTO);
        return userClient.createUser(userDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable("id") long id) {
        log.info("getUserById Id: {}", id);
        return userClient.getUserById(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable("id") long id, @Validated({Marker.OnUpdate.class}) @RequestBody UserDto userDto) {
        log.info("updateUser Id: {}, userDTo: {}", id, userDto);
        return userClient.updateUser(id,userDto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") long id) {
        log.info("deleteUser id: {},userId,from,size", id);
        return userClient.deleteUser(id);
    }

}
