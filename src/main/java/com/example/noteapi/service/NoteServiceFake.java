package com.example.noteapi.service;

import com.example.noteapi.api.Note;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Profile("fake")
public class NoteServiceFake implements NoteService {

    private AtomicLong idGenerator = new AtomicLong();
    private HashMap<String, Note> notes = new HashMap<>();

    {
        Note note = new Note();
        note.setId(Objects.toString(idGenerator.incrementAndGet()));
        note.setUserId("1");
        note.setTitle("Title");
        note.setContent("Content");
        note.setCreatedDate(Instant.now());
        note.setUpdatedDate(Instant.now());
        note.setLabels(List.of("label-1", "label-2"));
        notes.put(note.getId(), note);
    }

    @Override
    public Note create(Note note) {
        note.setId(Objects.toString(idGenerator.incrementAndGet()));
        notes.put(note.getId(), note);
        return note;
    }

    @Override
    public Note get(String id) {
        if (!notes.containsKey(id)) {
            throw new NoteNotFoundException(id);
        }
        return notes.get(id);
    }

    @Override
    public Note update(Note note) {
        String id = note.getId();
        if (!notes.containsKey(id)) {
            throw new NoteNotFoundException(id);
        }
        notes.put(id, note);
        return note;
    }

    @Override
    public void delete(String id) {
        if (!notes.containsKey(id)) {
            throw new NoteNotFoundException(id);
        }
        notes.remove(id);
    }
}
