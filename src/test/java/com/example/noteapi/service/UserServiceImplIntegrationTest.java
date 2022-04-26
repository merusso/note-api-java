package com.example.noteapi.service;

import com.example.noteapi.api.User;
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
class UserServiceImplIntegrationTest {

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

    static String createdUserId;

    @Autowired
    UserRepository repository;

    ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();

    UserServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new UserServiceImpl(repository, new UserConverter(), objectMapper);
    }

    @Test
    @Order(1)
    void create() {
        User user = new User();
        user.setName("mrusso");
        user.setNoteLabels(List.of("favorites"));

        user = service.create(user);
        createdUserId = user.getId();

        assertThat(user.getName()).isEqualTo("mrusso");
        assertThat(user.getJoinDate()).isBeforeOrEqualTo(Instant.now());
        assertThat(user.getId()).isNotBlank();
        assertThat(user.getNoteLabels()).containsExactly("favorites");
    }

    @Test
    @Order(2)
    void get() {
        String id = createdUserId;
        User user = service.get(id);

        assertThat(user.getId()).isEqualTo(id);
        assertThat(user.getName()).isEqualTo("mrusso");
    }

    @Test
    @Order(3)
    void update() {
        User user = service.get(createdUserId);
        user.setNoteLabels(List.of("test"));

        user = service.update(user);

        assertThat(user.getNoteLabels()).isEqualTo(List.of("test"));
    }

    @Test
    @Order(3)
    void patch() {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("name", "vsullivan");

        User user = service.patch(createdUserId, objectNode);

        assertThat(user.getName()).isEqualTo("vsullivan");
    }

    @Test
    @Order(value = Order.DEFAULT) // should run last
    void delete() {
        service.delete(createdUserId);

        assertThat(repository.existsById(createdUserId)).isFalse();
    }
}
