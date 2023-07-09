package ru.practicum.shareit;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.dto.ItemDtoIn;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JsonTest
public class ItemDtoInJsonTest {

    @Autowired
    private JacksonTester<ItemDtoIn> json;

    @Test
    void testSerializeItemDtoIn() throws Exception {
        ItemDtoIn dtoIn = new ItemDtoIn();
        dtoIn.setName("Test Name");
        dtoIn.setDescription("Test Description");
        dtoIn.setAvailable(true);
        dtoIn.setRequestId(123);

        String jsonResult = json.write(dtoIn).getJson();

        assertThat(jsonResult).isEqualTo("{\"name\":\"Test Name\",\"description\":\"Test Description\",\"available\":true,\"requestId\":123}");
    }

    @Test
    void testDeserializeItemDtoIn() throws Exception {
        String jsonContent = "{\"name\":\"Test Name\",\"description\":\"Test Description\",\"available\":true,\"requestId\":123}";

        ItemDtoIn dtoIn = json.parse(jsonContent).getObject();

        assertThat(dtoIn.getName()).isEqualTo("Test Name");
        assertThat(dtoIn.getDescription()).isEqualTo("Test Description");
        assertThat(dtoIn.getAvailable()).isTrue();
        assertThat(dtoIn.getRequestId()).isEqualTo(123);
    }

}