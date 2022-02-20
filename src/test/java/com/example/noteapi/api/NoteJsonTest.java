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

    /**
     * These tests verify custom serialization for Note.content property.
     * The API interface expects a base64-encoded string, but api.Note holds
     * the raw String value.
     */
    @Nested
    class ContentTest {
        @Test
        void deserialize() throws Exception {
            var json = """
            {
                "content": "VGhpcyBpcyBhIHRlc3Q="
            }
            """;
            assertThat(tester.parseObject(json))
                .hasFieldOrPropertyWithValue("content", "This is a test");
        }

        @Test
        void deserialize_multiLine() throws Exception {
            var json = """
            {
                "content": "TXVsdGktbGluZSBzdHJpbmcgdGVzdDoKbGluZTEKbGluZTIKbGluZTMK"
            }
            """;
            var expectedString = """
            Multi-line string test:
            line1
            line2
            line3
            """;
            assertThat(tester.parseObject(json))
                .hasFieldOrPropertyWithValue("content", expectedString);
        }

        @Test
        void serialize() throws Exception {
            Note note = new Note();
            note.setContent("This is a test");
            assertThat(tester.write(note))
                .extractingJsonPathStringValue("content")
                .isEqualTo("VGhpcyBpcyBhIHRlc3Q=");
        }
    }
}
