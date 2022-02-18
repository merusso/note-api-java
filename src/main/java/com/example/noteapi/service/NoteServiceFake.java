package com.example.noteapi.service;

import com.example.noteapi.api.Note;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class NoteServiceFake implements NoteService {

    private AtomicLong idGenerator = new AtomicLong();
    private HashMap<Long, Note> notes = new HashMap<>();

    {
        Note note = new Note();
        note.setId(idGenerator.incrementAndGet());
        note.setUserId(1L);
        note.setTitle("Title");
        note.setContent("Content".getBytes(StandardCharsets.UTF_8));
        note.setCreatedDate(Instant.now());
        note.setUpdatedDate(Instant.now());
        note.setLabels(List.of("label-1", "label-2"));
        notes.put(note.getId(), note);
    }

    @Override
    public Note create(Note note) {
        note.setId(idGenerator.incrementAndGet());
        notes.put(note.getId(), note);
        return note;
    }

    @Override
    public Note get(long id) {
        if (!notes.containsKey(id)) {
            throw new NoteNotFoundException(id);
        }
        return notes.get(id);
    }

    @Override
    public Note update(Note note) {
        Long id = note.getId();
        if (!notes.containsKey(id)) {
            throw new NoteNotFoundException(id);
        }
        notes.put(id, note);
        return note;
    }

    @Override
    public void delete(long id) {
        if (!notes.containsKey(id)) {
            throw new NoteNotFoundException(id);
        }
        notes.remove(id);
    }
}
