package com.example.noteapi.service;

import com.example.noteapi.api.Note;
import com.example.noteapi.data.NoteRepository;
import com.example.noteapi.data.User;
import com.example.noteapi.data.UserRepository;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.*;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Instant;
import java.util.List;

@DataMongoTest
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

    @Autowired
    NoteRepository noteRepository;

    @Autowired
    UserRepository userRepository;

    NoteServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new NoteServiceImpl(noteRepository, new NoteConverter(), userRepository);
    }

    @Test
    @Order(1)
    void create() {
        User dataUser = new User();
        String userId = userRepository.save(dataUser).id;

        Note note = new Note();
        note.setUserId(userId);
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
    void update() {
        Note note = service.get(createdNoteId);
        note.setLabels(List.of("test"));

        note = service.update(note);

        assertThat(note.getLabels()).containsExactly("test");
    }

    @Test
    @Order(4)
    void delete() {
        service.delete(createdNoteId);

        assertThat(noteRepository.existsById(createdNoteId)).isFalse();
    }
}
