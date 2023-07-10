package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.user.dto.UserDto;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JsonTest
public class UserDtoJsonTest {

    @Autowired
    private JacksonTester<UserDto> json;

    @Test
    void testSerializeUserDto() throws Exception {
        UserDto userDto = new UserDto(1L, "John Doe", "john@example.com");

        String jsonResult = json.write(userDto).getJson();

        assertThat(jsonResult).isEqualTo("{\"id\":1,\"name\":\"John Doe\",\"email\":\"john@example.com\"}");
    }

    @Test
    void testDeserializeUserDto() throws Exception {
        String jsonContent = "{\"id\":1,\"name\":\"John Doe\",\"email\":\"john@example.com\"}";

        UserDto userDto = json.parse(jsonContent).getObject();

        assertThat(userDto.getId()).isEqualTo(1L);
        assertThat(userDto.getName()).isEqualTo("John Doe");
        assertThat(userDto.getEmail()).isEqualTo("john@example.com");
    }
}