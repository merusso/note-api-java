package com.example.noteapi.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NoteRepository extends MongoRepository<Note, String> {
    Page<Note> findByUserId(String userId, Pageable pageable);

    // Note: doesn't work well with null values
    Page<Note> findByUserIdAndTitleContains(String userId, String title, Pageable pageable);
}
