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

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@WebMvcTest(controllers = NoteController.class)
class NoteControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    NoteService noteService;

    @Test
    void searchNote() throws Exception {
        NoteSearchRequest request = new NoteSearchRequest();
        request.setUserId("621a80c50f239c6d37c6313b");
        request.setTitle("2022 Hawaii");
        request.setLabel("favorites");
        PageResponse<Note> notePage = new PageResponse<>(List.of(note()), 20, "1", "2");
        when(noteService.search(request)).thenReturn(notePage);

        String responseJson = """
            {
                "items": [{
                    "id": "621a80c50f239c6d37c6313b",
                    "userId": "621e25d546ca105d43d1d073",
                    "title": "2022 Hawaii",
                    "content": "Content",
                    "createdDate": "2022-02-18T18:00:00Z",
                    "updatedDate": "2022-02-18T18:00:00Z",
                    "labels": ["favorites", "vacations"]
                }],
                "pageSize": 20,
                "pageToken": "1",
                "nextPageToken": "2"
            }
            """;
        mockMvc.perform(get("/notes")
                .queryParam("userId", "621a80c50f239c6d37c6313b")
                .queryParam("title", "2022 Hawaii")
                .queryParam("label", "favorites"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().json(responseJson));
    }

    @Test
    void createNote() throws Exception {
        when(noteService.create(any())).thenReturn(note());

        String requestJson = """
            {
                "userId": "621e25d546ca105d43d1d073",
                "title": "2022 Hawaii",
                "content": "Content",
                "labels": ["favorites", "vacations"]
            }""";
        String responseJson = """
            {
                "id": "621a80c50f239c6d37c6313b",
                "userId": "621e25d546ca105d43d1d073",
                "title": "2022 Hawaii",
                "content": "Content",
                "createdDate": "2022-02-18T18:00:00Z",
                "updatedDate": "2022-02-18T18:00:00Z",
                "labels": ["favorites", "vacations"]
            }""";
        mockMvc.perform(post("/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().json(responseJson));
    }

    @Test
    void createNote_invalidRequest() throws Exception {
        String requestJson = """
            {
                "title": "2022 Hawaii",
                "labels": ["label-1", "label-2"]
            }""";
        String responseJson = """
            {
                "type": "MethodArgumentNotValidException",
                "status": 400,
                "detail": "Validation errors: [ValidationErrorItem(field=content, message=must not be empty), ValidationErrorItem(field=userId, message=must not be null)]",
                "validationErrors": [{
                    "field": "content",
                    "message": "must not be empty"
                }, {
                    "field": "userId",
                    "message": "must not be null"
                }]
            }""";
        mockMvc.perform(post("/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().json(responseJson))
            .andExpect(jsonPath("instance", instanceOf(String.class)));
    }

    @Test
    void getNote() throws Exception {
        when(noteService.get("621a80c50f239c6d37c6313b")).thenReturn(note());

        String json = """
            {
                "id": "621a80c50f239c6d37c6313b",
                "userId": "621e25d546ca105d43d1d073",
                "title": "2022 Hawaii",
                "content": "Content",
                "createdDate": "2022-02-18T18:00:00Z",
                "updatedDate": "2022-02-18T18:00:00Z",
                "labels": ["favorites", "vacations"]
            }""";
        mockMvc.perform(get("/notes/621a80c50f239c6d37c6313b"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().json(json));
    }

    @Test
    void getNote_notFound() throws Exception {
        when(noteService.get("1")).thenThrow(new NoteNotFoundException("1"));

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
                "id": "621a80c50f239c6d37c6313b",
                "userId": "621e25d546ca105d43d1d073",
                "title": "2022 Hawaii",
                "content": "Content",
                "createdDate": "2022-02-18T18:00:00Z",
                "updatedDate": "2022-02-18T18:00:00Z",
                "labels": ["favorites", "vacations"]
            }""";
        mockMvc.perform(put("/notes/621a80c50f239c6d37c6313b")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().json(json));
    }

    @Test
    void patchNote() throws Exception {
        Note note = note();
        note.setTitle("2022 New York");
        note.setLabels(List.of());
        note.setContent(null);
        when(noteService.patch(eq("621a80c50f239c6d37c6313b"), any())).thenReturn(note);

        String requestJson = """
            {
                "title": "2022 New York",
                "labels": [],
                "content": null
            }""";
        String responseJson = """
            {
                "id": "621a80c50f239c6d37c6313b",
                "userId": "621e25d546ca105d43d1d073",
                "title": "2022 New York",
                "content": null,
                "createdDate": "2022-02-18T18:00:00Z",
                "updatedDate": "2022-02-18T18:00:00Z",
                "labels": []
            }""";
        mockMvc.perform(patch("/notes/621a80c50f239c6d37c6313b")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().json(responseJson));
    }

    @Test
    void deleteNote() throws Exception {
        mockMvc.perform(delete("/notes/621a80c50f239c6d37c6313b"))
            .andExpect(status().isNoContent());
        verify(noteService).delete("621a80c50f239c6d37c6313b");
    }

    private Note note() {
        Instant date = ZonedDateTime.of(
                LocalDate.of(2022, 2, 18),
                LocalTime.NOON,
                ZoneId.of("US/Central"))
            .toInstant();
        Note note = new Note();
        note.setId("621a80c50f239c6d37c6313b");
        note.setUserId("621e25d546ca105d43d1d073");
        note.setTitle("2022 Hawaii");
        note.setContent("Content");
        note.setCreatedDate(date);
        note.setUpdatedDate(date);
        note.setLabels(List.of("favorites", "vacations"));
        return note;
    }
}
