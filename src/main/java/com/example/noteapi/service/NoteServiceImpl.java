package com.example.noteapi.service;

import com.example.noteapi.api.Note;
import com.example.noteapi.data.NoteRepository;
import com.example.noteapi.data.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.Instant;

@Component
@Profile("live")
@AllArgsConstructor
public class NoteServiceImpl implements NoteService {

    private NoteRepository repository;
    private NoteConverter converter;
    private UserRepository userRepository;

    @Override
    public Note create(Note note) {
        Assert.notNull(note.getUserId(), "Note.userId is required");

        var dataNote = converter.convert(note);
        if (!userRepository.existsById(dataNote.userId)) {
            throw new UserNotFoundException(dataNote.userId);
        }
        dataNote.id = null;
        dataNote.createdDate = Instant.now();
        dataNote.updatedDate = dataNote.createdDate;
        dataNote = repository.save(dataNote);
        return converter.convertReverse(dataNote);
    }

    @Override
    public Note get(String id) {
        return repository.findById(id)
            .map(converter::convertReverse)
            .orElseThrow(() -> new NoteNotFoundException(id));
    }

    @Override
    public Note update(Note note) {
        if (!repository.existsById(note.getId())) {
            throw new NoteNotFoundException(note.getId());
        }
        var dataNote = converter.convert(note);
        if (!userRepository.existsById(dataNote.userId)) {
            throw new UserNotFoundException(dataNote.userId);
        }
        dataNote.updatedDate = Instant.now();
        dataNote = repository.save(dataNote);
        return converter.convertReverse(dataNote);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new NoteNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
