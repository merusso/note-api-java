package com.example.noteapi.api;

import com.example.noteapi.service.UserNotFoundException;
import com.example.noteapi.service.UserService;
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

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Test
    void createUser() throws Exception {
        when(userService.create(any())).thenReturn(user());

        String requestJson = """
            {
                "name": "ndrake"
            }
            """;
        String responseJson = """
            {
                "id": "621e25d546ca105d43d1d073",
                "name": "ndrake",
                "joinDate": "2022-02-18T18:00:00Z"
            }
            """;
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().json(responseJson));
    }

    @Test
    void createUser_invalidRequest() throws Exception {
        when(userService.create(any())).thenReturn(user());

        String requestJson = "{}";
        String responseJson = """
            {
                "type": "MethodArgumentNotValidException",
                "status": 400,
                "detail": "Validation errors: [ValidationErrorItem(field=name, message=must not be empty)]",
                "validationErrors": [{
                    "field": "name",
                    "message": "must not be empty"
                }]
            }
            """;
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().json(responseJson))
            .andExpect(jsonPath("instance", instanceOf(String.class)));
    }

    @Test
    void getUser() throws Exception {
        when(userService.get("621e25d546ca105d43d1d073")).thenReturn(user());

        String json = """
            {
                "id": "621e25d546ca105d43d1d073",
                "name": "ndrake",
                "joinDate": "2022-02-18T18:00:00Z",
                "noteLabels": ["favorites", "vacations"]
            }""";
        mockMvc.perform(get("/users/621e25d546ca105d43d1d073"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().json(json));
    }

    @Test
    void getUser_notFound() throws Exception {
        when(userService.get("1")).thenThrow(new UserNotFoundException("1"));

        String json = """
            {
                "type": "UserNotFoundException",
                "status": 404,
                "detail": "User with ID 1 not found"
            }""";
        mockMvc.perform(get("/users/1"))
            .andExpect(status().isNotFound())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().json(json));
    }

    @Test
    void updateUser() throws Exception {
        when(userService.update(any())).thenReturn(user());

        String json = """
            {
                "id": "621e25d546ca105d43d1d073",
                "name": "ndrake",
                "joinDate": "2022-02-18T18:00:00Z",
                "noteLabels": ["favorites", "vacations"]
            }""";
        mockMvc.perform(put("/users/621e25d546ca105d43d1d073")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().json(json));
    }

    @Test
    void patchUser() throws Exception {
        User user = user();
        user.setName("vsullivan");
        when(userService.patch(eq("621e25d546ca105d43d1d073"), any())).thenReturn(user);

        String requestJson = """
            {
                "name": "vsullivan"
            }""";
        String responseJson = """
            {
                "id": "621e25d546ca105d43d1d073",
                "name": "vsullivan",
                "joinDate": "2022-02-18T18:00:00Z",
                "noteLabels": ["favorites", "vacations"]
            }""";
        mockMvc.perform(patch("/users/621e25d546ca105d43d1d073")
                .contentType(MediaType.APPLICATION_JSON)
                .content(responseJson))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().json(responseJson));
    }

    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/users/621e25d546ca105d43d1d073"))
            .andExpect(status().isOk());
        verify(userService).delete("621e25d546ca105d43d1d073");
    }

    private User user() {
        Instant date = ZonedDateTime.of(
                LocalDate.of(2022, 2, 18),
                LocalTime.NOON,
                ZoneId.of("US/Central"))
            .toInstant();
        User user = new User();
        user.setId("621e25d546ca105d43d1d073");
        user.setName("ndrake");
        user.setJoinDate(date);
        user.setNoteLabels(List.of("favorites", "vacations"));
        return user;
    }
}
