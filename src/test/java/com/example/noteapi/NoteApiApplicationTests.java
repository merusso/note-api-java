package com.example.noteapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.*;

@SpringBootTest
@EnableAutoConfiguration(exclude = {
    MongoAutoConfiguration.class,
    MongoDataAutoConfiguration.class,
    MongoRepositoriesAutoConfiguration.class,
})
@ActiveProfiles("fake")
class NoteApiApplicationTests {

    /**
     * Tests whether Spring context loads successfully.
     * Load failure will throw an exception, failing test.
     */
	@Test
	void contextLoads() {
	}

}
