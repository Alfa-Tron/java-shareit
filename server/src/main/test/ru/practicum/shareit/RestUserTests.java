package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Import(UserController.class)
@Transactional

public class RestUserTests {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    @Test
    void saveNewUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(0L);
        userDto.setName("qe");
        userDto.setEmail("qwe.w@example.com");
        User user = new User();
        user.setName("qe");
        user.setEmail("qwe.w@example.com");

        when(userService.createUser(any())).thenReturn(user);
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    void getAllUsers() throws Exception {

        User user1 = new User();
        user1.setId(1L);
        user1.setName("John");
        user1.setEmail("john@example.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Jane");
        user2.setEmail("jane@example.com");

        UserDto userDto1 = new UserDto();
        userDto1.setId(1L);
        userDto1.setName("John");
        userDto1.setEmail("john@example.com");

        UserDto userDto2 = new UserDto();
        userDto2.setId(2L);
        userDto2.setName("Jane");
        userDto2.setEmail("jane@example.com");

        List<User> users = Arrays.asList(user1, user2);
        List<UserDto> userDtos = Arrays.asList(userDto1, userDto2);

        when(userService.getAllUsers()).thenReturn(users);
        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(userDtos.size()))
                .andExpect(jsonPath("$[0].id").value(userDto1.getId()))
                .andExpect(jsonPath("$[0].name").value(userDto1.getName()))
                .andExpect(jsonPath("$[0].email").value(userDto1.getEmail()))
                .andExpect(jsonPath("$[1].id").value(userDto2.getId()))
                .andExpect(jsonPath("$[1].name").value(userDto2.getName()))
                .andExpect(jsonPath("$[1].email").value(userDto2.getEmail()));
    }

    @Test
    void getUserById() throws Exception {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("John");
        user1.setEmail("john@example.com");
        when(userService.getUserById(user1.getId())).thenReturn(user1);
        mvc.perform(get("/users/{id}", user1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).getUserById(user1.getId());
    }

    @Test
    void updateUser() throws Exception {
        User user2 = new User();
        user2.setId(2L);
        user2.setName("Jane");
        user2.setEmail("jane@example.com");

        UserDto userDto = new UserDto();
        userDto.setId(2L);
        userDto.setName("Jane");
        userDto.setEmail("jane@example.com");


        when(userService.updateUser(any(User.class))).thenReturn(user2);

        mvc.perform(patch("/users/{id}", 2L)
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()));

        verify(userService, times(1)).updateUser(any(User.class));
    }

    @Test
    void deleteUser() throws Exception {
        long id = 1L;

        doNothing().when(userService).deleteUser(id);

        mvc.perform(delete("/users/{id}", id))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUser(id);
    }

}
