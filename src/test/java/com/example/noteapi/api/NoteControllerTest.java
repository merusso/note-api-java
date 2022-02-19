package com.example.noteapi.api;

import com.example.noteapi.service.NoteNotFoundException;
import com.example.noteapi.service.NoteService;
import static org.hamcrest.Matchers.*;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@WebMvcTest
class NoteControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    NoteService noteService;

    @Test
    void createNote() throws Exception {
        when(noteService.create(any())).thenReturn(note());

        String requestJson = """
            {
                "userId": 2,
                "title": "Title",
                "content": "Q29udGVudA==",
                "labels": ["label-1", "label-2"]
            }""";
        String responseJson = """
            {
                "id": 1,
                "userId": 2,
                "title": "Title",
                "content": "Q29udGVudA==",
                "createdDate": "2022-02-18T18:00:00Z",
                "updatedDate": "2022-02-18T18:00:00Z",
                "labels": ["label-1", "label-2"]
            }""";
        mockMvc.perform(post("/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().json(responseJson));
    }

    @Test
    void getNote() throws Exception {
        when(noteService.get(1)).thenReturn(note());

        String json = """
            {
                "id": 1,
                "userId": 2,
                "title": "Title",
                "content": "Q29udGVudA==",
                "createdDate": "2022-02-18T18:00:00Z",
                "updatedDate": "2022-02-18T18:00:00Z",
                "labels": ["label-1", "label-2"]
            }""";
        mockMvc.perform(get("/notes/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().json(json));
    }

    @Test
    void getNote_notFound() throws Exception {
        when(noteService.get(1)).thenThrow(new NoteNotFoundException(1));

        String json = """
            {
                "type": "NoteNotFoundException",
                "status": 404,
                "detail": "Note with ID 1 not found"
            }""";
        mockMvc.perform(get("/notes/1"))
            .andExpect(status().isNotFound())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().json(json))
            .andExpect(jsonPath("instance", instanceOf(String.class)));
    }

    @Test
    void updateNote() throws Exception {
        Note note = note();
        when(noteService.update(note)).thenReturn(note);

        String json = """
            {
                "id": 1,
                "userId": 2,
                "title": "Title",
                "content": "Q29udGVudA==",
                "createdDate": "2022-02-18T18:00:00Z",
                "updatedDate": "2022-02-18T18:00:00Z",
                "labels": ["label-1", "label-2"]
            }""";
        mockMvc.perform(put("/notes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().json(json));
    }

    @Test
    void deleteNote() throws Exception {
        mockMvc.perform(delete("/notes/1"))
            .andExpect(status().isNoContent());
    }

    private Note note() {
        Instant date = ZonedDateTime.of(
                LocalDate.of(2022, 2, 18),
                LocalTime.NOON,
                ZoneId.of("US/Central"))
            .toInstant();
        Note note = new Note();
        note.setId(1L);
        note.setUserId(2L);
        note.setTitle("Title");
        note.setContent("Content".getBytes(StandardCharsets.UTF_8)); // Q29udGVudA==
        note.setCreatedDate(date);
        note.setUpdatedDate(date);
        note.setLabels(List.of("label-1", "label-2"));
        return note;
    }
}
