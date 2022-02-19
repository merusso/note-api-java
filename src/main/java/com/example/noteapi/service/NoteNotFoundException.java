package com.example.noteapi.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoteNotFoundException extends RuntimeException {
    public NoteNotFoundException(long id) {
        super("Note with ID %s not found".formatted(id));
    }
}
