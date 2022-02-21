package com.example.noteapi.api;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

@JsonTest
class NoteJsonTest {

    @Autowired
    JacksonTester<Note> tester;

    @Nested
    class ContentTest {
        @Test
        void deserialize_multiLine() throws Exception {
            var expectedString = """
            # Header
            
            * list item 1
            * list item 2
            """;
            assertThat(tester.read("/NoteJsonTest/content-multiline.json"))
                .hasFieldOrPropertyWithValue("content", expectedString);
        }

        @Test
        void serialize_multiLine() throws Exception {
            Note note = new Note();
            var content = """
            # Header
            
            * list item 1
            * list item 2
            """;
            note.setContent(content);
            assertThat(tester.write(note))
                .isEqualToJson("/NoteJsonTest/content-multiline.json");
        }
    }
}
