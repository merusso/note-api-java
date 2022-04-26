package com.example.noteapi.api;

import com.example.noteapi.service.NoteService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("notes")
public class NoteController {

    private NoteService noteService;

    @GetMapping
    public PageResponse<Note> search(NoteSearchRequest request) {
        return noteService.search(request);
    }

    @PostMapping
    public Note create(@RequestBody @Valid Note note) {
        return noteService.create(note);
    }

    @GetMapping("{id}")
    public Note get(@PathVariable String id) {
        return noteService.get(id);
    }

    @PutMapping("{id}")
    public Note update(@PathVariable String id, @RequestBody @Valid Note note) {
        note.setId(id);
        return noteService.update(note);
    }

    @PatchMapping("{id}")
    public Note patch(@PathVariable String id, @RequestBody JsonNode json) {
        return noteService.patch(id, json);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        noteService.delete(id);
    }
}
