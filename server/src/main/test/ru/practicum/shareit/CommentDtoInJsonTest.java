package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.comment.CommentDtoIn;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JsonTest
public class CommentDtoInJsonTest {

    @Autowired
    private JacksonTester<CommentDtoIn> json;

    @Test
    void testSerializeCommentDtoIn() throws Exception {
        CommentDtoIn dtoIn = new CommentDtoIn();
        dtoIn.setText("Test Text");

        String jsonResult = json.write(dtoIn).getJson();

        assertThat(jsonResult).isEqualTo("{\"text\":\"Test Text\"}");
    }

    @Test
    void testDeserializeCommentDtoIn() throws Exception {
        String jsonContent = "{\"text\":\"Test Text\"}";

        CommentDtoIn dtoIn = json.parse(jsonContent).getObject();

        assertThat(dtoIn.getText()).isEqualTo("Test Text");
    }

}
