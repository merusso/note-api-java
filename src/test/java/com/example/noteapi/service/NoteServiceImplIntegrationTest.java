package com.example.noteapi.service;

import com.example.noteapi.api.Note;
import com.example.noteapi.api.NoteSearchRequest;
import com.example.noteapi.api.PageResponse;
import com.example.noteapi.data.NoteRepository;
import com.example.noteapi.data.User;
import com.example.noteapi.data.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.*;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Instant;
import java.util.List;

@DataMongoTest
@ActiveProfiles("live")
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NoteServiceImplIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:5"));

    @BeforeAll
    static void beforeAll() {
        mongoDBContainer.start();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.host", mongoDBContainer::getHost);
        registry.add("spring.data.mongodb.port", mongoDBContainer::getFirstMappedPort);
    }

    static String createdNoteId;
    static String createdUserId;

    @Autowired
    NoteRepository noteRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MongoTemplate template;

    ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();

    NoteServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new NoteServiceImpl(noteRepository, new NoteConverter(), userRepository, template, objectMapper);
    }

    @Test
    @Order(1)
    void create() {
        User dataUser = new User();
        createdUserId = userRepository.save(dataUser).id;

        Note note = new Note();
        note.setUserId(createdUserId);
        note.setTitle("Title");
        note.setContent("Content");
        note.setLabels(List.of("favorites"));

        note = service.create(note);
        createdNoteId = note.getId();

        assertThat(note.getId()).isNotBlank();
        assertThat(note.getTitle()).isEqualTo("Title");
        assertThat(note.getContent()).isEqualTo("Content");
        assertThat(note.getCreatedDate()).isBeforeOrEqualTo(Instant.now());
        assertThat(note.getCreatedDate()).isEqualTo(note.getUpdatedDate());
        assertThat(note.getLabels()).containsExactly("favorites");
    }

    @Test
    @Order(2)
    void get() {
        Note note = service.get(createdNoteId);

        assertThat(note.getId()).isEqualTo(createdNoteId);
        assertThat(note.getTitle()).isEqualTo("Title");
    }

    @Test
    @Order(3)
    void search() {
        NoteSearchRequest request = new NoteSearchRequest();
        request.setUserId(createdUserId);
        request.setTitle("itl"); // contains match for "Title"
        request.setLabel("favorites");

        PageResponse<Note> page = service.search(request);

        assertThat(page.getPageSize()).isEqualTo(20);
        assertThat(page.getPageToken()).isEqualTo("0");
        assertThat(page.getNextPageToken()).isNull();
        assertThat(page.getItems()).hasSize(1);
        Note note = page.getItems().iterator().next();
        assertThat(note.getId()).isEqualTo(createdNoteId);
        assertThat(note.getUserId()).isEqualTo(createdUserId);
    }

    @Test
    @Order(4)
    void update() {
        Note note = service.get(createdNoteId);
        note.setLabels(List.of("test"));

        note = service.update(note);

        assertThat(note.getLabels()).containsExactly("test");
    }

    @Test
    @Order(5)
    void patch() {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("title", "2022 New York");

        Note note = service.patch(createdNoteId, objectNode);

        assertThat(note.getTitle()).isEqualTo("2022 New York");
    }

    @Test
    @Order(Order.DEFAULT) // should run last
    void delete() {
        service.delete(createdNoteId);

        assertThat(noteRepository.existsById(createdNoteId)).isFalse();
    }
}
