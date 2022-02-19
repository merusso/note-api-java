package com.example.noteapi.api;

import com.example.noteapi.service.NoteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("notes")
public class NoteController {

    private NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public Note create(@RequestBody @Valid Note note) {
        return noteService.create(note);
    }

    @GetMapping("{id}")
    public Note get(@PathVariable long id) {
        return noteService.get(id);
    }

    @PutMapping("{id}")
    public Note update(@PathVariable long id, @RequestBody @Valid Note note) {
        return noteService.update(note);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        noteService.delete(id);
    }
}
