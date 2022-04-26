package com.example.noteapi.service;

import com.example.noteapi.api.Note;
import com.example.noteapi.api.NoteSearchRequest;
import com.example.noteapi.api.PageResponse;
import com.example.noteapi.data.NoteRepository;
import com.example.noteapi.data.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@Profile("live")
@AllArgsConstructor
public class NoteServiceImpl implements NoteService {

    private NoteRepository repository;
    private NoteConverter converter;
    private UserRepository userRepository;
    private MongoTemplate template;

    @Override
    public Note create(Note note) {
        Assert.notNull(note.getUserId(), "Note.userId is required");

        var dataNote = converter.convert(note);
        assertUserExists(dataNote.userId);
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
        assertNoteExists(note.getId());
        var dataNote = converter.convert(note);
        assertUserExists(dataNote.userId);
        dataNote.updatedDate = Instant.now();
        dataNote = repository.save(dataNote);
        return converter.convertReverse(dataNote);
    }

    @Override
    public void delete(String id) {
        assertNoteExists(id);
        repository.deleteById(id);
    }

    @Override
    public PageResponse<Note> search(NoteSearchRequest request) {
        // Build query
        Pageable requestPageable = Pageable.ofSize(request.getPageSize());
        List<Criteria> criteriaList = new ArrayList<>();
        Optional.ofNullable(request.getUserId())
            .map(userId -> Criteria.where("userId").is(userId))
            .ifPresent(criteriaList::add);
        Optional.ofNullable(request.getTitle())
            .map(title -> Criteria.where("title").regex(title, "i"))
            .ifPresent(criteriaList::add);
        Optional.ofNullable(request.getLabel())
            .map(label -> Criteria.where("labels").is(label))
            .ifPresent(criteriaList::add);
        Query query = Query.query(new Criteria().andOperator(criteriaList)).with(requestPageable);

        // Run query
        List<com.example.noteapi.data.Note> dataNotes = template.find(query, com.example.noteapi.data.Note.class);
        // Convert to Page using count query
        Page<com.example.noteapi.data.Note> page = PageableExecutionUtils.getPage(
            dataNotes,
            requestPageable,
            () -> template.count(query.with(Pageable.unpaged()), com.example.noteapi.data.Note.class)
        );

        // Convert to PageResponse
        List<Note> notes = page.get()
            .map(converter::convertReverse)
            .toList();
        Pageable pageable = page.getPageable();
        return new PageResponse<>(notes,
            pageable.getPageSize(),
            Objects.toString(pageable.getPageNumber()),
            page.hasNext() ? Objects.toString(pageable.next().getPageNumber()) : null);
    }

    private void assertNoteExists(String note) {
        if (!repository.existsById(note)) {
            throw new NoteNotFoundException(note);
        }
    }

    private void assertUserExists(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
    }
}
