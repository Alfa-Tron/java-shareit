package ru.practicum.shareit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.dto.BookingDtoIn;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JsonTest
public class BookingDtoInJsonTest {

    @Autowired
    private JacksonTester<BookingDtoIn> json;

    @Test
    void testSerializeBookingDtoIn() throws Exception {
        Long itemId = 1L;
        LocalDateTime start = LocalDateTime.of(2023, 7, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2023, 7, 1, 12, 0);
        BookingDtoIn dtoIn = new BookingDtoIn(itemId, start, end);

        String jsonResult = json.write(dtoIn).getJson();

        assertThat(jsonResult).isEqualTo("{\"itemId\":1,\"start\":\"2023-07-01T10:00:00\",\"end\":\"2023-07-01T12:00:00\"}");
    }

    @Test
    void testDeserializeBookingDtoIn() throws Exception {
        // JSON представление объекта BookingDtoIn
        String jsonContent = "{\"itemId\":1,\"start\":\"2023-07-01T10:00:00\",\"end\":\"2023-07-01T12:00:00\"}";

        // Десериализуем JSON в объект BookingDtoIn
        BookingDtoIn dtoIn = json.parse(jsonContent).getObject();

        // Проверяем, что поля объекта соответствуют значениям из JSON
        assertThat(dtoIn.getItemId()).isEqualTo(1L);
        assertThat(dtoIn.getStart()).isEqualTo(LocalDateTime.of(2023, 7, 1, 10, 0));
        assertThat(dtoIn.getEnd()).isEqualTo(LocalDateTime.of(2023, 7, 1, 12, 0));
    }

}
