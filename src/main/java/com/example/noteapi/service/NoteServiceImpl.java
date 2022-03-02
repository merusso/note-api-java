package com.example.noteapi.service;

import com.example.noteapi.api.Note;
import com.example.noteapi.api.NoteSearchRequest;
import com.example.noteapi.api.PageResponse;
import com.example.noteapi.data.NoteRepository;
import com.example.noteapi.data.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

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

    @Override
    public PageResponse<Note> search(NoteSearchRequest request) {
        Pageable requestPageable = Pageable.ofSize(request.getPageSize());
        var note = new com.example.noteapi.data.Note();
        note.userId = request.getUserId();
        note.title = request.getTitle();
        // Example query only supports exact match for non-strings
//        note.labels = Optional.ofNullable(request.getLabel())
//            .stream().toList();
        ExampleMatcher matcher = ExampleMatcher.matching()
            .withMatcher("title", match -> match.contains().ignoreCase());
        var example = Example.of(note, matcher);

        var page = repository.findAll(example, requestPageable);

        List<Note> notes = page.get()
            .map(converter::convertReverse)
            .toList();
        Pageable pageable = page.getPageable();
        return new PageResponse<>(notes,
            pageable.getPageSize(),
            Objects.toString(pageable.getPageNumber()),
            Objects.toString(pageable.next().getPageNumber()));
    }
}
