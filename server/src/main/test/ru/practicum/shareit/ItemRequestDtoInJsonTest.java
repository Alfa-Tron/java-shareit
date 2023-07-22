package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JsonTest
public class ItemRequestDtoInJsonTest {

    @Autowired
    private JacksonTester<ItemRequestDtoIn> json;

    @Test
    void testSerializeItemRequestDtoIn() throws Exception {
        ItemRequestDtoIn dtoIn = new ItemRequestDtoIn();
        dtoIn.setDescription("Test Description");

        String jsonResult = json.write(dtoIn).getJson();

        assertThat(jsonResult).isEqualTo("{\"description\":\"Test Description\"}");
    }

    @Test
    void testDeserializeItemRequestDtoIn() throws Exception {
        String jsonContent = "{\"description\":\"Test Description\"}";

        ItemRequestDtoIn dtoIn = json.parse(jsonContent).getObject();

        assertThat(dtoIn.getDescription()).isEqualTo("Test Description");
    }

}