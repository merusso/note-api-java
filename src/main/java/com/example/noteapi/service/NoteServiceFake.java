package com.example.noteapi.service;

import com.example.noteapi.api.Note;
import com.example.noteapi.api.NoteSearchRequest;
import com.example.noteapi.api.PageResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
        assertNoteExists(id);
        return notes.get(id);
    }

    @Override
    public Note update(Note note) {
        String id = note.getId();
        assertNoteExists(id);
        notes.put(id, note);
        return note;
    }

    @Override
    public void delete(String id) {
        assertNoteExists(id);
        notes.remove(id);
    }

    @Override
    public PageResponse<Note> search(NoteSearchRequest request) {
        List<Note> items = this.notes.values().stream()
            .filter(note -> Optional.ofNullable(request.getUserId())
                .filter(StringUtils::hasLength)
                .map(val -> val.equals(note.getUserId()))
                .orElse(true))
            .filter(note -> Optional.ofNullable(request.getTitle())
                .filter(StringUtils::hasLength)
                .map(val -> val.equals(note.getTitle()))
                .orElse(true))
            .filter(note -> Optional.ofNullable(request.getLabel())
                .filter(StringUtils::hasLength)
                .map(val -> Optional.ofNullable(note.getLabels()).orElse(Collections.emptyList())
                    .contains(val))
                .orElse(true))
            .toList();
        return new PageResponse<>(items, request.getPageSize(), null, null);
    }

    private void assertNoteExists(String id) {
        if (!notes.containsKey(id)) {
            throw new NoteNotFoundException(id);
        }
    }
}
